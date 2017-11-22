package com.work.shop.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.banggo.common.util.DateUtil;
import com.work.shop.bean.PromotionsLog;
import com.work.shop.service.ChannelApiLogService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TimeUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelApiLogVO;
import com.work.shop.vo.ChannelErpUpdownLogVO;
import com.work.shop.vo.ChannelStockLogVO;
import com.work.shop.vo.JsonResult;

@Controller
@RequestMapping(value = "/log/")
public class ChannelApiLogController extends BaseController {
	private static Logger log = Logger.getLogger(ChannelApiLogController.class);

	//private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final int PAGE_SIZE = 5000;

	@Resource(name = "channelApiLogService")
	private ChannelApiLogService channelApiLogService;

	@RequestMapping(value = "/search.spmvc")
	public void getChannelShopList(
			@ModelAttribute ChannelApiLogVO ChannelApiLogVO,
			HttpServletRequest request, HttpServletResponse response) {

		JsonResult result = channelApiLogService
				.searchChannelApiLog(ChannelApiLogVO);
		outPrintJson(response, result.getData());
	}
	
	@RequestMapping(value = "/searchStockLog.spmvc")
	public void getChannelStockLog(
			@ModelAttribute ChannelStockLogVO channelStockLogVO,
			HttpServletRequest request, HttpServletResponse response) {

		JsonResult result = channelApiLogService
				.searchChannelStockLog(channelStockLogVO);
		outPrintJson(response, result.getData());
	}

	@RequestMapping(value = "/searchErpUpdownLog.spmvc")
	public void searchErpUpdownLog(HttpServletRequest request,
			HttpServletResponse response, ChannelErpUpdownLogVO log,
			PageHelper helper) {
		JsonResult result = channelApiLogService
				.searchErpUpdownLog(log, helper);
		outPrintJson(response, result.getData());
	}
	
	@RequestMapping(value = "/searchPromotionsLog.spmvc")
	public void searchPromotionsLog(HttpServletRequest request,
			HttpServletResponse response, PromotionsLog log,
			PageHelper helper) {
		JsonResult result = channelApiLogService
				.searchPromotionsLog(log, helper);
		outPrintJson(response, result.getData());
	}

