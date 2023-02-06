package com.example.downloadingapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.listener.MultiFileDownloadListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUrl;

    private Button buttonDownload;
    File folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();


        // finding edittext by its id
        editTextUrl = findViewById(R.id.url_etText);
        // finding button by its id
        buttonDownload = findViewById(R.id.btn_download);

        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String url = editTextUrl.getText().toString().trim();
                downloadFile();
//                fileloadermethod();
            }
        });
    }

    private void downloadFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, 155);

            } else {
//                startdownloadmethod();
                prdownloadFile();

            }

        } else {
//            startdownloadmethod();
            prdownloadFile();
        }


    }
    private void prdownloadFile(){
        folder=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/mydir");
        String url="https://drive.google.com/uc?export=download&id=1EJagmcibdBwefNDNeWt11YwKtsVOyNPf";
//        String url="https://drive.google.com/uc?export=download&id=11w6h6tAKbJK_OHpAmt2eMVTPM3sWH6pc";
//        String url="https://drive.google.com/uc?export=download&id=1FGUcwrrlTHL0S00QcPlFNs24OREJch1R";
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, getString(R.string.NEWS_CHANNEL_ID))
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.ic_file_download);
        PRDownloader.download(url,folder.getPath(),System.currentTimeMillis()+".pdf")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                }).setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                }).setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                }).setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        long currentBytes=progress.currentBytes/1024;
                        long currentmb=currentBytes/1024;
                        long totalBytes= progress.totalBytes/1024;
                        long totalmb=totalBytes/1024;
                        /*Intent intent = new Intent(MainActivity.this, background_service.class);
                        intent.putExtra("currentsizemd",currentmb);
                        intent.putExtra("totalsizemd",currentmb);
                        MainActivity.this.startService(intent);*/

                                // .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_icon_large))

                        int one = (int) totalmb;
                        int two = (int) currentmb;
                        if (one!=two){
                            builder.setContentTitle("Downloading...");
                            builder.setProgress(one, two, false);
                            builder.setContentText(two+"MB"+"/"+one+"MB")
                                    .setOngoing(false);
                            notificationManagerCompat.notify(1, builder.build());
                        }else if (one==two){
                            builder.setContentTitle("Download Completed");
                            builder.setContentText("Done");
                            builder.setProgress(0, 0, false);
//                            builder.setContentText( "MB"+"/"+"MB")
                                    builder.setOngoing(false);
                            notificationManagerCompat.notify(1, builder.build());

                        }

//                        Toast.makeText(MainActivity.this, currentmb+"MB"+"/"+totalmb+"MB", Toast.LENGTH_SHORT).show();


                    }
                }).start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        /*builder.setContentTitle("Download Completed");
                        builder.setProgress(0, 0, false);
                        builder.setContentText( "MB"+"/"+"MB")
                                .setOngoing(false);
                        notificationManagerCompat.notify(1, builder.build());*/
                        Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(MainActivity.this, "Download Error", Toast.LENGTH_SHORT).show();


                    }
                });




    }

    private void startdownloadmethod() {
        String url = "https://cdn.stocksnap.io/img-thumbs/960w/pizza-wine_IJESKJTYB6.jpg";
//        String url =  editTextUrl.getText().toString().trim();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloding...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,   System.currentTimeMillis()+".jpg");
        //request.setMimeType("*/*");
        DownloadManager manager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }
    private void fileloadermethod(){

        final String[] uris = {"https://images.pexels.com/photos/45170/kittens-cat-cat-puppy-rush-45170.jpeg",
                "https://upload.wikimedia.org/wikipedia/commons/3/3c/Enrique_Simonet_-_Marina_veneciana_6MB.jpg",
                "https://d15shllkswkct0.cloudfront.net/wp-content/blogs.dir/1/files/2017/01/Google-acquires-Fabric.png"};
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, getString(R.string.NEWS_CHANNEL_ID))
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.drawable.ic_file_download);

        FileLoader.multiFileDownload(this)
                .fromDirectory(Environment.DIRECTORY_PICTURES, FileLoader.DIR_EXTERNAL_PUBLIC)
                .progressListener(new MultiFileDownloadListener() {
                    @Override
                    public void onProgress(File downloadedFile, int progress, int totalFiles) {
                        if (progress!=totalFiles){
                            builder.setContentTitle("Downloading...");
                            builder.setProgress(totalFiles, progress, false);
                            builder.setContentText(progress+"MB"+"/"+totalFiles+"MB")
                                    .setOngoing(false);
                            notificationManagerCompat.notify(1, builder.build());
                        }/*else if (progress==totalFiles){
                            builder.setContentTitle("Download Completed");
                            builder.setContentText("Done");
                            builder.setProgress(0, 0, false);
//                            builder.setContentText( "MB"+"/"+"MB")
                            builder.setOngoing(false);
                            notificationManagerCompat.notify(1, builder.build());

                        }*/


                    }

                    @Override
                    public void onError(Exception e, int progress) {
                        super.onError(e, progress);
                    }
                }).loadMultiple(uris);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 155: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
//                    startdownloadmethod();
                     prdownloadFile();

                } else {
                    Toast.makeText(this, "permission Denied...", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
