package tr.yildiz.wardrobe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class CombinationMenuActivity extends AppCompatActivity {

    private Button newCombinationButton;
    private LinearLayout CombinationListLL;
    private List<Dress> dressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination_menu);

        DatabaseHandler db = new DatabaseHandler(this);

        CombinationListLL = findViewById(R.id.addDrawerDrawerListLL);
        newCombinationButton = findViewById(R.id.createNewDrawerButton);
        newCombinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateUpdateDrawerActivity.class);
                intent.putExtra("New", true);
                startActivity(intent);
                finish();
            }
        });
    }
}