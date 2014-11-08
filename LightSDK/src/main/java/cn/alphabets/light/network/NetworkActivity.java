package cn.alphabets.light.network;

import android.app.Activity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by 罗浩 on 14/11/7.
 */
public class NetworkActivity extends Activity {
    private RequestQueue queue;

    public Request DoRequest(int method,
                             String url,
                             Map<String, String> params,
                             final SuccessHandler successHandler,
                             final ErrorHandler errorHandler) {

        return getRequest(method, url, params, null, successHandler, errorHandler);
    }

    public Request DoRequest(int method,
                             String url,
                             JSONObject jsonRequest,
                             final SuccessHandler successHandler,
                             final ErrorHandler errorHandler) {

        return getRequest(method, url, null, jsonRequest, successHandler, errorHandler);
    }

    public Request DoRequest(int method,
                             String url,
                             Map<String, String> params,
                             JSONObject jsonRequest,
                             final SuccessHandler successHandler,
                             final ErrorHandler errorHandler) {

        return getRequest(method, url, params, jsonRequest, successHandler, errorHandler);
    }

    private Request getRequest(int method, String url, Map<String, String> params, JSONObject jsonRequest, final SuccessHandler successHandler, final ErrorHandler errorHandler) {
        Request request = Request.NewRequest
                (this, method, url, params, jsonRequest,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                successHandler.onSuccess(response);
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                errorHandler.onError(error);

                            }
                        });
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }
        request.setTag(this);
        queue.add(request);
        return request;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(this);
        }
    }

    public interface ErrorHandler {
        /**
         * Callback method that an error has been occurred with the
         * provided error code and optional user-readable message.
         */
        public void onError(VolleyError error);
    }

    public interface SuccessHandler {
        /**
         * Callback method that an error has been occurred with the
         * provided error code and optional user-readable message.
         */
        public void onSuccess(JSONObject response);
    }


}
