package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
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
    GrameenPhonePackageAnalyzer pkg = new GrameenPhonePackageAnalyzer(this);
    DataLoader dataLoader = new DataLoader(this, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        service for Nougat or onward version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BackgroundServiceMarshmallow backgroundServiceMarshmallow = new BackgroundServiceMarshmallow(this);
            backgroundServiceMarshmallow.startBackgroundService();

            //        get required data
            if (checkPermission()) {
                getData();
            }

        } else {
            startService(new Intent(this, CallListenerService.class));
            getData();
        }

//        creating database
        db = new Database(getApplicationContext(), "CallLog", null, 13795);

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
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_BOOT_COMPLETED)
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
                            getData();
//                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
//                                    .show();
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


// get Data from call_history

    TextView packageName;
    TextView superfnf;
    ListView fnfList;
    ArrayAdapter<String> adapter;

    private void getData() {
        packageName = (TextView) findViewById(R.id.package_name);
        superfnf = (TextView) findViewById(R.id.super_fnf);
        fnfList = (ListView) findViewById(R.id.fnf_list);
        adapter = new ArrayAdapter<String>(this, 0);

        //        showing loading loaderHandler on create
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(R.layout.progress);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String operaotr = getOperator();
//                BanglalinkPackageAnalyser.Helper blhelper = new BanglalinkPackageAnalyser(getApplicationContext()).analyseBanglalink();
//                RobiPackageAnalyser.Helper helper = new RobiPackageAnalyser(getApplicationContext()).analyzeRobi();
//                GrameenPhonePackageAnalyzer.Helper gphelper = new GrameenPhonePackageAnalyzer(getApplicationContext()).analyzeGP();

                PackageAnalyser.Helper helper = new PackageAnalyser(getApplicationContext()).analysePackage();
                packageName.setText(helper.packageName);
                superfnf.setText(helper.superFnf);
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list, R.id.list_text, helper.fnf);
                fnfList.setAdapter(adapter);


//                switch (operaotr.toUpperCase().charAt(0)){
//                    case 'G':
//                        GrameenPhonePackageAnalyzer.Helper helper= pkg.analyzeGP();
//                        packageName.setText(helper.packageName);
//                        superfnf.setText(helper.superFnf);
//                        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list,R.id.list_text,helper.fnf);
//                        fnfList.setAdapter(adapter);
//                    break;
//                    case 'R': getData();
//                }

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

    //    check operator name
    public String getOperator() {
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }
}