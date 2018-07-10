package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //<editor-fold desc="All variable are declared here">
    Database db;
    Cursor c;
    static String operator;

    static String from = "";
    static String msgbody = "...";
    static String currentPackage = "Unknown";
    static String save = "xxx";

    String MY_PREFS_NAME = "Package & FnF Suggestion Generator";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    AirtlePackageAnalyser apa;
    BanglalinkPackageAnalyser bpa;
    GrameenPhonePackageAnalyzer gpa;
    RobiPackageAnalyser rpa;
    TeletalkPackageAnalyser tpa;
    PackageAnalyser pa;

    AlertDialog.Builder builder;
    AlertDialog.Builder chooser;
    Dialog dialog;
    //<editor-fold desc="final Handler handler = handleMessage(msg)->{};">
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setData();
                    dialog.dismiss();
                    break;

                case 1:
                    chooser.show();
                    break;

                default:
                    break;
            }
        }
    };
    //</editor-fold>

    protected static Helper bestPackage;
    protected static Helper bestOperator;

    List<String> listPermissionsNeeded = new ArrayList<>();
    DataLoader dataLoader = new DataLoader(this, this);

    TextView packageName;
    TextView superfnf;
    ListView fnfList;

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter vpa;
    //</editor-fold>

    //<editor-fold desc="String[] permission = new String[]{...};">
    String[] permission = new String[]{
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED
    };
    //</editor-fold>

// get Data from call_history

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        preferences
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = prefs.edit();

//        service for Nougat or onward version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            new Thread() {
                @Override
                public void run() {
                    BackgroundServiceMarshmallow backgroundServiceMarshmallow = new BackgroundServiceMarshmallow(MainActivity.this);
                    backgroundServiceMarshmallow.startBackgroundService();
                }
            }.start();

//        get required data
            if (checkPermission()) {
                getData();
            }

        } else {
            new Thread() {
                @Override
                public void run() {
                    startService(new Intent(MainActivity.this, CallListenerService.class));
                }
            }.start();
            if (checkPermission()) {
                getData();
            }
        }

//        creating database
        db = new Database(getApplicationContext(), "CallLog", null, 13795);

//        instantiate variables
        apa = new AirtlePackageAnalyser(this);
        bpa = new BanglalinkPackageAnalyser(this);
        gpa = new GrameenPhonePackageAnalyzer(this);
        rpa = new RobiPackageAnalyser(this);
        tpa = new TeletalkPackageAnalyser(this);
        pa = new PackageAnalyser(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.check:
//                show chooser
                handler.sendEmptyMessage(1);
                break;
            case R.id.settings:
                startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 5);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
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
                            && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED) {
                        getData();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
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
//                handler.sendEmptyMessage(0);
            }
        };

//        if the app has not run yet
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
        bestOperator = pa.analysePackage();

        System.out.println(operator);

        //<editor-fold desc="analyse best pack based on Operator">
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
        //</editor-fold>
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
        bestPackage = apa.analyseAirtel();

//        check package
        Thread t = new Thread() {
            @Override
            public void run() {

                String ussdCode = "*121*2*1*1" + Uri.encode("#");
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));
            }
        };

        t.start();

//        wait untill message is received
        while (!from.equals("8822")) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        currentPackage = msgbody;

        System.out.println("Main Activity");
        System.out.println(from + " : " + msgbody);

//        check fnf
        if (msgbody.substring(0, 4).equals(bestPackage.packageName.substring(0, 4))) {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("8363", null, "F", null, null);
            while (!from.equals("8363")) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

//        dismiss dialogue
        handler.sendEmptyMessage(0);
    }

    void gp() {

//        analyse best package
        bestPackage = gpa.analyzeGP();

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

//        check how much balance is saved
        String temp = msgbody.toLowerCase();
        if (temp.charAt(9) == 'B') {
            save = Double.toString(gpa.bondhuHelper.cost - bestPackage.cost).split("\\.")[0];
        } else if (temp.charAt(9) == 'N') {
            save = Double.toString(gpa.nishchintoHelper.cost - bestPackage.cost).split("\\.")[0];
        } else if (temp.charAt(9) == 'S') {
            save = Double.toString(gpa.smileHelper.cost - bestPackage.cost).split("\\.")[0];
        } else if (temp.charAt(9) == 'D') {
            save = Double.toString(gpa.djuiceHelper.cost - bestPackage.cost).split("\\.")[0];
        }

        handler.sendEmptyMessage(0);

//        check fnf only if the current package is same as best package
//        if (msgbody.charAt(9) == bestPackage.packageName.charAt(0)) {
//            smsManager.sendTextMessage("2888", null, "FF", null, null);
//            while (!from.equals("GP")) {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
////            when fnf list is received, analyse it
//            for (String number : msgbody.split("\\r?\\n"))
//                if (number != null)
//                    if (number.trim().charAt(0) == '0')
//                        System.out.println(":" + number + ":");
//            System.out.println(msgbody);
//        }
    }

    void robi() {
        //        analyse best package
        bestPackage = rpa.analyzeRobi();

//        check package
        SmsManager smsManager = SmsManager.getDefault();

//        save current package to shared preference when it doesn't exist
        String MY_PREFS_NAME = "Package & FnF Suggestion Generator";
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (!prefs.contains("currentPackage")) {

//            send message
            smsManager.sendTextMessage("8822", null, "P", null, null);

            //<editor-fold desc="wait untill message is received">
            while (!from.equals("8822")) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //</editor-fold>

            currentPackage = msgbody.split("\\.")[0];

//            save to preference
            editor.putString("currentPackage", currentPackage);
            editor.apply();
            editor.commit();

        } else {
            currentPackage = prefs.getString("currentPackage", "Apni Shorol");
        }

        save = Double.toString(rpa.shorolHelper.cost - bestPackage.cost).split("\\.")[0];
        System.out.println("Main Activity");
        System.out.println(from + " : " + msgbody);

        currentPackage = msgbody.split("\\.")[0];

        if (msgbody.substring(0, 4).equals(bestPackage.packageName.substring(0, 4))) {
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

        for (Helper h : bestPackage.packageList)
            System.out.println(h.packageName + " : " + (int) Math.round(h.cost));

//        dismiss dialogue
        handler.sendEmptyMessage(0);
    }

    void teletalk() {
//        analyse best package
        bestPackage = tpa.analyseTeletalk();

//        choose which package you have
        chooser = new AlertDialog.Builder(MainActivity.this);
        chooser.setTitle("Choose your current package");
        final String packageId[] = {"Youth Package", "Projonmo Package", "Shadheen Package"};
        chooser.setItems(packageId, new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (packageId[which].charAt(0)) {
                    case 'Y':
                        save = Double.toString(tpa.youthHelper.cost - bestPackage.cost).split("\\.")[0];
                        break;
                    case 'P':
                        save = Double.toString(tpa.projonmoHelper.cost - bestPackage.cost).split("\\.")[0];
                        break;
                    case 'S':
                        save = Double.toString(tpa.shadheenHelper.cost - bestPackage.cost).split("\\.")[0];
                        break;
                    default:
                        break;
                }

                currentPackage = packageId[which];
//                setData and dismiss dialogue
                handler.sendEmptyMessage(0);
            }
        });

        handler.sendEmptyMessage(1);

