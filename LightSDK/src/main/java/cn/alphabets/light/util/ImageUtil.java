package cn.alphabets.light.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;

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

//    public static String scale(String image, int width, int height) {
//
//        FileInputStream fs = null;
//        try {
//            fs = new FileInputStream(new File(image));
//            Bitmap o = BitmapFactory.decodeStream(fs);
//
//            if (height <= 0) {
//                height = width * o.getHeight() / o.getWidth();
//            }
//
//            Bitmap n = Bitmap.createScaledBitmap(o, width, height, false);
//            return FileUtil.saveBitmap(n);
//        } catch (IOException e) {
//
//            Logger.e(e);
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                if (fs != null) fs.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    public static String scale(String image, int width, int height) {
        int inWidth;
        int inHeight;

        try {
            InputStream in = new FileInputStream(image);

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            // decode full image pre-resized
            in = new FileInputStream(image);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth / width, inHeight / height);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, width, height);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);
            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
            return FileUtil.saveBitmap(resizedBitmap);
        } catch (IOException e) {
            Logger.e(e);
            throw new RuntimeException(e);
        }
    }
}

