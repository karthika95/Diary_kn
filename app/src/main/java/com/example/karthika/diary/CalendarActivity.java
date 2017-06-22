package com.example.karthika.diary;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
/**
 * Created by Karthika on 01-05-2017.
 */
public class CalendarActivity extends AppCompatActivity {

    ContentDatabase myDB;
    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view);
        myDB = new ContentDatabase(this);

        calendar = (CalendarView) findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String content = myDB.sendDate(dayOfMonth, month, year);
                int month_ok = month + 1;
                Intent intent = new Intent(CalendarActivity.this, DiaryContent.class);
                intent.putExtra("Date", dayOfMonth + "/" + month_ok + "/"+ year);
                intent.putExtra("Content", content);
                startActivity(intent);
            }


        });


    }
}
