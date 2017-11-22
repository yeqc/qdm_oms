package com.work.shop.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import com.alibaba.fastjson.JSON;
import com.work.shop.api.bean.ItemAdd;
import com.work.shop.api.bean.ItemAdddetail;
import com.work.shop.oms.utils.HttpClientUtil;
import com.work.shop.util.Constants;
import com.work.shop.util.Sha1;

public class YkApiAddProdTest {

	public static void main(String[] args) {
		String appKey = "EZP";
		String token = "1234Tk123";
		String url = "http://test.ezrpro.com:8085/";
		String method = "api/mprod/ShopProdBatchAdd";
		String timestamp = com.work.shop.oms.utils.TimeUtil.format3Date(new Date());
		String str = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Token=" + token;
		String shopCode = "21015";
		System.out.println("sign str:" + str);
		try {
			byte b[] = str.getBytes("utf-8");
			YkApiAddProdTest apiTest = new YkApiAddProdTest();
			String sign = Sha1.sha1(b);
			System.out.println("sign:" + sign.toUpperCase());
			
			ItemAdd itemAdd = new ItemAdd();
			itemAdd.setIsOnSale(false);
			itemAdd.setItemNo("04184190");
			itemAdd.setRetailPrice(0.01D);
			itemAdd.setSalePrice(0.01D);
			itemAdd.setShopCode(shopCode);
			List<ItemAdddetail> adddetails = new ArrayList<ItemAdddetail>();
			ItemAdddetail adddetail = new ItemAdddetail();
			adddetail.setBarCode("20170913");
			adddetail.setRetailPrice(0.01D);
			adddetail.setSalePrice(0.01D);
			adddetail.setStocks(3);
			adddetails.add(adddetail);
			itemAdd.setDetailItems(adddetails);
			System.out.println(JSON.toJSONString(itemAdd));
			
			
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			ps.add(new NameValuePair("AppId", appKey));
			ps.add(new NameValuePair("Timestamp", timestamp));
			ps.add(new NameValuePair("Sign", sign.toUpperCase()));
			ps.add(new NameValuePair("Args", JSON.toJSONString(itemAdd)));
			ps.add(new NameValuePair("AppSystem", "POS"));
			String paramstring = "AppId=" + appKey + "&Timestamp=" + timestamp + "&Sign=" + sign.toUpperCase()
					+ "&Args=" + JSON.toJSONString(itemAdd) + "&AppSystem=POS";
			System.out.println(paramstring);
			String msg = apiTest.post(url + method, ps.toArray(new NameValuePair[ps.size()]));
			System.out.println(msg);
			/*List<org.apache.http.NameValuePair> valuePairs = new ArrayList<org.apache.http.NameValuePair>();
			valuePairs.add(new BasicNameValuePair("AppId", appKey));
			valuePairs.add(new BasicNameValuePair("Timestamp", timestamp));
			valuePairs.add(new BasicNameValuePair("Sign", sign.toUpperCase()));
			valuePairs.add(new BasicNameValuePair("Args", JSON.toJSONString(param)));
			valuePairs.add(new BasicNameValuePair("AppSystem", "POS"));
			
			String msg = com.work.shop.util.HttpClientUtil.post(url, valuePairs);
			
//			System.out.println(msg);
			/*String url = "http://localhost:8070/ChannelService/custom/channelShopApi/addItem";
			ItemAdd itemAdd = new ItemAdd();
			itemAdd.setIsOnSale(false);
			itemAdd.setItemNo("OHIUN001");
			itemAdd.setRetailPrice(30D);
			itemAdd.setSalePrice(30D);
			itemAdd.setShopCode("YK01");
			List<ItemAdddetail> adddetails = new ArrayList<ItemAdddetail>();
			ItemAdddetail adddetail = new ItemAdddetail();
			adddetail.setBarCode("BHUJK001");
			adddetail.setRetailPrice(30D);
			adddetail.setSalePrice(30D);
			adddetail.setStocks(3);
			adddetails.add(adddetail);
			itemAdd.setDetailItems(adddetails);
			System.out.println(JSON.toJSONString(adddetails));
			List<org.apache.http.NameValuePair> valuePairs = new ArrayList<org.apache.http.NameValuePair>();
			valuePairs.add(new BasicNameValuePair("isOnSale", "false"));
			valuePairs.add(new BasicNameValuePair("ItemNo", "OHIUN001"));
			valuePairs.add(new BasicNameValuePair("retailPrice", "30"));
			valuePairs.add(new BasicNameValuePair("salePrice", "30"));
			valuePairs.add(new BasicNameValuePair("shopCode", "YK01"));
			valuePairs.add(new BasicNameValuePair("itemAdd.detailItems[0]", JSON.toJSONString(adddetail)));
			String msg = com.work.shop.util.HttpClientUtil.post(url, valuePairs);
			System.out.println(msg);*/
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
