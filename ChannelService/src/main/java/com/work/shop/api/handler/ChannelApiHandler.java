package com.work.shop.api.handler;

import java.util.Map;

import com.work.shop.api.bean.ApiResultVO;
import com.work.shop.api.bean.ItemAdd;
import com.work.shop.api.bean.ItemQuery;
import com.work.shop.api.bean.ItemUpdate;
import com.work.shop.api.bean.LocalItemQuery;

public abstract class ChannelApiHandler {

	protected Map<String, String> secretInforMap;

	public Map<String, String> getSecretInforMap() {
		return secretInforMap;
	}

	public void setSecretInforMap(Map<String, String> secretInforMap) {
		this.secretInforMap = secretInforMap;
	}

	public void buildApiClient(Map<String, String> secretInforMap) {
		this.secretInforMap = secretInforMap;
	}

	/**
	 * 修改商品价格
	 * 
	 * @param itemUpdate.goodsSn 商品编号或Sku所属商品数字id
	 * @param itemUpdate.itemFieldValue 价格
	 * @return UpdatePriceVo
	 */
	public abstract ApiResultVO updatePrice(ItemUpdate itemUpdate);

	/**
	 * 修改商品标题
	 * 
	 * @param itemUpdate.goodsSn 商品编号或Sku所属商品数字id
	 * @param itemUpdate.itemFieldValue 标题
	 * @return ApiResultVO
	 */
	public abstract ApiResultVO updateGoodsName(ItemUpdate itemUpdate);

	/**
	 * 商品上架
	 * @param itemUpdate.goodsSn 商品编号
	 * @return
	 */
	public abstract ApiResultVO shelvesUpOrDown(ItemUpdate itemUpdate);

	/**
	 * 查询商品基础数据
	 * 
	 * @param itemQuery
	 * @return
	 */
	public abstract ApiResultVO getBaseData(ItemQuery itemQuery);
	
	/**
	 * 修改商品(11位码)库存数量
	 * @param itemUpdate.skuSn 11位码
	 * @param itemUpdate.stockCount 库存数量
	 * @param itemUpdate.type 1：全量更新; 2：增量更新
	 * @return
	 */
	public abstract ApiResultVO updateItemStock(ItemUpdate itemUpdate);
	
	/**
	 * 添加商品
	 * @param itemAdd 添加商品对象所有属性不能为空
	 * @return
	 */
	public abstract ApiResultVO addItem(ItemAdd itemAdd);
	
	/**
	 * 查询本地商品数据
	 * @param query
	 * 			query.shopCode 店铺编码 query.itemNo 商品编码 query.page 页码  query.pageSize 每页条数 query.wareStatus 状态0 下架1 上架
	 * @return
	 */
	public abstract ApiResultVO searchItemPage(LocalItemQuery query);
}
