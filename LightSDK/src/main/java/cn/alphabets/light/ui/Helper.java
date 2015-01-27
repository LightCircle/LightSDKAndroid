package cn.alphabets.light.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 工具
 * Created by lin on 14/12/16.
 */
public class Helper {

    /**
     * 获取焦点时，显示软键盘
     * @param activity activity
     */
    public static void setShowSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 隐藏软键盘
     * @param activity activity
     */
    public static void setHideSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    /**
     * 设定无图标，显示回退按键的ActionBar
     * @param activity
     * @param title
     */
    public static void setNoIconBackActionBar(Activity activity, String title) {

        activity.setTitle(title);
        activity.getActionBar().setHomeButtonEnabled(true);
        activity.getActionBar().setDisplayHomeAsUpEnabled(true);

        // 隐藏图标
        ColorDrawable drawable = new ColorDrawable(activity.getResources().getColor(android.R.color.transparent));
        activity.getActionBar().setIcon(drawable);
    }
    public static void setNoIconBackActionBar(Activity activity, int resTitle) {
        setNoIconBackActionBar(activity, activity.getResources().getString(resTitle));
    }

    /**
     * 启动拨号画面
     * @param context context
     * @param number 电话号码
     * @return true: 启动成功
     */
    public static boolean startDial(Context context, String number) {
        try {
            context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)));
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 启动地图应用
     * @param context context
     * @param address 地址
     * @return true: 启动成功
     */
    public static boolean startGeo(Context context, String address) {
        try {
            Uri uri = Uri.parse("geo:0,0?q=" + address);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
