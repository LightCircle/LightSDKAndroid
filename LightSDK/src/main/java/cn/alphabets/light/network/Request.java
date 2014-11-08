package cn.alphabets.light.network;

import android.content.Context;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 罗浩 on 14/11/7.
 */
public class Request extends JsonObjectRequest {

    public static final int GET = 0;
    public static final int POST = 1;
    public static final int PUT = 2;
    public static final int DELETE = 3;

    private Map<String, String> mResponseHeaders;
    private Context mContext;

    public Request(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }


    public static Request NewRequest(Context ctx,
                                     int method,
                                     String url,
                                     Map<String, String> params,
                                     Response.Listener<JSONObject> listener,
                                     Response.ErrorListener errorListener) {
        return NewRequest(ctx, method, url, params, null, listener, errorListener);
    }

    public static Request NewRequest(Context ctx,
                                     int method,
                                     String url,
                                     JSONObject jsonRequest,
                                     Response.Listener<JSONObject> listener,
                                     Response.ErrorListener errorListener) {
        return NewRequest(ctx, method, url, null, jsonRequest, listener, errorListener);
    }

    public static Request NewRequest(Context ctx,
                                     int method,
                                     String url,
                                     Map<String, String> params,
                                     JSONObject jsonRequest,
                                     Response.Listener<JSONObject> listener,
                                     Response.ErrorListener errorListener) {


        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SessionManager.Scheme).encodedAuthority(SessionManager.Host + ":" + SessionManager.Port).appendEncodedPath(url);

        //用params构造url
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        //csrf 统一放到url中传给后台
        if (SessionManager.getCSRFString(ctx) != null) {
            builder.appendQueryParameter("_csrf", SessionManager.getCSRFString(ctx));
        }
        url = builder.build().toString();
        Request req = new Request(method, url, jsonRequest, listener, errorListener);
        req.mContext = ctx;
        return req;

    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        mResponseHeaders = response.headers;
        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> header = super.getHeaders();
        if (header == null || header.size() == 0) {
            header = new HashMap<String, String>();
            if (SessionManager.getCookieString(mContext) != null) {
                header.put("Cookie", SessionManager.getCookieString(mContext));
            }
        }
        return header;
    }

    public Map<String, String> getResponseHeaders() {
        return mResponseHeaders;
    }
}
