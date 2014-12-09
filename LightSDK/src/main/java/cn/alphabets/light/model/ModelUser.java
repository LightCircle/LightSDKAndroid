package cn.alphabets.light.model;

import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 用户数据
 * Created by lin on 14/11/21.
 */
public class ModelUser extends BasicModel {

    private String id;
    private String outer;
    private String name;
    private String password;
    private int type;
    private List<String> groups;
    private List<String> roles;
    private String email;
    private String lang;
    private String timezone;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOuter() {
        return outer;
    }

    public void setOuter(String outer) {
        this.outer = outer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static TypeToken getTypeToken() {
        return new TypeToken<ModelUser>() {};
    }

    public static TypeToken getListTypeToken() {
        return new TypeToken<List<ModelUser>>() {};
    }
}
