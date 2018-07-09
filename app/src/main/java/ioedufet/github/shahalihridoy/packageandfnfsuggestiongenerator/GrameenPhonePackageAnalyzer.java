package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Md Shah Ali on 31-Mar-18.
 */

public class GrameenPhonePackageAnalyzer {
    Database db;
    Context context;
    double cost = 0;
    boolean count_super_fnf = true;
    int counter;
    Helper final_helper;
    Cursor c;
    Double min = 99999999.0;
    String packageName = null;

    Helper bondhuHelper = new Helper("Bondhu Package","Grameenphone","B");
    Helper smileHelper = new Helper("Smile Package","Grameenphone","S");
    Helper nishchintoHelper = new Helper("Nishchinto Package","Grameenphone","N");
    Helper djuiceHelper = new Helper("Djuice Package","Grameenphone","D");

    //    constructor receiving context
    public GrameenPhonePackageAnalyzer(Context context) {
        db = new Database(context, "CallLog", null, 13795);
        this.context = context;
    }

    //    analyze overall grameenPhone
    public Helper analyzeGP() {

        analyseTenSecondPulse();

        if(min>bondhuHelper.cost){
            min = bondhuHelper.cost;
            final_helper = bondhuHelper;
            System.out.println(final_helper.packageName+": "+final_helper.cost);
        }
        if(min>smileHelper.cost){
            min = smileHelper.cost;
            final_helper = smileHelper;
            System.out.println(final_helper.packageName+": "+final_helper.cost);
        }
        if(min>nishchintoHelper.cost){
            min = nishchintoHelper.cost;
            final_helper = nishchintoHelper;
            System.out.println(final_helper.packageName+": "+final_helper.cost);
        }
        if(min>djuiceHelper.cost){
            min = djuiceHelper.cost;
            final_helper = djuiceHelper;
            System.out.println(final_helper.packageName+": "+final_helper.cost);
        }

        return final_helper;
    }

    public void analyseTenSecondPulse() {

        bondhuHelper.fnf.clear();
        smileHelper.fnf.clear();
        djuiceHelper.fnf.clear();


        c = db.tenSecondPulse();
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {

//                bondhu pack
//                total 18 fnf including super fnf
                if (bondhuHelper.counter < 18) {
                    if (c.getString(0).charAt(2) == '7' && bondhuHelper.sfnf) {
                        bondhuHelper.superFnf = c.getString(0);
                        bondhuHelper.cost += Double.valueOf(c.getString(1)) / 1000 * 5.5; //taka
                        bondhuHelper.sfnf = false;
                    } else {
                        bondhuHelper.fnf.add(c.getString(0));
                        bondhuHelper.cost += Double.valueOf(c.getString(1)) / 1000 * 11.5; //taka
                    }
                    bondhuHelper.counter++;
                }

//                other number calling cost
                else
                    bondhuHelper.cost += Double.valueOf(c.getString(1)) / 1000 * 27.5; //taka


//                smile pack
//                3 gp to gp fnf
                if (c.getString(0).charAt(2) == '7' && smileHelper.counter < 4) {
                    smileHelper.fnf.add(c.getString(0));
                    smileHelper.cost += Double.valueOf(c.getString(1)) / 1000 * 11.5; //taka
                    smileHelper.counter++;
                } else
                    smileHelper.cost += Double.valueOf(c.getString(1)) / 1000 * 27.5; //taka


//                nishchinto pack
                nishchintoHelper.cost += Double.valueOf(c.getString(1)) / 1000 * 21; //taka


//                djuice pack
 //                10 fnf to any number
                if (djuiceHelper.counter < 10) {
//                    adding number to fnf list
                    djuiceHelper.fnf.add(c.getString(0));
                    djuiceHelper.cost += Double.valueOf(c.getString(1)) / 1000 * 11.5; //taka
                    djuiceHelper.counter++;
                } else {
                    if (c.getString(0).charAt(2) == '7')
                        djuiceHelper.cost += Double.valueOf(c.getString(1)) / 1000 * 20.5; //taka
                    else
                        djuiceHelper.cost += Double.valueOf(c.getString(1)) / 1000 * 27.5; //taka
                }
            } while (c.moveToNext());
            db.close();
        }
    }

}
