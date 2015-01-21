package cn.alphabets.light.application;

import android.app.Fragment;
import android.os.Bundle;

import com.android.volley.VolleyError;

import cn.alphabets.light.network.AuthJsonRequest;
import cn.alphabets.light.network.AuthMultipartRequest;
import cn.alphabets.light.network.Parameter;
import cn.alphabets.light.network.VolleyManager;

/**
 * Fragment父类
 * Created by lin on 14/12/2.
 */
public class ABFragment extends Fragment {

    /** Activity快捷访问 */
    protected ABActivity activity;

    /** Fragment快捷访问 */
    protected ABFragment fragment;

    /**
     * Get请求
     * @param url url
     * @param params params
     * @param listener listener
     */
    public AuthJsonRequest GET(String url, Parameter params, ABActivity.Success listener) {
        AuthJsonRequest request = activity.GET(url, params, listener);
        request.setTag(this);
        return request;
    }

    public AuthJsonRequest POST(String url, Parameter params, ABActivity.Success listener) {
        AuthJsonRequest request = activity.POST(url, params, listener);
        request.setTag(this);
        return request;
    }

    public AuthJsonRequest PUT(String url, Parameter params, ABActivity.Success listener) {
        AuthJsonRequest request = activity.PUT(url, params, listener);
        request.setTag(this);
        return request;
    }

    public AuthJsonRequest DELETE(String url, Parameter params, ABActivity.Success listener) {
        AuthJsonRequest request = activity.DELETE(url, params, listener);
        request.setTag(this);
        return request;
    }

    public AuthMultipartRequest UPLOAD(String url, Parameter params, final ABActivity.Success listener) {
        AuthMultipartRequest request = activity.UPLOAD(url, params, listener);
        request.setTag(this);
        return request;
    }

    /**
     * 网路请求错误处理
     */
    protected void onRequestError(VolleyError error) {
        activity.onRequestError(error);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = (ABActivity) this.getActivity();
        this.fragment = this;
    }

    @Override
    public void onStop() {
        super.onStop();
        VolleyManager.getRequestQueue().cancelAll(this);
    }
}
