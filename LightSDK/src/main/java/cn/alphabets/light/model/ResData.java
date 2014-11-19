package cn.alphabets.light.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luohao on 14/10/17.
 */
public class ResData<T> implements Serializable {

    private String apiVersion;
    private Data<T> data;
    private JSONObject options;
    private Boolean singleMode;


    public static <C> ResData<C> fromJson(JSONObject jsonObj, TypeToken type) throws JSONException {
        Gson gson = new Gson();

        if (!jsonObj.has("data")) {
            return null;
        }
        if (jsonObj.getJSONObject("data").has("items")) {

            List<C> list = gson.fromJson(jsonObj.getJSONObject("data").getString("items"), type.getType());

            Data<C> data = new Data<C>();
            data.setItems(list);
            data.setTotalItems(jsonObj.getJSONObject("data").getInt("totalItems"));

            JSONObject options = jsonObj.getJSONObject("data").getJSONObject("options");
            ResData<C> resData = new ResData<C>();
            resData.setData(data);
            resData.setApiVersion(jsonObj.getString("apiVersion"));
            resData.setOptions(options);
            resData.setSingleMode(false);
            return resData;
        } else {
            C single = gson.fromJson(jsonObj.getString("data"), type.getType());
            Data<C> data = new Data<C>();
            data.setSingle(single);

            ResData<C> resData = new ResData<C>();
            JSONObject jdata = jsonObj.getJSONObject("data");
            if (jdata.has("options")) {

                JSONObject options = jdata.getJSONObject("options");
                resData.setOptions(options);
            }
            resData.setData(data);
            resData.setApiVersion(jsonObj.getString("apiVersion"));
            resData.setSingleMode(true);
            return resData;
        }
    }


    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Data<T> getData() {
        return data;
    }

    public void setData(Data<T> data) {
        this.data = data;
    }

    public JSONObject getOptions() {
        return options;
    }

    public void setOptions(JSONObject options) {
        this.options = options;
    }

    public Boolean getSingleMode() {
        return singleMode;
    }

    public void setSingleMode(Boolean singleMode) {
        this.singleMode = singleMode;
    }

    public JSONObject getOptionsUser() throws JSONException {
        return this.getOptions().getJSONObject("user");
    }

    public JSONObject getOptionsGroup() throws JSONException {
        return this.getOptions().getJSONObject("group");
    }

    public JSONObject getOptionsCategory() throws JSONException {
        return this.getOptions().getJSONObject("category");
    }

    public static class Data<R> implements Serializable {
        private int totalItems;
        private List<R> items;
        private R single;

        public int getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(int totalItems) {
            this.totalItems = totalItems;
        }

        public List<R> getItems() {
            return items;
        }

        public void setItems(List<R> items) {
            this.items = items;
        }

        public R getSingle() {
            return single;
        }

        public void setSingle(R single) {
            this.single = single;
        }
    }
}
