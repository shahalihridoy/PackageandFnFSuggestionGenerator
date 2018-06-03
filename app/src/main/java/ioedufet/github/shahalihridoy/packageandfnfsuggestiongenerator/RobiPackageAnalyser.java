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

        c = db.getDataForRobi();
        RobiPackageAnalyser.Helper bondhu = megaFnF();
        switch (packageName.charAt(0)) {
            case 'B':
                return bondhu;

        }
        return new RobiPackageAnalyser.Helper();
    }

    public boolean isPeakHour(String time) {

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        try {
            Date twelveAM = parser.parse("00:00");
            Date fourPM = parser.parse("16:00");
            Date userTime = parser.parse(time);
            if (userTime.after(twelveAM) && userTime.before(fourPM)) {
//                true means Peak hour
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

        RobiPackageAnalyser.Helper helper = new RobiPackageAnalyser.Helper();
        cost = 0;
        counter = 0;
        c = db.getDataForRobi();
        boolean count_super_fnf = true;

        if (c.getCount() > 0) {
            c.moveToFirst();

            do {
//                total 81 fnf & 0 super fnf
                if (counter < 81) {

                    System.out.println(c.getString(0));
                    System.out.println(c.getString(1));
//                  if number is Robi or Airtel
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {

//                    when call is creatd within  (12am to 4pm)
                        if (isPeakHour(c.getString(2))) {
                            cost += Double.valueOf(c.getString(1)) * 0.8;
                        } else
                            cost += Double.valueOf(c.getString(1)) * 1.1;
                    } else {
                        cost += Double.valueOf(c.getString(1)) * 1.1;
                    }
                    counter++;
                }

//                when number is not in FnF list
                if (isPeakHour(c.getString(2))) {
                    cost += Double.valueOf(c.getString(1)) * 2.55;
                } else
                    cost += Double.valueOf(c.getString(1)) * 2.60;
            }
            while (c.moveToNext());
        }

        return new RobiPackageAnalyser.Helper();
    }
}
