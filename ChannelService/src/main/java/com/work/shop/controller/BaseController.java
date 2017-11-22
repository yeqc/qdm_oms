package com.work.shop.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSONObject;
import com.work.shop.united.client.facade.UserStore;
import com.work.shop.util.DateTimeUtils;
import com.work.shop.util.PropertieFileReader;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.EditReturnResult;

public class BaseController {
	
	public static final String ftpRootPath = PropertieFileReader.getString("ftp_path");

	/**
	 * 编辑页面，提交信息后的可接收的后台返回的信息格式。
	 * 
	 * @param succOrError
	 * @param msg
	 * @throws IOException
	 */
	protected void writeMsg(boolean succOrError, String msg,HttpServletResponse response) {
		try {
			
			writeObject(new EditReturnResult(succOrError, msg),response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void writeObject(Object object,HttpServletResponse response) {
		try {
			String jsonObject = JSONObject.toJSONString(object);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(jsonObject);
			response.getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void writeMsgSucc(boolean code,String msg, HttpServletResponse response) throws IOException {
		try {
			response.setContentType("text/html;charset=utf-8"); 
			response.getWriter().write("{\"success\":\""+code+"\",\"msg\":\""+ msg + "\"}");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void writeJson(Paging paging, HttpServletResponse response) throws IOException {
		write(PageHelper.toJsonString(paging), response);
	}

	protected void write(String outputStr, HttpServletResponse response) {
		response.setContentType("application/json;charset=utf-8");
		try {
			response.getWriter().write(outputStr);
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 输出JSON字符串
	 * 
	 * @param response
	 * @param obj
	 */
	public static void outPrintJson(HttpServletResponse response, Object obj) {
		response.setCharacterEncoding("UTF-8");
		response.addHeader("CacheControl", "no-cache");
		response.addHeader("Content-Type", "application/json");
		PrintWriter out = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			out = response.getWriter();
			String jsonStr = mapper.writeValueAsString(obj);
			out.print(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
		}
	}
	
	/** 
     * 导出为CSV文件 
     *  
     */
	public void exportCsvFile(HttpServletRequest request,HttpServletResponse response, StringBuffer sb) throws Exception{
		
		String fileName = "";
		String dateStr = DateTimeUtils.format(new Date(),DateTimeUtils.dateTimeString4FileName);
		fileName = "goods_tickets"+dateStr+".csv";
		PrintWriter printer = initPrintWriter(fileName, response);
		
		writeData(printer, sb.toString(), true);
		//return fileName;
	}
	
     public void exportCsvFile1(HttpServletRequest request,HttpServletResponse response, StringBuffer sb,String fileName) throws Exception{
		
		//String fileName = "";
	//	String dateStr = DateTimeUtils.format(new Date(),DateTimeUtils.dateTimeString4FileName);
	//	fileName = "goods_tickets"+dateStr+".csv";
		PrintWriter printer = initPrintWriter(fileName, response);
		
		writeData(printer, sb.toString(), true);
		//return fileName;
	}
	
	/**
	 * 初始化输出
	 * @param fileName
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected PrintWriter initPrintWriter(String fileName,HttpServletResponse response) throws Exception{
		   response.setContentType("application/octet-stream;charset=GBK");
		   response.setHeader("Content-Disposition","attachment;  filename="+fileName);
		   PrintWriter out = response.getWriter();//放在第一句是会出现乱码 
		   return out;
	}
	
	/**
	 * 输出数据
	 * @param writer
	 * @param data
	 * @param isEnd
	 * @throws Exception
	 */
	protected void writeData(PrintWriter writer ,String data , boolean isEnd) throws Exception{
		writer.write(data);
		writer.flush();
		if(isEnd){
			writer.close();
		}
	}
	
	/**
	 * 获取当前用户对象
	 * */
	public String getUserName(HttpServletRequest request){
		String userName = "";
		if(UserStore.get(request)!=null){
			userName = UserStore.get(request).getUserName()== null ? "":UserStore.get(request).getUserName();
		}
		return userName;
	}

}
