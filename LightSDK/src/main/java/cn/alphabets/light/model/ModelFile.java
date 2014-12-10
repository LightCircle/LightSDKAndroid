package cn.alphabets.light.model;

import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

/**
 * 文件
 * Created by lin on 14/11/22.
 */
public class ModelFile extends BasicModel {

    private String fileId;
    private int length;
    private String name;
    private String category;
    private String description;
    private String contentType;
    private JSONObject extend;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public JSONObject getExtend() {
        return extend;
    }

    public void setExtend(JSONObject extend) {
        this.extend = extend;
    }

    public static TypeToken getTypeToken() {
        return new TypeToken<ModelFile>() {};
    }

    public static TypeToken getListTypeToken() {
        return new TypeToken<List<ModelFile>>() {};
    }

}
