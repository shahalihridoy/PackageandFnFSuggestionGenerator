package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    String superFnf = "Not Applicable";
    ArrayList<String> fnf = new ArrayList<String>();
    List<CostHelper> packageList = new ArrayList<CostHelper>();
    String packageName = "";
    String operator="";
    String code;
    double cost = 0;
    boolean sfnf = true;
    int counter = 0;

    public Helper(){

    }

    public Helper(String packageName,String operator,String code) {
        this.packageName = packageName;
        this.operator = operator;
        this.code = code;
        fnf.add("Not Applicable");
    }
}