	/***
	 * 导出日志
	 * @param channelApiLogVO
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportLog.spmvc")
	public void exportLog(@ModelAttribute ChannelApiLogVO channelApiLogVO,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		String path = getPath();
		String dateStr =  TimeUtil.format(new Date(),"yyyy_MM_dd");
		String fileName = "log_"+dateStr+".csv";
	
		File CreateFile = new File(path
				+ "/page/log/exportLog/");
		//文件路径;
		if (!CreateFile.exists()) {
			CreateFile.mkdirs();
		}
		//输出文件;
		File file = new File(path + "/page/log/exportLog/" + fileName);
		channelApiLogService.deleteBeforeDateForFile(CreateFile, fileName);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
			//总数
			int num = channelApiLogService.countChannelApiLog(channelApiLogVO);
			int pageNum = num / PAGE_SIZE;
			if (num % PAGE_SIZE > 0) {
				++pageNum;
			}
			StringBuffer sb = new StringBuffer();
			sb.append("渠道,店铺,调整单号类型,操作人,调整单号,调整结果,创建时间,执行信息\r\n");
			bw.write(sb.toString());
			bw.flush();
			sb = new StringBuffer();
			int logCount=PAGE_SIZE;
			for(int j = 0; j<pageNum;j++) {
				channelApiLogVO.setStart(j*PAGE_SIZE);
				channelApiLogVO.setLimit(logCount);
				List<ChannelApiLogVO> list = channelApiLogService.searchLog(channelApiLogVO);
				if (null != list && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
					String	channelTitle = list.get(i).getChannelTitle() == null ? "" : list.get(i).getChannelTitle();	
					sb.append(channelTitle + ",");
		
					String	shopTitle = list.get(i).getShopTitle() == null ? "" : list.get(i).getShopTitle();	
					sb.append(shopTitle + ","); 
					String	methodName = transMethodName(list.get(i).getMethodName());
					sb.append(methodName + ",");
					
					String	actionUser = list.get(i).getUser() == null ? "" : list.get(i).getUser();	
					sb.append(actionUser + ","); 
					
					String	paramInfo = list.get(i).getParamInfo() == null ? "" : list.get(i).getParamInfo();
					sb.append(paramInfo + ",");
					
					String	returnCode= "0".equals(list.get(i).getReturnCode()) ? "执行成功" :"执行失败";	
					sb.append(returnCode + ","); 
					
					String requestTime= list.get(i).getRequestTime() == null ? "" :DateUtil.dateToString( list.get(i).getRequestTime());	
					sb.append(requestTime + ",");
		
					String	returnMessage = list.get(i).getReturnMessage() == null ? "" : list.get(i).getReturnMessage();
					returnMessage = returnMessage.replaceAll("\"", "'");
					returnMessage = "\""+returnMessage + "\"";
					sb.append(returnMessage + "\r\n"); 
						bw.write(sb.toString());
						bw.flush();
						sb = new StringBuffer();
					}
				}
			}
			bw.flush();
			jsonResult.setIsok(true);
			jsonResult.setMessage(path+ "/page/log/exportLog/" + fileName);
		} catch (Exception e) {
			log.error("导出日志文件异常", e);
			jsonResult.setIsok(false);
			jsonResult.setMessage("导出失败！");
		} finally {
			if (null != bw) {
				try {
					bw.close();
				} catch (IOException e) {
					log.error("关闭写入流异常", e);
				}
			}
			writeObject(jsonResult, response);
		}
	}
	
	/***
	 * 导出库存同步日志
	 * @param channelStockLogVO
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportStockLog.spmvc")
	public void exportStockLog(@ModelAttribute ChannelStockLogVO channelStockLogVO,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		String path = getPath();
		String dateStr =  TimeUtil.format(new Date(),"yyyy_MM_dd");
		String fileName = "StockLog_"+dateStr+".csv";
	
		File CreateFile = new File(path
				+ "/page/log/exportStockLog/");
		//文件路径;
		if (!CreateFile.exists()) {
			CreateFile.mkdirs();
		}
		//输出文件;
		File file = new File(path
				+ "/page/log/exportStockLog/" + fileName);
		channelApiLogService.deleteBeforeDateForFile(CreateFile, fileName);
		//总数
		Paging page = (Paging) channelApiLogService.searchChannelStockLog(channelStockLogVO).getData();
		int num = page.getTotalProperty();
		int pageNum = num / PAGE_SIZE;
		if (num % PAGE_SIZE > 0) {
			++pageNum;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("渠道" + "," + "店铺" + "," + "商品11位码" + "," + "库存量" + ","
				+ "执行结果" +","+"执行时间"+","+"执行信息"+ "\r\n");

		int logCount=PAGE_SIZE;
		for(int j = 0; j<pageNum;j++) {
			channelStockLogVO.setStart(j*PAGE_SIZE);
			channelStockLogVO.setLimit(logCount);
			Paging page2 = (Paging) channelApiLogService.searchChannelStockLog(channelStockLogVO).getData();
			List<ChannelStockLogVO> list = (List<ChannelStockLogVO>) page2.getRoot();
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String	channelTitle = list.get(i).getChannelTitle() == null ? "" : list.get(i).getChannelTitle();	
					sb.append(channelTitle + ",");
		
					String	shopTitle = list.get(i).getShopTitle() == null ? "" : list.get(i).getShopTitle();	
					sb.append(shopTitle + ","); 
		
					String	skuSn = list.get(i).getSkuSn() == null ? "" : list.get(i).getSkuSn();	
					sb.append(skuSn + ","); 
			
					Integer	stock =  list.get(i).getStock()==null?0:list.get(i).getStock();
					sb.append(stock + ",");
					
					String	returnCode= "0".equals(list.get(i).getReturnCode()) ? "执行成功" :"执行失败";
					sb.append(returnCode + ","); 
					
					String requestTime= list.get(i).getRequestTime() == null ? "" :DateUtil.dateToString( list.get(i).getRequestTime());	
					sb.append(requestTime + ","); 
		
					String	returnMessage = list.get(i).getReturnMessage() == null ? "" : list.get(i).getReturnMessage();
					returnMessage = returnMessage.replaceAll("\"", "'");
					returnMessage = "\""+returnMessage + "\"";
					sb.append(returnMessage + "\r\n"); 
				}
			}
		}
		try {
//			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			BufferedWriter bw =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));

			bw.write(sb.toString());
			bw.close();
			jsonResult.setIsok(true);
			jsonResult.setMessage(path+ "/page/log/exportStockLog/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			jsonResult.setMessage("导出失败！");
		}
		writeObject(jsonResult, response);

	
	}
	
	
	/**
	 * 导出上下架日志
	 * @param channelErpUpdownLog
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportUpDownLog.spmvc")
	public void exportUpDownLog(@ModelAttribute ChannelErpUpdownLogVO channelErpUpdownLog,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		//取当前路径
		String path = getPath();
		//取当前时间
		String dateStr =  TimeUtil.format(new Date(),"yyyy_MM_dd");
		
		String fileName = "erpUpdownLog_"+dateStr+".csv";
	
		File CreateFile = new File(path + "/page/erpUpdownLog/exportLog/");
		//文件路径;
		if (!CreateFile.exists()) {
			CreateFile.mkdirs();
		}
		//输出文件;
		File file = new File(path + "/page/erpUpdownLog/exportLog/" + fileName);
		channelApiLogService.deleteBeforeDateForFile(CreateFile, fileName);
		BufferedWriter bw = null;
		try {
			//总数
			int num = channelApiLogService.countErpUpdownLog(channelErpUpdownLog);
			int pageNum = num / PAGE_SIZE;
			if (num % PAGE_SIZE > 0) {
				++pageNum;
			}
			StringBuffer sb = new StringBuffer();
			sb.append("渠道,店铺,商品编号,同步结果,状态,同步信息,创建时间\r\n");
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
			int logCount=PAGE_SIZE;
			for(int j=0; j<pageNum;j++){
				PageHelper helper = new PageHelper();
				helper.setStart(j*PAGE_SIZE);
				helper.setLimit(logCount);
				List<ChannelErpUpdownLogVO> list = channelApiLogService.searchUpdownLog(channelErpUpdownLog, helper);
				if (null != list && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String	channelTitle = list.get(i).getChannelTitle() == null ? "" : list.get(i).getChannelTitle();
						sb.append(channelTitle + ",");
						String	shopTitle = list.get(i).getShopTitle() == null ? "" : list.get(i).getShopTitle();
						sb.append(shopTitle+ ","); 
						String	goodsSn = list.get(i).getGoodsSn() == null ? "" : list.get(i).getGoodsSn();
						sb.append(goodsSn + ","); 
						String	code = list.get(i).getCode() == null ? "" : list.get(i).getCode();	
						sb.append(chanageCode(code) + ","); 
						
						String status = list.get(i).getStatus() == null ? "" : list.get(i).getStatus();
						sb.append(chanageStatus(status) + ","); 
						String message = list.get(i).getMessage() == null ? "" : list.get(i).getMessage();
						
						message = message.replaceAll("\"", "'");
						message = "\""+message + "\"";
						sb.append(message+ ",");
						String requestTime = list.get(i).getRequestTime() == null ? "" : 
							DateUtil.dateToString( list.get(i).getRequestTime());
						sb.append(requestTime + "\r\n");
						bw.write(sb.toString());
						bw.flush();
						sb = new StringBuffer();
					}
				}
			}
			bw.flush();
			jsonResult.setIsok(true);
			jsonResult.setMessage(path+ "/page/erpUpdownLog/exportLog/" + fileName);
		} catch (Exception e) {
			log.error("导入上下架日志信息异常", e);
			jsonResult.setIsok(false);
			jsonResult.setMessage("导出失败！");
		} finally {
			if (null != bw) {
				try {
					bw.close();
				} catch (IOException e) {
					log.error("关闭写入流异常", e);
				}
			}
			writeObject(jsonResult, response);
		}
	}
	
	/***
	 * 导出促销操作日志
	 * @param channelApiLogVO
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportPromotionsLog.spmvc")
	public void exportPromotionsLog(@ModelAttribute PromotionsLog log,
			HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = new JsonResult();
		String path = getPath();
		String dateStr =  TimeUtil.format(new Date(),"yyyy_MM_dd");
		String fileName = "promotionslog_"+dateStr+".csv";
	
		File CreateFile = new File(path
				+ "/page/log/exportLog/");
		//文件路径;
		if (!CreateFile.exists()) {
			CreateFile.mkdirs();
		}
		//输出文件;
		File file = new File(path
				+ "/page/log/exportLog/" + fileName);
		channelApiLogService.deleteBeforeDateForFile(CreateFile, fileName);
		//总数
		int num = channelApiLogService.countPromotionsLog(log);
		int pageNum = num / PAGE_SIZE;
		if (num % PAGE_SIZE > 0) {
			++pageNum;
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("店铺" + "," + "促销编码" + "," + "促销类型" + "," + "操作人" + "," + "操作时间" + "," + "操作结果" + "\r\n");

		int logCount=PAGE_SIZE;
		
		for(int j = 0; j<pageNum;j++) {
			PageHelper helper = new PageHelper();
			helper.setStart(j*PAGE_SIZE);
			helper.setLimit(logCount);
			List<PromotionsLog> list = channelApiLogService.searchOnePagePromotionsLog(log, helper);
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String	shopTitle = list.get(i).getShopTitle() == null ? "" : list.get(i).getShopTitle();	
					sb.append(shopTitle + ",");
		
					String	promCode = list.get(i).getPromCode() == null ? "" : list.get(i).getPromCode();	
					sb.append(promCode + ","); 
		
					String	promType = transPromType(String.valueOf(list.get(i).getPromType()));
					sb.append(promType + ",");
					
					String	userId = list.get(i).getUserId() == null ? "" : list.get(i).getUserId();
					sb.append(userId + ",");
			
					String	addTime = list.get(i).getFormatAddTime() == null ? "" : list.get(i).getFormatAddTime();
					sb.append(addTime + ",");
		
					String	message = list.get(i).getMessage() == null ? "" : list.get(i).getMessage();
					message = message.replaceAll("\"", "'");
					message = "\""+message + "\"";
					sb.append(message + "\r\n"); 
				}
			}
		}
		try {
			BufferedWriter bw =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));

			bw.write(sb.toString());
			bw.close();
			jsonResult.setIsok(true);
			jsonResult.setMessage(path+ "/page/log/exportLog/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			jsonResult.setMessage("导出失败！");
		}
		writeObject(jsonResult, response);

	
	}
	
	private String chanageCode(String value){
		String code="";
		if(value == null){
			return code;
		}
		if("1".equals(value)){
			code = "同步成功";
		} else if("0".equals(value)){
			code = "同步失败";
		} else{
			code = "";
		}
		return 	code;
	}

	private String chanageStatus(String value){
		String status="";
		if(value == null){
			return status;
		}
		if("1".equals(value)){
			status = "上架";
		} else if("0".equals(value)){
			status = "下架";
		} else{
			status = "";
		}
		
		return 	status;
	}
	
	/**
	 *取项目地址
	 ***/
	private String getPath() {
		String path = this.getClass().getResource("/").getPath(); // 得到d:/tomcat/webapps/工程名WEB-INF/classes/路径
		path = path.substring(0, path.indexOf("WEB-INF/classes"));// 从路径字符串中取出工程路劲
		path = path.replace("%20", " ");
		File file = new File(path);
		if (!file.exists()) {
			path = path.substring(1, path.length());
		} else {
			path = file.getPath();
		}
		return path;
	}
	
