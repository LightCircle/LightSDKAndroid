package cn.alphabets.light.network;

import android.app.Application;

import java.lang.reflect.Method;

/**
 * Context实例获取器, Android4.x以后可用.
 * 注意，该方法未被正式文档化.
 * Created by lin on 14/11/30.
 */
public class ContextManager {

    private static final String THREAD_NAME = "android.app.ActivityThread";
    private static final String METHOD_NAME = "currentApplication";

    private static Application context;

    /**
     * 获取Application实例
     * @return Application实例
     */
    public static Application getInstance() {

        if (context != null) {
            return context;
        }

        try {
            final Class<?> activityThread = Class.forName(THREAD_NAME);
            final Method method = activityThread.getMethod(METHOD_NAME);
            context = (Application) method.invoke(null, (Object[]) null);
        } catch (final Exception e) {
            throw new RuntimeException("Failed to get application instance.");
        }

        return context;
    }
}
