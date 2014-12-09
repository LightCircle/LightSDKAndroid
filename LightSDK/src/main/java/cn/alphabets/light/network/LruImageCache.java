package cn.alphabets.light.network;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * 图片内存缓存
 * LRU(Least Recently Used)，根据使用的顺序生成缓存，依赖LruCache
 * ImageLoader原则上禁止有可能阻塞处理的处理，如文件缓存等
 * Created by lilin on 14/12/1.
 */
public class LruImageCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> mMemoryCache;

    public LruImageCache() {

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //int cacheSize = maxMemory / 8;    // 使用最大内存的一定比例
        int cacheSize = 10 * 1024 * 1024;   // 10MB 使用固定大小

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 返回使用的缓存大小(KB)
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mMemoryCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }
}
