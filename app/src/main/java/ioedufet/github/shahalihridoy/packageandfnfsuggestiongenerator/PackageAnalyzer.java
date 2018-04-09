package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.widget.Toast;

/**
 * Created by Md Shah Ali on 31-Mar-18.
 */

public class PackageAnalyzer {
    Database db;
    Context context;
    double cost = 0;
    int counter;

    //    constructor receiving context
    public PackageAnalyzer(Context context1) {
        this.context = context1;
    }

    //    bondhu pakcage analysis
    public int bondhu(Cursor c) {
//        number will be sent based on call duration asc and 017
        cost = 0;
        counter = 0;
        boolean count_super_fnf = true;

        if (c.getCount() > 0) {
            c.moveToFirst();

            do {
//                total 18 fnf including super fnf
                if (counter < 19) {
                    if (c.getString(0).charAt(2) == '7' && count_super_fnf) {
                        cost += Double.valueOf(c.getString(1)) / 1000 * 5.5; //taka
                        count_super_fnf = false;
                        counter++;
                    } else
                        cost += Double.valueOf(c.getString(1)) / 1000 * 11.5; //taka
                    counter++;
                }

//                other number calling cost
                else
                    cost += Double.valueOf(c.getString(1)) / 1000 * 27.5; //taka

            }
            while (c.moveToNext());
        }

//        cost = cost + cost*.21; //with 21% vat
        Toast.makeText(context, "bondhu = " + Double.toString(cost), Toast.LENGTH_LONG).show();
        return 0;
    }

    //    smile package analysis
    public int smile(Cursor c) {
        counter = 0;
        cost = 0;
        if (c.moveToFirst()) {
            do {
//                3 gp to gp fnf
                if (c.getString(0).charAt(2) == '7' && counter < 4) {
                    cost += Double.valueOf(c.getString(1)) / 1000 * 11.5; //taka
                    counter++;
                } else
                    cost += Double.valueOf(c.getString(1)) / 1000 * 27.5; //taka
            }
            while (c.moveToNext());
        }
        Toast.makeText(context,"smile = "+Double.toString(cost),Toast.LENGTH_LONG).show();
        return 0;
}

    //    nishchinto package analysis
    public int nishchinto(Cursor c) {
        cost = 0;
        if (c.moveToFirst()) {
            do {
                cost += Double.valueOf(c.getString(1)) / 1000 * 21; //taka
            }
            while (c.moveToNext());
        }
        Toast.makeText(context, "nishchinto = " + Double.toString(cost), Toast.LENGTH_LONG).show();
        return 0;
    }

    //    djuice package analysis
    public int djuice(Cursor c) {
        counter = 0;
        cost = 0;
        if (c.moveToFirst()) {
            do {
//                10 fnf to any number
                if (counter < 11) {
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

            Toast.makeText(context, "djuice = " + Double.toString(cost), Toast.LENGTH_LONG).show();
        }
        return 0;
    }
}
