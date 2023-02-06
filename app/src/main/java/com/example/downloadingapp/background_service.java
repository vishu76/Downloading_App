package com.example.downloadingapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class background_service extends Service {
    double currentfilesize, totalfilesize;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        currentfilesize = intent.getDoubleExtra("currentsizemd", 0);
        totalfilesize = intent.getDoubleExtra("totalsizemd", 0);
        Log.e("service", "Background service is running... " + currentfilesize + "/" + totalfilesize);
//         Intent intent = new Intent(background_service.this, MainActivity.class);
//         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//         PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(background_service.this, getString(R.string.NEWS_CHANNEL_ID))
                .setSmallIcon(R.drawable.ic_file_download)
                // .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_icon_large))
                .setContentTitle("Contacts Loading...");
                 int one = (int) totalfilesize;
                 int two = (int) currentfilesize;
                 builder.setProgress(one, two, false);
                 builder.setContentTitle("Contant Loading...  " + one + "%")
                .setOngoing(false)
                .setOnlyAlertOnce(true);
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("This is text, that will be shown as part of notification"))
//                 .setPriority(NotificationCompat.PRIORITY_LOW);
//                 .setContentIntent(pendingIntent);
//                .setChannelId(getString(R.string.NEWS_CHANNEL_ID))
//                .setAutoCancel(true);
//                .setProgress(0,0,true);
//              notificationManagerCompat.notify(1, builder.build());
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                int one = (int) totalfilesize;
                int two = (int) currentfilesize;

                builder.setProgress(one, two, false);
                builder.setContentTitle("Contant Loading...  " + one + "%");
                notificationManagerCompat.notify(1, builder.build());
//                 SystemClock.sleep(1000);
*/
/*
                 for (incr = 0; incr <= totalfilesize; incr+=10) {
                     int perc=incr*100/arraysize;
                     builder.setProgress(arraysize, incr, false);
//                     builder.setContentTitle("Contant Loading...  "+perc+"%");
                    // builder.addAction(R.drawable.ic_import_contacts,"Cancel",null);
                     builder.setContentText(incr+" / "+arraysize);
                     notificationManagerCompat.notify(1, builder.build());
//                     SystemClock.sleep(1000);
                     try {
                         Thread.sleep(0);
                         Log.d("TAG", "sleep failure");
                     } catch (InterruptedException e) {
                }
                 }
*//*

                builder.setContentTitle("Contacts Loaded")
                        .setContentText("Done")
                        .setOngoing(false)
                        .setProgress(0, 0, false);
                notificationManagerCompat.notify(1, builder.build());
            }
        }).start();
*/
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("service", "Background service is not running...");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.NEWS_CHANNEL_ID), getString(R.string.CHANNEL_NEWS), NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("hello everyone welcome ");
            notificationChannel.setShowBadge(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
