package cn.alphabets.light.image;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 罗浩 on 14/11/12.
 */
public class ImageUtil {
    public static Bitmap ScaleFileBitmapToSize(String pathOfInputImage, int dstWidth, int dstHeight) throws IOException {
        int inWidth;
        int inHeight;
        InputStream in = new FileInputStream(pathOfInputImage);

        // decode image size (decode metadata only, not the whole image)
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();

        // save width and height
        inWidth = options.outWidth;
        inHeight = options.outHeight;

        // decode full image pre-resized
        in = new FileInputStream(pathOfInputImage);
        options = new BitmapFactory.Options();
        // calc rought re-size (this is no exact resize)
        options.inSampleSize = Math.max(inWidth / dstWidth, inHeight / dstHeight);
        // decode full image
        Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);
        in.close();

        // calc exact destination size
        Matrix m = new Matrix();
        RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
        RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
        m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
        float[] values = new float[9];
        m.getValues(values);
        // resize bitmap
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
        // save image

//                FileOutputStream out = new FileOutputStream(pathOfOutputImage);
//                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        return resizedBitmap;


    }

    public static String RealPhotoPath(Uri originalUri, Context context) {
        //获取照片的实际地址并保存，通知adapter
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(originalUri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        } else {
            return null;
        }
    }
}

