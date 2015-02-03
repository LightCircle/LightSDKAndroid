package cn.alphabets.light.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.alphabets.light.R;
import cn.alphabets.light.log.Logger;
import cn.alphabets.light.network.AuthJsonRequest;
import cn.alphabets.light.network.AuthMultipartRequest;
import cn.alphabets.light.network.Parameter;
import cn.alphabets.light.network.VolleyManager;
import cn.alphabets.light.setting.Default;
import cn.alphabets.light.ui.Dialog;
import cn.alphabets.light.ui.MaskFragment;
import cn.alphabets.light.util.SharedData;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Activity父类
 *
 * 说明
 *  1. Session超时时，会调用Default.BroadcastLogout，通常该broadcast应该实现注销任务，并跳转到登陆页面
 *  2. Action包含所有Fragment的网络请求，当Action的onStop被调用时，会尝试停止说有网络请求
 *
 * Created by lin on 14/12/2.
 */
public class ABActivity extends Activity {

    /** Mask屏 */
    protected MaskFragment mask;

    /** 是否显示Mask屏，缺省为显示 */
    private boolean isShowWaiting;

    /** 网络请求错误处理监听器 */
    private Response.ErrorListener error;

    /**
     * 请求成功时的回调方法
     */
    public interface Success {
        public void onResponse(JSONObject response);
    }

    /**
     * Get请求
     * @param url url
     * @param params params
     * @param listener success callback function
     */
    public AuthJsonRequest GET(String url, Parameter params, Success listener) {
        return this.request(Request.Method.GET, url, params, listener);
    }

    public AuthJsonRequest POST(String url, Parameter params, Success listener) {
        return this.request(Request.Method.POST, url, params, listener);
    }

    public AuthJsonRequest PUT(String url, Parameter params, Success listener) {
        return this.request(Request.Method.PUT, url, params, listener);
    }

    public AuthJsonRequest DELETE(String url, Parameter params, Success listener) {
        return this.request(Request.Method.DELETE, url, params, listener);
    }

    public AuthMultipartRequest UPLOAD(String url, Parameter params, final Success listener) {
        return this.request(url, params.toHash(), listener);
    }

    /**
     * 禁止，启用Mask屏的显示
     * @param isShowWaiting 是否显示Mask
     */
    public void showWaiting(boolean isShowWaiting) {
        this.isShowWaiting = isShowWaiting;
    }

    /**
     * 返回mask实例
     * @return mask
     */
    public MaskFragment getMask() {
        return this.mask;
    }

    /**
     * 显示Mask屏
     */
    private void showWaiting() {
        if (!this.isShowWaiting) {
            return;
        }

        if (this.mask.isVisible()) {
            return;
        }
        this.mask.show(getFragmentManager());
    }

    /**
     * 隐藏Mask屏
     */
    private void hideWaiting() {
        if (!this.mask.isVisible()) {
            return;
        }
        this.mask.hide();
    }

    /**
     * 文件上传
     * @param url url
     * @param params params
     * @param listener listener
     */
    private AuthMultipartRequest request(String url, Map<String, Object> params, final Success listener) {

        this.showWaiting();
        AuthMultipartRequest request = VolleyManager.getMultipartRequest(url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideWaiting();
                listener.onResponse(Parameter.parse(response));
            }
        }, error);

        // 设定tag
        request.setTag(this);
        return request;
    }

    /**
     * 调用网络请求，和Activity的方法相同。一种方法是可以调用activity的该方法
     * 现阶段不想和Activity发生关联，所以单独写了一套
     * @param method HTTP方法
     * @param url URL
     * @param params 请求参数
     * @param listener 请求成功
     */
    private AuthJsonRequest request(int method, String url, Parameter params, final Success listener) {

        this.showWaiting();
        AuthJsonRequest request = VolleyManager.getJsonRequest(method, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideWaiting();
                listener.onResponse(response);
            }
        }, error);

        // 设定tag
        request.setTag(this);
        return request;
    }

    /**
     * 网路请求错误处理
     */
    public void onRequestError(VolleyError error) {

        Logger.e(error);

        // 隐藏Mask屏
        hideWaiting();

        // Session timeout
        if (error instanceof AuthFailureError) {
            sendBroadcast(new Intent(Default.BroadcastLogout));
            return;
        }

        // 无法连接服务器
        if (error instanceof NoConnectionError) {

            // 旧版的Android，不支持401错误，都是NoConnectionError，只能判断消息体的内容
            if (error.getMessage().contains("authentication challenge")) {
                sendBroadcast(new Intent(Default.BroadcastLogout));
                return;
            }

            if (isDebug() && error.getMessage() != null) {
                Dialog.toast(error.getMessage());
            } else {
                Dialog.toast(R.string.network_error);
            }
            return;
        }

        // 服务器错误
        if (error instanceof ServerError) {
            Dialog.toast(R.string.server_error);
            return;
        }

        // 超时错误
        if (error instanceof TimeoutError) {
            Dialog.toast(R.string.timeout_error);
            return;
        }

        // 其他错误
        if (isDebug() && error.getMessage() != null) {
            Dialog.toast(error.getMessage());
        } else {
            Dialog.toast(R.string.unknown_error);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.isShowWaiting = true;
        this.mask = new MaskFragment();

        this.error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onRequestError(error);
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        VolleyManager.getRequestQueue().cancelAll(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideWaiting();
    }

    /**
     * 是否是调试模式
     * @return debug: true
     */
    private boolean isDebug() {
        return SharedData.getInstance().get(Default.DebugModel) != null;
    }
}
