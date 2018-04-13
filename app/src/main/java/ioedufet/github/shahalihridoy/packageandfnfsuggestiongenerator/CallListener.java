package ioedufet.github.shahalihridoy.packageandfnfsuggestiongenerator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CallListener extends Service {
    public CallListener() {
        System.out.println("I don't know what to do");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
