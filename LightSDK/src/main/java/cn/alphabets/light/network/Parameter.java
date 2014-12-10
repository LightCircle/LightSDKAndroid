package cn.alphabets.light.network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 *
 * Created by lilin on 14/12/2.
 */
public class Parameter extends JSONObject {

    public static JSONObject parse(String string) {
        try {
            return new JSONObject(string);
        } catch (JSONException e) {
            throw new RuntimeException("Converting object failed.");
        }
    }

    public Parameter(String... params) {
        super();
        this.add(params);
    }

    public Parameter put(String... params) {
        this.add(params);
        return this;
    }

    public Parameter put(String name, String value) {

        try {
            super.put(name, value);
        } catch (JSONException e) {
            throw new RuntimeException("Converting object failed.");
        }

        return this;
    }

    public Parameter put(String name, int value) {

        try {
            super.put(name, value);
        } catch (JSONException e) {
            throw new RuntimeException("Converting object failed.");
        }

        return this;
    }

    public Parameter put(String name, Object value) {

        try {
            super.put(name, value);
        } catch (JSONException e) {
            throw new RuntimeException("Converting object failed.");
        }

        return this;
    }

    public Map<String, Object> toHash() {
        return ParameterMapParser.jsonToMap(this);
    }

    private void add(String... params) {
        for (int i = 0; i < params.length; i+=2) {
            this.put(params[i], params[i+1]);
        }
    }
}
