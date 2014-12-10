package cn.alphabets.light.application;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import cn.alphabets.light.log.Logger;
import cn.alphabets.light.network.AuthJsonRequest;
import cn.alphabets.light.network.Parameter;
import cn.alphabets.light.network.VolleyManager;
import cn.alphabets.light.ui.MaskFragment;

/**
 * Activity父类
 * Created by lin on 14/12/2.
 */
public class ABActivity extends Activity {

    /** Mask屏 */
    protected MaskFragment mask;

    /**
     * 请求成功时的回调方法
     */
    public interface Success {
        public void onResponse(JSONObject response);
    }

    /**
     * Get请求
     * @param url
     * @param params
     * @param listener
     */
    public void GET(String url, Parameter params, Success listener) {
        this.request(Request.Method.GET, url, params, listener);
    }

    public void POST(String url, Parameter params, Success listener) {
        this.request(Request.Method.POST, url, params, listener);
    }

    public void PUT(String url, Parameter params, Success listener) {
        this.request(Request.Method.PUT, url, params, listener);
    }

    public void DELETE(String url, Parameter params, Success listener) {
        this.request(Request.Method.DELETE, url, params, listener);
    }

    private void request(int method, String url, Parameter params, final Success listener) {

        // 显示等待
        mask = (mask == null) ? new MaskFragment() : mask;
        mask.show(getFragmentManager());

        // 发送请求
        AuthJsonRequest request = VolleyManager.getJsonRequest(method, url, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                mask.hide();
                listener.onResponse(response);
            }
        }, error);

        // 添加至queue
        request.setTag(this);
        VolleyManager.getRequestQueue().add(request);
    }

    private Response.ErrorListener error = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            onRequestError(error);
        }
    };

    /**
     * 网路请求错误处理
     */
    protected void onRequestError(VolleyError error) {

        Logger.e(error);

        // 隐藏Mask屏
        if (mask != null) {
            mask.hide();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        VolleyManager.getRequestQueue().cancelAll(this);
    }
}
