package cn.alphabets.light.model;

import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 版本信息
 * Created by lin on 14/12/10.
 */
public class ModelVersion extends BasicModel {

    private int code;
    private String version;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static TypeToken getTypeToken() {
        return new TypeToken<ModelVersion>() {};
    }

    public static TypeToken getListTypeToken() {
        return new TypeToken<List<ModelVersion>>() {};
    }
}
