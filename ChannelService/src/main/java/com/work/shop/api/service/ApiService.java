package com.work.shop.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ChannelApiGoods;
import com.work.shop.api.bean.ItemAdd;
import com.work.shop.api.bean.ItemQuery;
import com.work.shop.api.bean.ItemUpdate;
import com.work.shop.api.bean.LocalItemQuery;
import com.work.shop.api.bean.ProdItem;
import com.work.shop.bean.InterfaceProperties;
import com.work.shop.bean.InterfacePropertiesExample;
import com.work.shop.dao.InterfacePropertiesMapper;
import com.work.shop.util.Constants;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.QueueManagerVo;

public abstract class ApiService implements ApiSecretSerivce {

	@Resource
	protected InterfacePropertiesMapper interfacePropertiesMapper;

	public abstract JsonResult ticketDisposal(QueueManagerVo managerVo);

	public abstract ApiResultVO<List<ChannelApiGoods>> searchItemPage(LocalItemQuery query);

	@Override
	public Map<String, String> getSecretInfo(String channelCode, String shopCode) {
		Map<String, String> secretInfoMap = new HashMap<String, String>();
		InterfacePropertiesExample example = new InterfacePropertiesExample();
		example.or().andChannelCodeEqualTo(channelCode).andShopCodeEqualTo(shopCode);
		List<InterfaceProperties> pptList = interfacePropertiesMapper.selectByExample(example);
		for (InterfaceProperties interfaceProperties : pptList) {
			secretInfoMap.put(interfaceProperties.getProName(), interfaceProperties.getProValue());
		}
		secretInfoMap.put(Constants.CHANNEL_CODE, channelCode);
		secretInfoMap.put(Constants.SHOP_CODE, shopCode);
		return secretInfoMap;
	}
	
	/**
	 * 修改商品(11位码)库存数量
	 * @param itemUpdate.skuSn 11位码
	 * @param itemUpdate.stockCount 库存数量
	 * @param type 1：全量更新，2：增量更新
	 * @return
	 */
	public abstract ApiResultVO updateItemStock(ItemUpdate itemUpdate);
	
	/**
	 * 修改商品价格
	 * 
	 * @param itemUpdate.goodsSn 商品编号或Sku所属商品数字id
	 * @param itemUpdate.itemFieldValue 价格
	 * @return UpdatePriceVo
	 */
	public abstract ApiResultVO updatePrice(ItemUpdate itemUpdate);

	/**
	 * 商品上架下架
	 * @param itemUpdate.goodsSn 商品编号
	 * @return
	 */
	public abstract ApiResultVO shelvesUpOrDown(ItemUpdate itemUpdate);

	/**
	 * 查询基础数据
	 * 
	 * @param itemQuery
	 * @param channelCode
	 * @param shopCode
	 * @return
	 */
	public abstract ApiResultVO<List<ProdItem>> getBaseData(ItemQuery itemQuery, String channelCode, String shopCode);
	
	/**
	 * 添加商品
	 * @param itemAdd 添加商品对象所有属性不能为空
	 * @param channelCode 渠道编码
	 * @return
	 */
	public abstract ApiResultVO addItem(ItemAdd itemAdd, String channelCode);
}
