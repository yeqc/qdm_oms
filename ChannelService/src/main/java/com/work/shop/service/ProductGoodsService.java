package com.work.shop.service;

import com.work.shop.bean.bgcontentdb.BGproductGoods;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;


public interface ProductGoodsService {
	
	/**
	 * 产品信息同步到渠道商品表中
	 * @param 0成功，1失败
	 * @author guoduanduan
	 * */
	public void synProductGoodsInfo(String channelCode,String shopCode);
	
	/**
	 * 产品信息
	 */
	Paging getProductGoodsList(BGproductGoods model, PageHelper helper);
	
	BGproductGoods selectByGoodsSn(String goodsSn);
	
}
