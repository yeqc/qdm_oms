package com.work.shop.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import au.com.bytecode.opencsv.CSVReader;

//import com.alibaba.fastjson.JSON;
import com.work.shop.bean.SystemArea;
import com.work.shop.bean.SystemAreaExample;
import com.work.shop.bean.SystemAreaExample.Criteria;
import com.work.shop.bean.SystemRegion;
import com.work.shop.bean.SystemRegionArea;
import com.work.shop.bean.SystemRegionMatch;
import com.work.shop.bean.SystemRegionMatchExample;
import com.work.shop.service.ChannelAreaService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelAreaMatchVo;
import com.work.shop.vo.ChannelAreaVo;
import com.work.shop.vo.JsonResult;

@Controller
@RequestMapping(value= "channelArea")
public class ChannelAreaController  extends BaseController{
	
	private static Logger log = Logger.getLogger(ChannelAreaController.class);

	@Resource(name = "channelAreaService")
	private ChannelAreaService channelAreaService;
	
	//树状菜单页面跳转
	@RequestMapping(value="toChannelAreaList.spmvc")
	public ModelAndView toChannelAreaList(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("channelArea/channelAreaList");
		return mav;
	}

	//树状菜单页面跳转
	@RequestMapping(value="toChannelAreaMatchList.spmvc")
	public ModelAndView toChannelAreaMatchList(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("channelArea/channelAreaMatchList");
		return mav;
	}
	
