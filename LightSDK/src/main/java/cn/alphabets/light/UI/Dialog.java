package cn.alphabets.light.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.Window;
import android.widget.Toast;

import java.io.IOException;

import cn.alphabets.light.exception.ApplicationException;
import cn.alphabets.light.log.Logger;
import cn.alphabets.light.network.ContextManager;
import cn.alphabets.light.util.FileUtil;

/**
 *
 * Created by lin on 14/12/9.
 */
public class Dialog {

    private static Toast toast;

    public static final int REQUEST_TAKE_PHOTO = 100;
    public static final int REQUEST_CHOOSE_PHOTO = 101;

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
    public static void confirm(Activity context, int msg, DialogInterface.OnClickListener listener) {
        confirm(context, ContextManager.getInstance().getResources().getString(msg), listener);
    }
    public static void confirm(Activity context, String msg, DialogInterface.OnClickListener listener) {

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

    /**
     * 拍摄或选择照片，调用端需要使用onActivityResult来获取图片
     * @param context Activity或Fragment
     * @param msg 对话框消息
     */
    public static void takePhoto(final Object context, String msg) {

        Activity activity = context instanceof Fragment ? ((Fragment) context).getActivity() : (Activity) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(msg);
        builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                // File file = FileUtil.getTemporaryFile();
                // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

                if (context instanceof  Fragment) {
                    ((Fragment) context).startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
                if (context instanceof  Activity) {
                    ((Activity) context).startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("选择", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                if (context instanceof  Fragment) {
                    ((Fragment) context).startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
                }
                if (context instanceof  Activity) {
                    ((Activity) context).startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    /**
     * 解析图片，与takePhoto方法配合使用
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data intent
     * @return 图片路径
     */
    public static String parsePhoto(int requestCode, int resultCode, Intent data) {

        if (requestCode == Dialog.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            try {
                return FileUtil.saveBitmap(bitmap);
            } catch (IOException e) {
                Logger.e(e);
                throw new ApplicationException(e);
            }
        }

        if (requestCode == Dialog.REQUEST_CHOOSE_PHOTO && resultCode == Activity.RESULT_OK) {
            return FileUtil.getPhotoLibraryPath(data.getData(), ContextManager.getInstance());
        }

        return "";
    }
}
