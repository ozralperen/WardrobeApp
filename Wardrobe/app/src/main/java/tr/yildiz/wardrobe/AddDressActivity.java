package tr.yildiz.wardrobe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class AddDressActivity extends AppCompatActivity {

    private ImageButton dressIB;
    private EditText dressColorET, dressTextureET, dressBuyDateET, dressPriceET;
    private Spinner dressStyleS;
    private DatePickerDialog.OnDateSetListener dressBuyDateSetListener;
    private Button saveDressButton;
    public static final int REQUEST_CODE = 1;
    private Uri imageUri;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dress);

        DatabaseHandler db = new DatabaseHandler(this);
        context = this;

        dressIB = findViewById(R.id.addDressIB);
        dressStyleS = findViewById(R.id.addDressDressStyle);
        dressColorET = findViewById(R.id.addDressColorET);
        dressTextureET = findViewById(R.id.addDressTextureET);
        dressBuyDateET = findViewById(R.id.addDressBuyDateET);
        dressPriceET = findViewById(R.id.addDressPriceET);
        saveDressButton = findViewById(R.id.addDressSaveButton);

        String[] styles = new String[]{"Head", "Face", "Upper Body", "Lower Body", "Feet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinneritem, styles);
        dressStyleS.setAdapter(adapter);

        dressIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Choose an image"), REQUEST_CODE);

            }

        });

        dressBuyDateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(dressBuyDateET.isFocused()){
                    dressBuyDateET.clearFocus();
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(AddDressActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth, dressBuyDateSetListener, year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dialog.show();
                }
            }
        });

        dressBuyDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                dressBuyDateET.setText(i2 + "/" + (i1 + 1) + "/" + i);
            }
        };

        saveDressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addDress(new Dress(dressStyleS.getSelectedItem().toString(), dressColorET.getText().toString(), dressTextureET.getText().toString(),
                        dressBuyDateET.getText().toString(), dressPriceET.getText().toString(), imageUri.toString()));
                Toast.makeText(context, "Dress is saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(imageStream), 540, 540, false);
                dressIB.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }
}

