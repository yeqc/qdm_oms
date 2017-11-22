package com.work.shop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelTemplate;
import com.work.shop.bean.ChannelTemplateContent;
import com.work.shop.bean.ChannelTemplateModule;
import com.work.shop.bean.ChannelTemplateWithBLOBs;
import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.TicketInfoExample;
import com.work.shop.service.ChannelTemplateService;
import com.work.shop.service.TicketInfoService;
import com.work.shop.util.Constants;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Pagination;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelTemplateVo;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.QueueManagerVo;

@Controller
@RequestMapping(value = "channelTemplate")
public class ChannelTemplateController extends BaseController {
	// 模板service
	@Resource(name = "channelTemplateService")
	private ChannelTemplateService channelTemplateService;

	// 平台接口service
//	@Resource(name = "apiService")
//	private ChannelApiService apiService;

	@Resource(name = "ticketInfoService")
	private TicketInfoService ticketInfoService;

	private static final Logger logger = Logger.getLogger(ChannelTemplateController.class);
	
	/**
	 * 模板列表查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "channelTemplateList.spmvc")
	public ModelAndView channelTemplateInfoPage(HttpServletRequest request,
			HttpServletResponse response, ChannelTemplate channelTemplate,
			PageHelper helper) throws Exception {
		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		if (!StringUtil.isTrimEmpty(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("template/channelTemplateList");
			return mav;
		}
		Paging paging = this.channelTemplateService.getChannelTemplatePage(
				channelTemplate, helper);
		writeJson(paging, response);
		return null;
	}

	/**
	 * 模板模块列表查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "channelTemplateModuleList.spmvc")
	public ModelAndView channelTemplateModulePage(HttpServletRequest request,
			HttpServletResponse response, ChannelTemplateModule model,
			PageHelper helper) throws Exception {
		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		if (!StringUtil.isTrimEmpty(method) && method.equals("init")) {
			ModelAndView mav = new ModelAndView();
			mav.setViewName("template/channelTemplateModuleList");
			return mav;
		}
		Paging paging = this.channelTemplateService
				.getChannelTemplateModulePage(model, helper);
		writeJson(paging, response);
		return null;
	}

	/**
	 * 新增模板
	 * 
	 * @param request
	 * @param response
	 * @param record
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "addTemplate.spmvc")
	public String addTemplate(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute ChannelTemplateVo record, ModelMap model,
			String contents) throws Exception {
		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		if (!StringUtil.isTrimEmpty(method) && method.equals("init")) {
			// 模块信息
			ChannelTemplateWithBLOBs template = null;
			if (null != record && null != record.getId()) {
				template = this.channelTemplateService.getTemplateById(record
						.getId());
			}
			// 模板编号
			String templateCode = "";
			if (null != template) {
				templateCode = template.getTemplateCode();
			} else {
				templateCode = "CT" + StringUtil.getSysCode();
			}
			// 自定义模块列表
			ChannelTemplateModule module = new ChannelTemplateModule();
			module.setShopCode(record.getShopCode());
			module.setChannelCode(record.getChannelCode());
			
			String returnPape="";
			//手机
		/*	if(null != record.getTemplateType() && 2 == record.getTemplateType()) {	
				returnPape = "template/addMobilePhoneTemplate";
				module.setModuleSize(1);
			} else {*/
				returnPape = "template/addTemplate";
				//module.setModuleSize(0);
			//}
		
