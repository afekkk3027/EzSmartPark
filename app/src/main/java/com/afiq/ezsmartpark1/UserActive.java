package com.afiq.ezsmartpark1;


/**
 * Created by Asus on 29/10/2017.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import android.os.Bundle;

import android.view.View;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import android.view.View.OnClickListener;

import android.widget.Button;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserActive extends AppCompatActivity {

    Timer _t,timer;
   // double j = 0.000111111;
    double j = 0.000277777;
    double _count, counting, countings, kira, lastkredit, updatekredit, kirakira, countcount, jumlah;



   DecimalFormat df = new DecimalFormat("0.00");




    private FirebaseAuth mauths;
    DatabaseReference rootRef,demoRef,demoAuthority,demoRef1;
    TextView balancevalue,plateValue,timeValue,nameValue,LocalityValue,AreaValue,CityValue,AddressValue, timeleft;
    Button stop;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_user_active);



        mauths = FirebaseAuth.getInstance();
        FirebaseUser user = mauths.getCurrentUser();
        final String uid;
        uid = user.getUid();

       // mtimeField = (TextView) findViewById(R.id.timetextview);

        rootRef = FirebaseDatabase.getInstance().getReference();
        stop = (Button) findViewById(R.id.deactivatebutton);
        balancevalue = (TextView) findViewById(R.id.credittextview);
        timeleft = (TextView) findViewById(R.id.tv_timeleft);
        plateValue = (TextView) findViewById(R.id.platetextview);
        timeValue = (TextView) findViewById(R.id.timetextview);
        nameValue = (TextView) findViewById(R.id.nametextview);
        CityValue = (TextView) findViewById(R.id.locationtextview);
        AreaValue = (TextView) findViewById(R.id.locationtextview2);
        LocalityValue = (TextView) findViewById(R.id.locationtextview3);
        AddressValue = (TextView) findViewById(R.id.locationtextview4);

        demoRef = rootRef.child("User");
        demoAuthority = rootRef.child("Authority");


        final Context context = this;
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final AlertDialog.Builder builder = new AlertDialog.Builder(UserActive.this);
                builder.setTitle("Stop Park!! If you stop park, your money cannot be refund");
                builder.setMessage("Do you want to Stop Parking ??");
                builder.setPositiveButton("Yes. Stop now!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent stopIntent = new Intent(UserActive.this, ForegroundService.class);
                        stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                        startService(stopIntent);

                         _t.cancel();
                         _t.purge();

                        Stoppark();
                        Intent intent = new Intent(context, UserPark.class);
                        startActivity(intent);

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

              //  Stoppark();

             //   Intent intent = new Intent(context, UserPark.class);
               // startActivity(intent);
               // Stoppark();
            }
        });


        demoRef.child("/" + uid + "/").child("Plate Numbers").setValue(null);

        demoRef.child("/"+uid+"/").child("Time Left").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                _count = Double.parseDouble(value);
                df.setRoundingMode(RoundingMode.HALF_UP);
                String str1 = df.format(_count);
                timeleft.setText(str1);







            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        demoRef.child("/"+uid+"/").child("Balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                counting = Double.parseDouble(value);
                df.setRoundingMode(RoundingMode.HALF_UP);
                String str12 = df.format(counting);
                balancevalue.setText(str12);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });








        deduct();

                demoRef.child("/"+uid+"/").child("Plate Number").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        plateValue.setText(value);
                        demoAuthority.child("Plate").child(value).child("Plate Number").setValue(value);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                demoRef.child("/"+uid+"/").child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        nameValue.setText(value);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                 });



                demoRef.child("/"+uid+"/").child("Time").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        timeValue.setText(value);
                     }
                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                     }
                  });

        demoRef.child("/"+uid+"/").child("City").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                CityValue.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        demoRef.child("/"+uid+"/").child("Area").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                AreaValue.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        demoRef.child("/"+uid+"/").child("Locality").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                LocalityValue.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        demoRef.child("/"+uid+"/").child("Address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                AddressValue.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //String time = mtimeField.getText().toString();

        Intent startIntent = new Intent(UserActive.this, ForegroundService.class);
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(startIntent);


    }



    private void   Stoppark(){



        _t.cancel();
        _t.purge();

        mauths = FirebaseAuth.getInstance();
        FirebaseUser user = mauths.getCurrentUser();
        final String uid;
        uid = user.getUid();

        String platevalue = plateValue.getText().toString();


        DatabaseReference dbAuthority = FirebaseDatabase.getInstance().getReference().getRoot().child("Authority").child("Plate").child(platevalue);
        dbAuthority.setValue(null);

        demoRef.child("/" + uid + "/").child("Plate Number").setValue("NoPark");
        demoRef.child("/" + uid + "/").child("Time").setValue(null);
        demoRef.child("/" + uid + "/").child("Locality").setValue(null);
        demoRef.child("/" + uid + "/").child("Area").setValue(null);
        demoRef.child("/" + uid + "/").child("City").setValue(null);
        demoRef.child("/" + uid + "/").child("Address").setValue(null);
        demoRef.child("/" + uid + "/").child("Date").setValue(null);
        demoRef.child("/"+uid+"/").child("Total Price").setValue(null);
        demoRef.child("/"+uid+"/").child("Time Left").setValue(null);


      //  _t.cancel();
      //  _t.cancel();
      //  _t.purge();
        _t.cancel();
        _t.purge();



    }







    private void deduct() {


        FirebaseUser user = mauths.getCurrentUser();
        final String uid;
        uid = user.getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        demoRef = rootRef.child("User");

/*
        demoRef.child("/"+uid+"/").child("Total Price").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valuess = dataSnapshot.getValue(String.class);
                kira = Double.parseDouble(valuess);

                String kreditlast = balancevalue.getText().toString();
                lastkredit = Double.parseDouble(kreditlast);

                updatekredit = lastkredit - kira;
                df.setRoundingMode(RoundingMode.HALF_UP);
                String str11 = df.format(updatekredit);

                balancevalue.setText(str11);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

*/





        _t = new Timer();
        _t.scheduleAtFixedRate( new TimerTask() {
            @Override
            public void run() {

                _count -= j;

                runOnUiThread(new Runnable() //run on ui threa
                {
                    public void run()
                    {

                        if(_count>=0)
                        {




                            demoRef.child("/" + uid + "/").child("Time Left").setValue(""+_count);

                            demoRef.child("/"+uid+"/").child("Time Left").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String value = dataSnapshot.getValue(String.class);
                                    _count = Double.parseDouble(value);
                                    df.setRoundingMode(RoundingMode.HALF_UP);
                                    String str1 = df.format(_count);

                                    timeleft.setText(str1);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });




                        }

                        if(_count==0)
                        {

                            Stoppark();
                            Toast.makeText(UserActive.this, "Please Renew Your Parking!",
                                    Toast.LENGTH_SHORT).show();
                            Intent stopIntent = new Intent(UserActive.this, ForegroundService.class);
                            stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                            startService(stopIntent);
                            Intent intent = new Intent(context, UserPark.class);
                            startActivity(intent);



                        }

                        if(_count<=0)
                        {

                            Stoppark();
                            Toast.makeText(UserActive.this, "Please Renew Your Parking!",
                                    Toast.LENGTH_SHORT).show();
                            Intent stopIntent = new Intent(UserActive.this, ForegroundService.class);
                            stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                            startService(stopIntent);
                            Intent intent = new Intent(context, UserPark.class);
                            startActivity(intent);



                        }

                      /*  else
                        {
                           // _t.cancel();
                          //  _t.purge();
                            Toast.makeText(UserActive.this, "Please Renew Park!",
                                    Toast.LENGTH_SHORT).show();
                            Intent stopIntent = new Intent(UserActive.this, ForegroundService.class);
                            stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                            startService(stopIntent);

                            mauths = FirebaseAuth.getInstance();
                            FirebaseUser user = mauths.getCurrentUser();
                            final String uid;
                            uid = user.getUid();

                            String platevalue = plateValue.getText().toString();


                            DatabaseReference dbAuthority = FirebaseDatabase.getInstance().getReference().getRoot().child("Authority").child("Plate").child(platevalue);
                            dbAuthority.setValue(null);

                            demoRef.child("/" + uid + "/").child("Plate Number").setValue("NoPark");
                            demoRef.child("/" + uid + "/").child("Time").setValue(null);
                            demoRef.child("/" + uid + "/").child("Locality").setValue(null);
                            demoRef.child("/" + uid + "/").child("Area").setValue(null);
                            demoRef.child("/" + uid + "/").child("City").setValue(null);
                            demoRef.child("/" + uid + "/").child("Address").setValue(null);
                            demoRef.child("/" + uid + "/").child("Date").setValue(null);

                            //Stoppark();
                            Intent intent = new Intent(context, UserPark.class);
                            startActivity(intent);
                        }   */
                    }
                });
            }
        }, 1000, 1000 );






    }









    @Override
    public void onBackPressed(){

        Intent backMainTest = new Intent(this,UserMenu.class);
        startActivity(backMainTest);
       // finish();
        /*
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserActive.this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit ??");
        builder.setPositiveButton("Yes. Exit now!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(context, UserActive.class);
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
        */
    }



    @Override
    public void onDestroy() {

        super.onDestroy();
        //Stoppark();
        Intent stopIntent = new Intent(UserActive.this, ForegroundService.class);
        stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        startService(stopIntent);
        Toast.makeText(UserActive.this, "You just close EzSmartPark! " +
                        "Open EzSmartPark if you want to perform another Parking",
                Toast.LENGTH_LONG).show();

    }







}
