package cn.alphabets.light.ui;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.Toast;

import cn.alphabets.light.network.ContextManager;

/**
 *
 * Created by lin on 14/12/9.
 */
public class Dialog {

    private static Toast toast;

    /**
     * 显示Toast信息
     * @param msg 信息内容
     */
    public static void toast(int msg) {
        toast(ContextManager.getInstance().getResources().getString(msg));
    }
    public static void toast(String msg) {
        if (toast == null) {
            Application context = ContextManager.getInstance();
            toast = Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }

        toast.show();
    }

    /**
     * 显示确认对话框
     * @param context activity
     * @param msg 信息内容
     */
    public static void confirm(Context context, int msg, DialogInterface.OnClickListener listener) {
        confirm(context, ContextManager.getInstance().getResources().getString(msg), listener);
    }
    public static void confirm(Context context, String msg, DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(msg);
        builder.setPositiveButton("Yes", listener);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
}
