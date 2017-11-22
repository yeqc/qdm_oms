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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ChannelApiGoods;
import com.work.shop.api.bean.ItemQuery;
import com.work.shop.api.bean.LocalItemQuery;
import com.work.shop.api.service.ApiService;
import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsVo;
import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.bgcontentdb.BGproductBarcodeList;
import com.work.shop.dao.ChannelShopMapper;
import com.work.shop.service.ChannelGoodsService;
import com.work.shop.util.Constants;
import com.work.shop.util.FtpUtil;
import com.work.shop.util.PropertieFileReader;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelApiGoodsVo;
import com.work.shop.vo.ExprotExcelResult;
import com.work.shop.vo.JsonResult;

@Controller
@RequestMapping(value = "shopBusinessGoods")
public class ShopBusinessGoodsController extends BaseController {
	
	private Logger logger = Logger.getLogger(ShopBusinessGoodsController.class);

	private final String EXPORT_FILE_URL = "/page/shopBusinessGoods/exportFile/";

	@Resource(name = "apiService")
	private ApiService apiService;

	@Resource(name = "channelGoodsService")
	private ChannelGoodsService channelGoodsService;
	
	@Resource
	private ChannelShopMapper channelShopMapper;

	// 树菜单页面跳转
	@RequestMapping(value = "shopBusinessGoodsPage.spmvc")
	public ModelAndView shopBusinessGoodsPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shopBusinessGoods/shopBusinessGoodsList");
		return mav;
	}

	@RequestMapping(value = "exprotShopbusinessGoods.spmvc")
	public ModelAndView exprotShopbusinessGoods(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		String shopCode =  request.getParameter("shopCode");
		String channelCode =  request.getParameter("channelCode");
		String status =  request.getParameter("status");
		
		ChannelApiGoodsVo channelShop = new ChannelApiGoodsVo();
		channelShop.setShopCode(shopCode);
		channelShop.setChannelCode(channelCode);
		channelShop.setStatus(status);
		mav.addObject("scs", channelShop);
		mav.setViewName("shopBusinessGoods/exprotShopbusinessGoods");
		return mav;
	}
	//跳转到导出页面，带入数据
	@RequestMapping(value = "exprotShopGoodsFile.spmvc")
	public ModelAndView exprotGoodsFile(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		String shopCode =  request.getParameter("shopCode");
		String type =  request.getParameter("type");
		mav.addObject("type", type);
		mav.addObject("shopCode", shopCode);
		mav.setViewName("shopBusinessGoods/exprotShopGoodsFile");
		return mav;
	}
	//获得ftp上已经生产的商品数据列表
		@RequestMapping(value = "getShopGoodsFileList.spmvc")
		public void getShopGoodsFileList(HttpServletRequest request,
				HttpServletResponse response) {
			String shopCode =  request.getParameter("shopCode");
			String type =  request.getParameter("type");
			String time=request.getParameter("time");
			List<String> list=new ArrayList<String>();
			if(type.equals("goods")){
				list=FtpUtil.getFileNameListByPath(PropertieFileReader.getString("ftp_path") + "/"+ time+"/"+shopCode+"/goods/");
			}else if(type.equals("sku")){
				list=FtpUtil.getFileNameListByPath(PropertieFileReader.getString("ftp_path") + "/"+ time+"/"+shopCode+"/sku/");
			}else if(type.equals("illegal")){
				list=FtpUtil.getFileNameListByPath(PropertieFileReader.getString("ftp_path") + "/"+ time+"/"+shopCode+"/illegal/");
			}
			
			outPrintJson(response,list);
		}
	
	
	//根据文件名和路径获取ftp上的文件
	@RequestMapping(value = "downloadFTPFile.spmvc")
	public void downloadFTPFile(HttpServletRequest request,
			HttpServletResponse response){
		String fileName =  request.getParameter("fileName");
		String type =  request.getParameter("type");
		String shopCode =  request.getParameter("shopCode");
		String time =  request.getParameter("time");
		String ftpPath=PropertieFileReader.getString("ftp_path") + "/"+time+"/"+shopCode+"/"+type;
		FtpUtil.downFtpFile2(fileName,ftpPath, response);
	}
	

	/**
	 * 经营商品列表 
	 * @throws IOException
	 ***/
	@RequestMapping(value = "getShopBusinessGoods.spmvc")
	public void getShopBusinessGoods(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String userName = getUserName(request);
		String limit = StringUtil.trim(request.getParameter("limit"));
		int pageSize = 15;
		if(StringUtil.isNotEmpty(limit)){
			pageSize = Integer.valueOf(limit);
		}
		String start = request.getParameter("start") == null ? "0" : request.getParameter("start");
		int iStart = Integer.valueOf(start)/pageSize;
		iStart++;
		String shopCode = StringUtil.trim(request.getParameter("shopCode"));
		String channelCode = StringUtil.trim(request.getParameter("channelCode"));
		String status = request.getParameter("status");
		String goodsSn = request.getParameter("goodsSn");
		LocalItemQuery itemQuery = new LocalItemQuery();
		itemQuery.setChannelCode(channelCode);
		itemQuery.setShopCode(shopCode);
		itemQuery.setItemNo(goodsSn);
		itemQuery.setPage(iStart);
		itemQuery.setPageSize(pageSize);
		itemQuery.setStatus(status);
		itemQuery.setUserName(userName);
		ApiResultVO<List<ChannelApiGoods>> apiResultVO = apiService.searchItemPage(itemQuery);
		List<ChannelApiGoods> list = apiResultVO.getApiGoods();

		Paging paging = new Paging(Integer.valueOf(apiResultVO.getTotal() == null ? 0 : apiResultVO.getTotal()),
				list);

		writeJson(paging, response);
	}

	/**
	 * 导出6位码经营商品
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "exportshopBusinessGoods.spmvc")
	public void exportshopBusinessGoods(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String userName = getUserName(request);
		int pageSize = 20;
		JsonResult jsonResult = new JsonResult();

		String shopCode = StringUtil.trim(request.getParameter("shopCode"));

		String channelCode = StringUtil.trim(request.getParameter("channelCode"));
		
		if(StringUtil.isTaoBaoChannel(channelCode)){
			if(!Constants.TB_FX.equals(shopCode)){
				pageSize = 200;
			} else {
				pageSize = 30;
			}
		}
		if(Constants.JD_CHANNEL_CODE.equals(channelCode)){
			pageSize = 100;
		}
		if(Constants.YHD_CHANNEL_CODE.equals(channelCode)){
			pageSize = 50;
		}
		if(Constants.WX_CHANNEL_CODE.equals(channelCode)){
			pageSize = 100;
		}
		String stauts = StringUtil.trim(request
				.getParameter("status"));
	
		if(StringUtil.isBlank(stauts)  ){
			jsonResult.setMessage("上下架状态不能为空！");
			jsonResult.setIsok(false);
			outPrintJson(response, jsonResult);
			return;
		}

		if(StringUtil.isBlank(shopCode) || StringUtil.isBlank(channelCode) ){
			jsonResult.setMessage("数据丢失，请刷新！");
			jsonResult.setIsok(false);
			outPrintJson(response, jsonResult);
			return;
		}
		
		String path = getPath();
		String fileName = "GOODSSN_" + shopCode + "_" + channelCode + ".csv";

		File CreateFile = new File(path + EXPORT_FILE_URL);
		//文件路径;
		if (!CreateFile.exists()) {
			CreateFile.mkdirs();
		}
		//输出文件;
		File file = new File(path + "/page/shopBusinessGoods/exportFile/" + fileName);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
			if(!stauts.equals("4")){
				bw.write("店铺编码,店铺名称,商品款号,店铺商品名称,上下架状态,店铺商品价格\r\n");
				bw.flush();
				writeStringForSixCode(bw, pageSize, shopCode,channelCode,stauts,userName);
				if(Constants.PP_CHANNEL_CODE.equals(channelCode) && "2".equals(stauts)){
					//待上架商品
//					upEer = writeStringForSixCode(upEer.getSb(), pageSize, shopCode,channelCode,"5",userName);
					//已售完商品
					writeStringForSixCode(bw, pageSize, shopCode,channelCode,"8",userName);
				}
			}else{
				writeStringForLocalSixCode(bw, shopCode, fileName, response);
			}
			bw.flush();
			jsonResult.setIsok(true);
			jsonResult.setMessage(fileName);
		} catch (Exception e) {
			logger.error("导出6位码商品列表异常", e);
			jsonResult.setIsok(false);
			jsonResult.setMessage("导出失败！");
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
		writeObject(jsonResult, response);
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
	 *  写入excel数据
	 * @param sheet 
	 * @param cell  每个格子
	 * @param shopCode 店铺码
	 * @param channelCode 渠道码
	 * @param wareStatus 状态
	 * @param iRow 第几行
	 * @return ExprotExcelResult
	 */
	private ExprotExcelResult writeExcel(HSSFSheet sheet,HSSFCell cell,String shopCode ,String channelCode, String wareStatus,int iRow, String userName){
		HSSFRow row= sheet.createRow((short)0);
		int pageSize = 20;
		LocalItemQuery itemQuery = new LocalItemQuery();
		itemQuery.setChannelCode(channelCode);
		itemQuery.setShopCode(shopCode);
		itemQuery.setItemNo(null);
		itemQuery.setPage(1);
		itemQuery.setPageSize(pageSize);
		itemQuery.setStatus(wareStatus);
		itemQuery.setUserName(userName);
		ApiResultVO<List<ChannelApiGoods>> rapiResultVO = apiService.searchItemPage(itemQuery);
		List<ChannelApiGoods> flist = rapiResultVO.getApiGoods();
		logger.info("shopbusinessGoodsController.exportshopBusinessGoods(" + "start : 1 " 
				+ ", pageSize : " + pageSize + ", page : "
				+ ", message : " + rapiResultVO.getMessage() + ")");
		//有异常返回
		if(Constants.API_RETURN_NO.equals(rapiResultVO.getCode())){
			/*jsonResult.setMessage(rapiResultVO.getMessage());
			jsonResult.setIsok(false);
			outPrintJson(response, jsonResult);*/
			ExprotExcelResult eer = new ExprotExcelResult();
			eer.setMsg(rapiResultVO.getMessage());
			return eer;
		}
		//总记录数
		int iTotal = rapiResultVO.getTotal();
		//int pageNum =0;
		//计算页数
		int pageNum = iTotal / pageSize;
		if (iTotal % pageSize > 0) {
			++pageNum;
		}

		//大于1页
		if (pageNum > 1) {
			for (int j = 1; j <= pageNum; j++) {
				if (pageNum == j) {
					if(iTotal % pageSize >0){
						pageSize = iTotal % pageSize;
					}
				}
				itemQuery.setPage(j);
				ApiResultVO<List<ChannelApiGoods>> apiResultVO = apiService.searchItemPage(itemQuery);
				List<ChannelApiGoods> list = apiResultVO.getApiGoods();
				logger.info("shopbusinessGoodsController.exportshopBusinessGoods(" + "start :  "+j 
						+ ", pageSize : " + pageSize + ", page : "
						+ ", message : " + apiResultVO.getMessage() + ")");
				
				if(!"0".equals(apiResultVO.getCode())){
				/*	jsonResult.setMessage(apiResultVO.getMessage());
					jsonResult.setIsok(false);
					outPrintJson(response, jsonResult);*/
					ExprotExcelResult eer = new ExprotExcelResult();
					eer.setMsg(rapiResultVO.getMessage());
					return eer;
				}
				
				if(null != list && list.size()>0){

					for (int i = 0; i < list.size(); i++) {
					
						row= sheet.createRow((short)iRow);
						
						cell = row.createCell((short)(0));
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(list.get(i).getShopCode() == null ? "" : list
								.get(i).getShopCode());
						
						cell = row.createCell((short)(1));
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(list.get(i).getShopName() == null ? "" : list
								.get(i).getShopName());
						
						cell = row.createCell((short)(2));
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(list.get(i).getGoodsSn() == null ? "" : list.get(i)
								.getGoodsSn());
						
						
						cell = row.createCell((short)(3));
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(list.get(i).getChannelGoodsSn() == null ? "" : list.get(
								i).getChannelGoodsSn());
						
						cell = row.createCell((short)(4));
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(list.get(i).getGoodsName() == null ? "" : list.get(
								i).getGoodsName());
						
						cell = row.createCell((short)(5));
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(list.get(i).getPrice() == null ? "" : list.get(i)
								.getPrice());
						
						cell = row.createCell((short)(6));
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(chanageStatus( list.get(i).getStatus()));
						
						cell = row.createCell((short)(7));
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(list.get(i).getStockCount() == null ? "" : list.get(i)
								.getStockCount());
				
						iRow++;
					}
				
				}
			}

		} else {
			
			if(null != flist && flist.size()>0){
				
				for (int i = 0; i < flist.size(); i++) {
				
					row= sheet.createRow((short)iRow);
					 
					cell = row.createCell((short)(0));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(flist.get(i).getShopCode() == null ? "" : flist
							.get(i).getShopCode());
					
					cell = row.createCell((short)(1));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(flist.get(i).getShopName() == null ? "" : flist
							.get(i).getShopName());
					
					cell = row.createCell((short)(2));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(flist.get(i).getGoodsSn() == null ? "" : flist.get(i)
							.getGoodsSn());
					
					
					cell = row.createCell((short)(3));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(flist.get(i).getChannelGoodsSn() == null ? "" : flist.get(
							i).getChannelGoodsSn());
					
					cell = row.createCell((short)(4));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(flist.get(i).getGoodsName() == null ? "" : flist.get(
							i).getGoodsName());
					
					cell = row.createCell((short)(5));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(flist.get(i).getPrice() == null ? "" : flist.get(i)
							.getPrice());
					
					cell = row.createCell((short)(6));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(chanageStatus( flist.get(i).getStatus()));
		
					cell = row.createCell((short)(7));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(flist.get(i).getStockCount() == null ? "" : flist.get(i)
							.getStockCount());
					iRow++;
				}
			}
		}
		ExprotExcelResult eer = new ExprotExcelResult();
		//传入当前excel行数
		eer.setiRow(iRow);
		return eer;
	}
	
	
	/**
	 *  导出11位码经营商品
	 **/
	@RequestMapping(value = "exportElevenShopBusinessGoods.spmvc")
	public void exportElevenShopBusinessGoods(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String userName = getUserName(request);
		int pageSize = 20;
		JsonResult jsonResult = new JsonResult();

		String shopCode = StringUtil.trim(request.getParameter("shopCode"));

		String channelCode = StringUtil.trim(request.getParameter("channelCode"));
		
		if(StringUtil.isTaoBaoChannel(channelCode)){
			if(!Constants.TB_FX.equals(shopCode)){
				pageSize = 200;
			}
		}
		if(Constants.JD_CHANNEL_CODE.equals(channelCode)){
			pageSize = 100;
		}
		if(Constants.YHD_CHANNEL_CODE.equals(channelCode)){
			pageSize = 50;
		}
		if(Constants.WX_CHANNEL_CODE.equals(channelCode)){
			pageSize = 100;
		}
		String stauts = StringUtil.trim(request.getParameter("status"));
		if(StringUtil.isBlank(shopCode) || StringUtil.isBlank(shopCode)){
			jsonResult.setMessage("数据丢失，请刷新！");
			jsonResult.setIsok(false);
			outPrintJson(response, jsonResult);
			return;
		}
		
		String path = getPath();
		String fileName = "CUSTOMCODE_" + shopCode + "_" + channelCode + ".csv";
		File CreateFile = new File(path + "/page/shopBusinessGoods/exportFile/");
		
		//文件路径;
		if (!CreateFile.exists()) {
			CreateFile.mkdirs();
		}
		//输出文件;
		File file = new File(path + "/page/shopBusinessGoods/exportFile/" + fileName);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
			bw.write("店铺编码,店铺名称,商品款号,渠道商品款号,店铺商品名称,上下架状态,店铺商品价格,库存量,商品6位码\r\n");
			bw.flush();
			writeStringForElevenCode(bw, pageSize, shopCode, channelCode, stauts, userName);
			if(Constants.PP_CHANNEL_CODE.equals(channelCode) && "2".equals(stauts)){
				//待上架商品
//				upEer = writeStringForSixCode(upEer.getSb(), pageSize, shopCode,channelCode,"5",userName);
				//已售完商品
				writeStringForElevenCode(bw, pageSize, shopCode,channelCode,"8",userName);
			}
			bw.flush();
			jsonResult.setIsok(true);
			jsonResult.setMessage(fileName);
		} catch (Exception e) {
			logger.error("导出11位码商品列表异常", e);
			jsonResult.setIsok(false);
			jsonResult.setMessage("导出失败！");
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
		writeObject(jsonResult, response);
	}

	
	/**
	 * 转换状态  
	 * @param value
	 * @return
	 */
	private String chanageStatus(String value){
		String status="";
		if(value==null){
			return status;
		}
		if("1".equals(value)){
			status="上架";
		} else if("2".equals(value)){
			status="下架";
//		} else if("5".equals(value)){
//			status="等待上架";
		} else if("8".equals(value)){
			status="已售完";
		} else{
			status="";
		}
		
		return 	status;
	}

	/***
	 * 添加6位码数据;
	 * @param sb
	 * @param pageSize
	 * @param shopCode
	 * @param channelCode
	 * @param wareStatus
	 * @return
	 * @throws IOException 
	 */
	private ExprotExcelResult writeStringForSixCode(BufferedWriter bw,int pageSize, String shopCode,
			String channelCode, String wareStatus, String userName) throws IOException {
	
		ApiResultVO<List<ChannelApiGoods>> rapiResultVO = null;
		List<ChannelApiGoods> flist = null;
		LocalItemQuery itemQuery = new LocalItemQuery();
		itemQuery.setChannelCode(channelCode);
		itemQuery.setShopCode(shopCode);
		itemQuery.setItemNo(null);
		itemQuery.setPage(1);
		itemQuery.setPageSize(pageSize);
		itemQuery.setStatus(wareStatus);
		itemQuery.setUserName(userName);
		try {
			rapiResultVO = apiService.searchItemPage(itemQuery);
			flist = rapiResultVO.getApiGoods();
		} catch(Exception e) {
			rapiResultVO = new ApiResultVO();
			rapiResultVO.setCode("1");
			logger.info("apiService.searchItemPage(" + "start : 1 " 
					+ ", pageSize : " + pageSize + ", page : "
					+ ", message : " +  e + ")");
		}
		logger.info("shopbusinessGoodsController.writeStringForSixCode(" + "start : 1 " 
				+ ", pageSize : " + pageSize + ", page : "
				+ ", message : " + rapiResultVO.getMessage() + ")");
		//有异常返回
		if(!"0".equals(rapiResultVO.getCode())){
			ExprotExcelResult eer = new ExprotExcelResult();
			eer.setMsg(rapiResultVO.getMessage());
		}
		StringBuffer sb = new StringBuffer();
		int iTotal = rapiResultVO.getTotal();
		//int pageNum =0;
		int pageNum = iTotal / pageSize;
		if (iTotal % pageSize > 0) {
			++pageNum;
		}
//		pageNum = 1;
		if (pageNum > 1) {
			for (int j = 1; j <= pageNum; j++) {
				ApiResultVO<List<ChannelApiGoods>> apiResultVO = null;
				List<ChannelApiGoods> list = null;
				try {
					itemQuery.setPage(j);
					apiResultVO = apiService.searchItemPage(itemQuery);
					list = apiResultVO.getApiGoods();
				} catch (Exception e){
					apiResultVO = new ApiResultVO();
					apiResultVO.setCode("1");
					logger.error("apiService.searchItemPage(" + "start : "+pageNum 
							+ ", pageSize : " + pageSize + ", page : "
							+ ", message)", e);
				}

				if(!Constants.API_RETURN_OK.equals(apiResultVO.getCode())){
						ExprotExcelResult eer = new ExprotExcelResult();
						eer.setMsg(rapiResultVO.getMessage());
						continue;
				}
				
				if(null != list && list.size() > 0){
					for (int i = 0; i < list.size(); i++) {
						sb.append(list.get(i).getShopCode() == null ? "" : list
								.get(i).getShopCode()
								+ ",");
						sb.append(list.get(i).getShopName() == null ? "" : list
								.get(i).getShopName()
								+ ",");
						sb.append(list.get(i).getGoodsSn() == null ? " " : list.get(
								i).getGoodsSn()).append(",");
						sb.append(list.get(i).getChannelGoodsSn() == null ? " "
								: list.get(i).getChannelGoodsSn()).append(",");
						sb.append(list.get(i).getGoodsName() == null ? "" : list
								.get(i).getGoodsName() + ",");
						sb.append(chanageStatus(list.get(i).getStatus())
								+ ",");
						sb.append(list.get(i).getPrice() == null ? "" : list.get(i)
								.getPrice()).append(",");
						sb.append(list.get(i).getStockCount() == null ? "" : list.get(i)
								.getStockCount());
						sb.append("\r\n");
						logger.info(list.get(i).getGoodsSn() + ";" + list.get(i).getGoodsName());
						bw.write(sb.toString());
						bw.flush();
						sb = new StringBuffer();
					}
				}
			}
		} else {
			if(null != flist && flist.size()>0){
				for (int i = 0; i < flist.size(); i++) {
					sb.append(flist.get(i).getShopCode() == null ? "" : flist
							.get(i).getShopCode()
							+ ",");
					sb.append(flist.get(i).getShopName() == null ? "" : flist
							.get(i).getShopName()
							+ ",");
					sb.append(flist.get(i).getGoodsSn() == null ? " " : flist.get(i)
							.getGoodsSn()).append(",");
					sb.append(flist.get(i).getChannelGoodsSn() == null ? " " : flist
							.get(i).getChannelGoodsSn()).append(",");
					sb.append(flist.get(i).getGoodsName() == null ? "" : flist.get(
							i).getGoodsName()
							+ ",");
					sb.append(chanageStatus(flist.get(i).getStatus())
							+ ",");
					sb.append(flist.get(i).getPrice() == null ? "" : flist.get(i)
							.getPrice()).append(",");
					sb.append(flist.get(i).getStockCount() == null ? "" : flist.get(i)
							.getStockCount());
					sb.append("\r\n");
					bw.write(sb.toString());
					bw.flush();
					sb = new StringBuffer();
				}
			}
		}
		ExprotExcelResult eer = new ExprotExcelResult();
		eer.setSb(null);
		return eer;
	
	}
	
	/***
	 * 添加11位码数据;
	 * @param sb
	 * @param pageSize
	 * @param shopCode
	 * @param channelCode
	 * @param wareStatus
	 * @return
	 */
	private ExprotExcelResult writeStringForElevenCode(BufferedWriter bw,int pageSize, String shopCode,
				String channelCode, String wareStatus, String userName) throws IOException{
		ApiResultVO rapiResultVO = null;
		List<ChannelApiGoods> flist = null;
		try{
			ItemQuery itemQuery = new ItemQuery();
//			rapiResultVO = apiService.searchChildItemPage(itemQuery);
//			flist = rapiResultVO.getApiGoods();
		} catch(Exception e){
			rapiResultVO = new ApiResultVO();
			rapiResultVO.setCode("1");
			logger.error("apiService.searchItemPage(" + "start : 1 "
					+ ", pageSize : " + pageSize + ", page : "
					+ ", message)", e);
		}
		//有异常返回
		if(!Constants.API_RETURN_OK.equals(rapiResultVO.getCode())){
			ExprotExcelResult eer = new ExprotExcelResult();
			eer.setMsg(rapiResultVO.getMessage());
			logger.error(rapiResultVO.getMessage());
			return eer;
		}
		
		int iTotal = rapiResultVO.getTotal();
		
		//int pageNum =0;
		int pageNum = iTotal / pageSize;
		if (iTotal % pageSize > 0) {
			++pageNum;
		}
		StringBuffer sb = new StringBuffer();
		//大于1页;
		if (pageNum > 1) {
			for (int j = 1; j <= pageNum; j++) {
				ApiResultVO apiResultVO = null;
				List<ChannelApiGoods> list = null;
				try {
//					apiResultVO = apiService.searchChildItemPage(
//							channelCode, shopCode, "", "", j, pageSize,wareStatus,userName);
//					list = apiResultVO.getApiGoods();
				} catch(Exception e) {
					apiResultVO = new ApiResultVO();
					apiResultVO.setCode("1");
					logger.error("apiService.searchItemPage(" + "start : "+ pageNum
							+ ", pageSize : " + pageSize + ", page : "
							+ ", message)", e);
				}
//				if(!Constants.API_RETURN_OK.equals(apiResultVO.getCode())){
//						ExprotExcelResult eer = new ExprotExcelResult();
//						eer.setMsg(rapiResultVO.getMessage());
//						return eer;
//				}
				if(StringUtil.isListNotNull(list)) {
					for (int i = 0; i < list.size(); i++) {
						ChannelApiGoods father = list.get(i);
						List<ChannelApiGoods> zzlist = list.get(i).getApiGoodsChild();
						if(StringUtil.isListNotNull(zzlist)) {
							for(int z=0;z<zzlist.size(); z++){
								sb.append(father.getShopCode() == null ? "" : father.getShopCode()
										+ ",");
								sb.append(father.getShopName() == null ? "" :father.getShopName()
										+ ",");
								sb.append(zzlist.get(z).getGoodsSn() == null ? " " : zzlist.get(z).getGoodsSn()).append(",");
								sb.append(zzlist.get(z).getChannelGoodsSn() == null ? " "
										: zzlist.get(z).getChannelGoodsSn()).append(",");
								
								String goodsName = zzlist.get(z).getGoodsName() == null ? "" : zzlist.get(z).getGoodsName();
								if(!StringUtil.isNotNull(goodsName)){
									goodsName = father.getGoodsName()==null ? "" : father.getGoodsName();
								}
								sb.append(goodsName + ",");
								
								String status = zzlist.get(z).getStatus() == null ? "" : zzlist.get(z)
										.getStatus();
								if(!StringUtil.isNotNull(status)){
									status = father.getStatus()==null ? "":father.getStatus();
								}
								sb.append(chanageStatus(status)
										+ ",");	
								sb.append(zzlist.get(z).getPrice() == null ? "" : zzlist.get(z)
										.getPrice()).append(",");
								sb.append(zzlist.get(z).getStockCount() == null ? "" : zzlist.get(z)
										.getStockCount()).append(",");;
								
								sb.append(zzlist.get(z).getLocalGoodsSn() == null ? "" : zzlist.get(z)
										.getLocalGoodsSn());
							
								sb.append("\r\n");
								bw.write(sb.toString());
								bw.flush();
								sb = new StringBuffer();
							}
						}
					}
				}
			}
		//1页
		} else {
			if(StringUtil.isListNotNull(flist)){
				for (int i = 0; i < flist.size(); i++) {
					ChannelApiGoods father = flist.get(i);
					List<ChannelApiGoods> zlist = flist.get(i).getApiGoodsChild();
					if(StringUtil.isListNotNull(zlist)){
						for(int z=0;z<zlist.size(); z++){
							sb.append(father.getShopCode() == null ? "" : father.getShopCode()
									+ ",");
							sb.append(father.getShopName() == null ? "" : father.getShopName()
									+ ",");
							sb.append(zlist.get(z).getGoodsSn() == null ? " " :zlist.get(z)
									.getGoodsSn()).append(",");
							sb.append(zlist.get(z).getChannelGoodsSn() == null ? " " : zlist.get(z).getChannelGoodsSn()).append(",");
							String goodsName = zlist.get(z).getGoodsName() == null ? "" : zlist.get(z).getGoodsName();
							if(!StringUtil.isNotNull(goodsName)){
								goodsName = father.getGoodsName()==null ? "" : father.getGoodsName();
							}
							sb.append(goodsName + ",");
							String status = zlist.get(z).getStatus() == null ? "" : zlist.get(z).getStatus();
							if(!StringUtil.isNotNull(status)){
								status = father.getStatus()==null ? "":father.getStatus();
							}
							sb.append(chanageStatus(status) + ",");
							sb.append(zlist.get(z).getPrice() == null ? "" : zlist.get(z).getPrice()).append(",");
							sb.append(zlist.get(z).getStockCount() == null ? "" : zlist.get(z)
									.getStockCount());
							sb.append("\r\n");
							bw.write(sb.toString());
							bw.flush();
							sb = new StringBuffer();
						}
					}
				}
			}
		}
		ExprotExcelResult eer = new ExprotExcelResult();
		eer.setSb(null);
		return eer;
	}
	
	/***
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
			if (StringUtil.isEmpty(path)) {
				return;
			}
			String serverPath = getPath() + EXPORT_FILE_URL;
			File filepath = new File(serverPath);
			path = filepath.getPath() + "/" + path;

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
			  logger.info("shopbusinessGoodsController.downloadFile( message : " + e + ")");
		}
	}
	
	
	/***
	 * 添加6位码数据;
	 * @param bw
	 * @param shopCode
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private ExprotExcelResult writeStringForLocalSixCode(BufferedWriter bw,String shopCode,String fileName, HttpServletResponse response) throws Exception {
		
		Paging paging = null;
		List<ChannelGoodsVo> flist = null;
		String shopTitle = "";
		try {
			ChannelGoods model = new ChannelGoods();
			model.setChannelCode(shopCode);
			PageHelper helper = new PageHelper();
			helper.setLimit(50);
			helper.setStart(0);
			paging = this.channelGoodsService.getChannelGoodsList(model, helper);
			flist = (List<ChannelGoodsVo>) paging.getRoot();
		} catch(Exception e) {
			paging = new Paging();
			logger.info("channelGoodsService.getChannelGoodsList(" + "start : 1 " 
					+ ", pageSize : 50" + ", page : "
					+ ", message : " +  e + ")");
		}
		logger.info("channelGoodsInfoController.writeStringForSixCode(" + "start : 1 " 
				+ ", pageSize : 15" + ", page : "
				+ ", message : " + paging.getMessage() + ")");
		//有异常返回
		if(CollectionUtils.isEmpty(paging.getRoot())){
			ExprotExcelResult eer = new ExprotExcelResult();
			eer.setMsg("channelCode :" + shopCode + ",start = 1, limit =50, searchChannelGoods results is null");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("店铺编码,店铺名称,商品编号,商品名称,价格,颜色尺码,库存同步状态,创建时间,更新时间\r\n");
		ChannelShopExample channelShopExample = new ChannelShopExample();
		channelShopExample.or().andShopCodeEqualTo(shopCode);
		List<ChannelShop> shops = channelShopMapper.selectByExample(channelShopExample);
		if(CollectionUtils.isNotEmpty(shops)){
			shopTitle = shops.get(0).getShopTitle();
		}
		int iTotal = paging.getTotalProperty();
		int pageNum = iTotal / 50;
		if (iTotal % 50 > 0) {
			++pageNum;
		}
		if (pageNum > 1) {
			for (int j = 1; j <= pageNum; j++) {
				Paging ipaging = null;
				List<ChannelGoodsVo> list = null;
				try {
					ChannelGoods model = new ChannelGoods();
					model.setChannelCode(shopCode);
					PageHelper helper = new PageHelper();
					helper.setLimit(50);
					helper.setStart((j-1)*50);
					ipaging = this.channelGoodsService.getChannelGoodsList(model, helper);
					list = (List<ChannelGoodsVo>) ipaging.getRoot();
				} catch (Exception e){
					paging = new Paging();
					logger.info("channelGoodsService.getChannelGoodsList(" + "start : 1 " 
							+ ", pageSize : 50" + ", page : " + j
							+ ", message : " +  e + ")");
				}

				if(CollectionUtils.isEmpty(ipaging.getRoot())){
						ExprotExcelResult eer = new ExprotExcelResult();
						eer.setMsg("channelCode :" + shopCode + ",start = " + j + ", limit =50, searchChannelGoods results is null");
						continue;
				}
				
				if(null != list && list.size() > 0){
					for (int i = 0; i < list.size(); i++) {
						sb.append(shopCode == null ? "" : shopCode).append(",");
						sb.append(shopTitle).append(",");
						sb.append(list.get(i).getGoodsSn() == null ? "" : list
								.get(i).getGoodsSn()).append(",");
						sb.append(list.get(i).getGoodsName() == null ? "" : list
								.get(i).getGoodsName()).append(",");
						sb.append(list.get(i).getChannelPrice() == null ? " "
								: list.get(i).getChannelPrice()).append(",");
						List<BGproductBarcodeList> barcodeList = list.get(i).getBarcodeList();
						if(CollectionUtils.isEmpty(barcodeList)){
							sb.append("").append(",");
						}else{
							sb.append("\"");
							for(BGproductBarcodeList barcode :barcodeList ){
								sb.append(barcode.getColorName()+":"+barcode.getSizeName()+"\r\n");
							}
							sb.append("\"").append(",");
						}
						if(null != list.get(i).getIsSyncStock()){
							if(list.get(i).getIsSyncStock().intValue()==0){
								sb.append("不同步").append(",");
							}else if(list.get(i).getIsSyncStock().intValue()==1){
								sb.append("同步").append(",");
							}
						}else{
							sb.append("").append(",");
						}
						if(list.get(i).getAddTime() == null || list.get(i).getAddTime() == 0){
							sb.append("").append(",");
						}else{
							sb.append(flist.get(i).getFormatAddTime() + ",");
						}
						if(list.get(i).getLastUpdateTime() == null || list.get(i).getLastUpdateTime() == 0){
							sb.append("").append(",");
						}else{
							sb.append(flist.get(i).getFormatUpdateTime() + ",");
						}
						sb.append("\r\n");
						bw.write(sb.toString());
						bw.flush();
						sb = new StringBuffer();
					}
				}
			}
		} else {
			if(null != flist && flist.size()>0){
				for (int i = 0; i < flist.size(); i++) {
					sb.append(shopCode == null ? "" : shopCode).append(",");
					sb.append(shopTitle).append(",");
					sb.append(flist.get(i).getGoodsSn() == null ? "" : flist
							.get(i).getGoodsSn()).append(",");
					sb.append(flist.get(i).getGoodsName() == null ? "" : flist
							.get(i).getGoodsName()).append(",");
					sb.append(flist.get(i).getChannelPrice() == null ? " "
							: flist.get(i).getChannelPrice()).append(",");
					List<BGproductBarcodeList> barcodeList = flist.get(i).getBarcodeList();
					if(CollectionUtils.isEmpty(barcodeList)){
						sb.append("").append(",");
					}else{
						sb.append("\"");
						for(BGproductBarcodeList barcode :barcodeList ){
							sb.append(barcode.getColorName()+":"+barcode.getSizeName()+"\r\n");
						}
						sb.append("\"").append(",");
					}
					if(null != flist.get(i).getIsSyncStock()){
						if(flist.get(i).getIsSyncStock().intValue()==0){
							sb.append("不同步").append(",");
						}else if(flist.get(i).getIsSyncStock().intValue()==1){
							sb.append("同步").append(",");
						}
					}else{
						sb.append("").append(",");
					}
					if(flist.get(i).getAddTime() == null || flist.get(i).getAddTime() == 0){
						sb.append("").append(",");
					}else{
						sb.append(flist.get(i).getFormatAddTime() + ",");
					}
					if(flist.get(i).getLastUpdateTime() == null || flist.get(i).getLastUpdateTime() == 0){
						sb.append("").append(",");
					}else{
						sb.append(flist.get(i).getFormatUpdateTime() + ",");
					}
					sb.append("\r\n");
					bw.write(sb.toString());
					bw.flush();
					sb = new StringBuffer();
				}
			}
		}
		ExprotExcelResult eer = new ExprotExcelResult();
		eer.setSb(null);
		return eer;
	
	}
}