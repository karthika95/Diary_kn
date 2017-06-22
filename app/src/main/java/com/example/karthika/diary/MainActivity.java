package com.example.karthika.diary;

/**
 * Created by Karthika on 17-05-2017.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity  extends AppCompatActivity {
    EditText Pass,new_pass,pre_pass;
    Button open,set,set_new_pass,btnSubmit;
    AlertDialog.Builder builder;
    static String saved_pass="";
    Spinner spinner;
    Locale myLocale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pass = (EditText) findViewById(R.id.password);
        open = (Button) findViewById(R.id.open);
        set = (Button) findViewById(R.id.setpassword);


        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPassword().equals(Pass.getText().toString())) {
                    startActivity(new Intent(MainActivity.this, CalendarActivity.class));
                } else {

                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Wrong password");
                    builder.setMessage("please enter correct password..");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //close alert dialog
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }

            }
        });
    }
    public void setpassword(View view){
        setContentView(R.layout.activity_register);
        pre_pass = (EditText)findViewById(R.id.previous_pass);
        new_pass = (EditText)findViewById(R.id.new_pass);
        set_new_pass=(Button)findViewById(R.id.set_new_password);
        set_new_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  writeToUserNameAndPassword(pre_pass.getText().toString());
                if (getPassword().equals(pre_pass.getText().toString()))
                {
                    writeToUserNameAndPassword(new_pass.getText().toString());
                    Toast.makeText(getBaseContext(),"new password saved",Toast.LENGTH_LONG).show();
                    //setContentView(R.layout.activity_main);
                    startActivity(new Intent(MainActivity.this, CalendarActivity.class));
                    // they have the right userName and password
                }
                else
                {
                    // these preference Strings for their userName/password have both not been created
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("previous password is wrong");
                    builder.setMessage("please enter correct password..");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //close alert dialog
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        spinner = (Spinner) findViewById(R.id.lang);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterlay = ArrayAdapter.createFromResource(this,
                R.array.language_layout, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapterlay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapterlay);


        btnSubmit = (Button) findViewById(R.id.setlang);


        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                String language = String.valueOf(spinner.getSelectedItem());
                if(language.equals("Kannada")){
                    setLocale("kn");}
                else{
                    setLocale("");
                }
            }


        });

    }
    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getBaseContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


        // do your work here
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);

    }

    public String getPassword()
    {
        SharedPreferences sp = getSharedPreferences("userNameAndPassword", 0);
        String str = sp.getString("password","");
        return str;
    }

    public void writeToUserNameAndPassword(String password)
    {
        SharedPreferences.Editor pref =
                getSharedPreferences("userNameAndPassword",0).edit();

        pref.putString("password", password);
        pref.commit();
    }

    public void password()
    {
        Toast.makeText(getBaseContext(),"new password saved",Toast.LENGTH_LONG).show();
        //setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, CalendarActivity.class));
        // }
    }

}
