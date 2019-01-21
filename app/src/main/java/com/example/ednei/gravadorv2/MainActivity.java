package com.example.ednei.gravadorv2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final int REQUEST_PERMISSION_CODE = 1000;
    private Button rec, play, stop, stopRec;
    private TextView textView;
    private String pathSalve = "";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rec = (Button) findViewById(R.id.rec);
        stopRec = (Button) findViewById(R.id.stopRec);
        textView = (TextView) findViewById(R.id.textViewRecording);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.inflateMenu(R.menu.menu_toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new MyList(getApplicationContext()));

        try {

            if (checkPermissionFromDevice()) {

                listView.setOnItemClickListener(this);
                rec.setOnClickListener(this);
                stopRec.setOnClickListener(this);

            } else {
                requestPermission();
                Toast.makeText(this, "RequestPermission ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ReadFilesPath.read();
    }

    private void setupMediaRecorder() {

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSalve);
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
    }

    //verify type permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkPermissionFromDevice() {

        int write_external_extorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recorder_audio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_extorage == PackageManager.PERMISSION_GRANTED && recorder_audio == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String files = (String) listView.getItemAtPosition(position).toString();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        Toast.makeText(this, "Files: " + files, Toast.LENGTH_SHORT).show();

        mediaPlayer = new MediaPlayer();

        try{

            mediaPlayer.setDataSource(files);
            mediaPlayer.prepare();
            mediaPlayer.start();
            textView.setText("Playing...");

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Error Mediaplayer: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rec:
                recording();
                break;
            case R.id.stopRec:
                stopRecording();
                break;
        }
    }

    private void stopRecording() {

        mediaRecorder.stop();
        stopRec.setEnabled(false);
        rec.setEnabled(false);
        textView.setVisibility(View.VISIBLE);
        textView.setText("Stop recording");
    }

    private void recording() {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        Date date = new Date();

        String audio = dateFormat.format(date) + ".3gp";
        Toast.makeText(MainActivity.this, "Data: " + audio, Toast.LENGTH_LONG).show();
        pathSalve = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + audio;

        setupMediaRecorder();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            stopRec.setEnabled(true);
            rec.setEnabled(false);
            textView.setVisibility(View.VISIBLE);
            textView.setText("Recording....");
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error MediaRecorder: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
