package com.afiq.ezsmartpark1;

/**
 * Created by Asus on 13/11/2017.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.content.Context;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.content.Context;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;


import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;

public class ForegroundService extends Service {
    Timer _t,timer;
    private static final String LOG_TAG = "ForegroundService";

    @Override
    public void onCreate() {
        super.onCreate();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // final Context context = this;
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ");



      //      NotificationCompat.Builder mBuilder =
             //       new NotificationCompat.Builder(this);

//Create the intent that’ll fire when the user taps the notification//

            // Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
      //      Intent intent = new Intent(this, UserActive.class);
            Intent notificationIntent = new Intent(this, UserActive.class);
            //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            notificationIntent.setAction(Long.toString(System.currentTimeMillis()));

            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);


         /*
            mBuilder.setContentIntent(pendingIntent);

            mBuilder.setSmallIcon(R.drawable.carparklogo);
            mBuilder.setContentTitle("EzSmartPark");
            mBuilder.setContentText("You are Parking Now!!");

            NotificationManager mNotificationManager =

                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(001, mBuilder.build());

            */


/*
            Intent notificationIntent = new Intent(this, UserActive.class);


            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            notificationIntent.setAction(Long.toString(System.currentTimeMillis()));

            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

*/


            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.carparklogo33);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("EzSmart Park")
                    .setTicker("EzSmart Park")
                    .setContentText("Now Parking....")
                    .setSmallIcon(R.drawable.carparklogo33)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)




            .build();
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                    notification);


        }


         else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {

           // _t.cancel();
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();


        }
        return START_STICKY;
    }






    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(LOG_TAG, "In onDestroy");
    }




    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }


    /*

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        // Handle application closing
        //_t.cancel();


        // Destroy the service
        stopSelf();
    }


*/


}
