package cn.alphabets.light.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

        FileInputStream fs = null;
        try {
            fs = new FileInputStream(new File(image));
            Bitmap o = BitmapFactory.decodeStream(fs);

            if (height <= 0) {
                height = width * o.getHeight() / o.getWidth();
            }

            Bitmap n = Bitmap.createScaledBitmap(o, width, height, false);
            return FileUtil.saveBitmap(n);
        } catch (IOException e) {

            Logger.e(e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (fs != null) fs.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

