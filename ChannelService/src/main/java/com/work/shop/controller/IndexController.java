package com.work.shop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.ChannelShopExample.Criteria;
import com.work.shop.bean.ConTree;
import com.work.shop.bean.InterfaceProperties;
import com.work.shop.bean.SystemResource;
import com.work.shop.service.ShopService;
import com.work.shop.service.SystemResourceService;
import com.work.shop.united.client.dataobject.DataPermission;
import com.work.shop.united.client.facade.AuthCenterFacade;
import com.work.shop.united.client.filter.config.Config;
import com.work.shop.util.Constants;
import com.work.shop.util.StringUtil;
import com.work.shop.vo.ChannelShopVo;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.SystemResourceVo;

@Controller
@RequestMapping(value = "/index/")
public class IndexController extends BaseController {

	private static Logger log = Logger.getLogger(IndexController.class);

	@Resource(name = "shopService")
	private ShopService shopService;

	@Resource(name = "systemResourceService")
	private SystemResourceService systemResourceService;

	@RequestMapping(value = "/getTree.spmvc", method = RequestMethod.POST)
	public ModelAndView getTree(HttpServletRequest request, HttpServletResponse response) {

		String pid = request.getParameter("id") == null ? "-1" : request.getParameter("id");
		log.info("pid = " + pid);

		String menuType = request.getParameter("menuType");
		log.info("menuType = " + menuType);

		String channelCode = request.getParameter("channelCode");
		log.info("channelCode = " + channelCode);

		String shopCode = request.getParameter("shopCode");
		log.info("shopCode = " + shopCode);

		AuthCenterFacade auth = Config.getAuthCenterFacade();
		List<DataPermission> authList = auth.allDataPermissionOfApp(getUserName(request), Config.getAppId());

		List<String> authCodes = new ArrayList<String>();
		for (DataPermission premission : authList) {
			authCodes.add(premission.getCode());
		}

		log.info("authCodes = " + authCodes);

		ModelAndView mav = new ModelAndView();
		if (StringUtil.isNotNull(pid) && !"-1".equals(pid)) {

			if (Constants.MENU_LOGS.equals(menuType)) {
				ConTree treeObj81 = new ConTree("81", "系统操作日志", "page/log/log-list.jsp", "file", "2", true);
				ConTree treeObj84 = new ConTree("84", "库存同步日志", "page/log/stock-log-list.jsp", "file", "2", true);
				List<ConTree> list = new ArrayList<ConTree>();
				list.add(treeObj81);
				list.add(treeObj84);
				mav.addObject("list", list);
				mav.setViewName("TreeNodeJson");
				return mav;
			}

			List<SystemResourceVo> menuList = systemResourceService.getMenuTree(menuType, getauthCodeByShopCode(authCodes, shopCode));
			if (menuList != null && menuList.size() > 0) {
				for (SystemResourceVo menu : menuList) {
					if (pid.equals(menu.getResourceCode())) {
						List<ConTree> list = new ArrayList<ConTree>();
						ConTree treeObj = null;
						for (SystemResource child : menu.getChildMenuList()) {
							treeObj = new ConTree(child.getResourceCode(), child.getResourceName(), child.getResourceUrl(), "file", "2", true);
							list.add(treeObj);
						}
						mav.addObject("list", list);
						mav.setViewName("TreeNodeJson");
						break;
					}
				}
			}
		} else {
			List<ConTree> list = new ArrayList<ConTree>();
			ConTree treeObj = null;
			if (Constants.MENU_LOGS.equals(menuType)) {
				treeObj = new ConTree(Constants.MENU_LOGS, "日志管理", null, "folder", "1", false);
				list.add(treeObj);
			} else {
				List<SystemResourceVo> menuList = systemResourceService.getMenuTree(menuType, getauthCodeByShopCode(authCodes, shopCode));
				for (SystemResourceVo vo : menuList) {
					treeObj = new ConTree(vo.getResourceCode(), vo.getResourceName(), null, "folder", "1", false);
					list.add(treeObj);
				}
			}
			mav.addObject("list", list);
			mav.setViewName("TreeNodeJson");
		}
		return mav;
	}

	private List<String> getauthCodeByShopCode(List<String> authCodes, String shopCode) {
		if (StringUtil.isEmpty(shopCode)) {
			return authCodes;
		}
		List<String> shopAuthCodes = new ArrayList<String>();
		String idx = shopCode + "_";
		for (String authCode : authCodes) {
			if (authCode.startsWith(idx)) {
				shopAuthCodes.add(authCode.replace(idx, ""));
			}
		}
		//特殊处理，套装列表；
		shopAuthCodes.add(Constants.GROUP_LIST_RESOURCE_KEY);
		return shopAuthCodes;
	}

	@RequestMapping(value = "/channelList.spmvc", method = RequestMethod.GET)
	public ModelAndView channelList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();

