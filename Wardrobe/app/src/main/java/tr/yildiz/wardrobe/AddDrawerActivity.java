package tr.yildiz.wardrobe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class AddDrawerActivity extends AppCompatActivity {

    private Button newDrawerButton;
    private LinearLayout drawerListLL;
    private List<Drawer> drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drawer);

        DatabaseHandler db = new DatabaseHandler(this);

        drawerListLL = findViewById(R.id.addDrawerDrawerListLL);
        newDrawerButton = findViewById(R.id.createNewDrawerButton);
        newDrawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateUpdateDrawerActivity.class);
                intent.putExtra("New", true);
                startActivity(intent);
                finish();
            }
        });

        drawerList = db.getAllDrawers();
        Button drawerButton;
        View whiteLine;
        for (Drawer drawer: drawerList){
            drawerButton = new Button(this);
            drawerButton.setBackgroundColor(getResources().getColor(R.color.dark_background));
            drawerButton.setText(drawer.getName());
            drawerButton.setTextColor(getResources().getColor(R.color.white));
            drawerButton.setHint(String.valueOf(drawer.getId()));
            drawerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CreateUpdateDrawerActivity.class);
                    intent.putExtra("New", false);
                    intent.putExtra("drawerID", Integer.valueOf(((Button)v).getHint().toString()));
                    intent.putExtra("drawerName", ((Button)v).getText().toString());
                    startActivity(intent);
                    finish();
                }
            });
            drawerListLL.addView(drawerButton);

            whiteLine = new View(this);
            whiteLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
            whiteLine.setBackgroundColor(getResources().getColor(R.color.dark_background));
            drawerListLL.addView(whiteLine);
        }
    }
}