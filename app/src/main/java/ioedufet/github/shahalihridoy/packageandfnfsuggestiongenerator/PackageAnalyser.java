package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;

import java.util.ArrayList;

public class PackageAnalyser {

    Context context;
    Helper helper;

    public PackageAnalyser(Context context){
        this.context = context;
    }

    public Helper analysePackage(){

        Helper blhelper = new BanglalinkPackageAnalyser(context).analyseBanglalink();
        Helper robihelper = new RobiPackageAnalyser(context).analyzeRobi();
        Helper gphelper = new GrameenPhonePackageAnalyzer(context).analyzeGP();
        Helper airtelHelper = new AirtlePackageAnalyser(context).analyseAirtel();
        Helper teletalkHelper = new TeletalkPackageAnalyser(context).analyseTeletalk();

        double min = 999999.0;
        if(min>robihelper.cost){
            min = robihelper.cost;
            helper = robihelper;
        }
        if(min>blhelper.cost){
            min = blhelper.cost;
            helper = blhelper;
        }
        if(min>gphelper.cost){
            min = gphelper.cost;
            helper = gphelper;
        }
        if(min>airtelHelper.cost){
            min = airtelHelper.cost;
            helper = airtelHelper;
        }
        if(min>teletalkHelper.cost){
            min = teletalkHelper.cost;
            helper = teletalkHelper;
        }

        helper.packageList.clear();
        helper.packageList.add(new CostHelper(airtelHelper.operator,airtelHelper.cost));
        helper.packageList.add(new CostHelper(blhelper.operator,blhelper.cost));
        helper.packageList.add(new CostHelper(gphelper.operator,gphelper.cost));
        helper.packageList.add(new CostHelper(robihelper.operator,robihelper.cost));
        helper.packageList.add(new CostHelper(teletalkHelper.operator,teletalkHelper.cost));

        return helper;
    }
}
