package com.work.shop.dao;

import java.util.List;
import java.util.Map;

import com.work.shop.bean.LuckyBagGoodsRelationDetailBean;

public interface LuckyBagDefineMapper {
	
	List<LuckyBagGoodsRelationDetailBean> getLuckyBagGoodsListByParams(Map params);
	
	int countLuckyBagGoodsListByParams(Map params);
	
	List<LuckyBagGoodsRelationDetailBean> getLuckyBagGoodsListByLuckyBagSku(Map params);

}
