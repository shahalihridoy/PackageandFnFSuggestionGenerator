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
        jobFinished(jobParameters, true);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
