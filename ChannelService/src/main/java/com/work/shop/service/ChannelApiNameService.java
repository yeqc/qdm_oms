package com.work.shop.service;

import java.util.List;

import com.work.shop.bean.ChannelApiName;
import com.work.shop.util.extjs.Pagination;
import com.work.shop.util.extjs.Paging;

public interface ChannelApiNameService {
	
	
	
	/**
	 * 渠道接口查询
	 * @param channelApiName
	 * @return
	 */
	public List<ChannelApiName> getChannelApi(ChannelApiName channelApiName);
	
	
	/**
	 * 渠道接口列表查询
	 * @param channelApiName
	 * @return
	 */
	public Paging getChannelApiList(ChannelApiName channelApiName,Pagination pagination);
	
	/**
	 * 渠道接口列表记录数量查询
	 * @param channelApiName
	 * @return
	 */
	public int count(ChannelApiName channelApiName);
	
	/**
	 * 添加渠道接口
	 * @param channelApiName
	 * @return
	 */
	public Boolean addChannelApiName(ChannelApiName channelApiName);
	
	/**
	 * 修改渠道接口
	 * @param channelApiName
	 * @return
	 */
	public Boolean updateChannelApiName(ChannelApiName channelApiName);
	
	
	/**
	 * 是否重名渠道接口
	 * @param channelApiName
	 * @return
	 */
	public Boolean isSameName(ChannelApiName channelApiName);

}
