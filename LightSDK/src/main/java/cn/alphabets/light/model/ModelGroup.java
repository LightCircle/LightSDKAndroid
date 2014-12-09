package cn.alphabets.light.model;

import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by sh on 14/12/3.
 */
public class ModelGroup extends BasicModel {

    private String name;
    private String outer;
    private String type;
    private String description;
    private String visible;
    private String parent;
    private String[] owners;
    private String sort;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOuter() {
        return outer;
    }

    public void setOuter(String outer) {
        this.outer = outer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String[] getOwners() {
        return owners;
    }

    public void setOwners(String[] owners) {
        this.owners = owners;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static TypeToken getTypeToken() {
        return new TypeToken<ModelGroup>() {};
    }

    public static TypeToken getListTypeToken() {
        return new TypeToken<List<ModelGroup>>() {};
    }
}
