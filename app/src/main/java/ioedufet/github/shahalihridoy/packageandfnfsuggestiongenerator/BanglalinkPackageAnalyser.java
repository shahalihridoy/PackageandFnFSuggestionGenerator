package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class BanglalinkPackageAnalyser {
    Database db;
    Context context;
    double cost = 0;
    int counter;
    Cursor c;
    Double min = 99999999.0;
    String packageName = null;
    Helper final_helper;
    Helper helper = new Helper();

    //    constructor receiving context
    public BanglalinkPackageAnalyser(Context context) {
        db = new Database(context, "CallLog", null, 13795);
        this.context = context;
    }

    public Helper analyseBanglalink() {
        play();
        desh10fnf();
        deshEkRateDarun();
        hello();
        return final_helper;
    }

    public void play() {
        int sfnf = 0;
        int otherfnf = 0;
        c = db.oneSecondPulse();

//        this loop is for one second pulse
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                if (c.getString(0).charAt(2) == '9' && sfnf < 1) {
                    cost += Double.valueOf(c.getString(1)) * 0.55 / 100;
                    helper.superFnf = c.getString(0);
                } else if (otherfnf < 18) {
                    if (c.getString(0).charAt(2) == '9') {
                        cost += Double.valueOf(c.getString(1)) * 1.1 / 100;
                        helper.fnf.add(c.getString(0));
                    } else {
                        helper.fnf.add(c.getString(0));
                    }
                    otherfnf++;
//                    other fnf is 10 second pulse
//                    calculate cost with other loop
                }
            } while (c.moveToNext());
        }

//        this loop is for ten second pulse
        c = db.tenSecondPulse();
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                if (helper.superFnf.equals(c.getString(0)))
                    continue;
                else if (helper.fnf.contains(c.getString(0)) && c.getString(0).charAt(2) != '9') {
                    cost += Double.valueOf(c.getString(1)) * 11 / 1000;
                } else {
                    if (isPeakHour("00:00", "16:00", c.getString(2)))
                        cost += Double.valueOf(c.getString(1)) * 22 / 1000;
                    else cost += Double.valueOf(c.getString(1)) * 27 / 1000;
                }
            } while (c.moveToNext());
        }

        if (cost < min) {
            min = cost;
            helper.packageName = "Play";
            final_helper = helper;
        }
        System.out.println("Play cost " + cost);
    }

    public void desh10fnf() {
//        10 second pulse already in cursor
        cost = 0;
        int sfnf = 0;
        int fnf = 0;
        double helloCost = 0;
        double deshekratecost = 0;
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                if (c.getString(0).charAt(2) == '9' && sfnf < 1) {
                    if (isPeakHour("00:00", "16:00", c.getString(2)))
                        cost += Double.valueOf(c.getString(1)) * 6 / 1000;
                    else cost += Double.valueOf(c.getString(1)) * 7 / 1000;
                } else if (fnf < 9) {
                    if (isPeakHour("00:00", "16:00", c.getString(2)))
                        cost += Double.valueOf(c.getString(1)) * 11 / 1000;
                    else cost += Double.valueOf(c.getString(1)) * 15 / 1000;
                } else if (c.getString(0).charAt(2) == '9')
                    cost += Double.valueOf(c.getString(1)) * 27 / 1000;
                else cost += Double.valueOf(c.getString(1)) * 28.67 / 1000;

            } while (c.moveToNext());
        }

        if (cost < min) {
            min = cost;
            helper.packageName = "Desh 10 FnF";
            final_helper = helper;
        }
        System.out.println("Desh 10fnf cost " + cost);
    }

    public void deshEkRateDarun(){
        cost = 0;
        if(c.getCount()>0) {
            c.moveToFirst();
            do {
                cost += Double.valueOf(c.getString(1)) * 20.83 / 1000;
            } while (c.moveToNext());
        }

        if (cost < min) {
            min = cost;
            helper.fnf.add("Not Applicable");
            helper.packageName = "Desh ek rate darun";
            final_helper = helper;
        }
        System.out.println("Desh ek rate darun " + cost);
    }

    public  void hello(){
        cost = 0;
        int sfnf = 0;
        if(c.getCount()>0) {
            c.moveToFirst();
            do {
                if (c.getString(0).charAt(2) == '9' && sfnf<1){
                    helper.superFnf = c.getString(0);
                    cost += Double.valueOf(c.getString(1)) * 5.5 / 1000;
                } else cost += Double.valueOf(c.getString(1)) * 12 / 1000;

            } while (c.moveToNext());
        }

        if (cost < min) {
            min = cost;
            helper.fnf.add("Not Applicable");
            helper.packageName = "Desh Hello";
            final_helper = helper;
        }
        System.out.println("Desh hello " + cost);
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

    public class Helper {
        String superFnf = "Not Applicable";
        ArrayList<String> fnf = new ArrayList<String>();
        Double cost = 0.0;
        String packageName = "";
    }
}
