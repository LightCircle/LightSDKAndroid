package cn.alphabets.light.application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import cn.alphabets.light.log.Logger;

/**
 * 服务自己被杀掉时，会从新启动的服务
 * Created by lin on 14/12/14.
 */
public class AutoRestartService extends Service {

    /** 服务被杀掉后，自动重启的时间间隔 */
    protected int autoRestartTime = 1000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 销毁前，通过AlarmManager从新启动服务
     */
    @Override
    public void onDestroy() {
        Logger.d("Service be destroyed. Try to restart the service.");
        this.scheduleNextTime();
        super.onDestroy();
    }

    /**
     * 设定过一定时间后服务重新启动
     */
    private void scheduleNextTime() {

        long now = System.currentTimeMillis();

        PendingIntent intent = PendingIntent.getService(this, 0, new Intent(this, this.getClass()), 0);
        AlarmManager manager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, now + autoRestartTime, intent);
    }
}
