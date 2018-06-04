package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RobiPackageAnalyser {
    Database db;
    Context context;
    double cost = 0;
    int counter;
    Cursor c;
    Double min = 99999999.0;
    String packageName = null;

    //    constructor receiving context
    public RobiPackageAnalyser(Context context) {
        db = new Database(context, "CallLog", null, 13795);
        this.context = context;
    }

    //    analyze overall Robi Package

    public RobiPackageAnalyser.Helper analyzeRobi() {
        megaFnF();
        hutHutChomok32();
        robiClub34();
        return new RobiPackageAnalyser.Helper();
    }

    public boolean isPeakHour(String time) {

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        try {
            Date twelveAM = parser.parse("00:00");
            Date fourPM = parser.parse("16:00");
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
        String superFnf = "";
        ArrayList<String> fnf = new ArrayList<String>();
        Double cost = 0.0;
        String packageName = "";
    }

    //    bondhu pakcage analysis
    public RobiPackageAnalyser.Helper megaFnF() {

        Helper helper = new Helper();
        cost = 0;
        counter = 0;
        c = db.oneSecondPulse();
        boolean count_super_fnf = true;

        if (c.getCount() > 0) {
            c.moveToFirst();

            do {
//                total 81 fnf & 0 super fnf
                if (counter < 81) {

                    helper.fnf.add(c.getString(0));
//                  if number is Robi or Airtel
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {

//                    when call is creatd within  (12am to 4pm)
                        if (isPeakHour(c.getString(2))) {
                            cost += Double.valueOf(c.getString(1)) * 0.8 / 100;
                        } else
                            cost += Double.valueOf(c.getString(1)) * 1.1 / 100;
                    } else {
                        cost += Double.valueOf(c.getString(1)) * 1.1 / 100;
                    }
                    counter++;
                }

//                when number is not in FnF list
                else if (isPeakHour(c.getString(2))) {
                    cost += Double.valueOf(c.getString(1)) * 2.55 / 100;
                } else
                    cost += Double.valueOf(c.getString(1)) * 2.60 / 100;
            }
            while (c.moveToNext());
        }
        System.out.println("robi MegaFNF = " + cost);
        return helper;
    }

    public Helper hutHutChomok32() {

        Helper helper = new Helper();
        int selfFnf = 0; // 3 robi/airtel fnf
        int otherFnf = 0; // 2 fnf to other number
        c = db.tenSecondPulse();
        cost = 0;
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
//                when fnf is robi/airtel
                if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                    if (selfFnf < 4)
                        helper.fnf.add(c.getString(0));
                    cost += Double.valueOf(c.getString(1)) * 13 / 1000;
                    selfFnf++;
                } else if ((c.getString(0).charAt(2) != '8' || c.getString(0).charAt(2) != '6') && otherFnf < 3) {
                    helper.fnf.add(c.getString(0));
                    cost += Double.valueOf(c.getString(1)) * 13 / 1000;
                    otherFnf++;
                } else {
                    cost += Double.valueOf(c.getString(1)) * 22 / 1000;
                }
            } while (c.moveToNext());
        }

        System.out.println(cost);
        return helper;
    }

    public Helper robiClub34() {
        Helper helper = new Helper();
        c = db.tenSecondPulse();
        cost = 0;

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                if ((c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') && isPeakHour(c.getString(2))) {
                    cost += Double.valueOf(c.getString(1)) * 13 / 1000;
                } else
                    cost += Double.valueOf(c.getString(1)) * 24 / 1000;
            } while (c.moveToNext());
        }

        System.out.println("robi club34 " + cost);
        return helper;
    }
}
