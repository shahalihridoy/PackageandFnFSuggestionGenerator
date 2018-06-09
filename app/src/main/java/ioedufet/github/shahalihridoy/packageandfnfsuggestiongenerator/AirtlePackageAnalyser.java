package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.content.Context;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AirtlePackageAnalyser {
    Database db;
    Context context;
    Cursor c;
    double min = 99999999.0;
    String packageName = null;
    Helper final_helper;

    int counter = 0;
    int addaCounter = 0;
    int superAddaCounter = 0;
    int dostiCounter = 0;
    int hoichoiCounter = 0;


    double cost = 0;
    double golpocost = 0;
    double addacost = 0;
    double superaddacost = 0;
    double dosticost = 0;
    double hoichoicost = 0;
    //    double gangtalkcost = 0;
//    double dolbolcost = 0;
    double foorticost = 0;
    double kothacost = 0;

    Helper golpoHelper = new Helper("Golpo");
    Helper addaHelper = new Helper("Adda");
    Helper superAddaHelper = new Helper("Super Adda");
    Helper dostiHelper = new Helper("Dosti");
    Helper hoichoiHelper = new Helper("Hoichoi");
    //    Helper gangtalkHelper = new Helper("Gangtalk");
//    Helper dolbolHelper = new Helper("Dolbol");
    Helper foortiHelper = new Helper("Foorti");
    Helper kothaHelper = new Helper("Kotha");

    //    constructor receiving context
    public AirtlePackageAnalyser(Context context) {
        db = new Database(context, "CallLog", null, 13795);
        this.context = context;
    }

    public Helper analyseAllPackages() {

        tenSecondPulsePackageAnalysis();
        oneSecondPulsePackageAnalysis();

        if (golpocost < min) {
            min = golpocost;
            final_helper = golpoHelper;
            final_helper.cost = min;
        } else if (addacost < min) {
            min = addacost;
            final_helper = addaHelper;
            final_helper.cost = min;
        }
        if (superaddacost < min) {
            min = superaddacost;
            final_helper = superAddaHelper;
            final_helper.cost = min;
        }
        if (dosticost < min) {
            min = dosticost;
            final_helper = dostiHelper;
            final_helper.cost = min;
        }
        if (foorticost < min) {
            min = foorticost;
            final_helper = foortiHelper;
            final_helper.cost = min;
        }
        if (hoichoicost < min) {
            min = hoichoicost;
            final_helper = hoichoiHelper;
            final_helper.cost = min;
        }
        if (kothacost < min) {
            min = kothacost;
            final_helper = kothaHelper;
            final_helper.cost = min;
        }
        return final_helper;
    }

    public void tenSecondPulsePackageAnalysis() {
        c = db.tenSecondPulse();
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {

//                golpo pack
                golpocost += Double.valueOf(c.getString(1)) * 1667 / 1000;

//                adda pack
                if (addaCounter < 8) {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        addacost += Double.valueOf(c.getString(1)) * 6 / 1000;
                        addaHelper.fnf.add(c.getString(0));
                    } else {
                        addacost += Double.valueOf(c.getString(1)) * 13.333 / 1000;
                        addaHelper.fnf.add(c.getString(0));
                    }
                } else {
                    addacost += Double.valueOf(c.getString(1)) * 22.5 / 1000;
                }

//                super add pack
                if (superAddaCounter < 29) {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        superaddacost += Double.valueOf(c.getString(1)) * 5 / 1000;
                        superAddaHelper.fnf.add(c.getString(0));
                    } else {
                        superaddacost += Double.valueOf(c.getString(1)) * 10 / 1000;
                        superAddaHelper.fnf.add(c.getString(0));
                    }
                } else {
                    superaddacost += Double.valueOf(c.getString(1)) * 22.5 / 1000;
                }

//                dosti pack
                if (dostiCounter < 5) {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        dosticost += Double.valueOf(c.getString(1)) * 4.1667 / 1000;
                        dostiHelper.fnf.add(c.getString(0));
                    } else {
                        dosticost += Double.valueOf(c.getString(1)) * 11 / 1000;
                        dostiHelper.fnf.add(c.getString(0));
                    }
                } else {
                    dosticost += Double.valueOf(c.getString(1)) * 22.5 / 1000;
                }

//                foorti pack
                if (isPeakHour("00:00", "15:00", c.getString(2))) {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        foorticost += Double.valueOf(c.getString(1)) * 6 / 1000;
                    } else foorticost += Double.valueOf(c.getString(1)) * 13.1667 / 1000;
                } else {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        foorticost += Double.valueOf(c.getString(1)) * 16 / 1000;
                    } else foorticost += Double.valueOf(c.getString(1)) * 21.5 / 1000;
                }

            } while (c.moveToNext());
        }
    }

    public void oneSecondPulsePackageAnalysis() {
        c = db.oneSecondPulse();
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
//                hoichoi pack
                if (hoichoiCounter < 2) {
                    if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                        hoichoicost += Double.valueOf(c.getString(1)) * 0.5 / 100;
                        hoichoiHelper.fnf.add(c.getString(1));
                    } else {
                        hoichoicost += Double.valueOf(c.getString(1)) * 1 / 100;
                        hoichoiHelper.fnf.add(c.getString(1));
                    }
                } else hoichoicost += Double.valueOf(c.getString(1)) * 1.667 / 100;

//                kotha pack
                if (c.getString(0).charAt(2) == '8' || c.getString(0).charAt(2) == '6') {
                    kothacost += Double.valueOf(c.getString(1)) * 1.65 / 100;
                } else kothacost += Double.valueOf(c.getString(1)) * 2.15 / 100;

            } while (c.moveToNext());
        }
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

        public Helper(String packageName) {
            this.packageName = packageName;
            fnf.add("Not Applicable");
        }
    }
}
