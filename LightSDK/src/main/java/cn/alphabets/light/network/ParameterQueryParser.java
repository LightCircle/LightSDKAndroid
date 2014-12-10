package cn.alphabets.light.network;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.alphabets.light.log.Logger;

/**
 * 将json转换为query parameter
 * Created by lin on 14/12/4.
 */
public class ParameterQueryParser {

    /**
     * 获取GET请求用Query参数
     * @param object
     * @return
     * @throws org.json.JSONException
     */
    public static String parse(JSONObject object) {

        Uri.Builder builder = new Uri.Builder();
        parse(object, builder);

        return builder.build().toString();
    }

    /**
     * 获取GET请求用Query参数
     * @param object
     * @param builder
     * @throws org.json.JSONException
     */
    public static void parse(JSONObject object, Uri.Builder builder) {
        try {
            parseHash(object, builder, null);
        } catch (JSONException e) {
            Logger.e(e);
            throw new RuntimeException("Converting object failed.");
        }
    }

    private static void parseHash(JSONObject object, Uri.Builder builder, String parent) throws JSONException {
        Iterator<String> keys = object.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object val = object.get(key);
            String name = parent == null ? key : parent + "[" + key + "]";

            // 数组
            if (val instanceof JSONArray) {
                parseArray((JSONArray) val, builder, name);
            }

            // 对象
            if (val instanceof JSONObject) {
                parseHash((JSONObject) val, builder, name);
            }

            // 其他
            if (val instanceof String || isBasicType(val)) {
                builder.appendQueryParameter(name, String.valueOf(val));
            }
        }
    }

    private static void parseArray(JSONArray array, Uri.Builder builder, String parent) throws JSONException {
        for (int i = 0; i < array.length(); i++) {

            Object val = array.get(i);
            String name = parent + "[" + String.valueOf(i) + "]";

            // 对象
            if (val instanceof JSONObject) {
                parseHash((JSONObject) val, builder, name);
            }

            // 其他
            if (val instanceof String || isBasicType(val)) {
                builder.appendQueryParameter(name, String.valueOf(val));
            }
        }
    }

    public static boolean isBasicType(Object value) {
        return value instanceof Double || value instanceof Boolean || value instanceof Integer || value instanceof Long;
    }


}
