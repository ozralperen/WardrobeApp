package tr.yildiz.wardrobe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class EventMenuActivity extends AppCompatActivity {

    private Button newEventButton;
    private LinearLayout eventListLL;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_menu);

        DatabaseHandler db = new DatabaseHandler(this);

        eventListLL = findViewById(R.id.eventMenuEventListLL);
        newEventButton = findViewById(R.id.createNewEventButton);
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateUpdateEventActivity.class);
                intent.putExtra("New", true);
                startActivity(intent);
                finish();
            }
        });

        eventList = db.getAllEvents();
        Button eventButton;
        View whiteLine;
        for (Event event: eventList){
            eventButton = new Button(this);
            eventButton.setBackgroundColor(getResources().getColor(R.color.dark_background));
            eventButton.setText(event.getEventName());
            eventButton.setTextColor(getResources().getColor(R.color.white));
            eventButton.setHint(String.valueOf(event.getEventId()));
            eventButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CreateUpdateEventActivity.class);
                    intent.putExtra("New", false);
                    intent.putExtra("eventID", Integer.valueOf(((Button)v).getHint().toString()));
                    intent.putExtra("eventName", ((Button)v).getText().toString());
                    startActivity(intent);
                    finish();
                }
            });
            eventListLL.addView(eventButton);

            whiteLine = new View(this);
            whiteLine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
            whiteLine.setBackgroundColor(getResources().getColor(R.color.dark_background));
            eventListLL.addView(whiteLine);
        }
    }
}