package com.work.shop.service;

import java.util.List;

import com.work.shop.bean.SystemResource;
import com.work.shop.bean.SystemResourceExample;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.SystemResourceVo;

public interface SystemResourceService {

	/**
	 * 插入新数据
	 */
	public JsonResult addNewData(SystemResource entity);

	/**
	 * 数据集合信息
	 */
	public Paging getSystemResourceList(SystemResourceExample example);

	/**
	 * 更新数据
	 */
	public JsonResult updateData(SystemResource entity);

	/**
	 * 删除数据
	 */
	public JsonResult deleteDataById(String ids);
	
	public List<SystemResourceVo> getMenuTree(String parentCode, List<String> authCodes);

}
