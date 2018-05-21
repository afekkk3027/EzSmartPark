package com.afiq.ezsmartpark1;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Asus on 28/11/2017.
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AuthorityCheckPark extends AppCompatActivity {

    Button b1,b2,b3;
    private FirebaseAuth mauth;
    DatabaseReference authref,demoRef;

    TextView nama,phone,platenumber,city,area,locality,time,status,date,platefill;
  //  EditText platefill;
    public String listItemCreationDate;

  //  final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_check_park);




        platefill = (TextView) findViewById(R.id.check_plate_fill);
        nama = (TextView) findViewById(R.id.checknametextview);
        phone = (TextView) findViewById(R.id.checkphonetextview);
        platenumber = (TextView) findViewById(R.id.checkplatetextview);
        time = (TextView) findViewById(R.id.checktimetextview);
        city = (TextView) findViewById(R.id.checklocationtextview);
        area = (TextView) findViewById(R.id.checklocationtextview2);
        locality = (TextView) findViewById(R.id.checklocationtextview3);
        status = (TextView) findViewById(R.id.checkstatustextview);
        date = (TextView) findViewById(R.id.datetextview);
        b1 = (Button) findViewById(R.id.checkactivatebutton);

        b3 = (Button) findViewById(R.id.captureimage);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        this.listItemCreationDate = sdf.format(new Date());

        mauth = FirebaseAuth.getInstance();
        FirebaseUser user = mauth.getCurrentUser();
        authref = FirebaseDatabase.getInstance().getReference();


        demoRef = authref.child("Authority");

        platefill.setText(getIntent().getStringExtra("NAME"));

     //   b2.setVisibility(b2.INVISIBLE); //button summon (belum buat)
     //   b3.setVisibility(b3.INVISIBLE);

        final Context context = this;
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, FirebaseStorage.class);
                startActivity(intent);

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!validateForm()) {

               //     b1.setVisibility(b1.INVISIBLE);
               //     b2.setVisibility(b2.VISIBLE);
               //     b3.setVisibility(b3.VISIBLE);

                    return;
                }


                final String Plates = platefill.getText().toString();
                demoRef.child("Plate").child(Plates).child("Plate Number").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String platedb = dataSnapshot.getValue(String.class);
                      //  cchck2.setText(platedb);

                        if (platedb == null){

                            status.setText("Not Pay");
                            platenumber.setText(Plates);
                            nama.setText("-");
                            area.setText("-");
                            city.setText("-");
                            locality.setText("-");
                            phone.setText("-");
                            time.setText("-");
                            platefill.setText(null);

                        }
                        if (platedb != null){
                            status.setText("Paid");
                            platefill.setText(null);


                            demoRef.child("Plate").child(platedb).child("Plate Number").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String plate1 = dataSnapshot.getValue(String.class);
                                    platenumber.setText(plate1);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });



                            demoRef.child("Plate").child(platedb).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String nama1 = dataSnapshot.getValue(String.class);
                                    nama.setText(nama1);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });



                            demoRef.child("Plate").child(platedb).child("Area").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String area1 = dataSnapshot.getValue(String.class);
                                    area.setText(area1);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });


                            demoRef.child("Plate").child(platedb).child("City").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String city1 = dataSnapshot.getValue(String.class);
                                    city.setText(city1);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });


                            demoRef.child("Plate").child(platedb).child("Locality").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String local1 = dataSnapshot.getValue(String.class);
                                    locality.setText(local1);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });



                            demoRef.child("Plate").child(platedb).child("Phone Number").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String phone1 = dataSnapshot.getValue(String.class);
                                    phone.setText(phone1);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });


                            demoRef.child("Plate").child(platedb).child("Time").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String time1 = dataSnapshot.getValue(String.class);
                                    time.setText(time1);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }
    private boolean validateForm() {
        boolean valid = true;

        String Plate = platefill.getText().toString();
        if (TextUtils.isEmpty(Plate)) {
            platefill.setError("Required.");
            valid = false;
        } else {
            platefill.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed(){
        Intent backMainTest = new Intent(this,AuthorityMenu.class);
        startActivity(backMainTest);
        finish();
    }
}
