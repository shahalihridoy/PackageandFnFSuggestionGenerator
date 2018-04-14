package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    TextView textView;
    Database db;
    Cursor c;
    AlertDialog.Builder builder;
    Dialog dialog;
    String[] permission = new String[]{
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED
    };

    List<String> listPermissionsNeeded = new ArrayList<>();
    PackageAnalyzer pkg = new PackageAnalyzer(this);
    DataLoader dataLoader = new DataLoader(this, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        starting the service
        startService(new Intent(this, CallListenerService.class));

//        creating database
        db = new Database(getApplicationContext(), "CallLog", null, 13795);
        textView = (TextView) findViewById(R.id.textview_call);
        textView.setText("No call log found");

//        get required data
        if(checkPermission())
        getData();
    }


    private boolean checkPermission() {

        for (String perm : permission) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED)
                listPermissionsNeeded.add(perm);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 5);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 5: {

                Map<String, Integer> perms = new HashMap<>();

                // Initialize the map with both permissions
                perms.put(Manifest.permission.READ_CALL_LOG, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECEIVE_BOOT_COMPLETED, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED) {
                        getData();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                                ) {
                            showDialogOK("Permission is required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkPermission();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    System.exit(0);
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void getData() {

        //        showing loading loaderHandler on create
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(R.layout.progress);
        dialog = builder.create();
        dialog.show();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                dataLoader.getCallDetailsFromDatabase(textView);
                dialog.dismiss();
            }
        };

        new Thread() {
            @Override
            public void run() {
                dataLoader.insertCallListToDatabase();
                handler.sendEmptyMessage(0);
                this.interrupt();
            }
        }.start();

    }
}