package com.material.materialmanager.model;

import com.material.materialmanager.utils.Constants;
import com.material.materialmanager.utils.HttpUtils;
import com.material.materialmanager.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Doing on 2016/12/24 0024.
 */
public class LoginModel {

    private static final String TAG = LoginModel.class.getSimpleName() + "======";

    private HttpUtils httpUtils;

    public LoginModel() {
        httpUtils = HttpUtils.getInstance();
    }

    /**
     *
     * @param accountId
     * @param password
     * @return null：密码错误， 非null：用户名字
     */
    public String login(String accountId, String password, String userType) throws IOException {
        String key = null;
        try {
            key = getMD5EncryptedString(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("accountId", accountId);
        map.put("password", key);
        map.put("userType", userType);
        LogUtils.i(map.toString());
        LogUtils.i(Constants.URL_POST_LOGIN_IN);
        String result = httpUtils.synPostForm(Constants.URL_POST_LOGIN_IN, map);
        LogUtils.i(result);
        try {
            String userName = parseUserName(result);
            return userName;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseUserName(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        boolean success = jsonObject.getBoolean("success");
        if (success) {
            String userName = jsonObject.getString("data");
            return userName;
        }else {
            return null;
        }
    }

    private String getMD5EncryptedString(String encTarget) throws NoSuchAlgorithmException {
        MessageDigest mdEnc = MessageDigest.getInstance("MD5");
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) {
            md5 = "0"+md5;
        }
        return md5;
    }

}
