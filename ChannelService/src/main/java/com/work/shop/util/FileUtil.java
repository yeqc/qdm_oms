package com.work.shop.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.work.shop.api.service.ChannelApiService;

public class FileUtil {
	private static final Logger logger = Logger.getLogger(FileUtil.class);
	/**
	 * 将网络文件写入内存中
	 * @param path 网络文件地址
	 * @return byte[]
	 */
	public static byte[] getByteFile(String path){
		URL url;
		ByteArrayOutputStream outStream = null;
		try {
			url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(10 * 1000);
			InputStream inStream = conn.getInputStream();
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			//使用一个输入流从buffer里把数据读取出来
			while((len=inStream.read(buffer)) != -1 ){
				//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
				outStream.write(buffer, 0, len);
			}
			//关闭输入流
			inStream.close();
			//把outStream里的数据写入内存
		} catch (MalformedURLException e) {
			logger.error("获取图片将网络文件写入内存中异常path=" + path + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("获取图片将网络文件写入内存中异常path=" + path + e.getMessage(), e);
		}
		return outStream.toByteArray();
	}

	
	/**
	 * 图片上传至平台服务器
	 */
	public static String imgServerUrl(String imgUrl, String channelCode, String shopCode, String goodsSn, String widthHeight, Integer imgType) {
		if (StringUtil.isNull(imgUrl)) {
			return "";
		}
		if (StringUtil.isNotNull(channelCode)) {
			if (StringUtil.isTaoBaoChannel(channelCode) || 
					Constants.JD_CHANNEL_CODE.equals(channelCode) || 
					Constants.YHD_CHANNEL_CODE.equals(channelCode)) {
				int jdIndex = imgUrl.indexOf(".360buyimg.com");
				int tbIndex = imgUrl.indexOf(".taobaocdn.com");
				int yhdIndex = imgUrl.indexOf(".yihaodianimg.com");
				if (jdIndex != -1 || tbIndex != -1 || yhdIndex != -1) {
					return imgUrl;
				}
			} else {
				int mbIndex = imgUrl.indexOf(Constants.PICTURE_DOMAIN);
				if (mbIndex != -1) {
					return imgUrl;
				}
			}
		}
		int index = imgUrl.lastIndexOf(".");
		if (index != -1) {
			String prefixPath = imgUrl.substring(0, index);
			String suffixPath = imgUrl.substring(index, imgUrl.length());
			int nameLenth = imgUrl.lastIndexOf("/");
			String imgName = imgUrl;
			if (nameLenth != -1) {
				imgName = imgUrl.substring(nameLenth+1, imgUrl.length());
				imgName = imgType != null ? imgType + "_" + imgName : imgName;
			}
			if (imgName.length() > 64) { //图片名称不能超过64字节
				imgName = imgName.substring(imgUrl.length() - 64, imgUrl.length());
			}
			imgUrl = Constants.PICTURE_DOMAIN + "/" +prefixPath + widthHeight +suffixPath;
			if (StringUtil.isNotNull(channelCode)) {
				if (StringUtil.isTaoBaoChannel(channelCode) || 
						Constants.JD_CHANNEL_CODE.equals(channelCode) || 
						Constants.YHD_CHANNEL_CODE.equals(channelCode)) {
					try {
						ChannelApiService apiService = (ChannelApiService) SpringContextUtil.getBean("apiService");
//						imgUrl = apiService.uploadImg(channelCode, shopCode, goodsSn, imgUrl, imgName);
					} catch (Exception e) {
						logger.error(goodsSn + "图片上传至平台服务器异常", e);
					}
				}
			}
		}
		return imgUrl;
	}
 	
	
	
	@SuppressWarnings("unchecked")
	private static String getPicCid(Map<String, Object> map) {
		List<Object> catList = (List<Object>) map.get("picture_category");
		Map<String,Object> catMap = (Map<String, Object>) catList.get(0);
		String cId = catMap.get("picture_category_id").toString();
		return cId;
	}
	
	/**
	 * 取项目地址
	 *
	 **/
	public static String getDeployPath() {
		String path = "";
		try {
			FileUtil util = new FileUtil();
			path = util.getClass().getResource("/").getPath(); // 得到项目工程WEB-INF/classes/路径
			path = path.substring(0, path.indexOf("WEB-INF/classes"));// 从路径字符串中取出工程路劲
			path = path.replace("%20", " ");
			File file = new File(path);
			if (!file.exists()) {
				path = path.substring(1, path.length());
			} else {
				path = file.getPath();
			}	
		} catch (Exception e) {
			logger.error(e);
		}
		return path;
	}
	
}
