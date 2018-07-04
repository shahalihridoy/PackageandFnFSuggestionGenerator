package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Database db;
    Cursor c;
    String operator;

    static String from = "";
    static String msgbody;
    String fnfFrom = "";
    String fnfMsgbody;
    static String currentPackage;

    AlertDialog.Builder builder;
    Dialog dialog;

    protected static Helper bestPackage = new Helper();
    protected static Helper bestOperator = new Helper();

    List<String> listPermissionsNeeded = new ArrayList<>();
    DataLoader dataLoader = new DataLoader(this, this);

    TextView packageName;
    TextView superfnf;
    ListView fnfList;

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter vpa;

    String[] permission = new String[]{
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED
    };

// get Data from call_history

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        service for Nougat or onward version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            BackgroundServiceMarshmallow backgroundServiceMarshmallow = new BackgroundServiceMarshmallow(this);
//            backgroundServiceMarshmallow.startBackgroundService();

            //        get required data
            if (checkPermission()) {
                getData();
            }

        } else {
            startService(new Intent(this, CallListenerService.class));
            if (checkPermission()) {
                getData();
            }
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
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECEIVE_BOOT_COMPLETED, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED) {
                        getData();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)
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

    private void getData() {

        packageName = (TextView) findViewById(R.id.package_name);
        superfnf = (TextView) findViewById(R.id.super_fnf);
        fnfList = (ListView) findViewById(R.id.fnf_list);

        //        showing loading loaderHandler on create
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(R.layout.progress);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

//        final CustomHandler handler = new CustomHandler(this, dialog, packageName, superfnf, fnfList);
//        handler.mainActivity = this;

        final Handler handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setData();
                dialog.dismiss();
            }
        };

//        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                String operaotr = getOperator();
////                BanglalinkPackageAnalyser.Helper helper = new BanglalinkPackageAnalyser(getApplicationContext()).analyseBanglalink();
////                RobiPackageAnalyser.Helper helper = new RobiPackageAnalyser(getApplicationContext()).analyzeRobi();
////                GrameenPhonePackageAnalyzer.Helper helper = new GrameenPhonePackageAnalyzer(getApplicationContext()).analyzeGP();
////                AirtlePackageAnalyser.Helper helper = new AirtlePackageAnalyser(getApplicationContext()).analyseAirtel();
//                PackageAnalyser.Helper helper = new PackageAnalyser(getApplicationContext()).analysePackage();
//                packageName.setText(helper.packageName);
//                superfnf.setText(helper.superFnf);
//
//                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list, R.id.list_text, helper.fnf);
//                fnfList.setAdapter(adapter);
//
//
////                switch (operaotr.toUpperCase().charAt(0)){
////                    case 'G':
////                        GrameenPhonePackageAnalyzer.Helper helper= pkg.analyzeGP();
////                        packageName.setText(helper.packageName);
////                        superfnf.setText(helper.superFnf);
////                        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list,R.id.list_text,helper.fnf);
////                        fnfList.setAdapter(adapter);
////                    break;
////                    case 'R': getData();
////                }
//
//                dialog.dismiss();
//            }
//        };
        Thread t = new Thread() {
            @Override
            public void run() {
//                send this thread to stop it after the work is finished
                dataLoader.insertCallListToDatabase();
                analyseData();
//                dataLoader.fakeCallLog();
                handler1.sendEmptyMessage(0);
            }
        };

        t.start();

//        let main thread wait untill t finishes

//        synchronized (t){
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        try {
//            t.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
////        Main thread after waiting...........
//        dialog.dismiss();
//        setData();
    }

    private void analyseData() {

        operator = getOperator();
        bestOperator = new PackageAnalyser(MainActivity.this).analysePackage();

        switch (operator.toUpperCase().charAt(0)) {
            case 'A':
                airtel();
                break;
            case 'G':
                gp();
                break;
            case 'R':
                robi();
                break;
            case 'B':
                banglalink();
                break;
            case 'T':
                teletalk();
                break;
            default:
                break;
        }
    }

    private void setData() {
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewPager);

        vpa = new ViewPagerAdapter(getSupportFragmentManager());
        vpa.addFragment(new BestPackage(), "Best Package");
        vpa.addFragment(new FnF(), "FnF List");
        vpa.addFragment(new BestOperator(), "Best Operator");

        viewPager.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPager);
    }

    //    check operator name
    public String getOperator() {
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }

    void airtel() {

//        analyse best package
        bestPackage = new AirtlePackageAnalyser(MainActivity.this).analyseAirtel();

//        check package
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("8822", null, "P", null, null);

        while (!from.equals("8822")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        currentPackage = msgbody.split(":")[0];
        System.out.println("Main Activity");
        System.out.println(from + " : " + msgbody);
        System.out.println(currentPackage);
        if (msgbody.substring(0, 5).equals(bestPackage.packageName.substring(0, 5))) {
//            check fnf
            smsManager.sendTextMessage("8363", null, "F", null, null);
            while (!from.equals("8363")) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(currentPackage);
        }
    }

    void gp(){
        //        analyse best package
        bestPackage = new GrameenPhonePackageAnalyzer(MainActivity.this).analyzeGP();

//        check package
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("4444", null, "P", null, null);

        while (!from.equals("GP")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Main Activity");
        System.out.println(from + " : " + msgbody);

        if (msgbody.substring(0, 5).equals(bestPackage.packageName.substring(0, 5))) {
            currentPackage = msgbody.split(":")[0];
//            check fnf
            smsManager.sendTextMessage("2888", null, "FF", null, null);
            while (!from.equals("2888")) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(msgbody);
        }
    }

    void robi(){
        //        analyse best package
        bestPackage = new RobiPackageAnalyser(MainActivity.this).analyzeRobi();

//        check package
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("8822", null, "P", null, null);

        while (!from.equals("8822")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Main Activity");
        System.out.println(from + " : " + msgbody);

        if (msgbody.substring(0, 5).equals(bestPackage.packageName.substring(0, 5))) {
            currentPackage = msgbody.split(":")[0];
//            check fnf
            smsManager.sendTextMessage("8363", null, "F", null, null);
            while (!from.equals("8363")) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(msgbody);
        }
    }

    void teletalk(){
        //        analyse best package
        bestPackage = new TeletalkPackageAnalyser(MainActivity.this).analyseTeletalk();

//        check package
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("154", null, "P", null, null);

        while (!from.equals("154")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Main Activity");
        System.out.println(from + " : " + msgbody);

        if (msgbody.substring(0, 5).equals(bestPackage.packageName.substring(0, 5))) {
            currentPackage = msgbody.split(":")[0];
//            check fnf
            smsManager.sendTextMessage("363", null, "see", null, null);
            while (!from.equals("363")) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(msgbody);
        }
    }

    void banglalink(){
        //        analyse best package
        bestPackage = new AirtlePackageAnalyser(MainActivity.this).analyseAirtel();

//        check package
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("8822", null, "P", null, null);

        while (!from.equals("8822")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Main Activity");
        System.out.println(from + " : " + msgbody);

        if (msgbody.substring(0, 5).equals(bestPackage.packageName.substring(0, 5))) {
            currentPackage = msgbody.split(":")[0];
//            check fnf
            smsManager.sendTextMessage("8363", null, "F", null, null);
            while (!from.equals("8363")) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(msgbody);
        }
    }
}