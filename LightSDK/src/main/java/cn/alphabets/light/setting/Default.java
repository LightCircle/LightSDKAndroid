package cn.alphabets.light.setting;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;

/**
 * 缺省值
 * Created by lin on 14/11/30.
 */
public class Default {

    /**
     * 服务器地址
     */
    public static String Server               = "124.193.141.162";
    public static String Port                 = "5003";
    public static String Protocol             = "http";

    public static int RequestTimeout          = 15 * 1000;// 超时15秒
    public static int MaxReTries              = 0;        // 重试次数
    public static int BackOffMultiplier       = 0;

    /**
     * Log相关
     */
    public static final int LogLevel          =  Log.DEBUG;
    public static final String LogTag         = "ABTag";
    public static final String DebugModel     = "DebugModel";
    public static final String LastError      = "LastError";

    /**
     *
     */
    public static String CookieName           = "Cookie";
    public static String CsrfName             = "_csrf";
    public static String ServerCookieName     = "set-cookie";
    public static String ServerCsrfName       = "csrftoken";


    /**
     *
     */
    public static String UrlLoadFile          = "file/download/";
    public static String UrlSendFile          = "file/create";
    public static String UrlCategoryList      = "category/list";
    public static String UrlSettingList       = "setting/list";


    /**
     * URL
     */
    public static String URL_VERSION_CHECK    = "system/versioncheck";


    /**
     * Broadcast names
     */
    public static String BroadcastLogout      = "cn.alphabets.light.logout";

    /**
     * 图片压缩的宽度
     */
    public static int ScaledWidth             = 960;
    public static int CompressQuality         = 60;
}
