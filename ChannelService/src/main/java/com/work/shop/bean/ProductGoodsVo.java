package com.work.shop.bean;

import java.util.List;

import com.work.shop.bean.bgcontentdb.BGproductBarcodeList;

public class ProductGoodsVo extends ProductGoods {

	
	/**
	 * 子颜色码
	 */
	private List<BGproductBarcodeList> barcodeChild;

	public List<BGproductBarcodeList> getBarcodeChild() {
		return barcodeChild;
	}

	public void setBarcodeChild(List<BGproductBarcodeList> barcodeChild) {
		this.barcodeChild = barcodeChild;
	}

}
