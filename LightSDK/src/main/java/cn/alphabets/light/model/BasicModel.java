package cn.alphabets.light.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Mod的父类，处理共通字段
 * Created by luohao on 14/10/17.
 */
public class BasicModel implements Serializable {

    protected String _id;
    protected String valid;
    protected Date createAt;
    protected String createBy;
    protected Date updateAt;
    protected String updateBy;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public JSONObject toJSON() {
        return toJSON(this);
    }

    public static JSONObject toJSON(BasicModel model) {
        try {
            return new JSONObject(new Gson().toJson(model));
        } catch (JSONException e) {
            throw new RuntimeException("Converting object failed.");
        }
    }

    public static BasicModel parse(JSONObject json, TypeToken typeToken) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateConverter()).create();
        return gson.fromJson(json.toString(), typeToken.getType());
    }

    public static TypeToken getTypeToken() {
        return new TypeToken<BasicModel>() {
        };
    }

    public static TypeToken getListTypeToken() {
        return new TypeToken<List<BasicModel>>() {
        };
    }
}
