package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 16/10/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.method.ScrollingMovementMethod;

public class UserTerms extends AppCompatActivity {
    Button b1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_terms);


        b1=(Button)findViewById(R.id.signupbutton);

        //tx1=(TextView)findViewById(R.id.textView3);
        //tx1.setVisibility(View.GONE);
        final Context context = this;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserRegister.class);
                startActivity(intent);

            }
        });

        TextView textView = (TextView) findViewById(R.id.text_views);
        textView.setMovementMethod(new ScrollingMovementMethod());


    }

    @Override
    public void onBackPressed() {

        Intent backMainTest = new Intent(this, UserRegister.class);
        startActivity(backMainTest);
        finish();

    }




}
