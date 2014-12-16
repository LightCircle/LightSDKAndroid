package cn.alphabets.light.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.alphabets.light.log.Logger;

/**
 * 图片处理
 * Created by 罗浩 on 14/11/12.
 */
public class ImageUtil {

    public static String scale(String image, int width) {
        return scale(image, width, 0);
    }

    public static String scale(String image, int width, int height) {

        try {
            Bitmap o = BitmapFactory.decodeStream(new FileInputStream(new File(image)));

            if (height <= 0) {
                height = width * o.getHeight() / o.getWidth();
            }

            Bitmap n = Bitmap.createScaledBitmap(o, width, height, false);
            return FileUtil.saveBitmap(n);
        } catch (IOException e) {

            Logger.e(e);
            throw new RuntimeException(e);
        }
    }
}