//        check package
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage("154", null, "P", null, null);
//
//        while (!from.equals("154")) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println("Main Activity");
//        System.out.println(from + " : " + msgbody);
//
//        if (msgbody.substring(0, 5).equals(bestPackage.packageName.substring(0, 5))) {
//            currentPackage = msgbody.split(":")[0];
////            check fnf
//            smsManager.sendTextMessage("363", null, "see", null, null);
//            while (!from.equals("363")) {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println(msgbody);
//        }
    }

    @SuppressLint("MissingPermission")
    void banglalink() {

        if(!prefs.contains("currentPackage")){
//        request for accessibility settings
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            if (!USSDService.isAccessibilityOn) {
                startActivityForResult(intent, 5);
            }
//        wait untill accessibility is turned on for ten seconds
            for (int i=0; i<30 && !USSDService.isAccessibilityOn; i++ ) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }

            String ussdCode = "*999*2" + Uri.encode("#");
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ussdCode)));

            while (msgbody.charAt(0) != 'Y'){
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }
            }

            System.out.println("this is form main activity "+msgbody);
            currentPackage = msgbody;

            editor.putString("currentPackage",msgbody);
            editor.apply();
            editor.commit();
        } else {
            currentPackage = prefs.getString("currentPackage","Play Package");
        }

//        analyse best package
        bestPackage = bpa.analyseBanglalink();

//        choose which package you have
        chooser = new AlertDialog.Builder(MainActivity.this);
        chooser.setTitle("Choose your current package");
        final String packageId[] = {"Play Package", "Hello Package", "Desh 10fnf Package", "Desh Ek Rate Package"};

        chooser.setItems(packageId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                taking the character before the word "package"
                String s = packageId[which];
                switch (s.charAt(s.length() - 9)) {
                    case 'y':
                        save = Double.toString(bpa.playHelper.cost - bestPackage.cost).split("\\.")[0];
//            save to preference
                        editor.putString("currentPackage", packageId[which]);
                        editor.apply();
                        editor.commit();
                        break;
                    case 'o':
//            save to preference
                        editor.putString("currentPackage", packageId[which]);
                        editor.apply();
                        editor.commit();
                        save = Double.toString(bpa.helloHelper.cost - bestPackage.cost).split("\\.")[0];
                        break;
                    case 'f':
//            save to preference
                        editor.putString("currentPackage", packageId[which]);
                        editor.apply();
                        editor.commit();
                        save = Double.toString(bpa.desh10Helper.cost - bestPackage.cost).split("\\.")[0];
                        break;
                    case 'e':
//            save to preference
                        editor.putString("currentPackage", packageId[which]);
                        editor.apply();
                        editor.commit();
                        save = Double.toString(bpa.deshEkRateHelper.cost - bestPackage.cost).split("\\.")[0];
                        break;
                    default:
                        break;
                }

                currentPackage = packageId[which];

//                send notification to handler to set UI and dismiss dialogue
                handler.sendEmptyMessage(0);
//                BestPackage.setVariable();

            }
        });

//        show chooser
//        save current package to shared preference when it doesn't exist
//        if (!prefs.contains("currentPackage")) {
//            handler.sendEmptyMessage(1);
//        } else {
//            currentPackage = prefs.getString("currentPackage", "Apni Shorol");
//            handler.sendEmptyMessage(0);
//        }

        handler.sendEmptyMessage(0);
//        long start = System.currentTimeMillis();
//        while (!from.equals("8822") && (System.currentTimeMillis()-start) < (2*60*1000)){
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println("Main Activity");
//        System.out.println(from + " : " + msgbody);
//
//        if (msgbody.substring(0, 5).equals(bestPackage.packageName.substring(0, 5))) {
//            currentPackage = msgbody.split(":")[0];
////            check fnf
//            smsManager.sendTextMessage("8363", null, "F", null, null);
//            while (!from.equals("8363")) {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println(msgbody);
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "you have turned it on", Toast.LENGTH_LONG).show();
            }
        }
    }
}