		mav.addObject("list", "aaa");
		mav.setViewName("list");
		return mav;
	}

	@RequestMapping(value = "/addChannelKey.spmvc")
	public void addChannelKey(HttpServletRequest request, HttpServletResponse response) {
		// ModelAndView mav = new ModelAndView();
		String proName = StringUtil.trim(request.getParameter("proName"));
		String proValue = StringUtil.trimLeft(request.getParameter("proValue"));
		JsonResult jsonResult = new JsonResult();

		if (StringUtil.isBlank(proName)) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("密钥key不能为空！");
			outPrintJson(response, jsonResult);
		}

		if (StringUtil.isBlank(proValue)) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("密钥值不能为空！");
			outPrintJson(response, jsonResult);
		}

		InterfaceProperties ip = new InterfaceProperties();

		ip.setProName(proName);
		ip.setProValue(proValue);

		try {
			int num = shopService.addChannelKey(ip);
			if (num > 0) {
				jsonResult.setIsok(true);
				jsonResult.setMessage("添加成功！");
			} else {
				jsonResult.setMessage("添加失败！");
				jsonResult.setIsok(false);
			}
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			outPrintJson(response, jsonResult);
		}
	}

	@RequestMapping(value = "/updateChannelKey.spmvc")
	public void updateChannelKey(HttpServletRequest request, HttpServletResponse response) {

		String id = StringUtil.trim(request.getParameter("id"));
		String proName = StringUtil.trim(request.getParameter("proName"));
		String proValue = StringUtil.trimLeft(request.getParameter("proValue"));

		JsonResult jsonResult = new JsonResult();

		if (StringUtil.isBlank(id)) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("id不能为空！");
			outPrintJson(response, jsonResult);
		}

		/*
		 * if(!RegExpValidator.IsNumber(id)){ jsonResult.setIsok(false);
		 * jsonResult.setMessage("id参数有错！"); outPrintJson(response, jsonResult);
		 * }
		 */
		if (StringUtil.isBlank(proName)) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("密钥key不能为空！");
			outPrintJson(response, jsonResult);
		}

		if (StringUtil.isBlank(proValue)) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("密钥值不能为空！");
			outPrintJson(response, jsonResult);
		}

		InterfaceProperties ip = new InterfaceProperties();
		ip.setId(Integer.valueOf(id));
		ip.setProName(proName);
		ip.setProValue(proValue);

		try {
			int num = shopService.updateChannelKey(ip);
			if (num > 0) {
				jsonResult.setIsok(true);
				jsonResult.setMessage("修改成功！");
			} else {
				jsonResult.setMessage("修改失败！");
				jsonResult.setIsok(false);
			}
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			outPrintJson(response, jsonResult);
		}

	}

	@RequestMapping(value = "/queryChannelKey.spmvc")
	public void queryChannelKey(HttpServletRequest request, HttpServletResponse response) {

		String id = StringUtil.trim(request.getParameter("id"));
		String proName = StringUtil.trim(request.getParameter("proName"));
		String proValue = StringUtil.trimLeft(request.getParameter("proValue"));

		JsonResult jsonResult = new JsonResult();

		if (StringUtil.isBlank(id)) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("id不能为空！");
			outPrintJson(response, jsonResult);
		}

		/*
		 * if(!RegExpValidator.IsNumber(id)){ jsonResult.setIsok(false);
		 * jsonResult.setMessage("id参数有错！"); outPrintJson(response, jsonResult);
		 * }
		 */
		if (StringUtil.isBlank(proName)) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("密钥key不能为空！");
			outPrintJson(response, jsonResult);
		}

		if (StringUtil.isBlank(proValue)) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("密钥值不能为空！");
			outPrintJson(response, jsonResult);
		}

		InterfaceProperties ip = new InterfaceProperties();
		ip.setId(Integer.valueOf(id));
		ip.setProName(proName);
		ip.setProValue(proValue);

		List<InterfaceProperties> list = shopService.queryChannelKey(ip);

		if (list.size() > 0) {
			jsonResult.setData(list);
			jsonResult.setIsok(true);
		}
		outPrintJson(response, jsonResult);
	}

	public ShopService getShopService() {
		return shopService;
	}

	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}

	@RequestMapping(value = "/index.spmvc")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		// Map map =new HashMap();
		String pid = request.getParameter("id") == null ? "-1" : request.getParameter("id");
		String system = request.getParameter("system");
		String shopCode = request.getParameter("shopCode");
		String menuType = request.getParameter("menuType");
		ChannelShopExample channelShopExample = new ChannelShopExample();
		Criteria criteria = channelShopExample.createCriteria();
		criteria.andShopStatusEqualTo((byte) 1);
		if (StringUtil.isNotBlank(shopCode)) {
			criteria.andShopCodeEqualTo(shopCode);
		}
		List<ChannelShopVo> ChannelShopVoList = shopService.getChannelShopList(channelShopExample, true);
		ChannelShopVo channelShopVo = null;
		ModelAndView mav = new ModelAndView();
		if (ChannelShopVoList.size() > 0) {
			channelShopVo = ChannelShopVoList.get(0);
			mav.addObject("channelCode", channelShopVo.getChannelCode());
		}

		mav.addObject("shopCode", shopCode);
		mav.addObject("pid", pid);
		mav.addObject("system", system);
		mav.addObject("channelShopVo", channelShopVo);
		mav.addObject("menuType", menuType);
		mav.setViewName("../index");
		return mav;
	}

}
