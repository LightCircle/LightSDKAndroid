package cn.alphabets.light.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.alphabets.light.R;
import cn.alphabets.light.network.ContextManager;
import cn.alphabets.light.util.FileUtil;

/**
 *
 * Created by lin on 14/12/9.
 */
public class Dialog {

    private static Toast toast;

    public static final int REQUEST_TAKE_PHOTO = 1000;
    // 用于拍照时保存
    public static Uri photoUri;

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

    private static int pixel(Activity activity, int dip){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                , dip
                , activity.getResources().getDisplayMetrics());
    }

    /**
     * 类似与IOS的ActionSheet，多个按钮选择框
     * @param context activity or fragment
     * @param buttons 按钮
     * @param listener 按钮事件
     */
    public static void actionSheet(final Object context, String[] buttons, String cancel, final Click listener) {

        Activity activity = context instanceof Fragment ? ((Fragment) context).getActivity() : (Activity) context;

        // 按钮容器
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_selector, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.selector_buttons);

        // 弹出window
        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);

        // 加上动画并显示
        window.setAnimationStyle(R.style.PopupAnimation);
        window.showAtLocation(activity.getWindow().getDecorView().getRootView(), Gravity.BOTTOM, 0, 0);

        // 动态添加按钮
        for (int i = 0; i < buttons.length; i++) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pixel(activity, 40));
            params.setMargins(0, pixel(activity, 8), 0, 0);

            TextView button = new TextView(activity);
            button.setLayoutParams(params);
            button.setGravity(Gravity.CENTER);
            button.setText(buttons[i]);
            button.setBackgroundResource(R.drawable.n_bt_corner);
            button.setTextColor(activity.getResources().getColor(R.color.blue));

            final int which = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        window.dismiss();
                        listener.done(which);
                    }
                }
            });

            layout.addView(button);
        }

        if (cancel != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pixel(activity, 40));
            params.setMargins(0, pixel(activity, 8), 0, 0);

            TextView button = new TextView(activity);
            button.setLayoutParams(params);
            button.setGravity(Gravity.CENTER);
            button.setText(cancel);
            button.setBackgroundResource(R.drawable.n_bt_corner);
            button.setTextColor(Color.GRAY);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        window.dismiss();
                        listener.done(-1);
                    }
                }
            });

            layout.addView(button);
        }

        // 点击空白区域或者点击取消则将窗口关闭
        FrameLayout emptyArea = (FrameLayout) view.findViewById(R.id.selector_area);
        emptyArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
    }


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
                ContentValues values = new ContentValues();
                photoUri = ContextManager.getInstance().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
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
            return FileUtil.getPhotoPath(photoUri, ContextManager.getInstance());
        } else {
            // 从图库选择
            return FileUtil.getPhotoLibraryPath(data.getData(), ContextManager.getInstance());
        }
    }
}
