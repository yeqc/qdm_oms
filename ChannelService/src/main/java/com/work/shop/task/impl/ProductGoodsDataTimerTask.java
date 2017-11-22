package com.work.shop.task.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.work.shop.bean.ProductAttrValues;
import com.work.shop.bean.ProductAttrValuesExample;
import com.work.shop.bean.ProductBarcodeList;
import com.work.shop.bean.ProductBarcodeListExample;
import com.work.shop.bean.ProductGoodsExample;
import com.work.shop.bean.ProductGoodsGallery;
import com.work.shop.bean.ProductGoodsGalleryExample;
import com.work.shop.bean.ProductGoodsWithBLOBs;
import com.work.shop.bean.ProductLibBrand;
import com.work.shop.bean.ProductLibBrandExample;
import com.work.shop.bean.ProductLibBrandWithBLOBs;
import com.work.shop.bean.ProductLibCategory;
import com.work.shop.bean.ProductLibCategoryExample;
import com.work.shop.bean.mbproduct.MBProductLibBrand;
import com.work.shop.bean.mbproduct.MBProductLibBrandExample;
import com.work.shop.bean.mbproduct.MBProductLibCategory;
import com.work.shop.bean.mbproduct.MBProductLibCategoryExample;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsAttrVO;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsAttrVOExample;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsBarcode;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsBarcodeExample;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsGallery;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsGalleryExample;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsSelect;
import com.work.shop.bean.mbproduct.MBProductSellerGoodsSelectExample;
import com.work.shop.dao.ProductAttrValuesMapper;
import com.work.shop.dao.ProductBarcodeListMapper;
import com.work.shop.dao.ProductGoodsGalleryMapper;
import com.work.shop.dao.ProductGoodsMapper;
import com.work.shop.dao.ProductLibBrandMapper;
import com.work.shop.dao.ProductLibCategoryMapper;
import com.work.shop.dao.mbproduct.MBProductAttrSelectMapper;
import com.work.shop.dao.mbproduct.MBProductLibBrandMapper;
import com.work.shop.dao.mbproduct.MBProductLibCategoryMapper;
import com.work.shop.dao.mbproduct.MBProductSellerGoodsBarcodeMapper;
import com.work.shop.dao.mbproduct.MBProductSellerGoodsGalleryMapper;
import com.work.shop.dao.mbproduct.MBProductSellerGoodsSelectMapper;
import com.work.shop.redis.RedisClient;
import com.work.shop.task.IProductGoodsDataTimerTask;
import com.work.shop.util.DateTimeUtils;

@Component("productGoodsDataTimerTask")
public class ProductGoodsDataTimerTask implements IProductGoodsDataTimerTask{
	
	@Resource(name = "mBProductLibCategoryMapper")
	private MBProductLibCategoryMapper mBProductLibCategoryMapper;

//	@Resource(name = "mBProductSellerGoodsMapper")
//	private MBProductSellerGoodsMapper mBProductSellerGoodsMapper;

	@Resource(name = "mBProductSellerGoodsBarcodeMapper")
	private MBProductSellerGoodsBarcodeMapper mBProductSellerGoodsBarcodeMapper;

	@Resource(name = "mBProductSellerGoodsGalleryMapper")
	private MBProductSellerGoodsGalleryMapper mBProductSellerGoodsGalleryMapper;

	@Resource(name = "mBProductLibBrandMapper")
	private MBProductLibBrandMapper mBProductLibBrandMapper;

//	@Resource(name = "mBProductSellerGoodsAttrMapper")
//	private MBProductSellerGoodsAttrMapper mBProductSellerGoodsAttrMapper;

	@Resource(name = "mBProductAttrSelectMapper")
	private MBProductAttrSelectMapper mBProductAttrSelectMapper;
	
	@Resource(name = "mBProductSellerGoodsSelectMapper")
	private MBProductSellerGoodsSelectMapper mBProductSellerGoodsSelectMapper;
	

	@Resource(name = "productLibCategoryMapper")
	private ProductLibCategoryMapper productLibCategoryMapper;

	@Resource(name = "productGoodsMapper")
	private ProductGoodsMapper productGoodsMapper;

	@Resource(name = "productBarcodeListMapper")
	private ProductBarcodeListMapper productBarcodeListMapper;

	@Resource(name = "productGoodsGalleryMapper")
	private ProductGoodsGalleryMapper productGoodsGalleryMapper;

	@Resource(name = "productLibBrandMapper")
	private ProductLibBrandMapper productLibBrandMapper;

	@Resource(name = "productAttrValuesMapper")
	private ProductAttrValuesMapper productAttrValuesMapper;
	
	@Resource(name = "redisClient")
	private RedisClient redisClient;

	private Logger logger = Logger.getLogger(ProductGoodsDataTimerTask.class);
	
	

