package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 31/12/2016.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity1 extends AppCompatActivity {
    Button b1,b2;

  //  private FirebaseAuth mauths;
   // DatabaseReference rootRef,dbPark;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.welcome123);
        mp.start();
     //   mauths = FirebaseAuth.getInstance();
      //  rootRef = FirebaseDatabase.getInstance().getReference();


        Button btnlogout = (Button) findViewById(R.id.closebutton2);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity1.this);
                builder.setTitle("Exit");
                builder.setMessage("Do you want to exit ??");
                builder.setPositiveButton("Yes. Exit now!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();
                        moveTaskToBack(true);

                    }
                });

                builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        //tx1=(TextView)findViewById(R.id.textView3);
        //tx1.setVisibility(View.GONE);


        final Context context = this;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(context, UserInterface.class);
                    startActivity(intent);

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AuthorityInterface.class);
                startActivity(intent);

            }
        });


    }

/*
    @Override
    public void onStart() {
        super.onStart();
        // fn_getlocation();
        FirebaseUser currentUser = mauths.getCurrentUser();
        updateUI(currentUser);

    }

*/
/*


    private void updateUI(FirebaseUser user) {

        if (user != null) {
            final String uid;
            uid = user.getUid();
            dbPark = rootRef.child("User");//.child("/"+uid+"/").child("Plate Number");

            dbPark.child("/" + uid + "/").child("Plate Number").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String valueplate = dataSnapshot.getValue(String.class);

                    if (valueplate.equals("NoPark")) {
                        Intent intent = new Intent(context, UserMenu.class);
                        startActivity(intent);

                    } else if (!valueplate.equals("NoPark")) {

                        Intent intent = new Intent(context, UserActive.class);
                        startActivity(intent);
                        //  return;


                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }

        else if (user == null) {

            Intent intent = new Intent(context, MainActivity1.class);
            startActivity(intent);


        }






    }
    */







    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity1.this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit ??");
        builder.setPositiveButton("Yes. Exit now!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(context, MainActivity1.class);
                startActivity(intent);
                finish();
                moveTaskToBack(true);

            }
        });


        builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}