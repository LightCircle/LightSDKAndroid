package cn.alphabets.light.network;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

/**
 *
 * Created by lin on 14/12/1.
 */
public class AuthImageLoader extends ImageLoader {

    /** image success callback when loaded. Need to get Bitmap, you can use the callback */
    private Success success;

    public interface Success {
        public void onResponse(Bitmap bitmap);
    }

    /**
     * add callback
     * @param success callback instance
     */
    public void addListener(Success success) {
        this.success = success;
    }

    /**
     * clear callback
     */
    public void clearListener() {
        this.success = null;
    }

    /**
     * Constructs a new ImageLoader.
     *
     * @param queue      The RequestQueue to use for making image requests.
     * @param imageCache The cache to use as an L1 cache.
     */
    public AuthImageLoader(RequestQueue queue, ImageCache imageCache) {
        super(queue, imageCache);
    }

    @Override
    protected Request<Bitmap> makeImageRequest(String requestUrl, int maxWidth, int maxHeight, final String cacheKey) {

        return new AuthImageRequest(requestUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                onGetImageSuccess(cacheKey, response);
                if (success != null) {
                    success.onResponse(response);
                    clearListener();
                }
            }
        }, maxWidth, maxHeight, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onGetImageError(cacheKey, error);
                clearListener();
            }
        });
    }
}
