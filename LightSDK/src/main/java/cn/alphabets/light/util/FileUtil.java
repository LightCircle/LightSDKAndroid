package cn.alphabets.light.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import cn.alphabets.light.exception.NetworkException;
import cn.alphabets.light.log.Logger;
import cn.alphabets.light.network.ContextManager;
import cn.alphabets.light.network.SessionManager;
import cn.alphabets.light.setting.Default;

/**
 * 文件操作
 * Created by lin on 14/12/4.
 */
public class FileUtil {

    /**
     * 保存图片文件
     * @param bitmap 图片
     * @return 图片路径
     * @throws IOException
     */
    public static String saveBitmap(Bitmap bitmap) throws IOException {

        File file = new File(getCacheDir(), UUID.randomUUID().toString());
        FileOutputStream stream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        stream.close();

        return file.getAbsolutePath();
    }

    /**
     * 获取临时目录
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
     * @param url URL
     * @param file 下载的文件
     * @throws NetworkException
     */
    public static void downloadFile(String url, File file) throws NetworkException{
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty(Default.CookieName, SessionManager.getCookie());
            urlConnection.connect();

            InputStream input = urlConnection.getInputStream();

            byte[] buffer = new byte[1024];
            int bufferLength;

            FileOutputStream output = new FileOutputStream(file);
            while ( (bufferLength = input.read(buffer)) > 0 ) {
                output.write(buffer, 0, bufferLength);
            }
            output.close();
            input.close();

        } catch (Exception e) {
            Logger.e(e);
            throw new NetworkException(e.getMessage());
        }
    }
}
