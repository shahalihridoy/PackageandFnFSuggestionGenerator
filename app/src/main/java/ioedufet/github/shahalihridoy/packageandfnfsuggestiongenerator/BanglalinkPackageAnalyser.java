package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BanglalinkPackageAnalyser {
    Database db;
    Context context;
    double cost = 0;
    int counter;
    Cursor c;
    Double min = 99999999.0;
    String packageName = null;
    Helper final_helper;

    Helper playHelper = new Helper("Banglalink Play", "Banglalink", "P");
    Helper desh10Helper = new Helper("Desh 10 FnF", "Banglalink", "*999*1*112");
    Helper deshEkRateHelper = new Helper("Desh Ek Rate Darun", "Banglalink", "*999*1*111");
    Helper helloHelper = new Helper("Desh Hello", "Banglalink", "H");
    Helper OneSecPulse = new Helper("1 Sec Pulse","Banglalink","*121*2*2*1*4*2");
    Helper businessHelper = new Helper("Business C&Control 4 large", "Banglalink", "");

    //    constructor receiving context
    public BanglalinkPackageAnalyser(Context context) {
        db = new Database(context, "CallLog", null, 13795);
        this.context = context;
    }

    public Helper analyseBanglalink() {

        analyseOneSecondPulse();
        analyseTenSecondPulse();

        if (min > playHelper.cost) {
            min = playHelper.cost;
            final_helper = playHelper;
            System.out.println(playHelper.packageName + ": " + playHelper.cost);
        }
        if (min > deshEkRateHelper.cost) {
            min = deshEkRateHelper.cost;
            final_helper = deshEkRateHelper;
            System.out.println(deshEkRateHelper.packageName + ": " + deshEkRateHelper.cost);
        }
        if (min > desh10Helper.cost) {
            min = desh10Helper.cost;
            final_helper = desh10Helper;
            System.out.println(desh10Helper.packageName + ": " + desh10Helper.cost);
        }
        if (min > helloHelper.cost) {
            min = helloHelper.cost;
            final_helper = helloHelper;
            System.out.println(helloHelper.packageName + ": " + helloHelper.cost);
        }
        if (min > OneSecPulse.cost) {
            min = OneSecPulse.cost;
            final_helper = OneSecPulse;
            System.out.println(OneSecPulse.packageName + ": " + OneSecPulse.cost);
        }

        final_helper.packageList.add(new CostHelper(playHelper.packageName,playHelper.cost));
        final_helper.packageList.add(new CostHelper(desh10Helper.packageName,desh10Helper.cost));
        final_helper.packageList.add(new CostHelper(deshEkRateHelper.packageName,deshEkRateHelper.cost));
        final_helper.packageList.add(new CostHelper(helloHelper.packageName,helloHelper.cost));
        final_helper.packageList.add(new CostHelper(OneSecPulse.packageName,OneSecPulse.cost));

        return final_helper;
    }

    public void analyseOneSecondPulse() {
        playHelper.fnf.clear();
        int otherfnf = 0;
        c = db.oneSecondPulse();

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {

//                play pack
                if (c.getString(0).charAt(2) == '9' && playHelper.sfnf) {
                    playHelper.cost += Double.valueOf(c.getString(1)) * 0.55 / 100;
                    playHelper.superFnf = c.getString(0);
                    playHelper.sfnf = false;
                } else if (playHelper.counter < 18) {
                    if (c.getString(0).charAt(2) == '9') {
                        playHelper.cost += Double.valueOf(c.getString(1)) * 1.1 / 100;
                        playHelper.fnf.add(c.getString(0));
                    } else {
                        playHelper.fnf.add(c.getString(0));
                    }
                    playHelper.counter++;

                }

//                One second pulse
                OneSecPulse.cost += Double.valueOf(c.getString(1)) * 2.2 / 100;

            } while (c.moveToNext());
        }
        db.close();
    }

    public void analyseTenSecondPulse() {
        c = db.tenSecondPulse();
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {

//                play pack
                if (playHelper.superFnf.equals(c.getString(0))) {

                } else if (playHelper.fnf.contains(c.getString(0)) && c.getString(0).charAt(2) != '9') {
                    playHelper.cost += Double.valueOf(c.getString(1)) * 11 / 1000;
                } else {
                    if (isPeakHour("00:00", "16:00", c.getString(2)))
                        playHelper.cost += Double.valueOf(c.getString(1)) * 22 / 1000;
                    else playHelper.cost += Double.valueOf(c.getString(1)) * 27 / 1000;
                }

//                desh 10 fnf
                if (c.getString(0).charAt(2) == '9' && desh10Helper.sfnf) {
                    desh10Helper.sfnf = false;
                    if (isPeakHour("00:00", "16:00", c.getString(2)))
                        desh10Helper.cost += Double.valueOf(c.getString(1)) * 6 / 1000;
                    else desh10Helper.cost += Double.valueOf(c.getString(1)) * 7 / 1000;
                } else if (desh10Helper.counter < 9) {
                    if (isPeakHour("00:00", "16:00", c.getString(2)))
                        desh10Helper.cost += Double.valueOf(c.getString(1)) * 11 / 1000;
                    else desh10Helper.cost += Double.valueOf(c.getString(1)) * 15 / 1000;
                    desh10Helper.counter++;
                } else if (c.getString(0).charAt(2) == '9')
                    desh10Helper.cost += Double.valueOf(c.getString(1)) * 27 / 1000;
                else desh10Helper.cost += Double.valueOf(c.getString(1)) * 28.67 / 1000;

//                desh ek rate darun pack
                deshEkRateHelper.cost += Double.valueOf(c.getString(1)) * 20.83 / 1000;

//                hello pack
                if (c.getString(0).charAt(2) == '9' && helloHelper.sfnf) {
                    helloHelper.superFnf = c.getString(0);
                    helloHelper.cost += Double.valueOf(c.getString(1)) * 5.5 / 1000;
                    helloHelper.sfnf = false;
                } else if (c.getString(0).charAt(2) == '9') {
                    helloHelper.cost += Double.valueOf(c.getString(1)) * 23 / 1000;
                } else helloHelper.cost += Double.valueOf(c.getString(1)) * 12 / 1000;

//                business package only for me :D
                businessHelper.cost += Double.valueOf(c.getString(1)) * 10 / 1000;

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
