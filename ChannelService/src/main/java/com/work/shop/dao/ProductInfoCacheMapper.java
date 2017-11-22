package com.work.shop.dao;

import java.util.List;

import com.work.shop.invocation.Writer;

public interface ProductInfoCacheMapper {
	
	/*@Writer
	ProductGoodsInfo selectProductGoodsInfoByBarcode(HashMap<String,String> param);*/
	
	@Writer
	List<String> getLuckyBagBarcodeList();
}
