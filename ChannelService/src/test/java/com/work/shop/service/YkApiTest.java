package com.work.shop.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import com.alibaba.fastjson.JSON;
import com.work.shop.oms.utils.HttpClientUtil;
import com.work.shop.util.Constants;
import com.work.shop.util.Sha1;

public class YkApiTest {

	public static void main(String[] args) {
		String appKey = "EZP";
		String token = "1234Tk123";
//		token:1234Tk123
		String url = "http://test.ezrpro.com:8085/";
		String method = "api/morder/ordgetbatch";
		String timestamp = com.work.shop.oms.utils.TimeUtil.format3Date(new Date());
		String str = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Token=" + token;
		System.out.println("sign str:" + str);
		try {
			byte b[] = str.getBytes("utf-8");
			YkApiTest apiTest = new YkApiTest();
//			String sign = MD5.md5(b);
			String sign = Sha1.sha1(b);
			System.out.println("sign:" + sign.toUpperCase());
			//拼装接口入参
			OrderParam param = new OrderParam();
			param.setOrderTimeStart("2017-08-01");
			param.setOrderTimeEnd("2017-09-15");
			param.setPageIndex("1");
			param.setHaveOrderDetail("true");
			param.setIsGetDiscountAmountDtl("true");
//			param.setOrderStatus("8");
			
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			ps.add(new NameValuePair("AppId", appKey));
			ps.add(new NameValuePair("Timestamp", timestamp));
			ps.add(new NameValuePair("Sign", sign.toUpperCase()));
			ps.add(new NameValuePair("Args", JSON.toJSONString(param)));
			ps.add(new NameValuePair("AppSystem", "POS"));
			String paramstring = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Sign=" + sign.toUpperCase()
					+ "&Args=" + JSON.toJSONString(param) + "&AppSystem=POS";
			System.out.println(paramstring);
			String msg = apiTest.post(url + method, ps.toArray(new NameValuePair[ps.size()]));
			/*List<org.apache.http.NameValuePair> valuePairs = new ArrayList<org.apache.http.NameValuePair>();
			valuePairs.add(new BasicNameValuePair("AppId", appKey));
			valuePairs.add(new BasicNameValuePair("Timestamp", timestamp));
			valuePairs.add(new BasicNameValuePair("Sign", sign.toUpperCase()));
			valuePairs.add(new BasicNameValuePair("Args", JSON.toJSONString(param)));
			valuePairs.add(new BasicNameValuePair("AppSystem", "POS"));
			
			String msg = com.work.shop.util.HttpClientUtil.post(url, valuePairs);*/
			
			System.out.println(msg);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	private String post(String URL, NameValuePair[] params) {
		String responseBody = "";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(URL);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
		managerParams.setConnectionTimeout(30000);
		managerParams.setSoTimeout(5000);
		try {
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
//			postMethod.addRequestHeader("Content-Type", "multipart/form-data; charset=UTF-8");
			postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			postMethod.setRequestBody(params);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				responseBody = postMethod.getResponseBodyAsString();
			} else {
				System.out.println("Eorr occus");
				responseBody = "跳转失败，错误编码：" + statusCode;
			}
		} catch (Exception e) {
			responseBody = "网关限制无法跳转!";
		} finally {
			postMethod.releaseConnection();
		}
		return responseBody;
	}
}
