package com.work.shop.dao.mbproduct;

import java.util.List;

import com.work.shop.invocation.ReadOnly;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsAttr;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsAttrExample;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsAttrVO;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsAttrVOExample;

public interface MBProductAttrSelectMapper {
	
	  @ReadOnly
	    List<MBProductSellerGoodsAttrVO> selectByExample(MBProductSellerGoodsAttrVOExample example);

	    @ReadOnly
	    int countByExample(MBProductSellerGoodsAttrVOExample example);
}