	@Override
	public void syncProduct(Map<String, String> para)  {
		String startTime = para.get("startTime");
		Date startDate = null;
		int interval = 0;
		String type = "";
		String goodsSn=null;
		try {
			if (para.get("interval") != null) {
				interval = Integer.parseInt(para.get("interval"));
			}
			if (para.get("type") != null) {
				type = para.get("type");
			}
			if(para.get("goodsSn") != null){
				goodsSn=para.get("goodsSn");
			}
			//如果goodsSn不为空则执行根据goodsSn进行同步数据，否则执行按修改时间进行同步
			if(goodsSn!=null){
				SyByGoodsSn(goodsSn);
			}else{
				if (startTime != null) {
					startDate = DateTimeUtils.parseStr(startTime);
				} else {
					startDate = DateTimeUtils.dateAdd(DateTimeUtils.INTERVAL_MINUTE,new Date(), interval);
				}
				Date nowDate=new Date();
				logger.info("调度中心操作：同步最后修改时间["+ DateTimeUtils.format(startDate,DateTimeUtils.YYYY_MM_DD_HH_mm_ss)+ "]至["+ DateTimeUtils.format(nowDate,DateTimeUtils.YYYY_MM_DD_HH_mm_ss)+"]的商品数据！");
				if (type.equals("PSG")) {
					SyMBProductSellerGoods(startDate,nowDate);
				} else if (type.equals("PSGB")) {
					SyMBProductSellerGoodsBarcode(startDate,nowDate);
				} else if (type.equals("PSGG")) {
					SyMBProductSellerGoodsGallery(startDate,nowDate);
				} else if (type.equals("PLC")) {
					SyMBProductLibCategory(startDate,nowDate);
				} else if (type.equals("PLB")) {
					SyMBProductLibBrand(startDate,nowDate);
				} else if (type.equals("PSGA")) {
					SyMBProductAttr(startDate,nowDate);
				} else {
					SyMBProductLibCategory(startDate,nowDate);
					SyMBProductSellerGoods(startDate,nowDate);
					SyMBProductSellerGoodsBarcode(startDate,nowDate);
					SyMBProductSellerGoodsGallery(startDate,nowDate);
					SyMBProductLibBrand(startDate,nowDate);
					SyMBProductAttr(startDate,nowDate);
				}
				logger.info("调度中心操作：同步结束！");
			}
		} catch (Exception e) {
			logger.error("execute异常：", e);
		}
	}

	// 更新6位码缓存
	private void SyncRediesGoodsSn(ProductGoodsWithBLOBs productGoods ){
		
	}
	
	// 更新11位码缓存
	private void SyncRediesSku(ProductBarcodeList productBarcodeList){
	}
	
	private void SyMBProductLibCategory(Date startDate,Date endDate) {
		int OSuccessNum=0;
		int OFailNum=0;
		int SyNum=0;
		try {
			MBProductLibCategoryExample mBProductLibCategoryExample = new MBProductLibCategoryExample();
			MBProductLibCategoryExample.Criteria criteria = mBProductLibCategoryExample.or();
			criteria.andLastUpdateDateBetween(startDate, endDate);
			criteria.andStatusEqualTo((byte) 1);
			List<MBProductLibCategory> list = mBProductLibCategoryMapper.selectByExample(mBProductLibCategoryExample);
			SyNum=list.size();
			for (MBProductLibCategory mBProductLibCategory : list) {
				ProductLibCategory productLibCategory = new ProductLibCategory();
				productLibCategory.setCatId(mBProductLibCategory.getRelCateId().shortValue());
				productLibCategory.setCatName(mBProductLibCategory.getCateName());
				productLibCategory.setCatCode("mb_"+mBProductLibCategory.getCateCode());
				productLibCategory.setSortOrder(mBProductLibCategory.getSort().intValue());
				if(mBProductLibCategory.getParentId()==0){
					productLibCategory.setParentId((short)0);
				}else{
					productLibCategory.setParentId(mBProductLibCategoryMapper.selectByPrimaryKey(mBProductLibCategory.getParentId()).getRelCateId().shortValue());
				}
				productLibCategory.setCategoryCode(mBProductLibCategory.getCrumb());
				productLibCategory.setLevelId(mBProductLibCategory.getLevel());
				productLibCategory.setIsShow(1);
				try {
						ProductLibCategoryExample productLibCategoryExample = new ProductLibCategoryExample();
						ProductLibCategory CproductLibCategory = productLibCategoryMapper.selectByPrimaryKey(productLibCategory.getCatId());
						if (CproductLibCategory!=null&&CproductLibCategory.getCatId()!=null) {
							productLibCategoryExample.or().andCatIdEqualTo(productLibCategory.getCatId());
							productLibCategoryMapper.updateByExampleSelective(productLibCategory,productLibCategoryExample);
						} else  {
							productLibCategoryMapper.insertSelective(productLibCategory);
						}
						OSuccessNum++;
						logger.info("ProductLibCategory同步"+productLibCategory.getCatId()+ ",成功!");
				} catch (Exception e) {
					OFailNum++;
					logger.error("ProductLibCategory同步"+productLibCategory.getCatId()+ ",失败:", e);
				}
			}
		} catch (Exception e) {
			logger.error("error:", e);
		}
		logger.info("调度中心操作：ProductLibCategory同步完成！需同步数据"+SyNum+",同步成功"+OSuccessNum+"失败"+OFailNum);
	}

