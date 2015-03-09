package cn.alphabets.light.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.android.volley.Request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import cn.alphabets.light.R;
import cn.alphabets.light.exception.NetworkException;
import cn.alphabets.light.log.Logger;
import cn.alphabets.light.network.ContextManager;
import cn.alphabets.light.network.SessionManager;
import cn.alphabets.light.network.VolleyManager;
import cn.alphabets.light.setting.Default;
import cn.alphabets.light.ui.Dialog;

/**
 * 文件操作
 * Created by lin on 14/12/4.
 */
public class FileUtil {

    /**
     * 保存图片文件
     *
     * @param bitmap 图片
     * @return 图片路径
     * @throws IOException
     */
    public static String saveBitmap(Bitmap bitmap) throws IOException {

        File file = getTemporaryFile();
        FileOutputStream stream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, Default.CompressQuality, stream);
        stream.close();

        return file.getAbsolutePath();
    }

    /**
     * 图像文件生成Bitmap实例
     *
     * @param path 图像所在位置
     * @return Bitmap实例
     */
    public static Bitmap loadBitmap(String path) {
        try {
            return BitmapFactory.decodeStream(new FileInputStream(new File(path)));
        } catch (FileNotFoundException e) {
            Logger.e(e);
        }

        return null;
    }

    public static Bitmap loadBitmap(int resource) {
        return BitmapFactory.decodeResource(ContextManager.getInstance().getResources(), resource);
    }

    /**
     * 获取调整大小后的图像
     *
     * @param path  图像所在位置
     * @param width 图像的宽度
     * @return
     */
    public static Bitmap loadScaledBitmap(String path, int width) {
        Bitmap bitmap = loadBitmap(path);
        if (bitmap == null) {
            return null;
        }

        int height = width * bitmap.getHeight() / bitmap.getWidth();
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public static String scaledBitmap(String path, int width, boolean isFromCamera) {
        Bitmap bitmap = loadBitmap(path);
        // 如果是拍照获得的图片则删除原照
        if (isFromCamera) {
            File originalFile = new File(path);
            if (originalFile.isFile()) {
                originalFile.delete();
            }
        }
        if (bitmap == null) {
            return null;
        }

        float originalWidth = bitmap.getWidth();
        float originalHeight = bitmap.getHeight();
        float ratio = originalHeight / originalWidth;
        // 不管横屏竖屏，width都作为最小边
        int height = (int) (width * ratio);
        if (ratio < 1) {
            height = width;
            width = (int) (height / ratio);
        }
        if (originalHeight > width || originalWidth > width) {
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }
        try {
            return saveBitmap(bitmap);
        } catch (IOException e) {
        }

        return null;
    }

    /**
     * 获取临时文件
     *
     * @return 临时文件路径
     */
    public static File getTemporaryFile() {
        return new File(getCacheDir(), UUID.randomUUID().toString());
    }

    /**
     * 获取指定后缀的临时文件
     *
     * @param fileEnding 包好.的后缀名如.txt
     * @return
     */
    public static File getTemporaryFile(String fileEnding) {
        return new File(getCacheDir(), UUID.randomUUID().toString() + fileEnding);
    }

    /**
     * 获取临时目录
     *
     * @return 目录
     */
    public static File getCacheDir() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED) && !Environment.isExternalStorageRemovable()) {
            return ContextManager.getInstance().getExternalCacheDir();
        }

        return ContextManager.getInstance().getCacheDir();
    }

    /**
     * 下载文件，因为是同步下载，建议使用AsyncTask
     *
     * @param url  URL
     * @param file 下载的文件
     * @throws NetworkException
     */
    public static void downloadFile(String url, File file) throws NetworkException {
        try {
            if (url.toLowerCase().contains("http") || url.toLowerCase().contains("https")) {
                url = VolleyManager.getURL(url, Request.Method.GET, null);
            } else {
                url = VolleyManager.getURL(Default.UrlLoadFile + url, Request.Method.GET, null);
            }
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty(Default.CookieName, SessionManager.getCookie());
            urlConnection.connect();

            InputStream input = urlConnection.getInputStream();

            byte[] buffer = new byte[1024];
            int bufferLength;

            FileOutputStream output = new FileOutputStream(file);
            while ((bufferLength = input.read(buffer)) > 0) {
                output.write(buffer, 0, bufferLength);
            }
            output.close();
            input.close();

        } catch (Exception e) {
            Logger.e(e);
            throw new NetworkException(e.getMessage());
        }
    }

    /**
     * 获取拍照所得图片的路径
     *
     * @param uri
     * @param context
     * @return
     */
    public static String getPhotoPath(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(1);
        cursor.close();
        return path;
    }

    /**
     * 获取图片库路径
     *
     * @param uri     url
     * @param context context
     * @return 路径
     */
    public static String getPhotoLibraryPath(Uri uri, Context context) {

        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        cursor.close();

        if (path == null) {
            // some devices (OS versions return an URI of com.android instead of com.google.android
            if (uri.toString().startsWith("content://com.android.gallery3d.provider"))  {
                // use the com.google provider, not the com.android provider.
                uri = Uri.parse(uri.toString().replace("com.android.gallery3d","com.google.android.gallery3d"));
            }

            InputStream is = null;
            ContentResolver res = context.getContentResolver();
            try {
                is = res.openInputStream(uri);
                File file = FileUtil.getTemporaryFile();
                FileUtil.inputStreamToFile(file, is);
                if (file.length() > 0) {
                    path = file.getAbsolutePath();
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }

        return path;
    }

    /**
     * 将InputStream写入文件
     * @param file
     * @param is
     */
    public static void inputStreamToFile(File file, InputStream is) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte buffer[] = new byte[4*1024];

            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
            os.close();
            is.close();
        } catch (Exception e) {
            throw new RuntimeException("File operation failed.");
        }
    }

    /**
     * 从文件里获取Mime类型
     *
     * @param file 文件路径
     * @return mime类型
     */
    public static String getMimeTypeOfFile(String file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);
        return options.outMimeType;
    }

    /**
     * 根据后缀打开文件
     *
     * @param context
     * @param file
     */
    public static void openFile(Context context, File file) {
        if (file.isFile()) {
            String fileName = file.getName();
            Intent intent;
            if (checkEnding(fileName, context.getResources().getStringArray(R.array.fileEndingImage))) {
                intent = getImageFileIntent(file);
                context.startActivity(intent);
            } else if (checkEnding(fileName, context.getResources().getStringArray(R.array.fileEndingWebText))) {
                intent = getHtmlFileIntent(file);
                context.startActivity(intent);
            } else if (checkEnding(fileName, context.getResources().getStringArray(R.array.fileEndingPackage))) {
                intent = getApkFileIntent(file);
                context.startActivity(intent);
            } else if (checkEnding(fileName, context.getResources().getStringArray(R.array.fileEndingAudio))) {
                intent = getAudioFileIntent(file);
                context.startActivity(intent);
            } else if (checkEnding(fileName, context.getResources().getStringArray(R.array.fileEndingVideo))) {
                intent = getVideoFileIntent(file);
                context.startActivity(intent);
            } else if (checkEnding(fileName, context.getResources().getStringArray(R.array.fileEndingText))) {
                intent = getTextFileIntent(file);
                context.startActivity(intent);
            } else if (checkEnding(fileName, context.getResources().getStringArray(R.array.fileEndingPdf))) {
                intent = getPdfFileIntent(file);
                context.startActivity(intent);
            } else if (checkEnding(fileName, context.getResources().getStringArray(R.array.fileEndingWord))) {
                intent = getWordFileIntent(file);
                context.startActivity(intent);
            } else if (checkEnding(fileName, context.getResources().getStringArray(R.array.fileEndingExcel))) {
                intent = getExcelFileIntent(file);
                context.startActivity(intent);
            } else if (checkEnding(fileName, context.getResources().getStringArray(R.array.fileEndingPPT))) {
                intent = getPPTFileIntent(file);
                context.startActivity(intent);
            } else {
                Dialog.toast(R.string.open_file_error);
            }
        } else {
            Dialog.toast(R.string.open_file_error);
        }
    }

    /**
     * 检查要打开的文件的后缀是否在fileedings.xml中
     *
     * @param fileEnding
     * @param fileEndings
     * @return
     */
    private static boolean checkEnding(String fileEnding, String[] fileEndings) {
        for (String end : fileEndings) {
            if (fileEnding.endsWith(end)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取用于打开HTML文件的intent
     *
     * @param file
     * @return
     */
    private static Intent getHtmlFileIntent(File file) {
        Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString()).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
     * 获取用于打开图片文件的intent
     *
     * @param file
     * @return
     */
    private static Intent getImageFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * 获取用于打开PDF文件的intent
     *
     * @param file
     * @return
     */
    private static Intent getPdfFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /**
     * 获取用于打开文本文件的intent
     *
     * @param file
     * @return
     */
    private static Intent getTextFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    /**
     * 获取用于打开音频文件的intent
     *
     * @param file
     * @return
     */
    private static Intent getAudioFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
     * 获取用于打开视频文件的intent
     *
     * @param file
     * @return
     */
    private static Intent getVideoFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**
     * 获取用于打开Word文件的intent
     *
     * @param file
     * @return
     */
    private static Intent getWordFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * 获取用于打开Excel文件的intent
     *
     * @param file
     * @return
     */
    private static Intent getExcelFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
     * 获取用于打开PPT文件的intent
     *
     * @param file
     * @return
     */
    private static Intent getPPTFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    /**
     * 获取用于打开apk文件的intent
     *
     * @param file
     * @return
     */
    private static Intent getApkFileIntent(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 获取选择文件的绝对路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFileAbsolutePath(Context context, Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
