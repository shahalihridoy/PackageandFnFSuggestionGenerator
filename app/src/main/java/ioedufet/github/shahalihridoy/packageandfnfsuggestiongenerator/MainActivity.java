package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    TextView textView;
    Database db;
    Cursor c;
    AlertDialog.Builder builder;
    Dialog dialog;
    String[] permissions = new String[]{
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS
//            Manifest.permission.ACCESS_FINE_LOCATION
    };
    List<String> listPermissionsNeeded = new ArrayList<>();
    PackageAnalyzer pkg = new PackageAnalyzer(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        checkPermission();
    }

    //    checking permission
    public boolean checkPermission() {

        //        taking permission on runtime
        for (String p : permissions){
            if (ContextCompat.checkSelfPermission(this, p)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
//
//                // Permission is not granted
//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
//
//                    showDialogOK("Call log and SMS Services Permission required for this app",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    switch (which) {
//                                        case DialogInterface.BUTTON_POSITIVE:
//                                            listPermissionsNeeded.add(p);
//                                            break;
//                                        case DialogInterface.BUTTON_NEGATIVE:
//                                            System.exit(0);
//                                            break;
//                                    }
//                                }
//                            });
//
////                ActivityCompat.requestPermissions(this,
////                        new String[]{Manifest.permission.READ_CALL_LOG}, 5
////                );
//                } else {
//                    // No explanation needed; request the permission
//                    System.out.println("taking permision again");
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{p}, 5
//                    );
//                }
            } else {
                // Permission has already been granted
                getCallDetails();
            }
        }
        if(!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 5
            );
        }
        return true;
    }

    //    showing dialogue
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // If request is cancelled, the result arrays are empty.
                for(int i=0;i<grantResults.length;i++)
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    getCallDetails();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Access denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
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
            if (callDuration.equals("0"))
                continue;
            if (phNumber.charAt(0) == '+')
                phNumber = phNumber.substring(3, phNumber.length());
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
            db.insertdata(phNumber, callDuration, dir);
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
                sb.append("\nPhone Number:--- " + c.getString(0) + " \nCall Type:--- " + c.getString(2) + " \nCall duration in sec :--- " + c.getString(1));
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
