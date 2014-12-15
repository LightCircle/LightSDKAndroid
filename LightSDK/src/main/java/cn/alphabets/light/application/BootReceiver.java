package cn.alphabets.light.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.alphabets.light.log.Logger;

/**
 * 此方案行不通！！！
 *
 * 系统启动时，启动的服务
 * 通过Android系统启动时的 BOOT_COMPLETED 广播实现
 *
 <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 <receiver
     android:name="cn.alphabets.light.application.BootReceiver"
     android:enabled="true"
     android:exported="true"
     android:permission="android.permission.RECEIVE_BOOT_COMPLETED" >
     <intent-filter>
         <action android:name="android.intent.action.BOOT_COMPLETED" />
         <category android:name="android.intent.category.DEFAULT" />
     </intent-filter>
 </receiver>
 *
 * 注: 在MIUI下行不通（被强行杀掉进程或由第三方杀掉进程时，不会重新启动）
 */
public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Logger.d("BootReceiver#onReceive");

    }
}
