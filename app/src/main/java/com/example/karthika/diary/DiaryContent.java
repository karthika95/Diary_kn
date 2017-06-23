package com.example.karthika.diary;

/**
 * Created by Karthika on 17-05-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DiaryContent extends AppCompatActivity implements TextToSpeech.OnInitListener,AdapterView.OnItemSelectedListener{
    TextView textView;
    Context context=this;
    DatabaseHelper myDB;
    ContentDatabase contentdb;
    Button save;
    ImageButton calendar,startRecognizer;
    private static final int RQS_RECOGNITION = 1;

    private static final String DB_NAME = "kannada_words.db";
    EditText editText;
    TextToSpeech tts;

    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_content);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_arrays, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // Notify the selected item text
                Toast.makeText
                        (getApplicationContext(), selectedItemText, Toast.LENGTH_SHORT)
                        .show();
                language = selectedItemText;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Bundle dateData = getIntent().getExtras();
        if(dateData == null) {
            return;
        }
        String dateSet = dateData.getString("Date");
        String content = dateData.getString("Content");

        textView = (TextView) findViewById(R.id.date);
        textView.setText(dateSet);
        calendar = (ImageButton) findViewById(R.id.calendarButton);
        startRecognizer = (ImageButton) findViewById(R.id.micButton);
        startRecognizer.setEnabled(false);
        editText = (EditText) findViewById(R.id.edittext);
        startRecognizer.setOnClickListener(startRecognizerOnClickListener);
        tts = new TextToSpeech(this, this);
        // myDB = new DatabaseHelper(this);
        myDB = DatabaseHelper.getInstance(this, DB_NAME);
        contentdb = new ContentDatabase(this);
        save = (Button) findViewById(R.id.save_button);
        editText.append(content);

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryContent.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());

// textView is the TextView view that should display it
        editText.append("\n");
        editText.append(currentDateTimeString);
        editText.append("\n");

    }
    public void AddData(View view){
        String content = editText.getText().toString();
        String date = textView.getText().toString();
        contentdb = new ContentDatabase(context);
        SQLiteDatabase db = contentdb.getWritableDatabase();
        contentdb.addData(date,content,db);
        Toast.makeText(getBaseContext(),R.string.saved,Toast.LENGTH_LONG).show();

    }


    private Button.OnClickListener startRecognizerOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-IN");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.speak);
            startActivityForResult(intent, RQS_RECOGNITION);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == RQS_RECOGNITION) && (resultCode == RESULT_OK)) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String eng = getString(R.string.english);
            String kan = getString(R.string.kannada);
            if (language.equals(eng)) {
                editText.append(result.get(0));
                editText.append(" ");
            } else if (language.equals(kan)) {


                String[] words = result.get(0).split(" ");
                for (String str : words) {

                    Cursor cursor = DatabaseHelper.translate(str);
                    if (cursor.moveToNext()) {
                        editText.append(cursor.getString(0));
                        editText.append(" ");
                    } else {

                        editText.append(str);
                        editText.append(" ");
                    }
                    cursor.close();
                }
            }
        }
    }

    @Override
    public void onInit(int status) {
        startRecognizer.setEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}



