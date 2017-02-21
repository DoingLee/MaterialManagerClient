package com.material.materialmanager.model;

import android.util.Log;

import com.material.materialmanager.Bean.WeightResult;
import com.material.materialmanager.utils.Constants;
import com.material.materialmanager.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Doing on 2017/1/13 0013.
 */
public class WeightSocket {

    private static final String TAG = "WeightSocket =============  ";

    private Socket mobileSocket;
    private int weight;
    private String weighterName;

    public WeightSocket(int weight, String weighterName) {
        this.weight = weight;
        this.weighterName = weighterName;
    }

    public WeightResult startWeight() {
        WeightResult weightResult = null;
        try {
            log("移动端开始连接");
//            mobileSocket = new Socket("127.0.0.1", 10002, null, 6002);
            mobileSocket = new Socket();
            mobileSocket.setReuseAddress(true);
            mobileSocket.setSoTimeout(60 * 1000);
            mobileSocket.connect(new InetSocketAddress(Constants.hostName, 10002));
            log("移动端连接成功");
            weightResult = weight();
            mobileSocket.close();
        } catch (IOException e) {
            try {
                e.printStackTrace();
                log("mobileSocket 异常 close 1");
                mobileSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            return weightResult;
        }
    }

    private WeightResult weight() {
        WeightResult weightResult = null;
        try {
            log("移动端开始发送称重请求信息");
            String requireString = "{weight:" +
                    weight +
                    ",weighterName:\"" +
                    weighterName +
                    "\"}";
            OutputStream outputStream = mobileSocket.getOutputStream();
            PrintStream out = new PrintStream(outputStream);
            out.println(requireString);
            out.println("end");
            out.flush();

            log("移动端阻塞等待称重完成");
            InputStream inputStream = mobileSocket.getInputStream();  //阻塞
            String s = inputStreamToString(inputStream);
            log("称重结果：" + s);

            JSONObject jsonObject = null;
            jsonObject = new JSONObject(s);
            boolean success = jsonObject.getBoolean("success");
            String msg = "";
            if (success) {
                int realWeight = jsonObject.getInt("realWeight");
                msg = "称重正确，实际称重：" + realWeight + "g";
            } else {
                String errorMsg = jsonObject.getString("msg");
                if (errorMsg.equals("weighter unConnected!")) {
                    msg = "请连接称重器再称重！";
                } else if (errorMsg.equals("wrong weight!")) {
                    msg = "称重错误，请重新称重！";
                } else {
                    msg = "服务器出错！";
                }
            }
            weightResult = new WeightResult(success, msg);

        } catch (IOException e) {
            e.printStackTrace();
            mobileSocket.close();
            weightResult = new WeightResult(false, "网络出错！");
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return weightResult;
        }
    }

    private String inputStreamToString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            boolean flag = true;
            while (flag) {
                //接收从客户端发送过来的数据
                String str = bufferedReader.readLine();
                if (str == null || "".equals(str)) {
                    flag = false;
                } else {
                    if ("end".equals(str)) {
                        flag = false;
                    } else {
                        sb.append(str);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void log(String s) {
        LogUtils.i(TAG + s);
    }

}
