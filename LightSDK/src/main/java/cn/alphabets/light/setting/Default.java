package cn.alphabets.light.setting;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.IOException;
import java.util.Enumeration;

import cn.alphabets.light.network.ContextManager;
import dalvik.system.DexFile;

/**
 * 缺省值
 * Created by lin on 14/11/30.
 */
public class Default {

    private static Class<?> constantClass;

    /**
     * 服务器地址
     */
    private static final String Server              = "fastfix.alphabets.cn";
    private static final String Port                = "80";
    private static final String Protocol            = "http";
    public static String Server() {
        return getString("Server", Server);
    }
    public static String Port() {
        return getString("Port", Port);
    }
    public static String Protocol() {
        return getString("Protocol", Protocol);
    }

    public static final int RequestTimeout          = 15 * 1000;// 超时15秒
    public static final int MaxReTries              = 0;        // 重试次数
    public static int FileRequestTimeout            = 300 * 1000;// 超时5分钟
    public static final int BackOffMultiplier       = 0;

    /**
     * Log相关
     */
    private static final int LogLevel               =  Log.DEBUG;
    public static final String LogTag               = "ABTag";
    public static final String DebugModel           = "DebugModel";
    public static final String LastError            = "LastError";
    public static int LogLevel() {
        return getInt("LogLevel", LogLevel);
    }

    /**
     *
     */
    public static final String CookieName           = "Cookie";
    public static final String CsrfName             = "_csrf";
    public static final String ServerCookieName     = "set-cookie";
    public static final String ServerCsrfName       = "csrftoken";


    /**
     *
     */
    public static final String UrlLoadFile          = "file/download/";
    public static final String UrlSendFile          = "file/create";
    public static final String UrlCategoryList      = "category/list";
    public static final String UrlSettingList       = "setting/list";


    /**
     * URL
     */
    public static final String URL_VERSION_CHECK    = "system/versioncheck";


    /**
     * Broadcast names
     */
    public static final String BroadcastLogout      = "cn.alphabets.light.logout";


    /**
     * 图片压缩的宽度
     */
    public static final int ScaledWidth             = 960;
    public static final int CompressQuality         = 60;


    /**
     * 检索所有的类，获取指定类名的Class。
     * 该方法只对Application包路径及以下路径进行匹配。
     * @param className 类名
     * @return Class
     */
    private static Class<?> findClass(String className) {

        Application context = ContextManager.getInstance();
        String packageName = context.getPackageName();

        try {
            String path = context.getPackageManager().getApplicationInfo(packageName, 0).sourceDir;
            Enumeration entries = new DexFile(path).entries();

            String fullClassName = null;
            while (entries.hasMoreElements()) {
                String name = (String) entries.nextElement();
                if (name.matches(packageName + ".*." + className)) {
                    fullClassName = name;
                    break;
                }
            }

            if (fullClassName == null) {
                return null;
            }

            return Thread.currentThread().getContextClassLoader().loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取String类型的静态变量的值
     * @param key 变量名
     * @param defaults 当变量不存在时的缺省值
     * @return 值
     */
    private static String getString(String key, String defaults) {

        if (constantClass == null) {
            constantClass = findClass("Constant");
        }

        try {
            return (String) constantClass.getField(key).get(null);
        } catch (NoSuchFieldException e) {
            return defaults;
        } catch (IllegalAccessException e) {
            return defaults;
        }
    }

    /**
     * 获取int类型的静态变量的值
     * @param key 变量名
     * @param defaults 当变量不存在时的缺省值
     * @return 值
     */
    private static int getInt(String key, int defaults) {

        if (constantClass == null) {
            constantClass = findClass("Constant");
        }

        try {
            return (Integer) constantClass.getField(key).get(null);
        } catch (NoSuchFieldException e) {
            return defaults;
        } catch (IllegalAccessException e) {
            return defaults;
        }
    }
}
