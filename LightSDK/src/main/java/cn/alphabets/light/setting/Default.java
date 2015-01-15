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
    public static final String Server               = "necdb.alphabets.cn";
    public static final String Port                 = "5003";
    public static final String Protocol             = "http";

    public static final int RequestTimeout          = 15 * 1000;// 超时15秒
    public static final int MaxReTries              = 0;        // 重试次数
    public static final int BackOffMultiplier       = 0;

    /**
     * Log相关
     */
    public static final int LogLevel                =  Log.DEBUG;
    public static final String LogTag               = "ABTag";


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
}
