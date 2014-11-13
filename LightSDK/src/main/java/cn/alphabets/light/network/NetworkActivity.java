package cn.alphabets.light.network;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
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

    public void UploadFile(final File file, final SuccessHandler successHandler, final ErrorHandler errorHandler) {

        class ResponseHolder {
            ResponseHolder(JSONObject json, Exception exception) {
                this.json = json;
                this.exception = exception;
            }

            JSONObject json;
            Exception exception;
        }

        new AsyncTask<File, Integer, ResponseHolder>() {

            @Override
            protected void onPostExecute(ResponseHolder responseHolder) {
                super.onPostExecute(responseHolder);

                if (responseHolder.exception != null) {
                    errorHandler.onError(new VolleyError(responseHolder.exception));
                } else {
                    successHandler.onSuccess(responseHolder.json);
                }
            }

            @Override
            protected ResponseHolder doInBackground(File... files) {

                HttpClient client = new DefaultHttpClient();
                Uri.Builder builder = new Uri.Builder();
                builder.scheme(SessionManager.Scheme).encodedAuthority(SessionManager.Host + ":" + SessionManager.Port).appendEncodedPath("file/create");


                //csrf 统一放到url中传给后台
                if (SessionManager.getCSRFString(NetworkActivity.this) != null) {
                    builder.appendQueryParameter("_csrf", SessionManager.getCSRFString(NetworkActivity.this));
                }
                String url = builder.build().toString();
                HttpPost post = new HttpPost(url);
                post.setHeader("Cookie", SessionManager.getCookieString(NetworkActivity.this));

                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

                for (File file : files) {
                    if (file.exists()) {
                        entityBuilder.addBinaryBody("files", file, ContentType.create(GetMimeType(file)), file.getName());
                    }
                }


                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                JSONObject jsonObject = null;
                Exception exception = null;
                try {
                    HttpResponse response = client.execute(post);
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode != HttpStatus.SC_OK) {
                        throw new HttpResponseException(statusCode, "Error status : " + response.getStatusLine().toString());
                    } else {
                        jsonObject = new JSONObject(EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exception = e;
                }
                return new ResponseHolder(jsonObject, exception);
            }
        }.execute(file);
    }

    private static String GetMimeType(File file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(file.getAbsolutePath());
        return type;
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
