package cn.alphabets.light.util;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import cn.alphabets.light.network.ContextManager;

/**
 * 网络工具
 * Created by lin on 14/12/9.
 */
public class NetworkUtil {

    /**
     * 检查网络是否可用
     * @return true: 可用
     */
    public static boolean isNetworkEnabled() {

        Application context = ContextManager.getInstance();
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.getState() == NetworkInfo.State.CONNECTED;
    }

    /**
     * 检查是否是时间同步
     * @return true: 同步
     */
    public static boolean isTimeAutoSync() {
        Application context = ContextManager.getInstance();
        return Settings.System.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, -1) == 1;
    }
}
