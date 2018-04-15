package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.provider.CallLog;
import android.support.annotation.RequiresApi;
import android.util.Log;

import static android.content.ContentValues.TAG;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BackgroundServiceWaker extends JobService {

    Context context;

    public BackgroundServiceWaker(){}

    BackgroundServiceWaker(Context context){
        this.context = context;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

//        start original backgroun service
        new BackgroundServiceMarshmallow(context).startBackgroundService();

        jobFinished(jobParameters,false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    public void wakeService(){

        ComponentName componentName = new ComponentName(context, BackgroundServiceWaker.class);
        @SuppressLint({"NewApi", "LocalSuppress"}) JobInfo info = new JobInfo.Builder(13795, componentName)
                .setPersisted(true)
                .build();

        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }
}
