package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class History extends ContentObserver {
    Context context;

    public History(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        System.out.println("content is changed");
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE+" DESC");
        cursor.moveToNext();
        int number = cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER);
        int duration = cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION);

        String phType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
        String phNumber = cursor.getString(number);
        String phDuration = cursor.getString(duration);
        if(phDuration.equals("0") || phType.charAt(0)== '2' || phNumber.length()<11){
            return;
        }
        else if(phNumber.charAt(0) == '+')
            phNumber = phNumber.substring(3, phNumber.length());

        System.out.println(phNumber);
        System.out.println(phDuration);
        Database db = new Database(context, "CallLog", null, 13795);
        db.insertdata(cursor.getString(number),cursor.getString(duration));
        db.close();
    }
}