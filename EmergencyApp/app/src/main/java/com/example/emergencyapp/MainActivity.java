package com.example.emergencyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button btnSendAlert;
    private FusedLocationProviderClient fusedLocationClient;
    private MediaRecorder recorder;
    private File audio = null;
    private Location currentLocation;
    private boolean callForHelp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSendAlert = findViewById(R.id.btnSendAlert);
        // TODO: request permissions for
        // <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
        //    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
        //    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
        //    <uses-permission android:name="android.permission.RECORD_AUDIO" />
        //    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
        //    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
        //    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btnSendAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callForHelp = !callForHelp;
                if (callForHelp) {
                    sendCurrentLocation();
                } else{
                    stopRecording();
                }
            }
        });
    }

    private void sendCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_LONG).show();
                        btnSendAlert.setBackgroundColor(getResources().getColor(R.color.teal_200));
                        btnSendAlert.setText("Stop Recording");
                        currentLocation = location;
                        startRecording();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                    }
                });
    }

    private void startRecording() {
        File dir = Environment.getExternalStorageDirectory();
        try {
            audio = File.createTempFile("aha", ".3gp", dir);
        } catch (IOException e) {
            Log.e("ko", e.toString());
            return;
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audio.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();

    }

    private void stopRecording() {
        btnSendAlert.setBackgroundColor(getResources().getColor(R.color.red));
        btnSendAlert.setText("Help Me");
        recorder.stop();
        recorder.release();
        addRecordingToMediaLibrary();
        sendInfo();
    }

    private void sendInfo() {
        String android_id = Settings.Secure.getString(getApplicationContext()
                .getContentResolver(), Settings.Secure.ANDROID_ID);
        // TODO: send the recording, location, and id to the server
        // you can get the absolute path from audio.getAbsolutePath
        // id and location are global variables
    }

    protected void addRecordingToMediaLibrary() {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audio.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audio.getAbsolutePath());
        ContentResolver contentResolver = getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
    }
}