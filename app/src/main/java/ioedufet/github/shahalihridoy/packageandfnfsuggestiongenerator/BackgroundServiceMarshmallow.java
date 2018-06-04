package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.CallLog;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BackgroundServiceMarshmallow extends JobService {

    Context context;
    BackgroundServiceMarshmallow(Context context){
        this.context = context;
    }

//    default constructor
    public BackgroundServiceMarshmallow(){
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentTitle("My Title")
                .setContentText("my content text")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(PendingIntent.getActivity(this,
                        0,
                        new Intent(this,MainActivity.class)
                        ,PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true);
        NotificationManager nfm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nfm.notify(324,notification.build());

        insertData();
        Log.d(TAG, "onStartJob: "+this);
        jobFinished(jobParameters, true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    private void insertData() {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Cursor cursor = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
        cursor.moveToNext();

        int number = cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER);
        int duration = cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION);
        int type = cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE);

        String phNumber = cursor.getString(number);
        String phDuration = cursor.getString(duration);
        String phType = cursor.getString(type);
        Date callDayTime = new Date(Long.valueOf(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE))));
        String callTime = new SimpleDateFormat("HH:mm").format(callDayTime);

        if(phDuration.equals("0") || phType.charAt(0)== '2' || phNumber.length()<11){
            return;
        }
        else if(phNumber.charAt(0) == '+')
            phNumber = phNumber.substring(3, phNumber.length());

        Database db = new Database(getApplicationContext(), "CallLog", null, 13795);
        db.insertdata(phNumber,phDuration,callTime);
        db.close();
    }


//    this function will start the service for nougat or onward version
    public void startBackgroundService(){
        ComponentName componentName = new ComponentName(context, BackgroundServiceMarshmallow.class);
        @SuppressLint({"NewApi", "LocalSuppress"}) JobInfo info = new JobInfo.Builder(123, componentName)
                .addTriggerContentUri(new JobInfo.TriggerContentUri(CallLog.Calls.CONTENT_URI
                        , JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS))
                .build();

        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled in marshmallow");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }
}
