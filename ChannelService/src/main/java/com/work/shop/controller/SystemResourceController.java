package com.work.shop.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.work.shop.bean.SystemResource;
import com.work.shop.bean.SystemResourceExample;
import com.work.shop.bean.SystemResourceExample.Criteria;
import com.work.shop.service.SystemResourceService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.JsonResult;

@Controller
@RequestMapping(value = "systemresource")
public class SystemResourceController extends BaseController {

	@Resource(name = "systemResourceService")
	private SystemResourceService systemResourceService;

	// 调整单商品信息查询
	@RequestMapping(value = "list.spmvc")
	public void getTicketInfoList(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SystemResource systemResource) {

		String start = request.getParameter("start") == null ? "" : request.getParameter("start");
		String limit = request.getParameter("limit") == null ? "" : request.getParameter("limit");

		SystemResourceExample ticketInfoExample = new SystemResourceExample();
		Criteria criteria = ticketInfoExample.or();
		if (systemResource != null) {
			if (StringUtil.isNotEmpty(systemResource.getResourceUrl())) {
				criteria.andResourceUrlLike("%" + systemResource.getResourceUrl() + "%");
			}

			if (StringUtil.isNotEmpty(systemResource.getResourceName())) {
				criteria.andResourceNameLike("%" + systemResource.getResourceName() + "%");
			}
		}

		// 分页字段
		if (!"".equals(start) && !"".equals(limit)) {
			criteria.limit(Integer.parseInt(start), Integer.parseInt(limit));
		}

		Paging paging = systemResourceService.getSystemResourceList(ticketInfoExample);

		try {
			writeJson(paging, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 调整单商品信息查询
	@RequestMapping(value = "addOrSave.spmvc")
	public void addOrSaveSystemResource(HttpServletRequest request, HttpServletResponse response, @ModelAttribute SystemResource systemResource) {
		try {
			JsonResult jsonResult = null;
			if (systemResource.getResourceId() == null) {
				systemResource.setOperUser(getUserName(request));
				jsonResult = systemResourceService.addNewData(systemResource);
			} else {
				jsonResult = systemResourceService.updateData(systemResource);
			}
			outPrintJson(response, jsonResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "deleteByIds.spmvc")
	public void deleteTicketInfo(HttpServletResponse response, @ModelAttribute("ids") String ids) {
		JsonResult jsonResult = systemResourceService.deleteDataById(ids);
		outPrintJson(response, jsonResult);
	}

}
