package cn.alphabets.light.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by 罗浩 on 14/11/18.
 */
public class AppKilledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AAABBB", "AppKilledReceiver onReceive !!!!!!");
    }
}
