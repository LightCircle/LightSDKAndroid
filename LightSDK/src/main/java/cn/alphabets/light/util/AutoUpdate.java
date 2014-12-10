package cn.alphabets.light.util;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

import cn.alphabets.light.exception.NetworkException;
import cn.alphabets.light.log.Logger;
import cn.alphabets.light.model.ModelVersion;
import cn.alphabets.light.model.ResponseParser;
import cn.alphabets.light.network.ContextManager;
import cn.alphabets.light.network.Parameter;
import cn.alphabets.light.network.VolleyManager;
import cn.alphabets.light.setting.Default;

/**
 * 自动更新
 *
 * 使用例：
 *  AutoUpdate.updateAvailable("DispatchManagerAndroid", new AutoUpdate.CheckUpdateListener() {
 *    public void onResult(boolean isAvailable, String currentVersion, String newVersion) {
 *    }
 *  });
 *
 *  AutoUpdate.downloadAPK("http://10.0.1.61:5003/file/download/548846f1f5e44a8352c115b3", new AutoUpdate.UpdateListener() {
 *    public void onResult(File file) {
 *      AutoUpdate.installAPK(SplashActivity.this, file);
 *    }
 *  });
 *
 * Created by lin on 14/12/10.
 */
public class AutoUpdate {

    /**
     * 检查更新回调
     */
    public interface CheckUpdateListener {
        public void onResult(boolean isAvailable, String currentVersion, String newVersion);
    }

    /**
     * 文件下载回调
     */
    public interface UpdateListener {
        public void onResult(File file);
    }

    /**
     * 检查更新
     * @param listener
     */
    public static void updateAvailable(String appName, CheckUpdateListener listener) {
        checkVersion(Default.URL_VERSION_CHECK, appName, listener);
    }

    /**
     * 下载应用程序文件
     * @param url
     * @param listener
     */
    public static void downloadAPK(String url, UpdateListener listener) {
        DownloadTask task = new DownloadTask();
        task.listener = listener;
        File file = new File(FileUtil.getCacheDir(), UUID.randomUUID().toString());
        task.execute(url, file.getAbsolutePath());
    }

    /**
     * 启动更新
     * @param apkFile
     */
    public static void installAPK(Activity activity, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    /**
     * 获取当前版本
     * @return 当前版本信息
     */
    private static ModelVersion getCurrentVersion() {

        Application context = ContextManager.getInstance();
        PackageManager manager = context.getPackageManager();

        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            ModelVersion version =  new ModelVersion();
            version.setCode(info.versionCode);
            version.setVersion(info.versionName);
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e);
        }

        return null;
    }

    /**
     * 检查版本
     * @param url 检查版本的URL
     * @param name 应用程序名
     * @param listener
     */
    private static void checkVersion(String url, String name, final CheckUpdateListener listener) {

        Response.Listener<JSONObject> success = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                ResponseParser<ModelVersion> result = ResponseParser.fromJson(response, ModelVersion.getTypeToken());
                ModelVersion remote = result.getDetail();
                ModelVersion current = getCurrentVersion();

                boolean isVersionChanged = current.getVersion().equals(remote.getVersion());
                listener.onResult(isVersionChanged, current.getVersion(), remote.getVersion());
            }
        };

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error);
                listener.onResult(false, null, null);
            }
        };

        VolleyManager.getJsonRequest(Request.Method.GET, url, new Parameter("name", name), success, error);
    }

    /**
     * 文件下载器
     */
    private static class DownloadTask extends AsyncTask<String, Void, Void> {

        public UpdateListener listener;
        private File result;

        @Override
        protected Void doInBackground(String... params) {
            try {
                this.result = new File(params[1]);
                FileUtil.downloadFile(params[0], new File(params[1]));
            } catch (NetworkException e) {
                Logger.e(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listener.onResult(this.result);
        }
    }
}
