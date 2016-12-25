package com.material.materialmanager.utils;

import com.material.materialmanager.BuildConfig;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Doing on 2016/12/23 0023.
 */
//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class, sdk = 18)
public class HttpUtilsTest extends TestCase {

    HttpUtils httpUtils = HttpUtils.getInstance();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testSynGet() throws Exception {
//        String r = httpUtils.synGet("http://localhost:8080/order/1/unsolved/");
        String r = httpUtils.synGet("http://localhost:8080/plan/product1/");
        System.out.println(r);
    }

    @Test
    public void testSynPostForm() throws Exception {
//        提交流程
//        HashMap<String, String> map = new HashMap<>();
//        map.put("userName", "张一");
//        map.put("orderId", "2");
//        map.put("action", "获取原料");
//        String r = httpUtils.synPostForm("http://localhost:8080/order_track/", map);
//        System.out.println(r);

//        提交登录
        HashMap<String, String> map = new HashMap<>();
        String key = getMD5EncryptedString("123");
        map.put("accountId", "123");
        map.put("password", key);
        map.put("userType", "line_worker");
        String r = httpUtils.synPostForm("http://localhost:8080/login/", map);
        System.out.println(r);

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