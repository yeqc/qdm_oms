package com.work.shop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.bean.SystemResource;
import com.work.shop.bean.SystemResourceExample;
import com.work.shop.dao.SystemResourceMapper;
import com.work.shop.service.SystemResourceService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.SystemResourceVo;

@Service("systemResourceService")
public class SystemResourceServiceImpl implements SystemResourceService {

	@Resource(name = "systemResourceMapper")
	private SystemResourceMapper systemResourceMapper;

	@Override
	public JsonResult addNewData(SystemResource entity) {
		JsonResult jsonResult = new JsonResult();
		try {

			if (StringUtil.isEmpty(entity.getParentCode())) {
				entity.setParentCode("0");
			}
			if (isExist(entity.getParentCode(), entity.getResourceCode())) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("父资源码(" + entity.getParentCode() + "),资源码(" + entity.getResourceCode() + ")已经存在!");
				return jsonResult;
			}

			entity.setAddTime(new Date());
			entity.setUpdateTime(new Date());

			int count = systemResourceMapper.insertSelective(entity);

			if (count != 1) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("数据添加失败!");
				return jsonResult;
			}

		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("数据添加失败!");
			e.printStackTrace();
		}

		jsonResult.setIsok(true);
		jsonResult.setMessage("数据添加成功!");
		return jsonResult;
	}

	@Override
	public Paging getSystemResourceList(SystemResourceExample example) {
		if (example == null) {
			return null;
		}
		Paging pageData = new Paging();
		int count = systemResourceMapper.countByExample(example);
		pageData.setTotalProperty(count);
		List<SystemResource> dataList = systemResourceMapper.selectByExample(example);
		pageData.setRoot(dataList);
		return pageData;
	}

	@Override
	public JsonResult updateData(SystemResource entity) {
		JsonResult jsonResult = new JsonResult();
		try {

			SystemResource oldEntit = systemResourceMapper.selectByPrimaryKey(entity.getResourceId());

			if (oldEntit == null) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("数据更新失败!");
				return jsonResult;
			}

			if (!oldEntit.getParentCode().equals(entity.getParentCode()) || !oldEntit.getResourceCode().equals(entity.getResourceCode())) {
				if (isExist(entity.getParentCode(), entity.getResourceCode())) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("父资源码(" + entity.getParentCode() + "),资源码(" + entity.getResourceCode() + ")已经存在!");
					return jsonResult;
				}
			}

			oldEntit.setResourceCode(entity.getResourceCode());
			oldEntit.setResourceName(entity.getResourceName());
			oldEntit.setResourceUrl(entity.getResourceUrl());
			oldEntit.setIsShow(entity.getIsShow());
			oldEntit.setParentCode(entity.getParentCode());
			oldEntit.setResourceType(entity.getResourceType());
			oldEntit.setUpdateTime(new Date());

			int count = systemResourceMapper.updateByPrimaryKeySelective(oldEntit);

			if (count != 1) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("数据更新失败!");
				return jsonResult;
			}

		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("数据更新失败!");
			e.printStackTrace();
		}

		jsonResult.setIsok(true);
		jsonResult.setMessage("数据更新成功!");
		return jsonResult;
	}

	@Override
	public JsonResult deleteDataById(String ids) {
		JsonResult jsonResult = new JsonResult();
		try {

			String[] idArray = ids.split(",");
			List<Integer> idList = new ArrayList<Integer>();

			for (int i = 0; i < idArray.length; i++) {
				idList.add(Integer.parseInt(idArray[i]));
			}

			SystemResourceExample example = new SystemResourceExample();
			example.or().andResourceIdIn(idList);
			int count = systemResourceMapper.deleteByExample(example);

			if (count != idList.size()) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("数据删除失败!");
				return jsonResult;
			}

		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("数据删除失败!");
			e.printStackTrace();
		}

		jsonResult.setIsok(true);
		jsonResult.setMessage("数据删除成功!");
		return jsonResult;
	}

	@Override
	public List<SystemResourceVo> getMenuTree(String parentCode, List<String> authCodes) {
		List<SystemResourceVo> menuList = new ArrayList<SystemResourceVo>();

		if (StringUtil.isEmpty(parentCode) || authCodes == null || authCodes.size() <= 0) {
			return menuList;
		}

		SystemResourceExample parentExample = new SystemResourceExample();
		parentExample.or().andParentCodeEqualTo(parentCode).andIsShowEqualTo((byte) 1);

		List<SystemResource> parentList = systemResourceMapper.selectByExample(parentExample);
		SystemResourceVo systemResourceVo = null;
		for (SystemResource parent : parentList) {
			String resourceCode = parent.getResourceCode();
			SystemResourceExample childExample = new SystemResourceExample();
			childExample.or().andParentCodeEqualTo(resourceCode).andResourceCodeIn(authCodes).andIsShowEqualTo((byte) 1);

			List<SystemResource> childList = systemResourceMapper.selectByExample(childExample);

			if (childList == null || childList.size() == 0) {
				continue;
			}

			systemResourceVo = new SystemResourceVo();

			systemResourceVo.setParentCode(parent.getParentCode());
			systemResourceVo.setResourceCode(parent.getResourceCode());
			systemResourceVo.setResourceName(parent.getResourceName());
			systemResourceVo.setChildMenuList(childList);

			menuList.add(systemResourceVo);
		}

		return menuList;
	}

	private boolean isExist(String parentCode, String resourceCode) {
		SystemResourceExample example = new SystemResourceExample();
		example.or().andParentCodeEqualTo(parentCode).andResourceCodeEqualTo(resourceCode);
		List<SystemResource> parentList = systemResourceMapper.selectByExample(example);
		if (parentList == null || parentList.size() <= 0) {
			return false;
		}
		return true;
	}

}
