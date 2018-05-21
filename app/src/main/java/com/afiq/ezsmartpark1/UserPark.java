package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 16/10/2017.
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import android.app.Activity;

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

import static android.R.attr.value;
import static android.icu.lang.UCharacter.toUpperCase;


public class UserPark extends AppCompatActivity implements LocationListener {


//    Timer _t,timer;
  //  double j = 0.000111111;
    double _count;




    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    TextView tv_latitude, tv_longitude, tv_address,tv_area,tv_city,tv_locality,tv_creditbal,tv_phone,tv_name;
    Geocoder geocoder;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude,longitude;
    LocationManager locationManager;
    Location location;
    public String listItemCreationDate;


    private FirebaseAuth mauths;
    EditText editText;
    Button submit,btn_start,refresh;
    DatabaseReference rootRef,demoUser,dbPark,demoAuthority,demoHistory;
    TextView timeValue;
    final Context context = this;

    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_park);

        mauths = FirebaseAuth.getInstance();
        FirebaseUser user = mauths.getCurrentUser();
        final String uid;
        uid = user.getUid();

        refresh = (Button) findViewById(R.id.refreshbutton);
        btn_start = (Button) findViewById(R.id.activatebutton);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_latitude = (TextView) findViewById(R.id.tv_latitude);
        tv_longitude = (TextView) findViewById(R.id.tv_longitude);
        tv_area = (TextView)findViewById(R.id.tv_area);
        tv_locality = (TextView)findViewById(R.id.tv_locality);
        tv_city = (TextView)findViewById(R.id.tv_city);
        tv_creditbal = (TextView)findViewById(R.id.tv_creditbal);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        this.listItemCreationDate = sdf.format(new Date());
       // tv_name = (TextView)findViewById(R.id.usernamebox);
      //  tv_email = (TextView)findViewById(R.id.useremailupdateview);
       // tv_phone = (TextView)findViewById(R.id.userphonebox);


        geocoder = new Geocoder(this, Locale.getDefault());


      /*  btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boolean_permission) {

                    fn_getlocation();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enable the gps", Toast.LENGTH_SHORT).show();
                }

            }
        });  */

      //  fn_permission();



        editText = (EditText) findViewById(R.id.platefill);
        timeValue = (TextView) findViewById(R.id.digitalClock);
        //submit = (Button) findViewById(R.id.activatebutton);
        //fetch = (Button) findViewById(R.id.btnFetch);
        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        //rootRef1 = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to demo node
        demoUser = rootRef.child("User");
        demoAuthority = rootRef.child("Authority");
        demoHistory = rootRef.child("History");


        fn_getlocation();


        demoUser.child("/"+uid+"/").child("Balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                _count = Double.parseDouble(value);
                df.setRoundingMode(RoundingMode.HALF_UP);
                String str1 = df.format(_count);

                tv_creditbal.setText(str1);
               // tv_creditbal.setText(value);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fn_getlocation();



            }
        });


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (!validateForm()) {
                    return;
                }

                park();





                Intent intent = new Intent(context, UserPay.class);
                startActivity(intent);



            }
        });

        fn_permission();
               // demoRef1 = rootRef1.child("User");
               // String value1 = editText.getText().toString();
              //  demoRef1.child("Plate Number:").setValue(value1);


      /*  fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoRef.child("Lain").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        demoValue.setText(value);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }); */

      return;
    }

    @Override
    public void onStart() {
        super.onStart();
       // fn_getlocation();
       // FirebaseUser currentUser = mauths.getCurrentUser();
      //  updateUI(currentUser);

        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnable) {

            showAlert();
            //Toast.makeText(getApplicationContext(), "Please enable the gps", Toast.LENGTH_SHORT).show();

        }
        else {
            fn_getlocation();



        }
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }



    public static InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String blockCharacterSet = "~#^|$%*!@/()-'\":;,?{}=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪:-);-):-D:-(:'(:O ";
            if (source != null && blockCharacterSet.contains(("" + source))) {

                return "";
            }
            return null;
        }
    };





    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {


            if ((ActivityCompat.shouldShowRequestPermissionRationale(UserPark.this, android.Manifest.permission.ACCESS_FINE_LOCATION))) {


            }

            else {
                ActivityCompat.requestPermissions(UserPark.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;

                } else {
                    Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                }
            }
        }
    }



     private void   fn_getlocation(){
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

            tv_area.setText(addresses.get(0).getAdminArea());
            tv_locality.setText(stateName);
            tv_address.setText(countryName);
            tv_city.setText(cityName);



        } catch (IOException e1) {
            e1.printStackTrace();
        }


        tv_latitude.setText(latitude+"");
        tv_longitude.setText(longitude+"");
        tv_address.getText();
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



    private boolean validateForm() {
        boolean valid = true;

        String Plate = editText.getText().toString();
        if (TextUtils.isEmpty(Plate)) {
            editText.setError("Required.");
            valid = false;
        } else {
            editText.setError(null);
        }

        String city = tv_city.getText().toString();
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(getApplicationContext(), "Please Refresh the Location", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            editText.setError(null);
        }



        return valid;
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


    private void park() {

        mauths = FirebaseAuth.getInstance();
        FirebaseUser user = mauths.getCurrentUser();
        final String uid;
        uid = user.getUid();



        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (boolean_permission && isGPSEnable) {

            String value = editText.getText().toString();
            String value1 = timeValue.getText().toString();
            String value2 = tv_locality.getText().toString();
            String value3 = tv_area.getText().toString();
            String value4 = tv_city.getText().toString();
            String value8 = tv_address.getText().toString();
            String value9 = listItemCreationDate.toString();

/*
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this);

//Create the intent that’ll fire when the user taps the notification//

            // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
            Intent intent = new Intent(this, UserActive.class);
            //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.setAction(Long.toString(System.currentTimeMillis()));

            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(pendingIntent);

            mBuilder.setSmallIcon(R.drawable.carparklogo);
            mBuilder.setContentTitle("EzSmartPark");
            mBuilder.setContentText("You are Parking Now!!");

            NotificationManager mNotificationManager =

                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(001, mBuilder.build());


*/

            demoUser.child("/"+uid+"/").child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value5 = dataSnapshot.getValue(String.class);
                    String value = editText.getText().toString();
                    demoAuthority.child("Plate").child(value).child("Name").setValue(value5);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            demoUser.child("/"+uid+"/").child("Email").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value6 = dataSnapshot.getValue(String.class);
                    String value = editText.getText().toString();
                    demoAuthority.child("Plate").child(value).child("Email").setValue(value6);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            demoUser.child("/"+uid+"/").child("Phone Number").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value7 = dataSnapshot.getValue(String.class);
                    String value = editText.getText().toString();
                    demoAuthority.child("Plate").child(value).child("Phone Number").setValue(value7);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            editText.setFilters(new InputFilter[] { filter });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable arg) {

                    String s = arg.toString();
                    if(!s.equals(s.toUpperCase())){
                        s = s.toUpperCase();
                        editText.setText(s);
                        editText.setSelection(editText.getText().length());

                    }

                }
            });




            //push creates a unique id in database
            demoUser.child("/"+uid+"/").child("Plate Number").setValue(value);
            demoAuthority.child("Plate").child(value).child("Plate Number").setValue("NoPark");
            demoHistory.child("/"+uid+"/").child(value9).child(value1).child("Plate Number").setValue(value);

            demoUser.child("/"+uid+"/").child("Time").setValue(value1);
            demoAuthority.child("Plate").child(value).child("Time").setValue(value1);


            demoUser.child("/"+uid+"/").child("Locality").setValue(value2);
            demoAuthority.child("Plate").child(value).child("Locality").setValue(value2);
            demoHistory.child("/"+uid+"/").child(value9).child(value1).child("Locality").setValue(value2);

            demoUser.child("/"+uid+"/").child("Area").setValue(value3);
            demoAuthority.child("Plate").child(value).child("Area").setValue(value3);
            demoHistory.child("/"+uid+"/").child(value9).child(value1).child("Area").setValue(value3);

            demoUser.child("/"+uid+"/").child("City").setValue(value4);
            demoAuthority.child("Plate").child(value).child("City").setValue(value4);
            demoHistory.child("/"+uid+"/").child(value9).child(value1).child("City").setValue(value4);

            demoUser.child("/"+uid+"/").child("Address").setValue(value8);
            demoAuthority.child("Plate").child(value).child("Address").setValue(value8);
            demoHistory.child("/"+uid+"/").child(value9).child(value1).child("Address").setValue(value8);

            demoUser.child("/"+uid+"/").child("Date").setValue(value9);
            demoAuthority.child("Plate").child(value).child("Date").setValue(value9);


            //  Intent intent = new Intent(context, UserActive.class);
            //  startActivity(intent);
            //deduct();

        }

        else {
            fn_getlocation();
            showAlert();
        }


    }








    @Override
    public void onBackPressed(){
        Intent backMainTest = new Intent(this,UserMenu.class);
        startActivity(backMainTest);
        finish();
    }

}

