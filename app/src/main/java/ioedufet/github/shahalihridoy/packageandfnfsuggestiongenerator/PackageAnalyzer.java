package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Md Shah Ali on 31-Mar-18.
 */

public class PackageAnalyzer {
    Database db;
    Context context;
    double cost = 0;
    int counter;
    Cursor c;
    Double min = 99999999.0;
    String packageName = null;

    //    constructor receiving context
    public PackageAnalyzer(Context context) {
        db = new Database(context, "CallLog", null, 13795);
        this.context = context;
    }

    //    bondhu pakcage analysis
    public Helper bondhu() {
//        number will be sent based on call duration asc and 017
        Helper helper = new Helper();
        cost = 0;
        counter = 0;
        boolean count_super_fnf = true;

        if (c.getCount() > 0) {
            c.moveToFirst();

            do {
//                total 18 fnf including super fnf
                if (counter < 19) {
                    if (c.getString(0).charAt(2) == '7' && count_super_fnf) {
                        helper.superFnf = c.getString(0);
                        cost += Double.valueOf(c.getString(1)) / 1000 * 5.5; //taka
                        count_super_fnf = false;
                        counter++;
                    } else {
                        helper.fnf.add(c.getString(0));
                        cost += Double.valueOf(c.getString(1)) / 1000 * 11.5; //taka
                    }
                    counter++;
                }

//                other number calling cost
                else
                    cost += Double.valueOf(c.getString(1)) / 1000 * 27.5; //taka

            }
            while (c.moveToNext());
        }

//        cost = cost + cost*.21; //with 21% vat
//        Toast.makeText(context, "bondhu = " + Double.toString(cost), Toast.LENGTH_SHORT).show();

        helper.packageName = "Bondhu Package";

        if(min>cost){
            min = cost;
            packageName = "Bondhu Package";
        }
        return helper;
    }

    //    smile package analysis
    public Helper smile() {
        Helper helper = new Helper();
        counter = 0;
        cost = 0;
        if (c.moveToFirst()) {
            do {
//                3 gp to gp fnf
                if (c.getString(0).charAt(2) == '7' && counter < 4) {
                    helper.fnf.add(c.getString(0));
                    cost += Double.valueOf(c.getString(1)) / 1000 * 11.5; //taka
                    counter++;
                } else
                    cost += Double.valueOf(c.getString(1)) / 1000 * 27.5; //taka
            }
            while (c.moveToNext());
        }
//        Toast.makeText(context,"smile = "+Double.toString(cost),Toast.LENGTH_SHORT).show();
        helper.packageName = "Smile Package";
        if(min>cost){
            min = cost;
            packageName = "Smile Package";
        }
        return helper;
}

    //    nishchinto package analysis
    public Helper nishchinto() {
        Helper helper = new Helper();
        cost = 0;
        if (c.moveToFirst()) {
            do {
                cost += Double.valueOf(c.getString(1)) / 1000 * 21; //taka
            }
            while (c.moveToNext());
        }
//        Toast.makeText(context, "nishchinto = " + Double.toString(cost), Toast.LENGTH_SHORT).show();
        helper.packageName = "Nishchinto Package";
        if(min>cost){
            min = cost;
            packageName = "Nishchinto Package";
        }
        return helper;
    }

    //    djuice package analysis
    public Helper djuice() {
        Helper helper = new Helper();
        counter = 0;
        cost = 0;
        if (c.moveToFirst()) {
            do {
//                10 fnf to any number
                if (counter < 11) {
//                    adding number to fnf list
                    helper.fnf.add(c.getString(0));
                    cost += Double.valueOf(c.getString(1)) / 1000 * 11.5; //taka
                    counter++;
                } else {
                    if (c.getString(0).charAt(2) == '7')
                        cost += Double.valueOf(c.getString(1)) / 1000 * 20.5; //taka
                    else
                        cost += Double.valueOf(c.getString(1)) / 1000 * 27.5; //taka
                }
            }
            while (c.moveToNext());

//            Toast.makeText(context, "djuice = " + Double.toString(cost), Toast.LENGTH_SHORT).show();
        }

        helper.packageName = "Djuice Package";
        if(min>cost){
            min = cost;
            packageName = "Djuice Package";
        }
        return helper;
    }

//    analyze overall grameenPhone
    public Helper analyzeGP(){
        c = db.getData();
        Helper smile = smile();
        Helper bondhu = bondhu();
        Helper nishchinto = nishchinto();
        Helper djuice = djuice();
        switch (packageName.charAt(0)){
            case 'S':return smile;
            case 'B': return bondhu;
            case 'D': return djuice;
            case 'N': return nishchinto;
        }
        return new Helper();
    }
    public String getOperator(){
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }

    public class Helper{
        String superFnf = "";
        ArrayList<String> fnf = new ArrayList<String>();
        Double cost = 0.0;
        String packageName="";
    }
}
