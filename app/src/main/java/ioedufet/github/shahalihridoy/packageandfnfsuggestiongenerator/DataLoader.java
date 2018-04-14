package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.ActionMenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class DataLoader extends Activity {
    Context context;
    Activity activity;
    Cursor cursor;
    Database db;
    Boolean isGranted = false;

    public DataLoader(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @SuppressLint("MissingPermission")
    public void insertCallListToDatabase() {

        db = new Database(context, "CallLog", null, 13795);
        cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        if(db.getData().getCount() != 0)
            return;
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);

            int dircode = Integer.parseInt(callType);

            if (dircode != CallLog.Calls.OUTGOING_TYPE || callDuration.equals("0") || phNumber.length() < 11)
                continue;

            try {
                if (phNumber.charAt(0) == '+')
                    phNumber = phNumber.substring(3, phNumber.length());
            } catch (Exception e) {
                continue;
            }

//            inserting in database
            db.insertdata(phNumber, callDuration);
            db.close();
        }
        cursor.close();
        db.close();
    }

    public void getCallDetailsFromDatabase(TextView textView) {

        db = new Database(context, "CallLog", null, 13795);
        StringBuffer sb = new StringBuffer();
        cursor = db.getData();

        if (cursor.moveToFirst()) {
            do {
                sb.append("\nPhone Number:--- " + cursor.getString(0) + " \nCall duration in sec :--- " + cursor.getString(1));
                sb.append("\n----------------------------------");
            } while (cursor.moveToNext());
        }
        cursor.close();
        textView.setText(sb);
    }

}