	private void SyMBProductSellerGoods(Date startDate,Date endDate) {
		int OSuccessNum=0;
		int OFailNum=0;
		int count=0;
		try {
			MBProductSellerGoodsSelectExample mBProductSellerGoodsSelectExample = new MBProductSellerGoodsSelectExample();
			MBProductSellerGoodsSelectExample.Criteria criteria = mBProductSellerGoodsSelectExample.or();
			criteria.andLastUpdateDateBetween(startDate, endDate).andStatusEqualTo((byte)1);
			int pagesize = 5000;
			count = mBProductSellerGoodsSelectMapper.countByExample(mBProductSellerGoodsSelectExample);
			for (int i = 0; i < (double) count / pagesize; i++) {
				mBProductSellerGoodsSelectExample.clear();
				mBProductSellerGoodsSelectExample.or().andLastUpdateDateBetween(startDate, endDate).andStatusEqualTo((byte)1).limit(i * pagesize, pagesize);
				List<MBProductSellerGoodsSelect> list = mBProductSellerGoodsSelectMapper.selectByExampleWithBLOBs(mBProductSellerGoodsSelectExample);
				for (MBProductSellerGoodsSelect mBProductSellerGoods : list) {
					
					ProductGoodsWithBLOBs productGoods = new ProductGoodsWithBLOBs();
					productGoods.setGoodsSn(mBProductSellerGoods.getProductSysCode());
					productGoods.setGoodsName(mBProductSellerGoods.getProductName());
					MBProductLibCategory mBProductLibCategory=mBProductLibCategoryMapper.selectByPrimaryKey(mBProductSellerGoods.getCategoryId());
					if(mBProductLibCategory!=null){
					productGoods.setCatId(mBProductLibCategory.getRelCateId());
					}
					productGoods.setBrandCode(mBProductSellerGoods.getBrandCode());
					productGoods.setMarketPrice(mBProductSellerGoods.getMarketPrice());
					productGoods.setKeywords(mBProductSellerGoods.getKeywords());
					productGoods.setGoodsBrief(mBProductSellerGoods.getProductBrief());
					productGoods.setGoodsDesc(mBProductSellerGoods.getProductRemark());
					productGoods.setOriginalImg(mBProductSellerGoods.getProductUrl());
					productGoods.setGoodsImg(mBProductSellerGoods.getProductUrl());
					productGoods.setGoodsThumb(mBProductSellerGoods.getProductUrl());
					productGoods.setAddTime(mBProductSellerGoods.getCreateDate());
					productGoods.setIsDelete(mBProductSellerGoods.getStatus() == 1 ? (byte) 0 : (byte) 1);
					productGoods.setVedioUrl(mBProductSellerGoods.getProductVideo());
					productGoods.setCostPrice(mBProductSellerGoods.getCostPrice());
					productGoods.setCardPicture(mBProductSellerGoods.getModelCard());
					productGoods.setLastUpdateTime(mBProductSellerGoods.getLastUpdateDate());
					productGoods.setSalePoint(mBProductSellerGoods.getSellPoint());
					productGoods.setSizeTable(mBProductSellerGoods.getSizeTable());
					productGoods.setProtectPrice(mBProductSellerGoods.getProtectPrice());
					productGoods.setSizePicture(mBProductSellerGoods.getSizePicture());
					productGoods.setOriginalBrand(mBProductSellerGoods.getBrandName());
					productGoods.setSellerCode(mBProductSellerGoods.getSellerCode());
					productGoods.setSalesMode(mBProductSellerGoods.getSalesMode());
					/*
					 * 更新6位码缓存
					 */
					SyncRediesGoodsSn(productGoods);
					try {
						ProductGoodsWithBLOBs cproductGoods = productGoodsMapper.selectByPrimaryKey(mBProductSellerGoods.getProductSysCode());
							if (cproductGoods != null&& cproductGoods.getGoodsSn() != null) {
								ProductGoodsExample productGoodsExample = new ProductGoodsExample();
								productGoodsExample.or().andGoodsSnEqualTo(productGoods.getGoodsSn());
								productGoodsMapper.updateByExampleSelective(productGoods, productGoodsExample);
							} else {
								productGoodsMapper.insertSelective(productGoods);
							}
							OSuccessNum++;
							logger.info("ProductSellerGoods:"+mBProductSellerGoods.getProductSysCode()+ ",成功！");
					} catch (Exception e) {
						OFailNum++;
						logger.error("ProductSellerGoods:"+mBProductSellerGoods.getProductSysCode()+ ",失败:", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("error:", e);
		}
		logger.info("调度中心操作：ProductSellerGoods同步完成！需同步数据"+count+",同步成功"+OSuccessNum+"失败"+OFailNum);
	}

	private void SyMBProductSellerGoodsBarcode(Date startDate,Date endDate) {
		int OSuccessNum=0;
		int OFailNum=0;
		int count=0;
		try {
		MBProductSellerGoodsBarcodeExample mBProductSellerGoodsBarcodeExample = new MBProductSellerGoodsBarcodeExample();
		MBProductSellerGoodsBarcodeExample.Criteria criteria = mBProductSellerGoodsBarcodeExample.or();
		criteria.andLastUpdateDateBetween(startDate, endDate);
		criteria.andStatusEqualTo((byte) 1);
		int pagesize = 5000;
		count = mBProductSellerGoodsBarcodeMapper.countByExample(mBProductSellerGoodsBarcodeExample);
		for (int i = 0; i < (double) count / pagesize; i++) {
			mBProductSellerGoodsBarcodeExample.clear();
			mBProductSellerGoodsBarcodeExample.or().andLastUpdateDateBetween(startDate, endDate).andStatusEqualTo((byte) 1).limit(i * pagesize, pagesize);
			List<MBProductSellerGoodsBarcode> list = mBProductSellerGoodsBarcodeMapper.selectByExample(mBProductSellerGoodsBarcodeExample);
			for (MBProductSellerGoodsBarcode mBProductSellerGoodsBarcode : list) {
				ProductBarcodeList productBarcodeList = new ProductBarcodeList();
				productBarcodeList.setGoodsSn(mBProductSellerGoodsBarcode.getProductSysCode());
				productBarcodeList.setColorCode(mBProductSellerGoodsBarcode.getSaleAttr1ValueCode());
				productBarcodeList.setSizeCode(mBProductSellerGoodsBarcode.getSaleAttr2ValueCode());
				productBarcodeList.setBarcode(mBProductSellerGoodsBarcode.getIntlCode());
				productBarcodeList.setCustumCode(mBProductSellerGoodsBarcode.getBarcodeSysCode());
				//13位码废弃，使用11位码加00代替
				productBarcodeList.setSkuSn(mBProductSellerGoodsBarcode.getBarcodeSysCode()+"00");
				productBarcodeList.setColorName(mBProductSellerGoodsBarcode.getSaleAttr1Value());
				productBarcodeList.setSizeName(mBProductSellerGoodsBarcode.getSaleAttr2Value());
				productBarcodeList.setLastUpdateTime(mBProductSellerGoodsBarcode.getLastUpdateDate());
				productBarcodeList.setSellerCode(mBProductSellerGoodsBarcode.getSellerCode());
				productBarcodeList.setBusinessBarcode(mBProductSellerGoodsBarcode.getBarcodeCode());
				// 更新11位码缓存
				SyncRediesSku(productBarcodeList);
				try {
						List<ProductBarcodeList> COList = new ArrayList<ProductBarcodeList>();
						ProductBarcodeListExample productBarcodeListExample = new ProductBarcodeListExample();
						productBarcodeListExample.or().andCustumCodeEqualTo(mBProductSellerGoodsBarcode.getBarcodeSysCode());
						COList = productBarcodeListMapper.selectByExample(productBarcodeListExample);
						if (COList.size() == 1) {
							//如果openshop已存在数据，不更新13位码
							productBarcodeList.setSkuSn(null);
							productBarcodeListMapper.updateByExampleSelective(productBarcodeList,productBarcodeListExample);
						} else if (COList.size() == 0) {
							productBarcodeListMapper.insertSelective(productBarcodeList);
						}
						OSuccessNum++;
						logger.info(mBProductSellerGoodsBarcode.getBarcodeSysCode()+ ",成功！");
				} catch (Exception e) {
					OFailNum++;
					logger.error(mBProductSellerGoodsBarcode.getBarcodeSysCode()+ ",失败:", e);
				}
			}
		}
	} catch (Exception e) {
		logger.error("error:", e);
	} 
		logger.info("调度中心操作：ProductSellerGoodsBarcode同步完成！需同步数据"+count+",同步成功"+OSuccessNum+"失败"+OFailNum);
	}

	private void SyMBProductSellerGoodsGallery(Date startDate,Date endDate) {
		int OSuccessNum=0;
		int OFailNum=0;
		int count=0;
		try {
		MBProductSellerGoodsGalleryExample mBProductSellerGoodsGalleryExample = new MBProductSellerGoodsGalleryExample();
		MBProductSellerGoodsGalleryExample.Criteria criteria = mBProductSellerGoodsGalleryExample.or();
		mBProductSellerGoodsGalleryExample.setDistinct(true);
		criteria.andLastUpdateDateBetween(startDate, endDate);
		criteria.andStatusEqualTo((byte) 1);
		int pagesize = 5000;
		count = mBProductSellerGoodsGalleryMapper.countByExample(mBProductSellerGoodsGalleryExample);
		for (int i = 0; i < (double) count / pagesize; i++) {
			mBProductSellerGoodsGalleryExample.clear();
			mBProductSellerGoodsGalleryExample.or().andLastUpdateDateBetween(startDate, endDate).andStatusEqualTo((byte) 1).limit(i * pagesize, pagesize);
			List<MBProductSellerGoodsGallery> list = mBProductSellerGoodsGalleryMapper.selectByExample(mBProductSellerGoodsGalleryExample);
			for (MBProductSellerGoodsGallery mBProductSellerGoodsGallery : list) {
				ProductGoodsGallery productGoodsGallery = new ProductGoodsGallery();
				productGoodsGallery.setGoodsSn(mBProductSellerGoodsGallery.getProductSysCode());
				productGoodsGallery.setImgUrl(mBProductSellerGoodsGallery.getImageUrl());
				productGoodsGallery.setImgDesc(mBProductSellerGoodsGallery.getImageDesc());
				productGoodsGallery.setImgOriginal(mBProductSellerGoodsGallery.getImageUrl());
				productGoodsGallery.setThumbUrl(mBProductSellerGoodsGallery.getImageUrl());
				productGoodsGallery.setColorCode(mBProductSellerGoodsGallery.getColorCode());
				productGoodsGallery.setType(mBProductSellerGoodsGallery.getImageType().intValue());
				productGoodsGallery.setSortOrder((int) mBProductSellerGoodsGallery.getSort());
				productGoodsGallery.setSellerCode( mBProductSellerGoodsGallery.getSellerCode());
				try {
						List<ProductGoodsGallery> COList = new ArrayList<ProductGoodsGallery>();
						ProductGoodsGalleryExample productGoodsGalleryExample = new ProductGoodsGalleryExample();
						productGoodsGalleryExample.or().andGoodsSnEqualTo(mBProductSellerGoodsGallery.getProductSysCode())
								.andImgUrlEqualTo(mBProductSellerGoodsGallery.getImageUrl());
						COList = productGoodsGalleryMapper.selectByExample(productGoodsGalleryExample);
						if (COList.size() == 1) {
							productGoodsGalleryMapper.updateByExampleSelective(productGoodsGallery,productGoodsGalleryExample);
						} else if (COList.size() == 0) {
							productGoodsGalleryMapper.insertSelective(productGoodsGallery);
						}
						OSuccessNum++;
						logger.info(mBProductSellerGoodsGallery.getProductSysCode()+","+mBProductSellerGoodsGallery.getImageUrl()+ ",成功！");
				} catch (Exception e) {
					OFailNum++;
					logger.error(mBProductSellerGoodsGallery.getProductSysCode()+","+mBProductSellerGoodsGallery.getImageUrl()+ ",失败:", e);
				}
			}
		}
	} catch (Exception e) {
		logger.error("error:", e);
	} 
		logger.info("调度中心操作：ProductSellerGoodsGallery同步完成！需同步数据"+count+",同步成功"+OSuccessNum+"失败"+OFailNum);
	}

	private void SyMBProductLibBrand(Date startDate,Date endDate) {
		int OSuccessNum=0;
		int OFailNum=0;
		int count=0;
		try{
		MBProductLibBrandExample mBProductLibBrandExample = new MBProductLibBrandExample();
		MBProductLibBrandExample.Criteria criteria = mBProductLibBrandExample.or();
		criteria.andLastUpdateDateBetween(startDate, endDate);
		criteria.andStatusEqualTo((byte) 1);
		List<MBProductLibBrand> list = mBProductLibBrandMapper.selectByExample(mBProductLibBrandExample);
		count=list.size();
		for (MBProductLibBrand mBProductLibBrand : list) {
			ProductLibBrandWithBLOBs productLibBrand = new ProductLibBrandWithBLOBs();
			productLibBrand.setBrandCode(mBProductLibBrand.getBrandCode());
			productLibBrand.setBrandName(mBProductLibBrand.getBrandName());
			productLibBrand.setBrandLogo(mBProductLibBrand.getImageUrl());
			productLibBrand.setBrandDesc(mBProductLibBrand.getRemark());
			productLibBrand.setSortOrder(mBProductLibBrand.getSort().byteValue());
			productLibBrand.setIsShow(1);
			productLibBrand.setCname(mBProductLibBrand.getCname());
			productLibBrand.setEname(mBProductLibBrand.getEname());
			productLibBrand.setFirstLetter(mBProductLibBrand.getFirstLetter());
			productLibBrand.setCreateTime(mBProductLibBrand.getCreateDate());
			productLibBrand.setUpdateTime(mBProductLibBrand.getLastUpdateDate());
			try {
					List<ProductLibBrand> COList = new ArrayList<ProductLibBrand>();
					ProductLibBrandExample productLibBrandExample = new ProductLibBrandExample();
					productLibBrandExample.or().andBrandCodeEqualTo(mBProductLibBrand.getBrandCode());
					COList = productLibBrandMapper.selectByExample(productLibBrandExample);
					if (COList.size() == 1) {
						productLibBrandMapper.updateByExampleSelective(productLibBrand, productLibBrandExample);
					} else if (COList.size() == 0) {
						productLibBrandMapper.insertSelective(productLibBrand);
					}
					OSuccessNum++;
					logger.info(mBProductLibBrand.getBrandCode()+ ",成功！");
			} catch (Exception e) {
				OFailNum++;
				logger.error(mBProductLibBrand.getBrandCode()+ ",失败:", e);
			}
		}
		} catch (Exception e) {
			logger.error("error:", e);
		}
		logger.info("调度中心操作：ProductLibBrand同步完成！需同步数据"+count+",同步成功"+OSuccessNum+"失败"+OFailNum);
	}

	private void SyMBProductAttr(Date startDate,Date endDate) {
		int OSuccessNum=0;
		int OFailNum=0;
		int count=0;
		try{
		MBProductSellerGoodsAttrVOExample mBProductSellerGoodsAttrVOExample = new MBProductSellerGoodsAttrVOExample();
		MBProductSellerGoodsAttrVOExample.Criteria criteria = mBProductSellerGoodsAttrVOExample.or();
		criteria.andLastUpdateDateBetween(startDate, endDate);
		int pagesize = 5000;
		count = mBProductAttrSelectMapper.countByExample(mBProductSellerGoodsAttrVOExample);
		for (int i = 0; i < (double) count / pagesize; i++) {
			mBProductSellerGoodsAttrVOExample.clear();
			mBProductSellerGoodsAttrVOExample.or().andLastUpdateDateBetween(startDate, endDate).limit(i * pagesize, pagesize);
			List<MBProductSellerGoodsAttrVO> list = mBProductAttrSelectMapper.selectByExample(mBProductSellerGoodsAttrVOExample);
			for (MBProductSellerGoodsAttrVO mBProductAttr : list) {
				ProductAttrValues productAttrValues = new ProductAttrValues();
				productAttrValues.setGoodsSn(mBProductAttr.getProductSysCode());
				productAttrValues.setAttrKeyId(0);
				productAttrValues.setAttrKeyName(mBProductAttr.getAttrKeyAlias());
				productAttrValues.setAttrValueId(0);
				productAttrValues.setLastUpdateTime( Integer.parseInt(String.valueOf(mBProductAttr.getLastUpdateDate().getTime()/1000)));
				if (mBProductAttr.getFormType() == 1||mBProductAttr.getFormType() == 4) {
					productAttrValues.setAttrValueName(mBProductAttr.getValue());
				} else {
					productAttrValues.setAttrValueName(mBProductAttr.getAttrValue());
				}
				try {
						List<ProductAttrValues> COList = new ArrayList<ProductAttrValues>();
						ProductAttrValuesExample productAttrValuesExample = new ProductAttrValuesExample();
						if(mBProductAttr.getAttrKeyAlias().equals("材质成分")){
							List<String> nameList=new ArrayList<String>();
							nameList.add("材质");
							nameList.add("材质成分");
							productAttrValuesExample.or().andGoodsSnEqualTo(mBProductAttr.getProductSysCode()).andAttrKeyNameIn(nameList);
							COList = productAttrValuesMapper.selectByExample(productAttrValuesExample);
							if (COList.size() == 1) {
								productAttrValuesMapper.updateByExampleSelective(productAttrValues,productAttrValuesExample);
							} else if (COList.size() == 0) {
								productAttrValuesMapper.insertSelective(productAttrValues);
							}
						}else{
							productAttrValuesExample.or().andGoodsSnEqualTo(mBProductAttr.getProductSysCode()).andAttrKeyNameEqualTo(mBProductAttr.getAttrKeyName());
							COList = productAttrValuesMapper.selectByExample(productAttrValuesExample);
							if (COList.size() == 1) {
								productAttrValuesMapper.updateByExampleSelective(productAttrValues,productAttrValuesExample);
							} else if (COList.size() == 0) {
								productAttrValuesMapper.insertSelective(productAttrValues);
							}
						}
						OSuccessNum++;
						logger.info(mBProductAttr.getProductSysCode()+","+mBProductAttr.getAttrKeyName()+","+productAttrValues.getAttrValueName()+",成功！");
				} catch (Exception e) {
					OFailNum++;
					logger.error(mBProductAttr.getProductSysCode()+","+mBProductAttr.getAttrKeyName()+","+productAttrValues.getAttrValueName()+",失败:", e);
				}
			}
		}
		} catch (Exception e) {
			logger.error("error:", e);
		} 
		logger.info("调度中心操作：ProductAttr同步完成！需同步数据"+count+",同步成功"+OSuccessNum+"失败"+OFailNum);
	}
	
	
	private void SyByGoodsSn(String goodsSn){
		logger.info("根据goodsSn同步数据：goodsSn="+goodsSn);
		/**
		 * 同步ProductSellerGoods
		 */
		try {
			MBProductSellerGoodsSelectExample mBProductSellerGoodsSelectExample = new MBProductSellerGoodsSelectExample();
			mBProductSellerGoodsSelectExample.or().andProductSysCodeEqualTo(goodsSn);
			List<MBProductSellerGoodsSelect> productSellerGoodsList = mBProductSellerGoodsSelectMapper.selectByExampleWithBLOBs(mBProductSellerGoodsSelectExample);
			for (MBProductSellerGoodsSelect mBProductSellerGoods : productSellerGoodsList) {
				ProductGoodsWithBLOBs productGoods = new ProductGoodsWithBLOBs();
				productGoods.setGoodsSn(mBProductSellerGoods.getProductSysCode());
				productGoods.setGoodsName(mBProductSellerGoods.getProductName());
				productGoods.setCatId(mBProductLibCategoryMapper.selectByPrimaryKey(mBProductSellerGoods.getCategoryId()).getRelCateId());
				productGoods.setBrandCode(mBProductSellerGoods.getBrandCode());
				productGoods.setMarketPrice(mBProductSellerGoods.getMarketPrice());
				productGoods.setKeywords(mBProductSellerGoods.getKeywords());
				productGoods.setGoodsBrief(mBProductSellerGoods.getProductBrief());
				productGoods.setGoodsDesc(mBProductSellerGoods.getProductRemark());
				productGoods.setOriginalImg(mBProductSellerGoods.getProductUrl());
				productGoods.setGoodsImg(mBProductSellerGoods.getProductUrl());
				productGoods.setGoodsThumb(mBProductSellerGoods.getProductUrl());
				productGoods.setAddTime(mBProductSellerGoods.getCreateDate());
				productGoods.setIsDelete(mBProductSellerGoods.getStatus() == 1 ? (byte) 0 : (byte) 1);
				productGoods.setVedioUrl(mBProductSellerGoods.getProductVideo());
				productGoods.setCostPrice(mBProductSellerGoods.getCostPrice());
				productGoods.setCardPicture(mBProductSellerGoods.getModelCard());
				productGoods.setLastUpdateTime(mBProductSellerGoods.getLastUpdateDate());
				productGoods.setSalePoint(mBProductSellerGoods.getSellPoint());
				productGoods.setSizeTable(mBProductSellerGoods.getSizeTable());
				productGoods.setProtectPrice(mBProductSellerGoods.getProtectPrice());
				productGoods.setSizePicture(mBProductSellerGoods.getSizePicture());
				productGoods.setOriginalBrand(mBProductSellerGoods.getBrandName());
				productGoods.setSellerCode(mBProductSellerGoods.getSellerCode());
				productGoods.setSalesMode(mBProductSellerGoods.getSalesMode());
				// 更新6位码缓存
				SyncRediesGoodsSn(productGoods);
				try {
					ProductGoodsWithBLOBs cproductGoods = productGoodsMapper.selectByPrimaryKey(mBProductSellerGoods.getProductSysCode());
						if (cproductGoods != null&& cproductGoods.getGoodsSn() != null) {
							ProductGoodsExample productGoodsExample = new ProductGoodsExample();
							productGoodsExample.or().andGoodsSnEqualTo(productGoods.getGoodsSn());
							productGoodsMapper.updateByExampleSelective(productGoods, productGoodsExample);
						} else {
							productGoodsMapper.insertSelective(productGoods);
						}
						logger.info(mBProductSellerGoods.getProductSysCode()+ ",成功！");
				} catch (Exception e) {
					logger.error(mBProductSellerGoods.getProductSysCode()+ ",失败:", e);
				}
					}
			logger.info("ProductSellerGoods同步数据完成：goodsSn="+goodsSn);
			/**
			 * 同步productsellergoodsbarcode
			 */
			MBProductSellerGoodsBarcodeExample mBProductSellerGoodsBarcodeExample = new MBProductSellerGoodsBarcodeExample();
			mBProductSellerGoodsBarcodeExample.or().andProductSysCodeEqualTo(goodsSn);
			List<MBProductSellerGoodsBarcode> productSellerGoodsBarcodeList = mBProductSellerGoodsBarcodeMapper.selectByExample(mBProductSellerGoodsBarcodeExample);
			for (MBProductSellerGoodsBarcode mBProductSellerGoodsBarcode : productSellerGoodsBarcodeList) {
				ProductBarcodeList productBarcodeList = new ProductBarcodeList();
				productBarcodeList.setGoodsSn(mBProductSellerGoodsBarcode.getProductSysCode());
				productBarcodeList.setColorCode(mBProductSellerGoodsBarcode.getSaleAttr1ValueCode());
				productBarcodeList.setSizeCode(mBProductSellerGoodsBarcode.getSaleAttr2ValueCode());
				productBarcodeList.setBarcode(mBProductSellerGoodsBarcode.getIntlCode());
				productBarcodeList.setCustumCode(mBProductSellerGoodsBarcode.getBarcodeSysCode());
				//13位码废弃，使用11位码加00代替
				productBarcodeList.setSkuSn(mBProductSellerGoodsBarcode.getBarcodeSysCode()+"00");
				productBarcodeList.setColorName(mBProductSellerGoodsBarcode.getSaleAttr1Value());
				productBarcodeList.setSizeName(mBProductSellerGoodsBarcode.getSaleAttr2Value());
				productBarcodeList.setLastUpdateTime(mBProductSellerGoodsBarcode.getLastUpdateDate());
				productBarcodeList.setSellerCode(mBProductSellerGoodsBarcode.getSellerCode());
				productBarcodeList.setBusinessBarcode(mBProductSellerGoodsBarcode.getBarcodeCode());
				// 更新11位码缓存
				SyncRediesSku(productBarcodeList);
				try {
						List<ProductBarcodeList> COList = new ArrayList<ProductBarcodeList>();
						ProductBarcodeListExample productBarcodeListExample = new ProductBarcodeListExample();
						productBarcodeListExample.or().andCustumCodeEqualTo(mBProductSellerGoodsBarcode.getBarcodeSysCode());
						COList = productBarcodeListMapper.selectByExample(productBarcodeListExample);
						if (COList.size() == 1) {
							//如果openshop已存在数据，不更新13位码
							productBarcodeList.setSkuSn(null);
							productBarcodeListMapper.updateByExampleSelective(productBarcodeList,productBarcodeListExample);
						} else if (COList.size() == 0) {
							productBarcodeListMapper.insertSelective(productBarcodeList);
						}
						logger.info(mBProductSellerGoodsBarcode.getBarcodeSysCode()+ ",成功！");
				} catch (Exception e) {
					logger.error(mBProductSellerGoodsBarcode.getBarcodeSysCode()+ ",失败:", e);
				}
			}
			logger.info("ProductSellerGoodsBarcode同步数据完成：goodsSn="+goodsSn);
			/**
			 * 同步ProductSellerGoodsGallery
			 */
			MBProductSellerGoodsGalleryExample mBProductSellerGoodsGalleryExample = new MBProductSellerGoodsGalleryExample();
			mBProductSellerGoodsGalleryExample.or().andProductSysCodeEqualTo(goodsSn);
			List<MBProductSellerGoodsGallery> productSellerGoodsGalleryList = mBProductSellerGoodsGalleryMapper.selectByExample(mBProductSellerGoodsGalleryExample);
			for (MBProductSellerGoodsGallery mBProductSellerGoodsGallery : productSellerGoodsGalleryList) {
				ProductGoodsGallery productGoodsGallery = new ProductGoodsGallery();
				productGoodsGallery.setGoodsSn(mBProductSellerGoodsGallery.getProductSysCode());
				productGoodsGallery.setImgUrl(mBProductSellerGoodsGallery.getImageUrl());
				productGoodsGallery.setImgDesc(mBProductSellerGoodsGallery.getImageDesc());
				productGoodsGallery.setImgOriginal(mBProductSellerGoodsGallery.getImageUrl());
				productGoodsGallery.setThumbUrl(mBProductSellerGoodsGallery.getImageUrl());
				productGoodsGallery.setColorCode(mBProductSellerGoodsGallery.getColorCode());
				productGoodsGallery.setType(mBProductSellerGoodsGallery.getImageType().intValue());
				productGoodsGallery.setSortOrder((int) mBProductSellerGoodsGallery.getSort());
				productGoodsGallery.setSellerCode(mBProductSellerGoodsGallery.getSellerCode());
				try {
					List<ProductGoodsGallery> COList = new ArrayList<ProductGoodsGallery>();
					ProductGoodsGalleryExample productGoodsGalleryExample = new ProductGoodsGalleryExample();
					productGoodsGalleryExample.or().andGoodsSnEqualTo(mBProductSellerGoodsGallery.getProductSysCode()).andImgUrlEqualTo(mBProductSellerGoodsGallery.getImageUrl());
					COList = productGoodsGalleryMapper.selectByExample(productGoodsGalleryExample);
					if (COList.size() == 1) {
						productGoodsGalleryMapper.updateByExampleSelective(productGoodsGallery,productGoodsGalleryExample);
					} else if (COList.size() == 0) {
						productGoodsGalleryMapper.insertSelective(productGoodsGallery);
					}
					logger.info(mBProductSellerGoodsGallery.getProductSysCode()+","+mBProductSellerGoodsGallery.getImageUrl()+ ",成功！");
				} catch (Exception e) {
					logger.error(mBProductSellerGoodsGallery.getProductSysCode()+","+mBProductSellerGoodsGallery.getImageUrl()+ ",失败:", e);
				}
			}
			logger.info("ProductSellerGoodsGallery同步数据完成：goodsSn="+goodsSn);
			/**
			 * ProductSellerGoodsAttr
			 */
			MBProductSellerGoodsAttrVOExample mBProductSellerGoodsAttrVOExample = new MBProductSellerGoodsAttrVOExample();
			mBProductSellerGoodsAttrVOExample.or().andProductSysCodeEqualTo(goodsSn);
			List<MBProductSellerGoodsAttrVO> productSellerGoodsAttrList = mBProductAttrSelectMapper.selectByExample(mBProductSellerGoodsAttrVOExample);
			for (MBProductSellerGoodsAttrVO mBProductAttr : productSellerGoodsAttrList) {
				ProductAttrValues productAttrValues = new ProductAttrValues();
				productAttrValues.setGoodsSn(mBProductAttr.getProductSysCode());
				productAttrValues.setAttrKeyId(0);
				productAttrValues.setAttrKeyName(mBProductAttr.getAttrKeyAlias());
				productAttrValues.setAttrValueId(0);
				productAttrValues.setLastUpdateTime( Integer.parseInt(String.valueOf(mBProductAttr.getLastUpdateDate().getTime()/1000)));
				if (mBProductAttr.getFormType() == 1||mBProductAttr.getFormType() == 4) {
					productAttrValues.setAttrValueName(mBProductAttr.getValue());
				} else {
					productAttrValues.setAttrValueName(mBProductAttr.getAttrValue());
				}
				try {
					List<ProductAttrValues> COList = new ArrayList<ProductAttrValues>();
					ProductAttrValuesExample productAttrValuesExample = new ProductAttrValuesExample();
					if(mBProductAttr.getAttrKeyAlias().equals("材质成分")){
						List<String> nameList=new ArrayList<String>();
						nameList.add("材质");
						nameList.add("材质成分");
						productAttrValuesExample.or().andGoodsSnEqualTo(mBProductAttr.getProductSysCode()).andAttrKeyNameIn(nameList);
						COList = productAttrValuesMapper.selectByExample(productAttrValuesExample);
						if (COList.size() == 1) {
							productAttrValuesMapper.updateByExampleSelective(productAttrValues,productAttrValuesExample);
						} else if (COList.size() == 0) {
							productAttrValuesMapper.insertSelective(productAttrValues);
						}
					}else{
						productAttrValuesExample.or().andGoodsSnEqualTo(mBProductAttr.getProductSysCode()).andAttrKeyNameEqualTo(mBProductAttr.getAttrKeyName());
						COList = productAttrValuesMapper.selectByExample(productAttrValuesExample);
						if (COList.size() == 1) {
							productAttrValuesMapper.updateByExampleSelective(productAttrValues,productAttrValuesExample);
						} else if (COList.size() == 0) {
							productAttrValuesMapper.insertSelective(productAttrValues);
						}
					}
					logger.info(mBProductAttr.getProductSysCode()+","+mBProductAttr.getAttrKeyName()+","+productAttrValues.getAttrValueName()+",成功！");
				} catch (Exception e) {
					logger.error(mBProductAttr.getProductSysCode()+","+mBProductAttr.getAttrKeyName()+","+productAttrValues.getAttrValueName()+",失败:", e);
				}
			}
			logger.info("ProductAttr同步数据完成：goodsSn="+goodsSn);
			logger.info("根据goodsSn同步商品数据完成：goodsSn="+goodsSn);
		} catch (Exception e) {
			logger.error("根据goodsSn同步商品数据异常：goodsSn="+goodsSn,e);
		}
}
}
