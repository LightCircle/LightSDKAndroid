package cn.alphabets.light.model;

import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 分类
 * Created by lin on 14/12/17.
 */
public class ModelCategory extends BasicModel {

    private String type;
    private String categoryId;
    private String name;
    private String sort;
    private String value;
    private String translation;
    private String description;
    private String parent;
    private List<String> ancestors;
    private String visible;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<String> getAncestors() {
        return ancestors;
    }

    public void setAncestors(List<String> ancestors) {
        this.ancestors = ancestors;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public static TypeToken getTypeToken() {
        return new TypeToken<ModelCategory>() {};
    }

    public static TypeToken getListTypeToken() {
        return new TypeToken<List<ModelCategory>>() {};
    }

}
