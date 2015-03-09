package cn.alphabets.light.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import cn.alphabets.light.log.Logger;
import cn.alphabets.light.setting.Default;
import cn.alphabets.light.util.FileUtil;

/**
 * 文件上传支持
 * 虽然没有使用httpclient，但是还是依赖于Apache的
 *  httpcore
 *  httpmime
 * Created by lin on 14/12/3.
 */
public class AuthMultipartRequest extends Request<String> {

    /** 后台接受Multipart时的名字 */
    private static final String MULTIPART_BODY_NAME = "files";

    private final Response.Listener<String> mListener;
    private MultipartProgressListener mProgressListener;
    private HttpEntity httpEntitiy;

    public static interface MultipartProgressListener {
        void onProgress(long transfered, int progress);
    }

    /**
     * 文件上传构造函数
     * @param url
     * @param params
     * @param listener
     * @param errorListener
     */
    public AuthMultipartRequest(
            String url,
            Map<String, Object> params,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener) {
        this(url, params, listener, errorListener, null);
    }

    public AuthMultipartRequest(
            String url,
            Map<String, Object> params,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener,
            MultipartProgressListener progressListener) {

        super(Method.POST, url, errorListener);

        mListener = listener;
        mProgressListener = progressListener;
        buildMultipartEntity(params);
    }

    /**
     * 生成Entity
     */
    private void buildMultipartEntity(Map<String, Object> params) {

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        for (Map.Entry<String, Object> entry : params.entrySet()) {

            if (entry.getValue() instanceof File) {
                File file = (File) entry.getValue();
                String name = file.getName();
                String contentType = FileUtil.getMimeTypeOfFile(file.getAbsolutePath());
                builder.addBinaryBody(MULTIPART_BODY_NAME, file, ContentType.create(contentType), name);
            }

            if (entry.getValue() instanceof String) {
                builder.addTextBody(entry.getKey(), (String) entry.getValue());
            }

        }

        httpEntitiy = builder.build();
    }

    @Override
    public String getBodyContentType() {
        return httpEntitiy.getContentType().getValue();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return Response.success(new String(response.data), getCacheEntry());
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpEntitiy.writeTo(new CountingOutputStream(bos, httpEntitiy.getContentLength(), mProgressListener));
        } catch (IOException e) {
            Logger.e(e);
            throw new RuntimeException("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
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

    /**
     * 已经上传的内容大小计量器
     */
    public static class CountingOutputStream extends FilterOutputStream {
        private final MultipartProgressListener progListener;
        private long transferred;
        private long fileLength;

        public CountingOutputStream(
                final OutputStream out,
                long fileLength,
                final MultipartProgressListener listener) {

            super(out);
            this.fileLength = fileLength;
            this.progListener = listener;
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            if (progListener != null) {
                this.transferred += len;
                int prog = (int) (transferred * 100 / fileLength);
                this.progListener.onProgress(this.transferred, prog);
            }
        }

        public void write(int b) throws IOException {
            out.write(b);
            if (progListener != null) {
                this.transferred++;
                int prog = (int) (transferred * 100 / fileLength);
                this.progListener.onProgress(this.transferred, prog);
            }
        }

    }
}
