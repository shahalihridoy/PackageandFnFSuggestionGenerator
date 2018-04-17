package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.CallLog;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static android.content.ContentValues.TAG;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BackgroundServiceWaker extends JobService {
    Context context;
    BackgroundServiceWaker(Context context){
        this.context = context;
    }

    //    default constructor
    public BackgroundServiceWaker(){
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Log.d(TAG, "onStartJob: insied waker");
        new BackgroundServiceWaker(this).startBackgroundService();
        jobFinished(jobParameters, true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    //    this function will start the service for nougat or onward version
    public void startBackgroundService(){

        ComponentName componentName = new ComponentName(context, BackgroundServiceWaker.class);
        @SuppressLint({"NewApi", "LocalSuppress"}) JobInfo info = new JobInfo.Builder(124, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(60)
                .setPersisted(true)
                .build();

        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled in waker");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }
}
