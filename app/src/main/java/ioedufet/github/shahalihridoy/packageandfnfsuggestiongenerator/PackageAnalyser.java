package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;

import java.util.ArrayList;

public class PackageAnalyser {
    Context context;
    Helper helper = new Helper();
    public PackageAnalyser(Context context){
        this.context = context;
    }

    public Helper analysePackage(){

        BanglalinkPackageAnalyser.Helper blhelper = new BanglalinkPackageAnalyser(context).analyseBanglalink();
        RobiPackageAnalyser.Helper robihelper = new RobiPackageAnalyser(context).analyzeRobi();
        GrameenPhonePackageAnalyzer.Helper gphelper = new GrameenPhonePackageAnalyzer(context).analyzeGP();
        AirtlePackageAnalyser.Helper airtelHelper = new AirtlePackageAnalyser(context).analyseAirtel();

        double min = 99999.0;
        if(min>robihelper.cost){
            min = robihelper.cost;
            helper.packageName = robihelper.packageName;
            helper.superFnf = robihelper.superFnf;
            helper.fnf = robihelper.fnf;
        }
        if(min>blhelper.cost){
            min = blhelper.cost;
            helper.packageName = blhelper.packageName;
            helper.superFnf = blhelper.superFnf;
            helper.fnf = blhelper.fnf;
        }
        if(min>gphelper.cost){
            min = gphelper.cost;
            helper.packageName = gphelper.packageName;
            helper.superFnf = gphelper.superFnf;
            helper.fnf = gphelper.fnf;
        }
        if(min>airtelHelper.cost){
            min = airtelHelper.cost;
            helper.packageName = airtelHelper.packageName;
            helper.superFnf = airtelHelper.superFnf;
            helper.fnf = airtelHelper.fnf;
        }

        return helper;
    }

    public class Helper {
        String superFnf = "Not Applicable";
        ArrayList<String> fnf = new ArrayList<String>();
        Double cost = 0.0;
        String packageName = "";
    }
}
