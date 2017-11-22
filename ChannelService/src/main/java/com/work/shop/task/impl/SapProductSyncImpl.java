package com.work.shop.task.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.work.shop.bean.bgcontentdb.BGproductBarcodeList;
import com.work.shop.bean.bgcontentdb.BGproductBarcodeListExample;
import com.work.shop.bean.bgcontentdb.BGproductGoodsWithBLOBs;
import com.work.shop.bean.mbproduct.ProductLibAttrValue;
import com.work.shop.bean.mbproduct.ProductLibAttrValueExample;
import com.work.shop.bean.mbproduct.SapProdCls;
import com.work.shop.bean.mbproduct.SapProdClsExample;
import com.work.shop.bean.mbproduct.SapProduct;
import com.work.shop.bean.mbproduct.SapProductExample;
import com.work.shop.dao.bgcontentdb.BGproductBarcodeListMapper;
import com.work.shop.dao.bgcontentdb.BGproductGoodsMapper;
import com.work.shop.dao.mbproduct.ProductLibAttrValueMapper;
import com.work.shop.dao.mbproduct.SapProdClsMapper;
import com.work.shop.dao.mbproduct.SapProductMapper;
import com.work.shop.redis.RedisClient;
import com.work.shop.task.SapProductSync;
import com.work.shop.util.DateTimeUtils;

@Component("sapProductSyncImpl")
public class SapProductSyncImpl implements SapProductSync {
	
	@Resource(name = "bGproductBarcodeListMapper")
	private BGproductBarcodeListMapper productBarcodeListMapper;
	
	@Resource(name = "bGproductGoodsMapper")
	private BGproductGoodsMapper productGoodsMapper;
	
	@Resource(name = "sapProdClsMapper")
	private SapProdClsMapper sapProdClsMapper;
	
	@Resource(name = "sapProductMapper")
	private SapProductMapper sapProductMapper;
	
	@Resource(name = "productLibAttrValueMapper")
	private ProductLibAttrValueMapper productLibAttrValueMapper;
	
	@Resource(name = "redisClient")
	private RedisClient redisClient;
	
	private Logger logger = Logger.getLogger(SapProductSyncImpl.class);
	
	@Override
	public void sProductSync(Map<String, String> para) {
		String startTime = para.get("startTime"); 
		Date startDate = null;
		int interval = 0;
		String goodsSn=null;
		try {
			if (para.get("interval") != null && para.get("interval").trim().length()>0) {
				interval = Integer.parseInt(para.get("interval"));
			}
			if(para.get("goodsSn") != null && para.get("goodsSn").trim().length()>0){
				goodsSn=para.get("goodsSn");
			}
			Date nowDate=new Date();
			if (startTime != null || goodsSn != null) {
				startDate = DateTimeUtils.parseStr(startTime);
			} else {
				Date redisTime = redisClient.getPojo("nowDate"); // 获取最后一次执行的时间段结束时间
				startDate = DateTimeUtils.dateAdd(DateTimeUtils.INTERVAL_MINUTE,new Date(), interval); // 获取本次执行的时间段开始时间
				if(redisTime == null){    // 当缓存中不存在，则将本次时间段结束时间记录缓存
					redisClient.setPojo("nowDate", nowDate);
				}else{
					int result = redisTime.compareTo(startDate); // 如果当前时间段开始时间，大于缓存中时间结束时间，则是当前时间段开始时间设为置为缓存中时间
					if(result < 0){
						startDate = redisTime;
					}	
					redisClient.setPojo("nowDate", nowDate); // 本次时间段结束时间计入缓存
				}	
			}
			
			if(null == goodsSn){
				logger.info("Sap商品基础数据同步开始：同步最后修改时间["+ DateTimeUtils.format(startDate,DateTimeUtils.YYYY_MM_DD_HH_mm_ss)+ "]至["+ DateTimeUtils.format(nowDate,DateTimeUtils.YYYY_MM_DD_HH_mm_ss)+"]的商品数据！");
			}
			syncProductGoods(startDate,nowDate,goodsSn);          // 更新bgcontentdb.product_goods
			syncProductBarcodeList(startDate,nowDate,goodsSn);    // 更新bgcontentdb.product_barcode_list
			
			logger.info("Sap同步调度任务：正常执行结束！");
			
		} catch (Exception e) {
			logger.error("Sap同步调度任务：执行异常！"+e.toString(), e);
		}
	
	}

