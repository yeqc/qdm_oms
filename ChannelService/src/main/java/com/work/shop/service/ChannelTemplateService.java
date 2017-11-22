package com.work.shop.service;

import java.util.List;

import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelTemplate;
import com.work.shop.bean.ChannelTemplateContent;
import com.work.shop.bean.ChannelTemplateContentExample;
import com.work.shop.bean.ChannelTemplateExample;
import com.work.shop.bean.ChannelTemplateModule;
import com.work.shop.bean.ChannelTemplateModuleExample;
import com.work.shop.bean.ChannelTemplateWithBLOBs;
import com.work.shop.bean.TicketInfo;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Pagination;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.QueueManagerVo;

public interface ChannelTemplateService {

	/**
	 * 模板列表
	 * 
	 * @param example
	 * @return List<ChannelTemplate>
	 */
	List<ChannelTemplate> getChannelTemplateList(ChannelTemplate channelTemplate) throws Exception;
	
	/**
	 * 模板列表(分页)
	 * 
	 * @param example
	 * @return Paging
	 */
	Paging getChannelTemplatePage(ChannelTemplate model, PageHelper helper) throws Exception;
	
	/**
	 * 模板表总记录
	 * 
	 * @param example
	 * @return List<ChannelTemplate>
	 */
	int getChannelTemplateCount(ChannelTemplateExample example) throws Exception;
	
	/**
	 * 模板模块列表
	 * 
	 * @param example
	 * @return List<ChannelTemplate>
	 */
	List<ChannelTemplateModule> getChannelTemplateModuleList(ChannelTemplateModule record) throws Exception;
	
	/**
	 * 模板模块列表(分页)
	 * 
	 * @param example
	 * @return Paging
	 */
	Paging getChannelTemplateModulePage(ChannelTemplateModule model, PageHelper helper) throws Exception;
	
	/**
	 * 模板模块表总记录
	 * 
	 * @param example
	 * @return List<ChannelTemplate>
	 */
	int getChannelTemplateModuleCount(ChannelTemplateModuleExample example) throws Exception;

	/**
	 * 新增模块
	 * 
	 * @param example
	 * @return int
	 */
	int insertTemplate(ChannelTemplateWithBLOBs record, List<ChannelTemplateContent> contents) throws Exception;

	/**
	 * 新增样式模块
	 * 
	 * @param example
	 * @return int
	 */
	int insertModule(ChannelTemplateModule record) throws Exception;
	
	
	/**
	 * 更新模块
	 * 
	 * @param example
	 * @return int
	 */
	int updateTemplate(ChannelTemplateWithBLOBs record, List<ChannelTemplateContent> contents) throws Exception;

	/**
	 * 更新样式模块
	 * 
	 * @param example
	 * @return int
	 */
	int updateModule(ChannelTemplateModule record) throws Exception;

	/**
	 * 删除模块
	 * 
	 * @param ids
	 * @return int
	 */
	int deleteTemplateById(String ids) throws Exception;

	/**
	 * 删除样式模块
	 * 
	 * @param ids
	 * @return int
	 */
	int deleteModuleById(String ids) throws Exception;
	
	/**
	 * 获取单个模块信息
	 * 
	 * @param id
	 * @return ChannelTemplateModule
	 */
	ChannelTemplateWithBLOBs getTemplateById(Integer id) throws Exception;
	
	/**
	 * 获取单个模块信息
	 * 
	 * @param id
	 * @return ChannelTemplate
	 */
	ChannelTemplateModule getModuleById(Integer id) throws Exception;
	
	/**
	 * 模板内容列表
	 * 
	 * @param example
	 * @return List<ChannelTemplateContent>
	 */
	List<ChannelTemplateContent> getChannelTemplateContentList(ChannelTemplateContentExample example) throws Exception;

	/**
	 * 生成商品详情
	 * 
	 * @param example
	 * @return List<ChannelTemplateContent>
	 */
	void createGoodsDetail(QueueManagerVo managerVo, List<TicketInfo> ticketInfos) throws Exception;
	
	/**
	 * 商品信息列表
	 * @param goods
	 * @param helper
	 * @return Pagination
	 * @throws Exception
	 */
	Pagination getChannelGoodsList(ChannelGoods goods, PageHelper helper) throws Exception;
	
	/**
	 * 根据GoodsSn获取商品信息列表
	 * @param goods
	 * @param idList
	 * @return List<ChannelGoods>
	 * @throws Exception
	 */
	List<ChannelGoods> getGoodsListByGoodsSn(ChannelGoods goods, List<String> idList) throws Exception;
	
	void createTemplate(QueueManagerVo managerVo); 
}
