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
    boolean sfnf = true;
    Cursor c;
    Double min = 99999999.0;
    String packageName = null;
    Helper final_helper;

    Helper megaHelper = new Helper("Mega FnF");
    Helper gotiHelper = new Helper("Goti Package");
    Helper hoothutHelper = new Helper("Hoot Hut Chomok");
    Helper clubHelper = new Helper("Robi Club");
    Helper nobannoHelper = new Helper("Nobanno Package");
    Helper shorolHelper = new Helper("Shorol Package");
    Helper noorHelper = new Helper("Noor Package");


    //    constructor receiving context
    public RobiPackageAnalyser(Context context) {
        db = new Database(context, "CallLog", null, 13795);
        this.context = context;
    }

    //    analyze overall Robi Package
    public Helper analyzeRobi() {

        analyseOneSecondPulse();
        analyseTenSecondPulse();

        if(min>megaHelper.cost){
            min = megaHelper.cost;
            final_helper = megaHelper;
            System.out.println(final_helper.packageName+": "+final_helper.cost);
        }
        if(min>clubHelper.cost){
            min = clubHelper.cost;
            final_helper = clubHelper;
            System.out.println(final_helper.packageName+": "+final_helper.cost);
        }
        if(min>gotiHelper.cost){
            min = gotiHelper.cost;
            final_helper = gotiHelper;
            System.out.println(final_helper.packageName+": "+final_helper.cost);
        }
        if(min>shorolHelper.cost){
            min = shorolHelper.cost;
            final_helper = shorolHelper;
            System.out.println(final_helper.packageName+": "+final_helper.cost);
        }
        if(min>noorHelper.cost){
            min = noorHelper.cost;
            final_helper = nobannoHelper;
            System.out.println(final_helper.packageName+": "+final_helper.cost);
        }
        if(min>hoothutHelper.cost){
            min = megaHelper.cost;
            final_helper = megaHelper;
            System.out.println(final_helper.packageName+": "+final_helper.cost);
        }
        if(min>nobannoHelper.cost){
            min = nobannoHelper.cost;
            final_helper = nobannoHelper;
            System.out.println(final_helper.packageName+": "+final_helper.cost);
        }
        return final_helper;
    }

    public void analyseOneSecondPulse(){
        megaHelper.fnf.clear();
        c = db.oneSecondPulse();
        if(c.getCount()>0){
            c.moveToFirst();
            do {
//                mega fnf pack
//                total 81 fnf & 0 super fnf
                if (megaHelper.counter < 80) {

                    megaHelper.fnf.add(c.getString(0));
//                  if number is Robi or Airtel
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {

//                    when call is creatd within  (12am to 4pm)
                        if (isPeakHour("00:00","16:00",c.getString(2))) {
                            megaHelper.cost += Double.valueOf(c.getString(1)) * 0.8 / 100;
                        } else
                            megaHelper.cost += Double.valueOf(c.getString(1)) * 1.1 / 100;
                    } else {
                        megaHelper.cost += Double.valueOf(c.getString(1)) * 1.1 / 100;
                    }
                    megaHelper.counter++;
                }

//                when number is not in FnF list
                else if (isPeakHour("00:00","16:00",c.getString(2))) {
                    megaHelper.cost += Double.valueOf(c.getString(1)) * 2.55 / 100;
                } else
                    megaHelper.cost += Double.valueOf(c.getString(1)) * 2.60 / 100;

            }while (c.moveToNext());
        }
        db.close();
    }

    public void analyseTenSecondPulse(){
        int selfFnf = 0;
        int otherFnf = 0;
        c = db.tenSecondPulse();
        if(c.getCount()>0){
            c.moveToFirst();
            do {
//                hoot hut pack
//                when fnf is robi/airtel
                if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                    if (selfFnf < 4)
                        hoothutHelper.fnf.add(c.getString(0));
                    hoothutHelper.cost += Double.valueOf(c.getString(1)) * 13 / 1000;
                    selfFnf++;
                } else if ((c.getString(0).charAt(2) != '8' || c.getString(0).charAt(2) != '6') && otherFnf < 3) {
                    hoothutHelper.fnf.add(c.getString(0));
                    hoothutHelper.cost += Double.valueOf(c.getString(1)) * 13 / 1000;
                    otherFnf++;
                } else {
                    megaHelper.cost += Double.valueOf(c.getString(1)) * 22 / 1000;
                }


//                robi club pack
                if ((c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') && isPeakHour("00:00","16:00",c.getString(2))) {
                    clubHelper.cost+= Double.valueOf(c.getString(1)) * 13 / 1000;
                } else
                    clubHelper.cost += Double.valueOf(c.getString(1)) * 24 / 1000;


//                goti pack
                gotiHelper.cost += Double.valueOf(c.getString(1))*21/1000;


//                nobanno pack
                if(isPeakHour("22:00","08:00",c.getString(2)))
                    nobannoHelper.cost += Double.valueOf(c.getString(1))*8/1000;
                else
                    nobannoHelper.cost += Double.valueOf(c.getString(1))*21/1000;


//                shorol pack
                if ((c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') && sfnf) {
                    shorolHelper.superFnf = c.getString(0);
                    shorolHelper.cost += Double.valueOf(c.getString(1)) * 6 / 1000;
                    sfnf = false;
                } else shorolHelper.cost += Double.valueOf(c.getString(1)) * 23 / 1000;


//                noor pack
                noorHelper.cost += Double.valueOf(c.getString(1))*21/1000;

            }while (c.moveToNext());
        }
        db.close();
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

}
