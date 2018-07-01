package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import java.util.ArrayList;

public class Helper {
    String superFnf = "Not Applicable";
    ArrayList<String> fnf = new ArrayList<String>();
    String packageName = "";
    double cost = 0;
    boolean sfnf = true;
    int counter = 0;

    public Helper(){

    }
    public Helper(String packageName) {
        this.packageName = packageName;
        fnf.add("Not Applicable");
    }
}
