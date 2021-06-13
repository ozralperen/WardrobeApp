package tr.yildiz.wardrobe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public class CreateUpdateEventActivity extends AppCompatActivity {

    private LinearLayout eventsListLL;
    private List<Dress> dresses;
    private Uri imageUri;
    private EditText eventNameET, eventTypeET, eventDateET;
    private RelativeLayout createEventRL;
    private Button eventSaveButton;
    private DatePickerDialog.OnDateSetListener eventDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_event);

        Context context = this;
        int eventid;

        final DatabaseHandler db = new DatabaseHandler(this);

        eventsListLL = findViewById(R.id.clothesEventLL);
        eventNameET = findViewById(R.id.createUpdateEventNameET);
        eventTypeET = findViewById(R.id.createUpdateEventTypeET);
        eventDateET = findViewById(R.id.createUpdateEventDateET);
        createEventRL = findViewById(R.id.createUpdateEventRL);
        eventSaveButton = findViewById(R.id.createSaveEventButton);

        eventDateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(eventDateET.isFocused()){
                    eventDateET.clearFocus();
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(CreateUpdateEventActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth, eventDateSetListener, year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dialog.show();
                }
            }
        });

        eventDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                eventDateET.setText(i2 + "/" + (i1 + 1) + "/" + i);
            }
        };

        List<Integer> eventDresses = null;
        if(!getIntent().getBooleanExtra("New", true)) {

            eventid = getIntent().getIntExtra("eventID", -1);
            if(eventid != -1){
                Event event = db.getEvent(eventid);
                eventNameET.setText(event.getEventName());
                eventTypeET.setText(event.getEventType());
                eventDateET.setText(event.getEventDate());
                eventSaveButton.setText("Save");
                eventSaveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder deleteCheckADBuilder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
                        View deleteQuestionCheckADLayout = getLayoutInflater().inflate(R.layout.delete_alert_dialog_layout, null);
                        deleteCheckADBuilder.setView(deleteQuestionCheckADLayout);
                        AlertDialog deleteQuestionCheckAD = deleteCheckADBuilder.create();
                        Button cancel = (Button)deleteQuestionCheckADLayout.findViewById(R.id.deletionADCancelButton);
                        Button delete = (Button)deleteQuestionCheckADLayout.findViewById(R.id.deletionADOKButton);
                        TextView desc = (TextView)deleteQuestionCheckADLayout.findViewById(R.id.deletionTextADTV);
                        desc.setText("You are about to update event information.");
                        delete.setText("Update");

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteQuestionCheckAD.cancel();
                            }
                        });
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                db.deleteAllDressesFromEvent(eventid);
                                int i;
                                for (i = 0; i < eventsListLL.getChildCount(); i++) { //whiteline
                                    if (((CheckBox) ((LinearLayout) eventsListLL.getChildAt(i)).getChildAt(0)).isChecked()) {
                                        db.addDressToEvent(eventid, dresses.get(i).getId());
                                    }
                                }
                                Event updated = new Event(eventNameET.getText().toString(), eventTypeET.getText().toString(), eventDateET.getText().toString(), "N/A");
                                updated.setEventId(eventid);
                                db.updateEvent(updated);
                                deleteQuestionCheckAD.cancel();
                                Intent intent = new Intent(getApplicationContext(), EventMenuActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        deleteQuestionCheckAD.show();
                    }
                });
                eventDresses = db.getEventDresses(eventid);
            }
        }
        //TODO: drawer'da olan resimleri bulup işaretle

        LinearLayout dressOptionLL;
        CheckBox dressCB;
        ImageView dressIW;

        dresses = db.getAllDresses();
        for (Dress dress : dresses) {
            dressOptionLL = new LinearLayout(this);
            dressOptionLL.setOrientation(LinearLayout.HORIZONTAL);
            dressOptionLL.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dressCB = new CheckBox(this);
            dressCB.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if((eventDresses != null) && eventDresses.contains(dress.getId()))
                dressCB.setChecked(true);
            dressOptionLL.addView(dressCB);
            dressIW = new ImageView(this);
            dressIW.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            try {
                imageUri = Uri.parse(dress.getPhoto());
                Log.d("URI: ", dress.getPhoto());
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(imageStream), 256, 256, false);
                dressIW.setImageDrawable(new BitmapDrawable(getResources(), selectedImage));
                dressOptionLL.addView(dressIW);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
            dressOptionLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout LL = (LinearLayout) v;
                    CheckBox c = (CheckBox) LL.getChildAt(0);
                    if (c.isChecked())
                        c.setChecked(false);
                    else
                        c.setChecked(true);
                }
            });
            eventsListLL.addView(dressOptionLL);

        }

        if(getIntent().getBooleanExtra("New", true)) {
            eventSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i;
                    List<Event> allEvents;

                    db.addEvent(new Event(eventNameET.getText().toString(), eventTypeET.getText().toString(), eventDateET.getText().toString(), "N/A"));
                    allEvents = db.getAllEvents();

                    for (i = 0; i < eventsListLL.getChildCount(); i++) { //whiteline
                        if (((CheckBox) ((LinearLayout) eventsListLL.getChildAt(i)).getChildAt(0)).isChecked()) {
                            db.addDressToEvent(allEvents.get(allEvents.size() - 1).getEventId(), dresses.get(i).getId());
                        }
                    }
                    Toast.makeText(context, "Event is created", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), EventMenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }




    }
}