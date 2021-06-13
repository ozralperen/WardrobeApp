package tr.yildiz.wardrobe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateUpdateDrawerActivity extends AppCompatActivity {

    private LinearLayout clothesListLL;
    private List<Dress> dresses;
    private Uri imageUri;
    private EditText drawerNameET;
    private RelativeLayout createDrawerRL;
    private Button drawerSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int drawerID;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_drawer);

        Context context = this;


        final DatabaseHandler db = new DatabaseHandler(this);

        clothesListLL = findViewById(R.id.clothesDrawerLL);
        drawerNameET = findViewById(R.id.createUpdateDrawerNameET);
        createDrawerRL = findViewById(R.id.createUpdateDrawerRL);
        drawerSaveButton = findViewById(R.id.createSaveDrawerButton);

        //TODO: drawer'da olan resimleri bulup işaretle

        LinearLayout dressOptionLL;
        CheckBox dressCB;
        ImageView dressIW;
        if(getIntent().getBooleanExtra("New", true)) {
            dresses = db.getAllDresses();
            for (Dress dress : dresses) {
                dressOptionLL = new LinearLayout(this);
                dressOptionLL.setOrientation(LinearLayout.HORIZONTAL);
                dressOptionLL.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                dressCB = new CheckBox(this);
                dressCB.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                clothesListLL.addView(dressOptionLL);
            }


            drawerSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i;
                    List<Drawer> allDrawers;

                    db.addDrawer(drawerNameET.getText().toString());
                    allDrawers = db.getAllDrawers();

                    for (i = 0; i < clothesListLL.getChildCount(); i++) { //whiteline
                        if(((CheckBox)((LinearLayout)clothesListLL.getChildAt(i)).getChildAt(0)).isChecked()){
                            db.addDressToDrawer(allDrawers.get(allDrawers.size() - 1).getId(), dresses.get(i).getId());
                        }
                    }
                    Intent intent = new Intent(getApplicationContext(), AddDrawerActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }

        else{
            drawerID = getIntent().getIntExtra("drawerID", -1);
            if(drawerID != -1){
                drawerNameET.setText(getIntent().getStringExtra("drawerName"));
                drawerNameET.setClickable(false);
                drawerNameET.setFocusable(false);
                drawerSaveButton.setText("Delete");
                drawerSaveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder deleteCheckADBuilder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);
                        View deleteQuestionCheckADLayout = getLayoutInflater().inflate(R.layout.delete_alert_dialog_layout, null);
                        deleteCheckADBuilder.setView(deleteQuestionCheckADLayout);
                        AlertDialog deleteQuestionCheckAD = deleteCheckADBuilder.create();
                        Button cancel = (Button)deleteQuestionCheckADLayout.findViewById(R.id.deletionADCancelButton);
                        Button delete = (Button)deleteQuestionCheckADLayout.findViewById(R.id.deletionADOKButton);

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteQuestionCheckAD.cancel();
                            }
                        });
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                db.deleteDrawer(drawerID);
                                deleteQuestionCheckAD.cancel();
                                Intent intent = new Intent(getApplicationContext(), AddDrawerActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        deleteQuestionCheckAD.show();
                    }
                });
                dresses = db.getDrawerDresses(drawerID);

                for(Dress dress: dresses){
                    dressIW = new ImageView(this);
                    dressIW.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    try {
                        imageUri = Uri.parse(dress.getPhoto());
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(imageStream), 256, 256, false);
                        dressIW.setImageDrawable(new BitmapDrawable(getResources(), selectedImage));
                        clothesListLL.addView(dressIW);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}