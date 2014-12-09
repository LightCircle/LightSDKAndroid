package cn.alphabets.light.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Mod的父类，处理共通字段
 * Created by luohao on 14/10/17.
 */
public class BasicModel {

    protected String _id;
    protected String valid;
    protected String createAt;
    protected String createBy;
    protected String updateAt;
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public JSONObject toJSON() {
        try {
            return new JSONObject(new Gson().toJson(this));
        } catch (JSONException e) {
            throw new RuntimeException("Converting object failed.");
        }
    }
}
