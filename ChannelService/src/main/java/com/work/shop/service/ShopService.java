package com.work.shop.service;

import java.util.List;

import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.InterfaceProperties;
import com.work.shop.bean.OmsChannelInfo;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.ChannelShopVo;
import com.work.shop.vo.JsonResult;




public interface ShopService {

	/**
	 * 获取所有外部渠道店铺 信息，有分页
	 *  @author chenrui;  
	 *  @return List<ChannelShop>
	 ***/
	public List<ChannelShopVo> getChannelShopList(ChannelShopExample channelShopExample, boolean includeChildFlag);
	
    /***
     * 
     * 获取所有店铺 数量 
     * @author chenrui;  
	 *  @return int;
     ***/
	public int getChannelShopCount(ChannelShopExample channelShopExample );

	/**
	 * 添加店铺信息 
	 * @author chenrui;
	 * @ChannelShop channelShop 店铺bean;
	 * @int shopChannel;
	 * @return int;
	 **/
	public JsonResult insertChannelShop(ChannelShop channelShop);
	
	/**
	 * 关联添加修改店铺
	 * @param channelShop
	 * @param openShopChannelInfo
	 * @return
	 */
	public JsonResult insertChannelShop(ChannelShop channelShop,OmsChannelInfo omsChannelInfo);
	/**
	 * 修改店铺信息
	 * @author chenrui;
	 * @ChannelShop channelShop 店铺bean;
	 * @return int;
	 **/
	public int updateChannelShop(ChannelShop channelShop);
	
	
	
	
	/**
	 * 查询指定店铺信息
	 * @author chenrui;
	 * @ChannelShop channelShop 店铺bean;
	 * @return int;
	 **/
	public ChannelShop queryChannelShop(ChannelShop channelShop);
	
	
	/**
	 * 删除指定店铺信息
	 * @author chenrui;
	 * @ChannelShop channelShop 店铺bean;
	 * @return int;
	 **/
	public int deleteChannelShopByPrimaryKey(String ids);
	
	/**
	 * 激活店铺状态
	 * @author chenrui;
	 * @ChannelShop channelShop 店铺bean;
	 * @param shopCode TODO
	 * @return int;
	 **/
	public int activeChannelShop(String ids,String shopStatus, String shopCodes);
	
	
	/**
	 * 添加渠道密钥值
	 * @author chenrui;
	 * @ChannelShop channelShop 店铺bean;
	 * @return int;
	 **/
	public int addChannelKey(InterfaceProperties interfaceProperties);
	
	
	public int updateChannelKey(InterfaceProperties interfaceProperties);
	
	
	public List<InterfaceProperties> queryChannelKey(InterfaceProperties interfaceProperties);
	
	
	public List<ChannelShop> getAllChannelShopList(ChannelShopExample channelShopExample);

	
	/**
	 * 父店铺编号列表查询
	 * @param channelShopExample
	 * @return
	 */
	public Paging getParentChannelShop(ChannelShopExample channelShopExample);
	
	/**
	 * 获取线上在售店铺列表
	 * @return
	 */
	public List<ChannelShop> selectOnlineOnsaleChannelShop();
}
