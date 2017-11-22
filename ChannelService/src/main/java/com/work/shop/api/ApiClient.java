/*
 * Copyright (C), 2002-2013, 苏宁易购电子商务有限公司
 * FileName: SopHttpClient.java
 * Author:   12090158
 * Date:     2013-1-16 下午3:17:16
 * Description: //模块目的、功能描述
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.work.shop.api;

import com.work.shop.util.Constants;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈API调用示例-JAVA版〉<br>
 * 〈功能详细描述〉
 *
 * @author 12090158
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ApiClient {

    public ApiClient(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    private String appKey;
    private String appSecret;

    public String _excute(String aipMethod, String params) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appMethod", aipMethod);
        map.put("resparams", params);

        map.put(Constants.SN_APP_KEY, this.appKey);
        map.put(Constants.SN_APP_SECRET, this.appSecret);
        map.put("format", ConstantValues.SN_FORMT_JSON);
        map.put("versionNo", ConstantValues.SN_API_VERSION);
        map.put("appRequestTime", getNowTime());

        String content = doRequest(map);

        return content;
    }

    /**
     * API请求
     */
    private String doRequest(Map<String, Object> map) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(Constants.SN_SERVER_URL).openConnection();
            mergeHttpHead(con, map);
            mergeHttpBody(con, (String) map.get("resparams"));
            return doResponse(con);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 封装API请求头
     */
    private void mergeHttpHead(HttpURLConnection con, Map<String, Object> map) {

        try {
            // 上传图片的一些参数设置
            con.setRequestProperty(
                    "Accept",
                    "image/gif,   image/x-xbitmap,   image/jpeg,   image/pjpeg,   application/vnd.ms-excel,   application/vnd.ms-powerpoint,   application/msword,   application/x-shockwave-flash,   application/x-quickviewplus,   */*");
            con.setRequestProperty("Accept-Language", "zh-cn");
            con.setRequestProperty("Content-type", "multipart/form-data;   boundary=---------------------------7d318fd100112");
            // con.setRequestProperty("Accept-Encoding", "gzip,   deflate");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setUseCaches(false);
            con.setRequestProperty("appKey", (String) map.get(Constants.SN_APP_KEY));
            con.setRequestProperty("appMethod", (String) map.get("appMethod"));
            con.setRequestProperty("format", (String) map.get("format"));
            con.setRequestProperty("appRequestTime", map.get("appRequestTime").toString());
            con.setRequestProperty("signInfo", paramsSign(map));
            con.setRequestProperty("versionNo", map.get("versionNo").toString());
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

    }

    /**
     * 封装http请求体
     */
    private void mergeHttpBody(HttpURLConnection con, String params) {
        try {

            OutputStream out = con.getOutputStream();
            // 写入业务数据
            out.write(params.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 反馈响应
     */
    private String doResponse(HttpURLConnection con) {
        int responseCode;
        StringBuffer strContents = new StringBuffer();
        try {
            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream urlStream = con.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(urlStream);
                byte[] contents = new byte[1024];
                int byteRead = 0;

                try {
                    while ((byteRead = bis.read(contents)) != -1) {
                        strContents.append(new String(contents, 0, byteRead, "utf-8"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bis.close();

                System.out.println(strContents.toString());

                return strContents.toString();
            } else {
                System.out.println("API请求响应" + responseCode);
            }
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前时间
     */
    private static String getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());

    }

    /**
     * 签名信息
     */
    @SuppressWarnings("static-access")
    private String paramsSign(Map<String, Object> map) {

        String resparams = map.get("resparams").toString();
        String baseStr = encryptMessage.base64Encode(resparams.getBytes()).replaceAll("\r|\n", "");
        StringBuffer signStr = new StringBuffer();
        signStr.append(map.get(Constants.SN_APP_SECRET).toString()).append(map.get("appMethod").toString()).append(map.get("appRequestTime").toString())
                .append(map.get(Constants.SN_APP_KEY).toString()).append(map.get("versionNo").toString()).append(baseStr);
        return encryptMessage.encryptMessage(encryptMessage.MD5_CODE, signStr.toString());
    }

    EncryptMessage encryptMessage = new EncryptMessage();
}
