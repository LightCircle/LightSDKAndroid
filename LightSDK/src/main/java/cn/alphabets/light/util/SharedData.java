package cn.alphabets.light.util;

import android.app.Application;
import android.content.SharedPreferences;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.alphabets.light.network.ContextManager;

/**
 * 保存应用程序的设定等数据，应用退出后不会消失
 * Created by sh on 14-9-25.
 */
public class SharedData {

    private static final String PREFERENCES_NAME = "android.common.util.SharedData";
    private static final int PREFERENCES_MODE = 0;
    private static SharedData instance = null;
    private SharedPreferences preferences;

    /**
     * 禁止生成实例使用
     */
    private SharedData() {
    }

    /**
     * 获取实例
     * @return 实例
     */
    public static SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
            instance.initPreferences();
        }

        return instance;
    }

    /**
     * 保存一个值
     * @param key 标识
     * @param val 值
     */
    public void push(String key, String val) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(key, val);
        editor.commit();
    }

    /**
     * 获取一个值，并删除
     * @param key 标识
     * @return 值
     */
    public String pop(String key) {

        SharedPreferences.Editor editor = this.preferences.edit();

        String result = this.get(key);
        editor.remove(key);
        editor.commit();
        return result;
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取一个值
     * @param key 标识
     * @return 值
     */
    public String get(String key) {
        return this.preferences.getString(key, null);
    }

    /**
     * 添加一个对象，对象会被转换成base64编码的字符串保存
     * @param key 标识
     * @param val 对象
     */
    public void push(String key, Object val) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {
            new ObjectOutputStream(stream).writeObject(val);
        } catch (IOException e) {
            throw new RuntimeException("Object can not be converted to a byte.");
        }

        // 将字节流编码成base64的字符串
        String base64 = new String(Base64.encodeBase64(stream.toByteArray()));

        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(key, base64);
        editor.commit();
    }

    /**
     * 获取一个对象
     * @param key 标识
     * @return 对象
     */
    public Object getObject(String key) {

        String string = this.preferences.getString(key, null);
        if (string == null) {
            return null;
        }

        byte[] base64 = Base64.decodeBase64(string.getBytes());

        try {
            return new ObjectInputStream(new ByteArrayInputStream(base64)).readObject();
        } catch (Exception e) {
            throw new RuntimeException("Byte can not be converted to an object.");
        }
    }

    /**
     * 保存数值
     * @param key 标识
     * @param val 数
     */
    public void push(String key, int val) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    /**
     * 获取数值
     * @param key 标识
     * @return 数
     */
    public int getInt(String key) {
        return this.preferences.getInt(key, 0);
    }

    /**
     * 生成SharedPreferences的实例
     */
    private void initPreferences() {
        Application context = ContextManager.getInstance();
        instance.preferences = context.getSharedPreferences(PREFERENCES_NAME, PREFERENCES_MODE);
    }
}
