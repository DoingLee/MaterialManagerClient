package com.material.materialmanager.utils;

import com.material.materialmanager.BuildConfig;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;

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
        HashMap<String, String> map = new HashMap<>();
        map.put("userName", "张一");
        map.put("orderId", "2");
        map.put("action", "获取原料");
        String r = httpUtils.synPostForm("http://localhost:8080/order_track/", map);
        System.out.println(r);
    }
}