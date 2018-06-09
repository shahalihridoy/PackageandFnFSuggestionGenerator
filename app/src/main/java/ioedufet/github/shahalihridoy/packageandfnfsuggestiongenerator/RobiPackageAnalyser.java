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
    Helper final_helper;
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
        goti36();
        nobanno37();
        shorol39();
        noor();
        return final_helper;
    }

    public boolean isPeakHour(String start,String end,String time) {

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

    public void megaFnF() {

        Helper helper = new Helper();
        cost = 0;
        counter = 0;
        c = db.oneSecondPulse();

        if (c.getCount() > 0) {
            c.moveToFirst();

            do {
//                total 81 fnf & 0 super fnf
                if (counter < 80) {

                    helper.fnf.add(c.getString(0));
//                  if number is Robi or Airtel
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {

//                    when call is creatd within  (12am to 4pm)
                        if (isPeakHour("00:00","16:00",c.getString(2))) {
                            cost += Double.valueOf(c.getString(1)) * 0.8 / 100;
                        } else
                            cost += Double.valueOf(c.getString(1)) * 1.1 / 100;
                    } else {
                        cost += Double.valueOf(c.getString(1)) * 1.1 / 100;
                    }
                    counter++;
                }

//                when number is not in FnF list
                else if (isPeakHour("00:00","16:00",c.getString(2))) {
                    cost += Double.valueOf(c.getString(1)) * 2.55 / 100;
                } else
                    cost += Double.valueOf(c.getString(1)) * 2.60 / 100;
            }
            while (c.moveToNext());
        }
        System.out.println("robi MegaFNF = " + cost);
        if(cost<min){
            min = cost;
            helper.packageName = "Mega FnF";
            helper.cost = cost;
            final_helper = helper;
        }
    }

    public void hutHutChomok32() {

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
        if(cost<min){
            min = cost;
            helper.packageName = "Hoot Hut Chomok";
            helper.cost = cost;
            final_helper = helper;
        }
        System.out.println(cost);
    }

    public void robiClub34() {
        Helper helper = new Helper();
        c = db.tenSecondPulse();
        cost = 0;

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                if ((c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') && isPeakHour("00:00","16:00",c.getString(2))) {
                    cost += Double.valueOf(c.getString(1)) * 13 / 1000;
                } else
                    cost += Double.valueOf(c.getString(1)) * 24 / 1000;
            } while (c.moveToNext());
        }

        System.out.println("robi club34 " + cost);
        if(cost<min){
            min = cost;
            helper.packageName = "Robi Club";
            helper.fnf.add("Not Applicable");
            helper.cost = cost;
            final_helper = helper;
        }
    }

    public void goti36(){
        Helper helper = new Helper();
        c = db.tenSecondPulse();
        cost = 0;
        if(c.getCount()>0){
            c.moveToFirst();
            do {
                cost += Double.valueOf(c.getString(1))*21/1000;
            } while (c.moveToNext());
        }

        System.out.println("Goti 36 "+cost);
        if(cost<min){
            min = cost;
            helper.packageName = "Goti";
            helper.fnf.add("Not Applicable");
            helper.cost = cost;
            final_helper = helper;
        }
    }

    public void nobanno37(){
        Helper helper = new Helper();
//        no need to call db.tenSecondPulse as it is already in c
        cost = 0;

        if(c.getCount()>0){
            c.moveToFirst();
            do {
                if(isPeakHour("22:00","08:00",c.getString(2)))
                    cost += Double.valueOf(c.getString(1))*8/1000;
                else
                    cost += Double.valueOf(c.getString(1))*21/1000;
            }while (c.moveToNext());
        }

        System.out.println("Nobanno " + cost);
        if(cost<min){
            min = cost;
            helper.packageName = "Nobanno";
            helper.fnf.add("Not Applicable");
            helper.cost = cost;
            final_helper = helper;
        }
    }

    public void shorol39(){
//        1 super FnF/Priyo number
        Helper helper = new Helper();
        cost = 0;
        int sfnf = 0;

        if(c.getCount()>0){
            c.moveToFirst();
            do {
                if ((c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') && sfnf<1) {
                    helper.superFnf = c.getString(0);
                    cost += Double.valueOf(c.getString(1)) * 6 / 1000;
                    sfnf++;
                } else cost += Double.valueOf(c.getString(1)) * 23 / 1000;
            }while (c.moveToNext());
        }

        System.out.println("Shorol "+cost);
        if(cost<min){
            min = cost;
            helper.packageName = "Shorol";
            helper.fnf.add("Not Applicable");
            helper.cost = cost;
            final_helper = helper;
        }
    }

    public void noor(){
        Helper helper = new Helper();
        cost = 0;
        if(c.getCount()>0){
            c.moveToFirst();
            do {
                cost += Double.valueOf(c.getString(1))*21/1000;
            } while (c.moveToNext());
        }

        System.out.println("Noor "+ cost);
        if(cost<min){
            min = cost;
            helper.packageName = "Mega FnF";
            helper.fnf.add("Not Applicable");
            helper.cost = cost;
            final_helper = helper;
        }
    }
}
