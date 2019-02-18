package com.major.project.drishti;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EmergencyDialer extends AppCompatActivity {

    EditText number,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_dialer);
        number = findViewById(R.id.number);
        message = findViewById(R.id.message);
        Button submit = findViewById(R.id.button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Mob = number.getText().toString();
                String Msg = message.getText().toString();

                if(Mob.isEmpty() || Msg.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter value in both fields",Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences sharedPreferences
                            = getBaseContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Mobile",Mob);
                    editor.putString("Message",Msg);
                    editor.putBoolean("IsFirstRun",false);
                    editor.commit();
                    Intent i1 = new Intent();
                    i1.setClass(getApplicationContext(),MainActivity.class);
                    startActivity(i1);
                    finish();
                }
            }
        });
    }
}
