package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AirtlePackageAnalyser {
    Database db;
    Context context;
    Cursor c;
    double min = 99999999.0;
    String packageName = null;
    Helper final_helper;

    static final String MY_PREFS_NAME = "Package & FnF Suggestion Generator";
    SharedPreferences prefs;

    Helper golpoHelper = new Helper("Golpo","Airtel","*121*82"); //add # after code
    Helper addaHelper = new Helper("Adda","Airtel","*121*81");
    Helper superAddaHelper = new Helper("Super Adda","Airtel","*121*88");
    Helper dostiHelper = new Helper("Dosti","Airtel","*121*89");
    Helper hoichoiHelper = new Helper("Hoichoi","Airtel","*121*87");
    Helper shobaiEk = new Helper("Shobai Ek","Airtel","Recharge taka 26");
    Helper shobaiFnf = new Helper("Shobai FnF","Airtel","Recharge taka 26");
    Helper foortiHelper = new Helper("Foorti","Airtel","*121*85");
    Helper kothaHelper = new Helper("Kotha","Airtel","*121*83");

    //    constructor receiving context
    public AirtlePackageAnalyser(Context context) {
        db = new Database(context, "CallLog", null, 13795);
        this.context = context;
        prefs = context.getSharedPreferences(MY_PREFS_NAME,Context.MODE_PRIVATE);
    }

    public Helper analyseAirtel() {

        addaHelper.fnf.clear();
        superAddaHelper.fnf.clear();
        dostiHelper.fnf.clear();
        hoichoiHelper.fnf.clear();

        tenSecondPulsePackageAnalysis();
        oneSecondPulsePackageAnalysis();
        shobaiFnf();

        if (golpoHelper.cost < min) {
            min = golpoHelper.cost;
            final_helper = golpoHelper;
            System.out.println(final_helper.packageName + ": " + final_helper.cost);
        }
        if (addaHelper.cost < min) {
            min = addaHelper.cost;
            final_helper = addaHelper;
            System.out.println(final_helper.packageName + ": " + final_helper.cost);
        }
        if (superAddaHelper.cost < min) {
            min = superAddaHelper.cost;
            final_helper = superAddaHelper;
            System.out.println(final_helper.packageName + ": " + final_helper.cost);
        }
        if (dostiHelper.cost < min) {
            min = dostiHelper.cost;
            final_helper = dostiHelper;
            System.out.println(final_helper.packageName + ": " + final_helper.cost);
        }
        if (foortiHelper.cost < min) {
            min = foortiHelper.cost;
            final_helper = foortiHelper;
            System.out.println(final_helper.packageName + ": " + final_helper.cost);
        }
        if (hoichoiHelper.cost < min) {
            min = hoichoiHelper.cost;
            final_helper = hoichoiHelper;
            System.out.println(final_helper.packageName + ": " + final_helper.cost);
        }
        if (kothaHelper.cost < min) {
            min = kothaHelper.cost;
            final_helper = kothaHelper;
            System.out.println(final_helper.packageName + ": " + final_helper.cost);
        }
        if(shobaiEk.cost < min){
            min = shobaiEk.cost;
            final_helper = shobaiEk;
            System.out.println(final_helper.packageName + ": " + final_helper.cost);
        }
        if(shobaiFnf.cost < min){
            min = shobaiFnf.cost;
            final_helper = shobaiFnf;
            System.out.println(final_helper.packageName + ": " + final_helper.cost);
        }

        final_helper.packageList.add(new CostHelper(addaHelper.packageName,addaHelper.cost));
        final_helper.packageList.add(new CostHelper(dostiHelper.packageName,dostiHelper.cost));
        final_helper.packageList.add(new CostHelper(foortiHelper.packageName,foortiHelper.cost));
        final_helper.packageList.add(new CostHelper(golpoHelper.packageName,golpoHelper.cost));
        final_helper.packageList.add(new CostHelper(hoichoiHelper.packageName,hoichoiHelper.cost));
        final_helper.packageList.add(new CostHelper(kothaHelper.packageName,kothaHelper.cost));
        final_helper.packageList.add(new CostHelper(superAddaHelper.packageName,superAddaHelper.cost));
        final_helper.packageList.add(new CostHelper(shobaiEk.packageName,shobaiEk.cost));
        final_helper.packageList.add(new CostHelper(shobaiFnf.packageName,shobaiFnf.cost));

        return final_helper;
    }

    private void shobaiFnf() {
        c = db.rawData();
        Long duration = (new Date().getTime() - Long.parseLong(prefs.getString("startDate","0")))/(1000*60*60*24);
        Long cost1 = duration*7*26;
        Long cost2 = duration*15*24;
        if(c.getCount()>0){
            c.moveToFirst();
            do {

//                shobai ek
                shobaiEk.cost += Double.valueOf(c.getString(1)) * 1 / 100;

//                shobai fnf
                if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                    shobaiFnf.cost += Double.valueOf(c.getString(1)) * 0.5 / 100;
                } else shobaiFnf.cost += Double.valueOf(c.getString(1)) * 1 / 100;

            }while (c.moveToNext());
        }

//        here cost is fixed and obvious for user
        if(cost1 > shobaiFnf.cost)
            shobaiFnf.cost = cost1;
        if(cost2 > shobaiEk.cost)
            shobaiEk.cost = cost2;

    }

    public void tenSecondPulsePackageAnalysis() {
        c = db.tenSecondPulse();
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
//                golpo pack
                golpoHelper.cost += Double.valueOf(c.getString(1)) * 16.67 / 1000;

//                adda pack
                if (addaHelper.counter < 8) {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        addaHelper.cost += Double.valueOf(c.getString(1)) * 6 / 1000;
                        addaHelper.fnf.add(c.getString(0));
                    } else {
                        addaHelper.cost += Double.valueOf(c.getString(1)) * 13.333 / 1000;
                        addaHelper.fnf.add(c.getString(0));
                    }
                    addaHelper.counter++;
                } else {
                    addaHelper.cost += Double.valueOf(c.getString(1)) * 22.5 / 1000;
                }