	/**
	 * 下载文件
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "downloadFile.spmvc")
	public void downloadFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		try {
			String path = StringUtil.trim(request.getParameter("path"));
			File file = new File(path);
			String fileName = file.getName();
			 
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream;charset=GBK;");

			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			
		} catch(Exception e) {
			e.printStackTrace();
			//logger.info("shopbusinessGoodsController.downloadFile( message : " + e + ")");
		}
	}
	private String transMethodName(String methodType) {
		if (StringUtil.isEmpty(methodType)) {
			return "";
		}
		String methodName = methodType;
		if("0".equals(methodType)){
			methodName="价格调整";
		}
		if("1".equals(methodType)){
			methodName="上下架调整";
		}
		if("2".equals(methodType)){
			methodName="商品详情调整";
		}
		if("3".equals(methodType)){
			methodName="卖点调整";
		}
		if("4".equals(methodType)){
			methodName="商品名称调整";
		}
		if("5".equals(methodType)){
			methodName="商品条形码调整";
		}
		if("6".equals(methodType)){
			methodName="商品运费承担方式调整";
		}
		if("7".equals(methodType)){
			methodName="商品支持会员打折调整";
		}
		if("8".equals(methodType)){
			methodName="商品线上线下调整";
		}
		if("9".equals(methodType)){
			methodName="商品详情生成";
		}
		if("10".equals(methodType)){
			methodName="店铺经营商品生成";
		}
		return methodName;
	}
	
	private String transPromType(String promType) {
		if (StringUtil.isEmpty(promType)) {
			return "";
		}
		String methodName = promType;
		if("0".equals(promType)){
			methodName="满赠";
		}
		if("2".equals(promType)){
			methodName="买赠";
		}
		if("3".equals(promType)){
			methodName="集合赠";
		}
		return methodName;
	}
}
