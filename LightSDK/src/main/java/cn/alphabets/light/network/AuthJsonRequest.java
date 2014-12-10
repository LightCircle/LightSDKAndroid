package cn.alphabets.light.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.alphabets.light.setting.Default;

/**
 * 封装后台请求
 * Created by lin on 14/12/1.
 */
public class AuthJsonRequest extends JsonObjectRequest {

    public Map<String, String> headers;

    public AuthJsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public AuthJsonRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        headers = response.headers;
        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> header = super.getHeaders();
        if (header == null || header.size() == 0) {
            header = new HashMap<String, String>();
            if (SessionManager.getCookie() != null) {
                header.put(Default.CookieName, SessionManager.getCookie());
            }
        }
        return header;
    }

    public Map<String, String> getResponseHeaders() {
        return headers;
    }
}
