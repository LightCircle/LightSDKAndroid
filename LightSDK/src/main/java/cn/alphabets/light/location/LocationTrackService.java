package cn.alphabets.light.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by 罗浩 on 14/11/17.
 */

public class LocationTrackService extends Service {

    private static final String TAG = "light.location";
    public int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        Log.d(TAG, Thread.currentThread().getId() + "");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        Log.d(TAG, Thread.currentThread().getId() + "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        count++;
                        Log.d(TAG, Thread.currentThread().getId() + ":" + count);
                        Log.d(TAG, "+++++++++++++++++");
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "*************** onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

