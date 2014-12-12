package cn.alphabets.light.network;

import com.android.volley.AuthFailureError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.alphabets.light.log.Logger;
import cn.alphabets.light.model.ModelUser;
import cn.alphabets.light.model.ResponseParser;
import cn.alphabets.light.setting.Default;
import cn.alphabets.light.util.SharedData;

/**
 * Session管理
 * Created by luohao on 14/10/17.
 */
public class SessionManager {

    public static final String USER_INFO = "UserInfo";

    private static String cookie;
    private static String csrf;

    public static void initialize(AuthJsonRequest request) {
        Map<String, String> handlers = request.getResponseHeaders();
        setCookie(handlers.get(Default.ServerCookieName).split(";")[0]);
        setCsrf(handlers.get(Default.ServerCsrfName));
    }

    public static String getCookie() {
        if (cookie != null) {
            return cookie;
        }

        return SharedData.getInstance().get(Default.CookieName);
    }

    public static void setCookie(String cookie) {
        if (cookie == null) {
            return;
        }

        if (SessionManager.cookie == null || !SessionManager.cookie.equals(cookie)) {
            SessionManager.cookie = cookie;
            SharedData.getInstance().push(Default.CookieName, cookie);
        }
    }

    public static String getCsrf() {
        if (csrf != null) {
            return csrf;
        }
        return SharedData.getInstance().get(Default.CsrfName);
    }

    public static void setCsrf(String csrf) {
        if (csrf == null) {
            return;
        }

        if (SessionManager.csrf == null || !SessionManager.csrf.equals(csrf)) {
            SessionManager.csrf = csrf;
            SharedData.getInstance().push(Default.CsrfName, csrf);
        }
    }

    /**
     * 获取用户对象，从SharedPreferences里获取登陆时保存的用户数据
     * 转换出错，或未能获取用户数据会返回空的ModUser对象
     * @return 用户对象
     */
    public static ModelUser getUser() {

        // 获取保存的用户数据
        String json = SharedData.getInstance().get(USER_INFO);
        if (json == null) {
            return null;
        }

        JSONObject user;
        try {
            user = new JSONObject(json);
        } catch (JSONException e) {
            return null;
        }

        ResponseParser<ModelUser> modUser = ResponseParser.fromJson(user, ModelUser.getTypeToken());
        return modUser.getDetail();
    }

    /**
     * 保存登陆用户信息
     * @param response
     */
    public static void saveUser(JSONObject response) {
        SharedData.getInstance().push(USER_INFO, response.toString());
    }

    /**
     * 清除用户数据
     */
    public static void clearUser() {
        SharedData.getInstance().pop(USER_INFO);
    }
}
