package cn.alphabets.light.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cn.alphabets.light.R;
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

    public static final int REQUEST_TAKE_PHOTO = 1000;
    private static final String PHOTO_NAME = "temp.jpg";

    /**
     * 选择行
     */
    public interface Click {
        public void done(int which);
    }

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
    public static void confirm(Activity context, Click listener, int msg) {
        confirm(context, listener, ContextManager.getInstance().getResources().getString(msg));
    }
    public static void confirm(Activity context, final Click listener, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(msg);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.done(which);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }


    /**
     * 选择
     * @param context
     * @param listener
     * @param positive
     * @param neutral
     */
    public static void choose(final Object context, final Click listener, String title, String positive, String neutral) {

        Activity activity = context instanceof Fragment ? ((Fragment) context).getActivity() : (Activity) context;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(title);
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.done(which);
            }
        });

        builder.setNeutralButton(neutral, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.done(which);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.done(which);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }


//    /**
//     * 拍摄或选择照片，调用端需要使用onActivityResult来获取图片
//     * @param context Activity或Fragment
//     * @param msg 对话框消息
//     */
//    public static void takePhoto(final Object context, String msg, final int requestCode) {
//
//        Activity activity = context instanceof Fragment ? ((Fragment) context).getActivity() : (Activity) context;
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//
//        builder.setMessage(msg);
//        builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                // File file = FileUtil.getTemporaryFile();
//                // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//
//                if (context instanceof  Fragment) {
//                    ((Fragment) context).startActivityForResult(intent, requestCode);
//                }
//                if (context instanceof  Activity) {
//                    ((Activity) context).startActivityForResult(intent, requestCode);
//                }
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNeutralButton("选择", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//
//                if (context instanceof  Fragment) {
//                    ((Fragment) context).startActivityForResult(intent, requestCode);
//                }
//                if (context instanceof  Activity) {
//                    ((Activity) context).startActivityForResult(intent, requestCode);
//                }
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.show();
//    }

//    public static void takePhoto(final Object context, String msg) {
//        takePhoto(context, msg, Dialog.REQUEST_TAKE_PHOTO);
//    }

    /**
     * 拍摄或选择照片，调用端需要使用onActivityResult来获取图片
     * @param context Activity或Fragment
     */
    public static void takePhoto(final Object context, final int requestCode) {

        Activity activity = context instanceof Fragment ? ((Fragment) context).getActivity() : (Activity) context;

        View popupWindowView = activity.getLayoutInflater().inflate(R.layout.dialog_photo_selector, null);
        PopupWindow popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 加上动画并显示
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(activity.getWindow().getDecorView().getRootView(), Gravity.BOTTOM, 0, 0);

        // 绑定拍照，从相册中选等按钮的事件
        initPopupWindow(popupWindowView, popupWindow, activity, requestCode);
    }


    public static void initPopupWindow(View popupWindowView, final PopupWindow popupWindow,
                                final Object context, final int requestCode) {
        FrameLayout popupWindowArea = (FrameLayout) popupWindowView.findViewById(R.id.selector_area);
        TextView takePhoto = (TextView) popupWindowView.findViewById(R.id.take_photo);
        TextView choosePhoto = (TextView) popupWindowView.findViewById(R.id.choose_photo);
        TextView cancel = (TextView) popupWindowView.findViewById(R.id.take_photo);

        View.OnClickListener cancelListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        };
        // 点击空白区域或者点击取消则将窗口关闭
        popupWindowArea.setOnClickListener(cancelListener);
        cancel.setOnClickListener(cancelListener);

        // 拍照
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outDir, PHOTO_NAME)));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                if (context instanceof Fragment) {
                    ((Fragment) context).startActivityForResult(intent, requestCode);
                }
                if (context instanceof  Activity) {
                    ((Activity) context).startActivityForResult(intent, requestCode);
                }
                popupWindow.dismiss();
            }
        });

        // 从相册中选
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                if (context instanceof  Fragment) {
                    ((Fragment) context).startActivityForResult(intent, requestCode);
                }
                if (context instanceof  Activity) {
                    ((Activity) context).startActivityForResult(intent, requestCode);
                }
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 解析图片，与takePhoto方法配合使用
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data intent
     * @return 图片路径
     */
    public static String parsePhoto(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            // 拍摄的照片
            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            return new File(outDir, PHOTO_NAME).getAbsolutePath();
        } else {
            // 从图库选择
            return FileUtil.getPhotoLibraryPath(data.getData(), ContextManager.getInstance());
        }
    }
}
