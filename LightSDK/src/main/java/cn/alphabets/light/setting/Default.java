package cn.alphabets.light.setting;

import android.util.Log;

/**
 * 缺省值
 * Created by lin on 14/11/30.
 */
public class Default {

    /**
     * 服务器地址
     */
    public static final String Server       = "10.0.1.61";
    public static final String Port         = "6002";
    public static final String Protocol     = "http";

    public static final int RequestTimeout  = 10 * 1000; // 十秒

    /**
     * Log相关
     */
    public static final int LogLevel        =  Log.DEBUG;
    public static final String LogTag       = "ABTag";


    /**
     *
     */
    public static final String CookieName   = "Cookie";
    public static final String CsrfName     = "_csrf";


    /**
     *
     */
    public static final String UrlLoadFile  = "file/";


}
