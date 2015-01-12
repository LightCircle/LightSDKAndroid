package cn.alphabets.light.model;

import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 设定
 * Created by lin on 15/1/12.
 */
public class ModelSetting extends BasicModel {

    private String type;
    private String  key;
    private String value;
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static TypeToken getTypeToken() {
        return new TypeToken<ModelSetting>() {};
    }

    public static TypeToken getListTypeToken() {
        return new TypeToken<List<ModelSetting>>() {};
    }

}
