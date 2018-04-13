package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
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
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_BOOT_COMPLETED
//            Manifest.permission.ACCESS_FINE_LOCATION
    };
    List<String> listPermissionsNeeded = new ArrayList<>();
    PackageAnalyzer pkg = new PackageAnalyzer(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        starting the service
        startService(new Intent(this,CallListenerService.class));

//        showing loading sign on create
        builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress);
        dialog = builder.create();
        dialog.show();

//        creating database
        db = new Database(getApplicationContext(), "CallLog", null, 13795);
        textView = (TextView) findViewById(R.id.textview_call);
        textView.setText("No call log found");

//        checking permission
        if (checkPermission())
            getCallDetails();
    }


    private boolean checkPermission() {

        for(String perm: permission){
            if(ContextCompat.checkSelfPermission(this,perm) != PackageManager.PERMISSION_GRANTED)
                listPermissionsNeeded.add(perm);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 5);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case 5: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                        getCallDetails();
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showDialogOK("SMS and Call Log Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkPermission();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
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
                            //                            //proceed with logic by disabling the related features or quit the app.
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


    private void getCallDetails() {
        StringBuffer sb = new StringBuffer();
//        check if data is already inserted from call list
        c = db.getData();
        if (c.getCount() != 0) {
            getCallDetailsFromDatabase(sb);
            return;
        }

        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Log :");
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            if (callDuration.equals("0") || phNumber.length()<11)
                continue;
            try{
                if (phNumber.charAt(0) == '+')
                    phNumber = phNumber.substring(3, phNumber.length());
            }
            catch (Exception e){
                continue;
            }
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
//                    for incoming call no process needed
                    continue;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
//                    for missed call no process neede
                    continue;
            }

//            inserting in database
            db.insertdata(phNumber, callDuration);
        }
        managedCursor.close();

//        call database after data is inserted
        c = db.getData();
        getCallDetailsFromDatabase(sb);
    }

    public void getCallDetailsFromDatabase(StringBuffer sb) {
        //        reading data from database
        if (c.moveToFirst()) {
            do {
//                appending data to string buffer
                sb.append("\nPhone Number:--- " + c.getString(0)+" \nCall duration in sec :--- " + c.getString(1));
                sb.append("\n----------------------------------");
            }
            while (c.moveToNext());
        }
        textView.setText(sb);

//        sending data to analyze bondhu package
        pkg.bondhu(c);
        pkg.nishchinto(c);
        pkg.djuice(c);
        pkg.smile(c);
//        hiding alert dialog
        dialog.dismiss();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
}


//
//
//        Thread t = new Thread(){
//            @Override
//            public void run() {
//                super.run();
//            }
//        };
//        Thread a = new Thread();
//        try {
//            a.join();
//            t.wait();
//            t.notify();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