//                super add pack
                if (superAddaHelper.counter < 29) {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        superAddaHelper.cost += Double.valueOf(c.getString(1)) * 5 / 1000;
                        superAddaHelper.fnf.add(c.getString(0));
                    } else {
                        superAddaHelper.cost += Double.valueOf(c.getString(1)) * 10 / 1000;
                        superAddaHelper.fnf.add(c.getString(0));
                    }
                    superAddaHelper.counter++;
                } else {
                    superAddaHelper.cost += Double.valueOf(c.getString(1)) * 22.5 / 1000;
                }

//                dosti pack
                if (dostiHelper.counter < 5) {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        dostiHelper.cost += Double.valueOf(c.getString(1)) * 4.1667 / 1000;
                        dostiHelper.fnf.add(c.getString(0));
                    } else {
                        dostiHelper.cost += Double.valueOf(c.getString(1)) * 11 / 1000;
                        dostiHelper.fnf.add(c.getString(0));
                    }
                    dostiHelper.counter++;
                } else {
                    dostiHelper.cost += Double.valueOf(c.getString(1)) * 22.5 / 1000;
                }

//                foorti pack
                if (isPeakHour("00:00", "15:00", c.getString(2))) {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        foortiHelper.cost += Double.valueOf(c.getString(1)) * 6 / 1000;
                    } else foortiHelper.cost += Double.valueOf(c.getString(1)) * 13.1667 / 1000;
                } else {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        foortiHelper.cost += Double.valueOf(c.getString(1)) * 16 / 1000;
                    } else foortiHelper.cost += Double.valueOf(c.getString(1)) * 21.5 / 1000;
                }

            } while (c.moveToNext());
        }
        db.close();
    }

    public void oneSecondPulsePackageAnalysis() {
        c = db.oneSecondPulse();
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
//                hoichoi pack
                if (hoichoiHelper.counter < 2) {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        hoichoiHelper.cost += Double.valueOf(c.getString(1)) * 0.5 / 100;
                        hoichoiHelper.fnf.add(c.getString(0));
                    } else {
                        hoichoiHelper.cost += Double.valueOf(c.getString(1)) * 1 / 100;
                        hoichoiHelper.fnf.add(c.getString(0));
                    }
                    hoichoiHelper.counter++;
                } else hoichoiHelper.cost += Double.valueOf(c.getString(1)) * 1.667 / 100;

//                kotha pack
                if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                    kothaHelper.cost += Double.valueOf(c.getString(1)) * 1.65 / 100;
                } else kothaHelper.cost += Double.valueOf(c.getString(1)) * 2.15 / 100;

            } while (c.moveToNext());
        }
        db.close();
    }

    public boolean isPeakHour(String start, String end, String time) {

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        try {
            Date twelveAM = parser.parse(start);
            Date fourPM = parser.parse(end);
            Date userTime = parser.parse(time);
            if (userTime.after(twelveAM) && userTime.before(fourPM)) {
//                true means Peak hour 12am to 4pm
                return true;
            } else
                return false;
        } catch (ParseException e) {
        }

        return false;
    }

}