	//点击添加按钮页面跳转 
	@RequestMapping(value = "addChannelArea.spmvc")
	public ModelAndView toAddChannelAreaPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("channelArea/addChannelArea");
		return mav;
	}

	//获取渠道映射列表Vo
	@RequestMapping(value = "/getchannelAreaList.spmvc")
	public void getChannelAreaList(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("systemArea") ChannelAreaVo systemArea, PageHelper helper) throws IOException {
		try {
			Paging paging= channelAreaService.getSystemAreaList(systemArea, helper);
			writeJson(paging, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//导入信息用的是同一个方法
	@RequestMapping(value = "/insertChannelArea.spmvc")
	public void insertChannelShop(HttpServletResponse response,@ModelAttribute("systemArea") SystemArea systemArea) {
		try {
			JsonResult jsonResult = new JsonResult();
			List<SystemArea> list = new ArrayList<SystemArea>();
			list.add(systemArea);
			jsonResult = channelAreaService.insertSystemArea(list);
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//批量删除ids
	@RequestMapping(value = "/deleteChannelArea.spmvc")
	public void deleteChannelArea(HttpServletResponse response,@ModelAttribute("ids") String ids) {
		try {		
			JsonResult jsonResult = channelAreaService.deleteSystemArea(ids);
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
		
	//查看信息
	@RequestMapping(value="viewchannelArea.spmvc")
	public ModelAndView viewchannelArea(HttpServletResponse response,SystemArea area){
		ModelAndView mav = new ModelAndView();
		SystemAreaExample systemAreaExample = new SystemAreaExample();
		Criteria criteria = systemAreaExample.or();
		if(null != area && null != area.getId()){
			criteria.andIdEqualTo(area.getId());
			SystemArea systemArea = this.channelAreaService.getSystemArea(area);
			if(null != systemArea){
				mav.addObject("cArea",systemArea);
			}
		}
		mav.setViewName("channelArea/updateChannelArea");
		return mav;
	}
	
	//修改映射关系
	@RequestMapping(value="updateChannelArea.spmvc")
	public void updatehannelArea(HttpServletResponse response,@ModelAttribute("systemArea") SystemArea systemArea){
		JsonResult jsonResult = new JsonResult();
		jsonResult = channelAreaService.updateSystemArea(systemArea);
		outPrintJson(response, jsonResult);
	}
	
	//导入excel.xls
	@RequestMapping(value="inportChannelArea.spmvc")
	public void inportChannelArea(@RequestParam MultipartFile myfile,HttpServletResponse response, 
			HttpServletRequest request, String channelCode, int type){
		JsonResult jsonResult = new JsonResult();
		InputStream is = null;
		List<SystemArea> list = null;
		try {
			is= myfile.getInputStream();
			StringBuffer errorMsg = new StringBuffer();
			if (type == 0) {
				list = this.readCsvFile(is, errorMsg, channelCode);
			} else {
				list = this.readMappedCsvFile(is, errorMsg, channelCode);
			}
			if(list != null){
				jsonResult = channelAreaService.insertSystemArea(list);
				outPrintJson(response, jsonResult);
			}
		} catch (Exception e) {
			log.error("导入区域文件异常：" , e);
		} finally {
			if(is != null){
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					log.error("关闭文件流异常：" , e);
				}
			}
		}
	}
	
	/**
	 * CSV文件
	 * @param is
	 * @param sb
	 * @return
	 * @throws Exception
	 */
	private List<SystemArea> readCsvFile(InputStream is, StringBuffer sb, String channelCode) throws Exception {
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		List<SystemArea> list = new ArrayList<SystemArea>();
		String[] nextLine;
		SystemArea systemArea = null;
		try {
			int j = 0;
			while ((nextLine = reader.readNext()) != null) {
				if (j != 0) {
					systemArea = new SystemArea();
					// 渠道区域Id
					String areaId = nextLine[0];
					if (StringUtil.isEmpty(areaId)) {
						continue;
					}
					systemArea.setAreaId(areaId.trim().replace("'", ""));
					// 渠道区域名称
					String areaName = nextLine[1];
					if (StringUtil.isEmpty(areaName)) {
						continue;
					}
					systemArea.setAreaName(areaName.trim());
					// 渠道区域类型
					String areaType = nextLine[2];
					if (StringUtil.isEmpty(areaType)) {
						continue;
					}
					systemArea.setAreaType(areaType.trim());
					// 渠道区域父code
					String pareaCode = nextLine[3];
					if (StringUtil.isNotEmpty(pareaCode)) {
						systemArea.setPareaCode(pareaCode.replace("'", ""));
					}
					// 渠道区域父Name
					String pareaName = nextLine[4];
					if (StringUtil.isEmpty(pareaName)) {
						pareaName = null;
					}
					// 渠道ZIP
					String zip = nextLine[5];
					if (StringUtil.isNotEmpty(zip)) {
						systemArea.setZip(zip);
					}
					SystemRegionArea region = new SystemRegionArea();
					region.setRegionName(areaName);
					int type = Integer.valueOf(areaType);
					region.setRegionType(type);
					region.setParentName(pareaName);
					
					//精确匹配：地区名，地区等级，地区父名称  ；if areaType is 0 [Country],searchType = "0", search case is equal;
					String searchType = "0";
					List<SystemRegionArea> regions = this.channelAreaService.getOS_SystemRegionArea(region,searchType);
					if (StringUtil.isNotNullForList(regions)) {
						fullSystemArea(systemArea,regions);
					}else{
						// 3个条件的 模糊匹配：地区名，地区等级，地区父名称  ；areaType is not 0 , search case is like
						searchType = "1";
						regions = this.channelAreaService.getOS_SystemRegionArea(region,searchType);
						if (StringUtil.isNotNullForList(regions)) {
							fullSystemArea(systemArea,regions);
						} else if(type>1){
							// 2个条件的 模糊匹配：地区名，地区等级  ；areaType is not 0 , search case is like
							region.setRegionType(type-1);
							regions = this.channelAreaService.getOS_SystemRegionArea(region,searchType);
							if (StringUtil.isNotNullForList(regions)) {
								fullSystemArea(systemArea,regions);
							}else{
								region.setRegionType(type+1);
								regions = this.channelAreaService.getOS_SystemRegionArea(region,searchType);
								//systemArea.setMatchType(2);
								if (StringUtil.isNotNullForList(regions)) {
									fullSystemArea(systemArea,regions);
								}else{
									systemArea.setMatchType(2);
								}
							}
						}
					}
					
					systemArea.setChannelCode(channelCode);
					list.add(systemArea);
				}
				j++;
				
				/*List<SystemRegionArea> regions = this.channelAreaService.getSystemRegion(region);
				if (StringUtil.isNotNullForList(regions)) {
					//判断当前级别地区的父地区是否存在
					if (getParentRegion(pareaName,type)) {
						SystemRegionArea obj = regions.get(0);
						if (null !=obj.getRegionId()) {
							systemArea.setOsRegionId(obj.getRegionId());
						}
						systemArea.setOsRegionName(obj.getRegionName());
						
						systemArea.setRelateOsArea(appendString(regions));
						systemArea.setPareaCode(obj.getParentId());
						systemArea.setMatchType(regions.size()==1?0:1);
					}
				} else {
					if (type - 1 >= 0) {
						region.setRegionType(type-1); // 没找到往上一级找
						List<SystemRegionArea> regionsList = this.channelAreaService.getSystemRegion(region);
						if (StringUtil.isNotNullForList(regionsList)) {
							//判断当前级别地区的父地区是否存在
							if (getParentRegion(pareaName,type-1)) {
								SystemRegionArea obj = regionsList.get(0);
								if (null !=obj.getRegionId()) {
									systemArea.setOsRegionId(obj.getRegionId());
								}
								systemArea.setOsRegionName(obj.getRegionName());
								
								systemArea.setRelateOsArea(appendString(regionsList));
								systemArea.setPareaCode(obj.getParentId());
								systemArea.setMatchType(regions.size()==1?0:1);
							}
						}else{
							if (type + 1 <= 4) {
								//判断当前级别地区的父地区是否存在
								if (getParentRegion(pareaName,type+1)) {
									region.setRegionType(type+1); // 没找到往下一级找
									regionsList = this.channelAreaService.getSystemRegion(region);
									if (StringUtil.isNotNullForList(regionsList)) {
										SystemRegionArea obj = regionsList.get(0);
										if (null !=obj.getRegionId()) {
											systemArea.setOsRegionId(obj.getRegionId());
										}
										systemArea.setOsRegionName(obj.getRegionName());
										
										systemArea.setRelateOsArea(appendString(regionsList));
										systemArea.setPareaCode(obj.getParentId());
										systemArea.setMatchType(regions.size()==1?0:1);
									}
								}
							}
						}
					}
				}*/
			}
		} catch (IOException e) {
			sb.append("解析CSV文件失败："+e);
			log.error("解析CSV文件失败：", e);
		} finally {
			if (null != reader) {
				reader.close();
			}
		}
		return list;
	}
	
	private SystemArea fullSystemArea(SystemArea systemArea,List<SystemRegionArea> regions){

		SystemRegionArea obj = regions.get(0);
		if (regions.size() == 1) {
			systemArea.setMatchType(0);
			systemArea.setOsParentId(obj.getParentId());
		}else{
			systemArea.setMatchType(1);
		}
		if (null !=obj.getRegionId()) {
			systemArea.setOsRegionId(obj.getRegionId());
		}
		systemArea.setOsRegionName(obj.getRegionName());
		return systemArea;
	}

	/**
	 * CSV文件
	 * @param is
	 * @param sb
	 * @return
	 * @throws Exception
	 */
	private List<SystemArea> readMappedCsvFile(InputStream is, StringBuffer sb, String channelCode) throws Exception {
		CSVReader reader = new CSVReader(new InputStreamReader(is, "GBK"));
		List<SystemArea> list = new ArrayList<SystemArea>();
		String[] nextLine;
		SystemArea systemArea = null;
		int j = 0;
		try {
			while ((nextLine = reader.readNext()) != null) {
				if (j != 0) {
					systemArea = new SystemArea();
					// 渠道区域Id
					String areaId = nextLine[0];
					if (StringUtil.isEmpty(areaId)) {
						continue;
					}
					systemArea.setAreaId(areaId.trim());
					// 渠道区域名称
					String areaName = nextLine[1];
					if (StringUtil.isEmpty(areaName)) {
						continue;
					}
					systemArea.setAreaName(areaName.trim());
					// 渠道区域类型
					String areaType = nextLine[2];
					if (StringUtil.isEmpty(areaType)) {
						continue;
					}
					systemArea.setAreaType(areaType.trim());
					// 渠道区域父code
					String pareaCode = nextLine[3];
					if (StringUtil.isNotEmpty(pareaCode)) {
						systemArea.setPareaCode(pareaCode);
					}
					// 渠道ZIP
					String zip = nextLine[5];
					if (StringUtil.isNotEmpty(zip)) {
						systemArea.setZip(zip);
					}
					// OS Id
					String osRegionId = nextLine[6];
					if (StringUtil.isNotEmpty(osRegionId)) {
						systemArea.setOsRegionId(osRegionId);
					}
					// OS Name
					String osRegionName = nextLine[7];
					if (StringUtil.isNotEmpty(osRegionName)) {
						systemArea.setOsRegionName(osRegionName);
					}
					systemArea.setChannelCode(channelCode);
					//匹配度 0 ： 完全匹配；1：部分匹配；2：完全不匹配
					systemArea.setMatchType(0);
					
					list.add(systemArea);
				}
				j++;
			}
		} catch (Exception e) {
			sb.append("解析CSV文件失败："+e);
			log.error(j+" line 解析CSV文件失败：", e);
		} finally {
			if (null != reader) {
				reader.close();
			}
		}
		return list;
	}
	
	//获取渠道映射列表Vo
	@RequestMapping(value = "/getchannelAreaMatchList.spmvc")
	public void getChannelAreaMatchList(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("systemRegionMatch") ChannelAreaMatchVo systemRegionMatch, PageHelper helper) throws IOException {
		try {
			Paging paging= channelAreaService.getSystemRegionMatchList(systemRegionMatch, helper);
			writeJson(paging, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//修改映射关系
	@RequestMapping(value="matchChannelArea.spmvc")
	public void matchChannelArea(HttpServletResponse response,@ModelAttribute("systemArea") SystemArea systemArea){
		JsonResult jsonResult = new JsonResult();
		jsonResult = channelAreaService.matchSystemRegion(systemArea);
		//get system region
		outPrintJson(response, jsonResult);
	}
	
	//查看信息
	@RequestMapping(value="viewchannelAreaMatch.spmvc")
	public ModelAndView viewchannelAreaMatch(HttpServletResponse response,SystemRegionMatch regionMatch){
		ModelAndView mav = new ModelAndView();
		SystemRegionMatchExample systemAreaExample = new SystemRegionMatchExample();
		SystemRegionMatchExample.Criteria criteria = systemAreaExample.or();
		if(null != regionMatch && null != regionMatch.getId()){
			criteria.andIdEqualTo(regionMatch.getId());
			SystemRegionMatch systemRegionMatch = this.channelAreaService.getSystemRegionMatch(regionMatch);
			if(null != systemRegionMatch){
				SystemRegion systemRegion = new SystemRegion();
				try {
					systemRegion.setRegionId(Short.parseShort(systemRegionMatch.getPareaCode()));
					systemRegion = channelAreaService.getSystemRegion(systemRegion);
					systemRegionMatch.setParentName(systemRegion.getRegionName());
				} catch (Exception e) {
				}

				systemRegionMatch.setMatchType(changeMatchTypeValue(systemRegionMatch.getMatchType()));
				systemRegionMatch.setOldRegionType(changeRegionTypeValue(systemRegionMatch.getOldRegionType()));
				
				mav.addObject("sRegionMatch",systemRegionMatch);
			}
		}
		mav.setViewName("channelArea/updateChannelAreaMatch");
		return mav;
	}
	
	//获取渠道映射列表Vo
	@RequestMapping(value = "/getSystemRegionAreaList.spmvc")
	public void getSystemRegionAreaList(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("systemRegionArea") SystemRegionArea systemRegionArea, PageHelper helper) throws IOException {
//		
//		if (StringUtil.isEmpty(systemRegionArea.getRegionName())) {
//			Paging paging = new Paging();
//			paging.setRoot(new ArrayList<String>());
//			paging.setTotalProperty(1);
//			writeJson(paging, response);
//		}
//		
		try {
			Paging paging= channelAreaService.getSystemRegionAreaList(systemRegionArea, helper);
			writeJson(paging, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//修改映射关系
	@RequestMapping(value="updateChannelAreaMatch.spmvc")
	public void updatehannelAreaMatch(HttpServletResponse response,@ModelAttribute("systemRegionMatch") SystemRegionMatch systemRegionMatch){
		JsonResult jsonResult = new JsonResult();
		jsonResult = channelAreaService.updateSystemRegionMatch(systemRegionMatch);
		outPrintJson(response, jsonResult);
	}

	private String changeMatchTypeValue(String matchTypeValue){
		String matchType = "";
		if ("0".equals(matchTypeValue)) {
			matchType = "匹配";
		}else if("1".equals(matchTypeValue)){
			matchType = "部分匹配";
		}else if("2".equals(matchTypeValue)){
			matchType = "不匹配";
		}
		return matchType;
	}
	
	private String changeRegionTypeValue(String regionTypeValue){
		String regionType = "";
		if ("0".equals(regionTypeValue)) {
			regionType = "国家";
		}else if("1".equals(regionTypeValue)){
			regionType = "省";
		}else if("2".equals(regionTypeValue)){
			regionType = "市";
		}else if("3".equals(regionTypeValue)){
			regionType = "区";
		}
		return regionType;
	}
	
	
	//获取渠道映射列表Vo
	@RequestMapping(value = "/searchSystemAreaMatch.spmvc")
	public void searchSystemAreaMatch(HttpServletRequest request,HttpServletResponse response,
			String province,String city,String distinct,String channelCode) throws IOException {
		try {
			//log.info("****************** searchSystemAreaMatch start ****************");
//			province = "安徽";
//			city = "巢湖市";
//			distinct = "居巢区";
//			channelCode = "YHD_CHANNEL_CODE";
			Map<String,String> resultMap = channelAreaService.getSystemAreaMatch(province,city,distinct,channelCode);
			//System.out.println(channelCode+","+province+","+city+","+distinct+","+resultMap.get("provinceName")+","+resultMap.get("cityName")+","+resultMap.get("distinctName"));
			writeObject(resultMap,response);
			
			//log.info("****************** searchSystemAreaMatch end ****************");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}