	// 更新6位码缓存
	private void SyncRediesGoodsSn(BGproductGoodsWithBLOBs productGoods ){
	}
	
	// 更新11位码缓存
	private void SyncRediesSku(BGproductBarcodeList productBarcodeList){
	}
	
/*
 * 如果，goodsSn不为空，那么时间参数无效
 */
	private void syncProductGoods(Date startDate, Date nowDate,String goodsSn) {
		logger.info("Sap同步bgcontentdb库，product_goods表,参数列表输出-->startDate:"+startDate+",nowDate:"+nowDate+",goodsSn:"+goodsSn);
		int SuccessNum = 0;     // 执行成功条数
		int FailedNum = 0;      // 执行失败条数
		int limit = 2;    // 分页条数
		int start = 0;
		int countNum = 0;
		int count = 0;   
		try{
			if(goodsSn != null && goodsSn.trim().length()>0){
				SapProdClsExample sapProdClsExample = new SapProdClsExample();
				SapProdClsExample.Criteria criteria = sapProdClsExample.or();
				criteria.andProdClsNumEqualTo(goodsSn);
				count =  sapProdClsMapper.countByExample(sapProdClsExample);
				if(count>0){
					List<SapProdCls> sapProdClsList = sapProdClsMapper.selectByExample(sapProdClsExample);
					BGproductGoodsWithBLOBs productGoods = new BGproductGoodsWithBLOBs();
					productGoods.setGoodsSn(sapProdClsList.get(0).getProdClsNum());
					productGoods.setGoodsName(sapProdClsList.get(0).getProductName());
					productGoods.setBrandCode(sapProdClsList.get(0).getBrandCode());
					productGoods.setSellerCode("MB");
					productGoods.setSalesMode((byte)1);
					productGoods.setMarketPrice(sapProdClsList.get(0).getOnBrandPrc());
					productGoods.setSalePoint(sapProdClsList.get(0).getAdvantage());
					productGoods.setAddTime(new Date());
					productGoods.setLastUpdateTime(sapProdClsList.get(0).getLastUpdateDate());
					//写入缓存
					SyncRediesGoodsSn(productGoods);
					try{
						BGproductGoodsWithBLOBs productGoodsLocat =  productGoodsMapper.selectByPrimaryKey(goodsSn);
						if(null == productGoodsLocat){
							productGoodsMapper.insertSelective(productGoods);
							SuccessNum ++;
						}
					}catch(Exception e){
						FailedNum ++;
					}
				}
				
				logger.info("Sap指定goodsSn同步bgcontentdb库,->product_goods同步完成:goodsSn->"+goodsSn+",sap查询数目:"+count+",成功条数:"+SuccessNum+",失败条数:"+FailedNum);
			}else{
				// 查询时间段内所有更新过的活动状态的商品数据
				SapProdClsExample sapProdClsExample = new SapProdClsExample();
				SapProdClsExample.Criteria criteria = sapProdClsExample.or();
				criteria.andLastUpdateDateBetween(startDate, nowDate).andProdStatusEqualTo("A");
				count = sapProdClsMapper.countByExample(sapProdClsExample);
				countNum = count;
				
				while(count-limit>0){
					sapProdClsExample.clear();	
					sapProdClsExample.or().andLastUpdateDateBetween(startDate, nowDate).andProdStatusEqualTo("A").limit(start, limit);;
					List<SapProdCls> list = sapProdClsMapper.selectByExample(sapProdClsExample);
					for(SapProdCls sapProdCls : list){		
						BGproductGoodsWithBLOBs productGoods = new BGproductGoodsWithBLOBs();
						productGoods.setGoodsSn(sapProdCls.getProdClsNum());
						productGoods.setGoodsName(sapProdCls.getProductName());
						productGoods.setBrandCode(sapProdCls.getBrandCode());
						productGoods.setSellerCode("MB");
						productGoods.setSalesMode((byte)1);
						productGoods.setMarketPrice(sapProdCls.getOnBrandPrc());
						productGoods.setSalePoint(sapProdCls.getAdvantage());
						productGoods.setAddTime(new Date());
						productGoods.setLastUpdateTime(sapProdCls.getLastUpdateDate());
						//写入缓存
						SyncRediesGoodsSn(productGoods);
						// 如果本地库中不存在，那么做插入操作，如果存在则不做更新
						try{
							BGproductGoodsWithBLOBs productGoodsLocat =  productGoodsMapper.selectByPrimaryKey(sapProdCls.getProdClsNum());
							if(null == productGoodsLocat){
								productGoodsMapper.insertSelective(productGoods);
								SuccessNum++;
								logger.debug("SapProdCls同步bgcontentdb库,product_goods,goodSn:"+sapProdCls.getProdClsNum()+ ",成功！");	
							}
						}catch(Exception e){
							FailedNum++;
							logger.error("SapProdCls同步bgcontentdb库,product_goods,goodSn:"+sapProdCls.getProdClsNum()+ ",失败！");	
						}
					}	
					start+=limit;
					count=count-limit;
				}
				sapProdClsExample.clear();
				sapProdClsExample.or().andLastUpdateDateBetween(startDate, nowDate).andProdStatusEqualTo("A").limit(start, limit);
				List<SapProdCls> list = sapProdClsMapper.selectByExample(sapProdClsExample); 
				if(null != list && list.size()>0){
					for(SapProdCls sapProdCls : list){		
						BGproductGoodsWithBLOBs productGoods = new BGproductGoodsWithBLOBs();
						productGoods.setGoodsSn(sapProdCls.getProdClsNum());
						productGoods.setGoodsName(sapProdCls.getProductName());
						productGoods.setBrandCode(sapProdCls.getBrandCode());
						productGoods.setSellerCode("MB");
						productGoods.setSalesMode((byte)1);
						productGoods.setMarketPrice(sapProdCls.getOnBrandPrc());
						productGoods.setSalePoint(sapProdCls.getAdvantage());
						productGoods.setAddTime(new Date());
						productGoods.setLastUpdateTime(sapProdCls.getLastUpdateDate());
						//更新缓存
						SyncRediesGoodsSn(productGoods);
						// 如果本地库中不存在，那么做插入操作，如果存在则不做更新
						try{
							BGproductGoodsWithBLOBs productGoodsLocat =  productGoodsMapper.selectByPrimaryKey(sapProdCls.getProdClsNum());
							if(null == productGoodsLocat){
								productGoodsMapper.insertSelective(productGoods);
								SuccessNum++;
								logger.debug("SapProdCls同步bgcontentdb库,product_goods,goodSn:"+sapProdCls.getProdClsNum()+ ",成功！");	
							}
						}catch(Exception e){
							FailedNum++;
							logger.error("SapProdCls同步bgcontentdb库,product_goods,goodSn:"+sapProdCls.getProdClsNum()+ ",失败！");	
						}
					}
				}
				logger.info("Sap根据时间段同步bgcontentdb库,->product_goods同步完成，需要同步数量:"+countNum+",成功条数:"+SuccessNum+",失败条数:"+FailedNum);
			}
			
		}catch(Exception e){
			logger.error("Sap同步bgcontentdb库,product_goods异常：",e);
		}
	}

