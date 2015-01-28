package cn.alphabets.light.application;

import android.app.Application;

import java.io.PrintWriter;
import java.io.StringWriter;

import cn.alphabets.light.log.Logger;
import cn.alphabets.light.network.VolleyManager;
import cn.alphabets.light.setting.Default;
import cn.alphabets.light.store.Eternal;
import cn.alphabets.light.util.SharedData;

/**
 * Application共同类
 * Created by lin on 14/12/2.
 */
public class ABApplication extends Application implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this);

        // 初始化网络连接
        VolleyManager.init();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        // 将Trace转换成字符
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        String stackTrace = stringWriter.toString();

        Logger.e(stackTrace);

        // 保存最后的异常信息
        Eternal.saveString(Default.LastError, stackTrace);

        // 执行缺省Handler，并强行结束
        System.exit(1);
    }
}
