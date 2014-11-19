package cn.alphabets.light.store;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import cn.alphabets.light.Util;

/**
 * Created by 罗浩 on 14/11/19.
 */
public class Storeable {
    private static final Logger log = Logger.getLogger(Storeable.class);
    private static final String StorePrefix = "cn.alphabets.light.store";

    public static void store(Context ctx, String key, Object data) throws IOException, NoSuchAlgorithmException, UnsupportedOperationException {
        if (isMetaType(data)) {
            storeToSP(ctx, key, data);
        } else if (data instanceof Serializable) {
            storeToFile(ctx, key, (Serializable) data);
        } else {
            throw new UnsupportedOperationException("data type is not support");
        }

    }

    private static void storeToSP(Context ctx, String key, Object data) {
        SharedPreferences sp = ctx.getApplicationContext().getSharedPreferences(StorePrefix, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();


        if (data instanceof String) {
            e.putString(key, (String) data);

        }
        if (data instanceof Integer) {
            e.putInt(key, (Integer) data);
        }
        if (data instanceof Long) {
            e.putLong(key, (Long) data);
        }
        if (data instanceof Float) {
            e.putFloat(key, (Float) data);
        }
        if (data instanceof Boolean) {
            e.putBoolean(key, (Boolean) data);

        }
        e.commit();
    }

    private static void storeToFile(Context ctx, String key, Serializable data) throws IOException, NoSuchAlgorithmException {

        File dir = new File(ctx.getFilesDir() + File.separator + "store");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File path = new File(dir.toString() + File.separator + Util.md5(key) + ".dat");
        if (path.exists()) {
            path.delete();
        }
        FileOutputStream out = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(data);
        oos.close();
        out.close();
    }

    public static int getInt(Context ctx, String key, int i) {
        return ctx.getApplicationContext().getSharedPreferences(StorePrefix, Context.MODE_PRIVATE).getInt(key, i);
    }

    public static long getLong(Context ctx, String key, long l) {
        return ctx.getApplicationContext().getSharedPreferences(StorePrefix, Context.MODE_PRIVATE).getLong(key, l);
    }

    public static float getFloat(Context ctx, String key, float v) {
        return ctx.getApplicationContext().getSharedPreferences(StorePrefix, Context.MODE_PRIVATE).getFloat(key, v);
    }

    public static boolean getBoolean(Context ctx, String key, boolean b) {
        return ctx.getApplicationContext().getSharedPreferences(StorePrefix, Context.MODE_PRIVATE).getBoolean(key, b);
    }

    public static String getString(Context ctx, String key, String s) {
        return ctx.getApplicationContext().getSharedPreferences(StorePrefix, Context.MODE_PRIVATE).getString(key, s);
    }

    public static <T> T getData(Context ctx, String key) {
        FileInputStream in = null;
        ObjectInputStream ois = null;
        T result = null;
        try {
            File dir = new File(ctx.getFilesDir() + File.separator + "store");
            File path = new File(dir.toString() + File.separator + Util.md5(key) + ".dat");
            in = new FileInputStream(path);
            ois = new ObjectInputStream(in);
            result = (T) ois.readObject();
            ois.close();
            in.close();
        } catch (Exception e) {
            log.error(e);
        } finally {
            return result;
        }
    }


    private static boolean isMetaType(Object data) {
        return data instanceof String
                || data instanceof Integer
                || data instanceof Long
                || data instanceof Float
                || data instanceof Boolean;

    }
}