	/*
	 * 如果，goodsSn不为空，那么时间参数无效
	 */
	private void syncProductBarcodeList(Date startDate, Date nowDate, String goodsSn) {
		logger.info("Sap同步bgcontentdb库,product_barcode_list表,参数列表输出-->startDate:"+startDate+",nowDate:"+nowDate+",goodsSn:"+goodsSn);
		int SuccessNum = 0;     // 执行成功条数
		int FailedNum = 0;      // 执行失败条数
		int limit = 2;    // 分页条数
		int start = 0;
		int count = 0; 
		int countNum = 0;
		try{
			if(goodsSn != null && goodsSn.trim().length()>0){
				SapProductExample sapProductExample = new SapProductExample();
				SapProductExample.Criteria criteria = sapProductExample.or();
				criteria.andProdClsNumEqualTo(goodsSn);
				count = sapProductMapper.countByExample(sapProductExample);
				countNum = count;
				List<SapProduct> list = sapProductMapper.selectByExample(sapProductExample);
				if(countNum>0){
					for(SapProduct sapProduct : list){
						BGproductBarcodeList productBarcodeList = new BGproductBarcodeList();
						productBarcodeList.setGoodsSn(sapProduct.getProdClsNum());
						productBarcodeList.setCustumCode(sapProduct.getProdNum());
						productBarcodeList.setBarcode(sapProduct.getIntnlBc());
						productBarcodeList.setColorCode(sapProduct.getColorCode());
						productBarcodeList.setColorName(sapProduct.getColorName());
						productBarcodeList.setSizeName(sapProduct.getSpecName());
						productBarcodeList.setSkuSn(sapProduct.getProdNum()+"00");
						productBarcodeList.setSellerCode("MB");
						productBarcodeList.setLastUpdateTime(sapProduct.getLastUpdateDate());
						productBarcodeList.setBusinessBarcode(sapProduct.getProdNum());
						try{
							ProductLibAttrValueExample productLibAttrValueExample = new ProductLibAttrValueExample();
							ProductLibAttrValueExample.Criteria productLibAttrValuecriteria = productLibAttrValueExample.or();
							productLibAttrValuecriteria.andAttrValueEqualTo(sapProduct.getSpecName());
							List<ProductLibAttrValue> attrValueList = productLibAttrValueMapper.selectByExample(productLibAttrValueExample);
							if(null != attrValueList && attrValueList.size() > 0){
								productBarcodeList.setSizeCode(attrValueList.get(0).getAttrValueCode());
							}else{
								logger.debug("Sku:"+sapProduct.getProdNum()+"没有找到对应的尺码！！");
							}
							//更新缓存
							SyncRediesSku(productBarcodeList);
							List<BGproductBarcodeList> productBarcodeListLocal = new ArrayList<BGproductBarcodeList>();
							BGproductBarcodeListExample productBarcodeListExample = new BGproductBarcodeListExample();
							productBarcodeListExample.or().andCustumCodeEqualTo(sapProduct.getProdNum());
							productBarcodeListLocal = productBarcodeListMapper.selectByExample(productBarcodeListExample);
							if(null == productBarcodeListLocal || productBarcodeListLocal.size()==0){
								productBarcodeListMapper.insertSelective(productBarcodeList);
								SuccessNum++;
							}
							
						}catch(Exception e){
							FailedNum++;
						}
					}
				}
				logger.info("Sap指定goodsSn同步bgcontentdb库,goodsSn:"+goodsSn+"-->product_barcode_list同步完成，需要同步数量:"+countNum+",成功条数:"+SuccessNum+",失败条数:"+FailedNum);
			}else{
				SapProductExample sapProductExample = new SapProductExample();
				SapProductExample.Criteria criteria = sapProductExample.or();
				
				criteria.andLastUpdateDateBetween(startDate, nowDate).andProdStatusEqualTo("A");
				count = sapProductMapper.countByExample(sapProductExample);
				countNum = count;
				
				while(count-limit>0){
					sapProductExample.clear();
					sapProductExample.or().andLastUpdateDateBetween(startDate, nowDate).andProdStatusEqualTo("A").limit(start, limit);
					List<SapProduct> list = sapProductMapper.selectByExample(sapProductExample);
					
					for(SapProduct sapProduct : list){
						BGproductBarcodeList productBarcodeList = new BGproductBarcodeList();
						productBarcodeList.setGoodsSn(sapProduct.getProdClsNum());
						productBarcodeList.setCustumCode(sapProduct.getProdNum());
						productBarcodeList.setBarcode(sapProduct.getIntnlBc());
						productBarcodeList.setColorCode(sapProduct.getColorCode());
						productBarcodeList.setColorName(sapProduct.getColorName());
						productBarcodeList.setSizeName(sapProduct.getSpecName());
						productBarcodeList.setSellerCode("MB");
						productBarcodeList.setSkuSn(sapProduct.getProdNum()+"00");
						productBarcodeList.setLastUpdateTime(sapProduct.getLastUpdateDate());
						productBarcodeList.setBusinessBarcode(sapProduct.getProdNum());
						try{
							ProductLibAttrValueExample productLibAttrValueExample = new ProductLibAttrValueExample();
							ProductLibAttrValueExample.Criteria productLibAttrValuecriteria = productLibAttrValueExample.or();
							productLibAttrValuecriteria.andAttrValueEqualTo(sapProduct.getSpecName());
							List<ProductLibAttrValue> attrValueList = productLibAttrValueMapper.selectByExample(productLibAttrValueExample);
							if(null != attrValueList && attrValueList.size() > 0){
								productBarcodeList.setSizeCode(attrValueList.get(0).getAttrValueCode());
							}else{
								logger.debug("Sku:"+sapProduct.getProdNum()+"没有找到对应的尺码！！");
							}
							//更新缓存
							SyncRediesSku(productBarcodeList);
							List<BGproductBarcodeList> productBarcodeListLocal = new ArrayList<BGproductBarcodeList>();
							BGproductBarcodeListExample productBarcodeListExample = new BGproductBarcodeListExample();
							productBarcodeListExample.or().andCustumCodeEqualTo(sapProduct.getProdNum());
							productBarcodeListLocal = productBarcodeListMapper.selectByExample(productBarcodeListExample);
							if(null == productBarcodeListLocal || productBarcodeListLocal.size()==0){
								productBarcodeListMapper.insertSelective(productBarcodeList);
								SuccessNum++;
								logger.debug("SapProduct同步bgcontentdb库,product_barcode_list,sku"+sapProduct.getProdNum()+"成功！");
							}
							
						}catch(Exception e){
							FailedNum++;
							logger.error("SapProduct同步bgcontentdb库,product_barcode_list,sku"+sapProduct.getProdNum()+ ",失败:", e);
						}
					}
					start+=limit;
					count=count-limit;
				}
				sapProductExample.clear();
				sapProductExample.or().andLastUpdateDateBetween(startDate, nowDate).andProdStatusEqualTo("A").limit(start, limit);
				List<SapProduct> list = sapProductMapper.selectByExample(sapProductExample);
				if(null != list && list.size()>0){
					for(SapProduct sapProduct : list){
						BGproductBarcodeList productBarcodeList = new BGproductBarcodeList();
						productBarcodeList.setGoodsSn(sapProduct.getProdClsNum());
						productBarcodeList.setCustumCode(sapProduct.getProdNum());
						productBarcodeList.setBarcode(sapProduct.getIntnlBc());
						productBarcodeList.setColorCode(sapProduct.getColorCode());
						productBarcodeList.setColorName(sapProduct.getColorName());
						productBarcodeList.setSizeName(sapProduct.getSpecName());
						productBarcodeList.setSellerCode("MB");
						productBarcodeList.setSkuSn(sapProduct.getProdNum()+"00");
						productBarcodeList.setLastUpdateTime(sapProduct.getLastUpdateDate());
						productBarcodeList.setBusinessBarcode(sapProduct.getProdNum());
						try{
							ProductLibAttrValueExample productLibAttrValueExample = new ProductLibAttrValueExample();
							ProductLibAttrValueExample.Criteria productLibAttrValuecriteria = productLibAttrValueExample.or();
							productLibAttrValuecriteria.andAttrValueEqualTo(sapProduct.getSpecName());
							List<ProductLibAttrValue> attrValueList = productLibAttrValueMapper.selectByExample(productLibAttrValueExample);
							if(null != attrValueList && attrValueList.size() > 0){
								productBarcodeList.setSizeCode(attrValueList.get(0).getAttrValueCode());
							}else{
								logger.debug("Sku:"+sapProduct.getProdNum()+"没有找到对应的尺码！！");
							}
							//更新缓存
							SyncRediesSku(productBarcodeList);
							List<BGproductBarcodeList> productBarcodeListLocal = new ArrayList<BGproductBarcodeList>();
							BGproductBarcodeListExample productBarcodeListExample = new BGproductBarcodeListExample();
							productBarcodeListExample.or().andCustumCodeEqualTo(sapProduct.getProdNum());
							productBarcodeListLocal = productBarcodeListMapper.selectByExample(productBarcodeListExample);
							if(null == productBarcodeListLocal || productBarcodeListLocal.size()==0){
								productBarcodeListMapper.insertSelective(productBarcodeList);
								SuccessNum++;
								logger.debug("SapProduct同步bgcontentdb库,product_barcode_list,sku"+sapProduct.getProdNum()+"成功！");
							}
							
						}catch(Exception e){
							FailedNum++;
							logger.error("SapProduct同步bgcontentdb库,product_barcode_list,sku"+sapProduct.getProdNum()+ ",失败:", e);
						}
					}
				}
				
				logger.info("Sap指定时间段内同步bgcontentdb库,->product_barcode_list同步完成，需要同步数量:"+countNum+",成功条数:"+SuccessNum+",失败条数:"+FailedNum);
			}
		}catch(Exception e){
			logger.error("Sap同步bgcontentdb库,product_barcode_list异常："+e.toString(),e);
		}
	}
}
