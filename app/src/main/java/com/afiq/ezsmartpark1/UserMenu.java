package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 16/10/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
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

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.afiq.ezsmartpark1.R.string.emailpassword_status_fmt;

public class UserMenu extends AppCompatActivity implements LocationListener {



    Geocoder geocoder;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude,longitude;
    LocationManager locationManager;
    Location location;

    Button b1, b2, b3, b4;
  //  private TextView mStatusTextView;
  private FirebaseAuth mauths;
  DatabaseReference rootRef,dbPark;

    private FirebaseAuth mAuth;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        b1 = (Button) findViewById(R.id.parkbutton);
        b2 = (Button) findViewById(R.id.accountbutton);
        b3 = (Button) findViewById(R.id.aboutbutton);
        b4 = (Button) findViewById(R.id.logoutbutton);



      //  mStatusTextView = (TextView) findViewById(R.id.pleasetextview);


        mAuth = FirebaseAuth.getInstance();
        mauths = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        geocoder = new Geocoder(this, Locale.getDefault());

        //tx1=(TextView)findViewById(R.id.textView3);
        //tx1.setVisibility(View.GONE);
        fn_getlocation();

        final Context context = this;
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentUser = mAuth.getCurrentUser();
                updateUI(currentUser);
                //Intent intent = new Intent(context, UserPark.class);
                //startActivity(intent);

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(context, UserAbout.class);
                startActivity(intent1);
                //Intent intent = new Intent(context, UserPark.class);
                //startActivity(intent);

            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserAccount.class);
                startActivity(intent);

            }
        });




        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent stopIntent = new Intent(UserMenu.this, ForegroundService.class);
                stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(stopIntent);
                Toast.makeText(UserMenu.this, "You just signout EzSmartPark! " +
                                "Signin EzSmartPark if you want to perform another Parking",
                        Toast.LENGTH_LONG).show();
                mAuth.signOut();
                Intent intent = new Intent(context, UserInterface.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnable) {

            showAlert();
        }

    }


    private void updateUI(FirebaseUser user) {

        if (user != null) {
            final String uid;
            uid = user.getUid();
            dbPark = rootRef.child("User");//.child("/"+uid+"/").child("Plate Number");

            dbPark.child("/" + uid + "/").child("Plate Number").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String valueplate = dataSnapshot.getValue(String.class);

                    if (!valueplate.equals("NoPark")) {

                        Intent intent = new Intent(context, UserActive.class);
                        startActivity(intent);
                        //  return;


                    }
                    else if (valueplate.equals("NoPark")) {

                        Intent intent = new Intent(context, UserPark.class);
                        startActivity(intent);

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


 /*   private void updateUI(FirebaseUser user) {
        //hideProgressDialog();

        if (user != null) {
           // mStatusTextView.setText("signed in");
            return;


        }

        if (user == null) {

            Intent intent = new Intent(context, UserInterface.class);
            startActivity(intent);


        }
    }  */


    private void fn_getlocation(){
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable){

            Toast.makeText(getApplicationContext(), "Please enable the network & gps", Toast.LENGTH_SHORT).show();

        }else {

            if (isNetworkEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location!=null){

                        Log.e("latitude",location.getLatitude()+"");
                        Log.e("longitude",location.getLongitude()+"");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }

            }


            if (isGPSEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location!=null){
                        Log.e("latitude",location.getLatitude()+"");
                        Log.e("longitude",location.getLongitude()+"");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }
            }


        }

    }

    public void fn_update(Location location){
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
            String area = addresses.get(0).getAdminArea();




        }
        catch (IOException e1) {
            e1.printStackTrace();
        }


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }





    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }




        /*} else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        }*/

    @Override
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserMenu.this);
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


