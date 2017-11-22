package com.work.shop.bean;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.work.shop.util.FileUtil;
import com.work.shop.util.StringUtil;

public class GoodsProperty{

	private String brandName;
	
	private String catName;

	private String sizePicture;
		
	private String cardPicture;
	
	private String channelCode;
	
	private String shopCode;

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getSizePicture() {
		String imgUrl = FileUtil.imgServerUrl(sizePicture, channelCode, shopCode, goodsSn, "--w_730_h_191", null);
		return imgUrl;
	}

	// 尺寸图原图 模板非空判断使用
	public String getSizePictureT() {
		return sizePicture;
	}
	
	public void setSizePicture(String sizePicture) {
		this.sizePicture = sizePicture;
	}

	public String getCardPicture() {
		String imgUrl = FileUtil.imgServerUrl(cardPicture, channelCode, shopCode, goodsSn, "--w_730_h_90", null);
		return imgUrl;
	}
	
	// 模特信息图原图 模板非空判断使用
	public String getCardPictureT() {
		return cardPicture;
	}
	
	public void setCardPicture(String cardPicture) {
		this.cardPicture = cardPicture;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	private String goodsSn;

	private String goodsName;

	private Integer catId;

	private Integer clickCount;

	private String brandCode;

	private String sizeCompareCode;

	private BigDecimal goodsWeight;

	private BigDecimal marketPrice;

	private BigDecimal protectPrice;

	private Integer warnNumber;

	private String keywords;

	private String goodsBrief;

	private String goodsThumb;

	private String goodsImg;

	private String originalImg;

	private Byte isReal;

	private String extensionCode;

	private String seasonCode;

	private String seriesCode;

	private String depotCode;

	private String seatCode;

	private Integer integral;

	private Integer sortOrder;

	private String sellerNote;

	private Integer giveIntegral;

	private Integer rankIntegral;

	private Boolean isAloneSale;

	private Byte salesType;

	private String vedioUrl;

	private String materialCode;

	private BigDecimal purPrice;

	private String vedioImgUrl;

	private String goodsTitle;

	private BigDecimal salePrice;

	private Boolean isOnSell;

	private Boolean isNew;

	private Integer newOrder;

	private Boolean isBest;

	private Integer bestOrder;

	private Boolean isHot;

	private Integer hotOrder;

	private Boolean isOutlets;

	private Float outletsCount;

	private Date marketDate;

	private String personClass;

	private Integer saleCount;

	private Integer favCount;

	private Integer likeCount;

	private Integer unlikeCount;

	private BigDecimal costPrice;

	private String thickness;

	private String elasticity;

	private String softness;

	private String palceCode;

	private String sizeTable;

	private String collocationGoodsSn;
	
	private String salePoint;

	public String getGoodsSn() {
		return goodsSn;
	}

	public void setGoodsSn(String goodsSn) {
		this.goodsSn = goodsSn == null ? null : goodsSn.trim();
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName == null ? null : goodsName.trim();
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode == null ? null : brandCode.trim();
	}

	public String getSizeCompareCode() {
		return sizeCompareCode;
	}

	public void setSizeCompareCode(String sizeCompareCode) {
		this.sizeCompareCode = sizeCompareCode == null ? null : sizeCompareCode.trim();
	}

	public BigDecimal getGoodsWeight() {
		return goodsWeight;
	}



	public void setGoodsWeight(BigDecimal goodsWeight) {
		this.goodsWeight = goodsWeight;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getProtectPrice() {
		return protectPrice;
	}

	public void setProtectPrice(BigDecimal protectPrice) {
		this.protectPrice = protectPrice;
	}

	public Integer getWarnNumber() {
		return warnNumber;
	}

	public void setWarnNumber(Integer warnNumber) {
		this.warnNumber = warnNumber;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords == null ? null : keywords.trim();
	}

	public String getGoodsBrief() {
		return goodsBrief;
	}

	public void setGoodsBrief(String goodsBrief) {
		this.goodsBrief = goodsBrief == null ? null : goodsBrief.trim();
	}

	public String getGoodsThumb() {
		return goodsThumb;
	}

	public void setGoodsThumb(String goodsThumb) {
		this.goodsThumb = goodsThumb == null ? null : goodsThumb.trim();
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg == null ? null : goodsImg.trim();
	}

	public String getOriginalImg() {
		return originalImg;
	}

	public void setOriginalImg(String originalImg) {
		this.originalImg = originalImg == null ? null : originalImg.trim();
	}

	public Byte getIsReal() {
		return isReal;
	}

	public void setIsReal(Byte isReal) {
		this.isReal = isReal;
	}

	public String getExtensionCode() {
		return extensionCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode == null ? null : extensionCode.trim();
	}

	public String getSeasonCode() {
		return seasonCode;
	}

	public void setSeasonCode(String seasonCode) {
		this.seasonCode = seasonCode == null ? null : seasonCode.trim();
	}

	public String getSeriesCode() {
		return seriesCode;
	}

	public void setSeriesCode(String seriesCode) {
		this.seriesCode = seriesCode == null ? null : seriesCode.trim();
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode == null ? null : depotCode.trim();
	}

	public String getSeatCode() {
		return seatCode;
	}

	public void setSeatCode(String seatCode) {
		this.seatCode = seatCode == null ? null : seatCode.trim();
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSellerNote() {
		return sellerNote;
	}

	public void setSellerNote(String sellerNote) {
		this.sellerNote = sellerNote == null ? null : sellerNote.trim();
	}

	public Integer getGiveIntegral() {
		return giveIntegral;
	}

	public void setGiveIntegral(Integer giveIntegral) {
		this.giveIntegral = giveIntegral;
	}

	public Integer getRankIntegral() {
		return rankIntegral;
	}

	public void setRankIntegral(Integer rankIntegral) {
		this.rankIntegral = rankIntegral;
	}
 
	public Boolean getIsAloneSale() {
		return isAloneSale;
	}

	public void setIsAloneSale(Boolean isAloneSale) {
		this.isAloneSale = isAloneSale;
	}

	public Byte getSalesType() {
		return salesType;
	}

	public void setSalesType(Byte salesType) {
		this.salesType = salesType;
	}

	public String getVedioUrl() {
		return vedioUrl;
	}

	public void setVedioUrl(String vedioUrl) {
		this.vedioUrl = vedioUrl == null ? null : vedioUrl.trim();
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode == null ? null : materialCode.trim();
	}

	public BigDecimal getPurPrice() {
		return purPrice;
	}

	public void setPurPrice(BigDecimal purPrice) {
		this.purPrice = purPrice;
	}

	public String getVedioImgUrl() {
		return vedioImgUrl;
	}

	public void setVedioImgUrl(String vedioImgUrl) {
		this.vedioImgUrl = vedioImgUrl == null ? null : vedioImgUrl.trim();
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle == null ? null : goodsTitle.trim();
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public Boolean getIsOnSell() {
		return isOnSell;
	}

	public void setIsOnSell(Boolean isOnSell) {
		this.isOnSell = isOnSell;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public Integer getNewOrder() {
		return newOrder;
	}

	public void setNewOrder(Integer newOrder) {
		this.newOrder = newOrder;
	}

	public Boolean getIsBest() {
		return isBest;
	}

	public void setIsBest(Boolean isBest) {
		this.isBest = isBest;
	}

	public Integer getBestOrder() {
		return bestOrder;
	}

	public void setBestOrder(Integer bestOrder) {
		this.bestOrder = bestOrder;
	}

	public Boolean getIsHot() {
		return isHot;
	}

	public void setIsHot(Boolean isHot) {
		this.isHot = isHot;
	}

	public Integer getHotOrder() {
		return hotOrder;
	}

	public void setHotOrder(Integer hotOrder) {
		this.hotOrder = hotOrder;
	}

	public Boolean getIsOutlets() {
		return isOutlets;
	}

	public void setIsOutlets(Boolean isOutlets) {
		this.isOutlets = isOutlets;
	}

	public Float getOutletsCount() {
		return outletsCount;
	}

	public void setOutletsCount(Float outletsCount) {
		this.outletsCount = outletsCount;
	}

	public Date getMarketDate() {
		return marketDate;
	}

	public void setMarketDate(Date marketDate) {
		this.marketDate = marketDate;
	}

	public String getPersonClass() {
		return personClass;
	}

	public void setPersonClass(String personClass) {
		this.personClass = personClass == null ? null : personClass.trim();
	}

	public Integer getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(Integer saleCount) {
		this.saleCount = saleCount;
	}

	public Integer getFavCount() {
		return favCount;
	}
 
	public void setFavCount(Integer favCount) {
		this.favCount = favCount;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}


	public Integer getUnlikeCount() {
		return unlikeCount;
	}

	
	public void setUnlikeCount(Integer unlikeCount) {
		this.unlikeCount = unlikeCount;
	}

	public BigDecimal getCostPrice() {
		return costPrice;	
	}


	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}


	public String getThickness() {
		return thickness;
	}


	public void setThickness(String thickness) {
		this.thickness = thickness == null ? null : thickness.trim();
	}


	public String getElasticity() {
		return elasticity;
	}

	public void setElasticity(String elasticity) {
		this.elasticity = elasticity == null ? null : elasticity.trim();
	}

	public String getSoftness() {
		return softness;
	}


	public void setSoftness(String softness) {
		this.softness = softness == null ? null : softness.trim();
	}


	public String getPalceCode() {
		return palceCode;
	}

	public void setPalceCode(String palceCode) {
		this.palceCode = palceCode == null ? null : palceCode.trim();
	}

	public String getSizeTable() {
		if (StringUtil.isNotEmpty(sizeTable)) {
			String size = "";
			StringBuffer tab = new StringBuffer("");
			try {
				size = URLDecoder.decode(sizeTable, "utf-8");
				JSONArray lv0 = JSON.parseArray(size);
				String [][] sizeArr = null;
				int lv0Length = lv0.size();
				int lv1Length = 0;
				for (int i = 0;i< lv0Length;i++) {
					JSONArray lv1 = JSONArray.parseArray(lv0.get(i).toString());
					if (i == 0) {
						lv1Length = lv1.size();
						sizeArr = new String[lv1Length][lv0Length];
					}
					for (int j = 0; j < lv1Length; j++) {
						try {
							sizeArr[j][i] = (String) lv1.get(j);
						} catch (Exception e) {
							sizeArr[j][i] = "-";
						}
					}
				}
				for (int a = 0;a < lv1Length;a++) {
					tab.append("<tr>\n");
					for (int b = 0;b < lv0Length;b++) {
						try {
							if (StringUtil.isNotNull(sizeArr[a][b])) {
								if(StringUtil.isNotEmpty(sizeArr[a][b])) {
									tab.append("<td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>"+ sizeArr[a][b] +"</td>\n");
								} else {
									tab.append("<td <td style='border:1px solid #ccc; text-align:center; line-height:25px; font-size: 12px;'>>-</td>\n");
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					tab.append("</tr>\n");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return tab.toString();
		}
		return sizeTable;
	}

	public void setSizeTable(String sizeTable) {
		this.sizeTable = sizeTable == null ? null : sizeTable.trim();
	}
	
	// 尺寸数据表非空判断使用
	public String getSizeTableT() {
		return sizeTable;
	}

	public String getCollocationGoodsSn() {
		return collocationGoodsSn;
	}

	public void setCollocationGoodsSn(String collocationGoodsSn) {
		this.collocationGoodsSn = collocationGoodsSn == null ? null : collocationGoodsSn.trim();
	}

	public String getSalePoint() {
		return salePoint;
	}

	public void setSalePoint(String salePoint) {
		this.salePoint = salePoint;
	}
}
