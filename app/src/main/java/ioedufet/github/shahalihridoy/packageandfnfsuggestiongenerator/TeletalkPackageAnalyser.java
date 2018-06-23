package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TeletalkPackageAnalyser {
    Database db;
    Context context;
    double cost = 0;
    int counter;
    Cursor c;
    Double min = 99999999.0;
    String packageName = null;

    Helper final_helper;
    Helper projonmoHelper = new Helper("Projonmo Package");
    Helper youthHelper = new Helper("Youth Package");
    Helper shadheenHelper = new Helper("Shadheen Package");

    //    constructor receiving context
    public TeletalkPackageAnalyser(Context context) {
        db = new Database(context, "CallLog", null, 13795);
        this.context = context;
    }

    public Helper analyseTeletalk() {

        analyseOneSecondPulse();
        analyseTenSecondPulse();

        if(min>projonmoHelper.cost){
            min = projonmoHelper.cost;
            final_helper = projonmoHelper;
            System.out.println(projonmoHelper.packageName+": "+projonmoHelper.cost);
        }
        if(min>youthHelper.cost){
            min = youthHelper.cost;
            final_helper = youthHelper;
            System.out.println(youthHelper.packageName+": "+youthHelper.cost);
        }
        if(min>shadheenHelper.cost){
            min = shadheenHelper.cost;
            final_helper = shadheenHelper;
            System.out.println(shadheenHelper.packageName+": "+shadheenHelper.cost);
        }
//        if(min>helloHelper.cost){
//            min = helloHelper.cost;
//            final_helper = helloHelper;
//            System.out.println(helloHelper.packageName+": "+helloHelper.cost);
//        }

//        play();
//        desh10fnf();
//        deshEkRateDarun();
//        hello();
        return final_helper;
    }

    public void analyseOneSecondPulse() {

        c = db.oneSecondPulse();
        youthHelper.fnf.clear();

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {

//                youth pack
                if(youthHelper.counter<3){
                    youthHelper.fnf.add(c.getString(0));
                    youthHelper.counter++;
                    if(c.getString(0).charAt(2) == '5'){
                        youthHelper.cost += Double.valueOf(c.getString(1))*0.5/100;
                    } else youthHelper.cost += Double.valueOf(c.getString(1))*1/100;
                } else if(c.getString(0).charAt(2) == '5'){
                    if(isPeakHour("08:00","12:00",c.getString(2)))
                        youthHelper.cost += Double.valueOf(c.getString(1))*1/100;
                    else youthHelper.cost += Double.valueOf(c.getString(1))*0.5/100;
                } else youthHelper.cost += Double.valueOf(c.getString(1))*1.5/100;

        } while (c.moveToNext()) ;
    }
        db.close();
}

    public void analyseTenSecondPulse() {
        c = db.tenSecondPulse();
        shadheenHelper.fnf.clear();

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {

//                projonmo pack
                if(projonmoHelper.sfnf){
                    projonmoHelper.superFnf = c.getString(0);
                    projonmoHelper.cost += Double.valueOf(c.getString(1))*4.17/1000;
                    projonmoHelper.sfnf = false;
                } else if(c.getString(0).charAt(2) == '5'){
                    if(isPeakHour("08:00","12:00",c.getString(2))){
                        projonmoHelper.cost += Double.valueOf(c.getString(1))*10/1000;
                    } else projonmoHelper.cost += Double.valueOf(c.getString(1))*5/1000;
                } else projonmoHelper.cost += Double.valueOf(c.getString(1))*16/1000;


//                shadheen pack
                if(shadheenHelper.counter<9){
                    shadheenHelper.fnf.add(c.getString(0));
                    shadheenHelper.counter++;
                    if (c.getString(0).charAt(2) == '5')
                    shadheenHelper.cost += Double.valueOf(c.getString(1))*4.17/1000;
                    else shadheenHelper.cost += Double.valueOf(c.getString(1))*10/1000;
                } else if(c.getString(0).charAt(2) == '5'){
                    if(isPeakHour("08:00","12:00",c.getString(2)))
                        shadheenHelper.cost += Double.valueOf(c.getString(1))*10/1000;
                    else shadheenHelper.cost += Double.valueOf(c.getString(1))*5/1000;
                } else {
                    if(isPeakHour("08:00","12:00",c.getString(2)))
                        shadheenHelper.cost += Double.valueOf(c.getString(1))*15/1000;
                    else shadheenHelper.cost += Double.valueOf(c.getString(1))*10/1000;
                }

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

public class Helper {
    String superFnf = "Not Applicable";
    ArrayList<String> fnf = new ArrayList<String>();
    String packageName = "";
    double cost = 0;
    boolean sfnf = true;
    int counter = 0;

    public Helper(String packageName) {
        this.packageName = packageName;
        fnf.add("Not Applicable");
    }
}
}
