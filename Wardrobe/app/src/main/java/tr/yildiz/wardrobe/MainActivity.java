package tr.yildiz.wardrobe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static tr.yildiz.wardrobe.AddDressActivity.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private Button drawerButton, dressButton, cabinButton, eventsButton;
    private Activity main;
    public int STORAGE_PERMISSION = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerButton = findViewById(R.id.mainMenuDrawersButton);
        dressButton = findViewById(R.id.mainMenuAddDressButton);
        cabinButton = findViewById(R.id.MainMenuCabinButton);
        eventsButton = findViewById(R.id.MainMenuEventButton);

        main = MainActivity.this;

        if(ContextCompat.checkSelfPermission(main, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestStoragePermission();
        }

        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddDrawerActivity.class);
                startActivity(intent);
            }
        });

        dressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddDressActivity.class);
                startActivity(intent);
            }
        });

        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(main,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(main).setTitle("PERMISSION NEEDED").setMessage("Permission to reach photos is needed.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(main, new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            },STORAGE_PERMISSION);
                        }
                    }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },STORAGE_PERMISSION);
        }
    }
}