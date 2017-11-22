package com.work.shop.bean;

import java.math.BigDecimal;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.work.shop.bean.bgcontentdb.BGproductBarcodeList;
import com.work.shop.util.TimeUtil;

public class ChannelGoodsVo {

	/**
	 * 商品货号
	 */
	private String goodsSn;

	/**
	 * 商品名称
	 */
	// private String goods_title;

	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 是否同步
	 */
	private Byte isSyncStock;
	
	/**
	 * 是否同步库存，0，不同步，1，同步
	 */
	private String isSync;

	/**
	 * 价格
	 */
	private BigDecimal channelPrice;

	/**
	 * 创建时间
	 */
	private Integer addTime;

	/**
	 * 修改时间
	 */
	private Integer lastUpdateTime;

	/**
	 * 子颜色码
	 */
	private List<BGproductBarcodeList> barcodeChild;

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public BigDecimal getChannelPrice() {
		return channelPrice;
	}

	public void setChannelPrice(BigDecimal channelPrice) {
		this.channelPrice = channelPrice;
	}

	public Integer getAddTime() {
		return addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

	public Integer getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Integer lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/*
	 * public List<ProductBarcodeList> getBarcodeChild() { String bardcodeStr
	 * ="";
	 * 
	 * 
	 * 
	 * return barcodeChild; }
	 */

	public String getBarcodeChild() {
		// StringBuffer bardcodeStr = new StringBuffer();
		String str = "";

		if (null != barcodeChild && barcodeChild.size() > 0) {

			str = JSON.toJSONString(barcodeChild, true);
	
			//System.out.println(str);
		}
		/*
		 * bardcodeStr.append("{["); if(null != barcodeChild &&
		 * barcodeChild.size()>0) { for(int i=0; i<barcodeChild.size(); i++) {
		 * ProductBarcodeList pbList = barcodeChild.get(i);
		 * 
		 * if(null != pbList.getColorName() &&
		 * !"".equals(pbList.getColorName())){
		 * bardcodeStr.append("{\"colorName\" : \""+
		 * pbList.getColorName()+"\"},"); }
		 * 
		 * if(null != pbList.getSizeName() && !"".equals(pbList.getSizeName())){
		 * bardcodeStr.append("{\"sizeName\" : \""+ pbList.getSizeName()
		 * +"\"},"); }
		 * 
		 * 
		 * } } bardcodeStr.append("]}");
		 */

		JSONArray jsonArr = JSON.parseArray(str);

		if (null != jsonArr && !"".equals(jsonArr)) {
			return jsonArr.toString();
		}
		return "";
	}

	public void setBarcodeChild(List<BGproductBarcodeList> barcodeChild) {
		this.barcodeChild = barcodeChild;
	}

	public String getFormatAddTime() {
		String time = "";
		if (addTime != null && addTime != 0) {
			time = TimeUtil.parseDayDate(addTime, "yyyy-MM-dd HH:mm:ss");
		}
		return time;
	}

	public String getFormatUpdateTime() {
		String time = "";
		if (lastUpdateTime != null && lastUpdateTime != 0) {
			time = TimeUtil.parseDayDate(lastUpdateTime, "yyyy-MM-dd HH:mm:ss");
		}
		return time;
	}

	public Byte getIsSyncStock() {
		return isSyncStock;
	}

	public void setIsSyncStock(Byte isSyncStock) {
		this.isSyncStock = isSyncStock;
	}

	public String getIsSync() {
		return isSync;
	}

	public void setIsSync(String isSync) {
		this.isSync = isSync;
	}
	
	public List<BGproductBarcodeList> getBarcodeList(){
		return barcodeChild;
	}

	
}