			List<ChannelTemplateModule> modules = this.channelTemplateService
					.getChannelTemplateModuleList(module);
			model.addAttribute("modules", modules);
			model.addAttribute("template", template);
			model.addAttribute("templateCode", templateCode);
			return returnPape;
		}
		List<ChannelTemplateContent> contentList = null;
		if (StringUtil.isNotNull(contents)) {
			contentList = JSON.parseArray(contents,
					ChannelTemplateContent.class);
		}
		if (method.equals("add")) {
			try {
				this.channelTemplateService.insertTemplate(record, contentList);
				writeMsgSucc(true, "保存成功！", response);
			} catch (Exception e) {
				e.printStackTrace();
				writeMsgSucc(false, "保存失败！", response);
			}
		} else {
			try {
				this.channelTemplateService.updateTemplate(record, contentList);
				writeMsgSucc(true, "保存成功！", response);
			} catch (Exception e) {
				e.printStackTrace();
				writeMsgSucc(false, "保存失败！", response);
			}
		}
		return null;
	}

	/**
	 * 新增模板模块
	 * 
	 * @param request
	 * @param response
	 * @param record
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "addModule.spmvc")
	public ModelAndView addModule(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute ChannelTemplateModule record) throws Exception {
		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		if (!StringUtil.isTrimEmpty(method) && method.equals("init")) {
			ChannelTemplateModule module = null;
			if (null != record && record.getId() != null) {
				module = this.channelTemplateService.getModuleById(record
						.getId());
			}
			ModelAndView mav = new ModelAndView();
			mav.addObject("obj", module);
			mav.setViewName("template/addModule");
			return mav;
		}
		if (method.equals("add")) {
			try {
				this.channelTemplateService.insertModule(record);
				writeMsgSucc(true, "保存成功！", response);
			} catch (Exception e) {
				e.printStackTrace();
				writeMsgSucc(false, "保存失败！", response);
			}
		} else {
			try {
				this.channelTemplateService.updateModule(record);
				writeMsgSucc(true, "保存成功！", response);
			} catch (Exception e) {
				e.printStackTrace();
				writeMsgSucc(false, "保存失败！", response);
			}
		}
		return null;
	}

	/**
	 * 获取模板模块内容
	 * 
	 * @param request
	 * @param response
	 * @param record
	 * @return ModelAndView
	 * @throws Exception
	 */
	@RequestMapping(value = "moduleContent.spmvc")
	public ModelAndView moduleContent(HttpServletRequest request,
			HttpServletResponse response,
			@ModelAttribute ChannelTemplateModule record) throws Exception {
		ChannelTemplateModule module = null;
		if (null != record && record.getId() != null) {
			module = this.channelTemplateService.getModuleById(record.getId());
		}
		writeObject(module, response);
		return null;
	}

	// 删除模板

	@RequestMapping(value = "deleteTemplate.spmvc")
	public void deleteTemplate(HttpServletRequest request,
			HttpServletResponse response, String ids) throws Exception {
		if (StringUtil.isNotNull(ids)) {
			try {
				this.channelTemplateService.deleteTemplateById(ids);
				writeMsgSucc(true, "删除成功！", response);
			} catch (Exception e) {
				e.printStackTrace();
				writeMsgSucc(false, "删除失败！", response);
			}
		} else {
			writeMsgSucc(false, "没有选中行", response);
		}
	}

	//
	/**
	 * 删除模板模块
	 * 
	 * @param request
	 * @param response
	 * @param ids
	 * @exception Exception
	 */
	@RequestMapping(value = "deleteModule.spmvc")
	public void deleteModule(HttpServletRequest request,
			HttpServletResponse response, String ids) throws Exception {
		if (StringUtil.isNotNull(ids)) {
			try {
				this.channelTemplateService.deleteModuleById(ids);
				writeMsgSucc(true, "删除成功！", response);
			} catch (Exception e) {
				e.printStackTrace();
				writeMsgSucc(false, "删除失败！", response);
			}
		} else {
			writeMsgSucc(false, "没有选中行", response);
		}
	}

	/**
	 * 编辑模板内容
	 * 
	 * @param request
	 * @param response
	 * @param record
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "editTemplateContent.spmvc")
	public ModelAndView editTemplateContent(HttpServletRequest request,
			HttpServletResponse response, ChannelTemplateWithBLOBs record)
			throws Exception {
		String method = request.getParameter("method") == null ? "" : request
				.getParameter("method");
		if (!StringUtil.isTrimEmpty(method) && method.equals("init")) {
			ChannelTemplateWithBLOBs tempalte = null;
			if (null != record && record.getId() != null) {
				tempalte = this.channelTemplateService.getTemplateById(record
						.getId());
			}
			ModelAndView mav = new ModelAndView();
			mav.addObject("template", tempalte);
			mav.setViewName("template/editTemplateContent");
			return mav;
		}
		if (method.equals("update")) {
			try {
				this.channelTemplateService.updateTemplate(record, null);
				writeMsgSucc(true, "保存成功！", response);
			} catch (Exception e) {
				e.printStackTrace();
				writeMsgSucc(false, "保存失败！", response);
			}
		}
		return null;
	}

	/**
	 * 模板列表查询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "channelTemplateSelect.spmvc")
	public ModelAndView channelTemplateList(HttpServletRequest request,
			HttpServletResponse response, ChannelTemplate channelTemplate)
			throws Exception {
		List<ChannelTemplate> list = this.channelTemplateService
				.getChannelTemplateList(channelTemplate);
		outPrintJson(response, list);
		return null;
	}

	/**
	 * 生成宝贝详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "createGoodsDetail.spmvc")
	public ModelAndView createGoodsDetail(HttpServletRequest request,
			HttpServletResponse response, ChannelTemplate template)
			throws Exception {
		logger.debug("生成宝贝详情 channelCode:" + template.getChannelCode()
				+ "  shopCode" + template.getShopCode() + "  templateCode"
				+ template.getTemplateCode());
//		new GoodsDistributeThread(template).start();
		JsonResult jsonResult = new JsonResult();
		jsonResult.setIsok(true);
		jsonResult.setMessage("后台服务正在处理生成宝贝详情");
		outPrintJson(response, jsonResult);
		return null;
	}

	/**
	 * 根据商品编号号 更新宝贝详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "createGoodsDetailByGoodsSn.spmvc")
	public void createGoodsDetailByGoodsSn(HttpServletRequest request,
			HttpServletResponse response, ChannelTemplate template, String[] ids)
			throws Exception {
		logger.debug("生成宝贝详情 channelCode:" + template.getChannelCode()
				+ "  shopCode" + template.getShopCode() + "  templateCode"
				+ template.getTemplateCode());
		if (ids == null || ids.length == 0) {
			return;
		}
		List<String> idList = new ArrayList<String>();
		for (int i = 0; i < ids.length; i++) {
			idList.add(ids[i]);
		}

		ChannelGoods extension = new ChannelGoods();
		extension.setChannelCode(template.getShopCode());
		JsonResult jsonResult = new JsonResult();
		try {
			String queueName = Constants.QUEUE_FIX_ITEM + template.getShopCode();
			String ticketCode = "CD" + ids[0];
			// 调整单信息分页查询
			// 调整单中所有商品 追条添加至MQ中
			TicketInfoExample example = new TicketInfoExample();
			com.work.shop.bean.TicketInfoExample.Criteria criteria = example.or();
			criteria.andTicketCodeEqualTo(ids[0]);
			List<TicketInfo> ticketInfos = ticketInfoService.getTicketInfoList(example);
			for (TicketInfo info : ticketInfos) {
				QueueManagerVo managerVo = new QueueManagerVo();
				managerVo.setChannelCode(template.getChannelCode());
				managerVo.setShopCode(template.getShopCode());
				managerVo.setTemplateCode(template.getTemplateCode());
				managerVo.setOperateType(1);
				managerVo.setOperUser(getUserName(request));
				info.setTicketCode(ticketCode);
				managerVo.setTicketInfo(info);
				String msg = JSONObject.toJSONString(managerVo);
				logger.info("调整单" + ticketCode + "-" + info.getGoodsSn() + "生成宝贝详情任务加入MQ队列");
			}
			jsonResult.setIsok(true);
			jsonResult.setMessage("生成宝贝详情任务加入MQ队列");
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			logger.info("生成宝贝详情信息失败！" + e.getMessage());
			jsonResult.setIsok(false);
			jsonResult.setMessage("生成宝贝详情失败");
			outPrintJson(response, jsonResult);
		}
	}

	class GoodsDistributeThread extends Thread {
		private ChannelTemplate template;

		public GoodsDistributeThread(ChannelTemplate template) {
			this.template = template;
		}

		public void run() {
			PageHelper helper = new PageHelper();
			int start = 0;
			int limit = Constants.EVERY_PAGE_QUERY_SIZE;
			helper.setStart(start);
			helper.setLimit(limit);
			ChannelGoods goods = new ChannelGoods();
			goods.setChannelCode(template.getShopCode());
			try {
				Pagination pagination = channelTemplateService
						.getChannelGoodsList(goods, helper);
				if (pagination != null
						&& StringUtil.isNotNullForList(pagination.getData())) {
					logger.info("生成宝贝详情.查询渠道商品 第" + 1 + "页 数据查询");
					try {
//						channelTemplateService.createGoodsDetail(template,
//								(List<ChannelGoods>) pagination.getData());
					} catch (Exception e) {
						logger.info("生成宝贝详情信息失败！" + e.getMessage());
					}
					int pageCount = pagination.getTotalPages();
					for (int i = 2; i <= pageCount; i++) {
						start = (i - 1) * limit;
						helper.setStart(start);
						helper.setLimit(limit);
						logger.info("生成宝贝详情.查询渠道商品 第" + i + "页 数据查询");
						try {
							pagination = channelTemplateService
									.getChannelGoodsList(goods, helper);
						} catch (Exception e) {
							logger.info("查询渠道商品信息失败！" + e.getMessage());
						}
						if (pagination != null
								&& StringUtil.isNotNullForList(pagination
										.getData())) {
							try {
//								channelTemplateService.createGoodsDetail(
//										template,
//										(List<ChannelGoods>) pagination
//												.getData());
							} catch (Exception e) {
								logger.info("生成宝贝详情信息失败！" + e.getMessage());
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("查询渠道商品信息失败！");
			}
			// int pageSize = Constants.EVERY_PAGE_QUERY_SIZE;
			// int pageCount = 0;
			// //第一页数据查询
			// Pagination pagination = new Pagination(0, pageSize);
			// ApiResultVO resultVO = apiService.searchItemPage(channelCode,
			// shopCode, null, null, 1, pageSize, "1");
			// if (null != resultVO && "0".equals(resultVO.getCode())) {
			// if (StringUtil.isNotNullForList(resultVO.getApiGoods())) {
			// try {
			// // channelTemplateService.createGoodsDetail(channelCode,
			// shopCode, templateCode, resultVO.getApiGoods());
			// } catch (Exception e1) {
			// e1.printStackTrace();
			// logger.info("生成宝贝详情出错！");
			// }
			// pagination.setTotalSize(Integer.valueOf(resultVO.getTotal()));
			// pageCount = pagination.getTotalPages();
			// }
			// } else {
			// logger.error("查询在售商品出错："+ resultVO.getMessage());
			// resultVO = apiService.searchItemPage(channelCode, shopCode, null,
			// null, 1, pageSize, "1");
			// if (null != resultVO && "0".equals(resultVO.getCode())) {
			// if (StringUtil.isNotNullForList(resultVO.getApiGoods())) {
			// try {
			// // channelTemplateService.createGoodsDetail(channelCode,
			// shopCode, templateCode, resultVO.getApiGoods());
			// } catch (Exception e1) {
			// e1.printStackTrace();
			// logger.info("生成宝贝详情出错！");
			// }
			// pagination.setTotalSize(Integer.valueOf(resultVO.getTotal()));
			// pageCount = pagination.getTotalPages();
			// }
			// }
			// }
			// int i = 2;
			// 第二页以后数据查询
			// while(pageCount >=i){
			// logger.info("生成宝贝详情.查询在售商品 第"+i+"页 数据查询");
			// try {
			// resultVO = apiService.searchItemPage(channelCode, shopCode,
			// "210627", null, i, pageSize, "1");
			// if (null != resultVO && "0".equals(resultVO.getCode())) {
			// if (StringUtil.isNotNullForList(resultVO.getApiGoods())) {
			// try {
			// channelTemplateService.createGoodsDetail(channelCode, shopCode,
			// templateCode, resultVO.getApiGoods());
			// } catch (Exception e) {
			// e.printStackTrace();
			// logger.error("生成宝贝详情出错！");
			// }
			// }
			// } else {
			// logger.info("查询在售商品出错："+ resultVO.getMessage());
			// }
			// } catch (Exception e) {
			// logger.error("查询在售商品出错："+ e.getMessage());
			// }
			// i++;
			// }
		}

		public ChannelTemplate getTemplate() {
			return template;
		}

		public void setTemplate(ChannelTemplate template) {
			this.template = template;
		}
	}
}