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

        return helper;
    }

    public class Helper {
        String superFnf = "Not Applicable";
        ArrayList<String> fnf = new ArrayList<String>();
        Double cost = 0.0;
        String packageName = "";
    }
}
