package cn.alphabets.light.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import cn.alphabets.light.network.ContextManager;

/**
 * 文件操作
 * Created by lin on 14/12/4.
 */
public class FileUtil {

    public static String saveBitmap(Bitmap bitmap) throws IOException {

        File file = new File(getCacheDir(), UUID.randomUUID().toString());
        FileOutputStream stream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        stream.close();

        return file.getAbsolutePath();
    }

    public static File getCacheDir() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED) && !Environment.isExternalStorageRemovable()) {
            return ContextManager.getInstance().getExternalCacheDir();
        }

        return ContextManager.getInstance().getCacheDir();
    }
}
