package cn.alphabets.light.ui;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

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


}
