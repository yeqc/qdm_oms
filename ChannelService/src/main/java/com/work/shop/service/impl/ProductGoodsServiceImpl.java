package com.work.shop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.work.shop.bean.ChannelApiLog;
import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExample;
import com.work.shop.bean.ProductGoodsVo;
import com.work.shop.bean.bgcontentdb.BGproductBarcodeList;
import com.work.shop.bean.bgcontentdb.BGproductBarcodeListExample;
import com.work.shop.bean.bgcontentdb.BGproductBarcodeListExample.Criteria;
import com.work.shop.bean.bgcontentdb.BGproductGoods;
import com.work.shop.bean.bgcontentdb.BGproductGoodsExample;
import com.work.shop.bean.bgcontentdb.BGproductGoodsWithBLOBs;
import com.work.shop.dao.BgGoodsPropertyMapper;
import com.work.shop.dao.ChannelApiLogMapper;
import com.work.shop.dao.ChannelGoodsMapper;
import com.work.shop.dao.bgcontentdb.BGproductBarcodeListMapper;
import com.work.shop.dao.bgcontentdb.BGproductGoodsMapper;
import com.work.shop.service.ProductGoodsService;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TimeUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Paging;

@Service("productGoodsService")
public class ProductGoodsServiceImpl implements ProductGoodsService {
	
	 private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "bgGoodsPropertyMapper")
	private BgGoodsPropertyMapper goodsPropertyMapper;
	
	@Resource(name="bGproductGoodsMapper")
	private BGproductGoodsMapper productGoodsMapper;
	
	@Resource(name="channelGoodsMapper")
	private ChannelGoodsMapper channelGoodsMapper;
	
	@Resource(name="channelApiLogMapper")
	private ChannelApiLogMapper channelApiLogMapper;
	
	@Resource(name = "bGproductBarcodeListMapper")
	private BGproductBarcodeListMapper productBarcodeListMapper;
	
	public void synProductGoodsInfo(String channelCode,String shopCode){
		
		List<BGproductGoodsWithBLOBs> list = new ArrayList<BGproductGoodsWithBLOBs>();
		BGproductGoodsExample example = new BGproductGoodsExample();
		
		int page = 0;
		int start = 0;
		int pageSize = 1000;
		int totalSize = productGoodsMapper.countByExample(example); //查出所有记录的总数
		int totalPages = (totalSize % pageSize == 0? totalSize/pageSize : totalSize/pageSize + 1);
		for(int i=0;i<totalPages;i++){
			StringBuffer sb =new StringBuffer("");
			if(!StringUtil.isNotNull(shopCode)){ //渠道不为空
				sb.append("渠道code为空！");
				ChannelApiLog channelApiLog = new ChannelApiLog();
				channelApiLog.setChannelCode(channelCode);
				channelApiLog.setShopCode(shopCode);
				channelApiLog.setMethodName("渠道商品生成");
				channelApiLog.setParamInfo("channelCode:"+channelCode+"ShopCode:"+shopCode);
				channelApiLog.setIpAddress("");
				channelApiLog.setUser("");
				channelApiLog.setRequestTime(TimeUtil.parseString2Date(TimeUtil.getDate()));
				if("".equals(sb.toString())){
					channelApiLog.setReturnCode("0"); //成功
					channelApiLog.setReturnMessage("渠道商品生成成功！");
					
				}else{
					channelApiLog.setReturnCode("1"); //失败
					channelApiLog.setReturnMessage("编号："+sb.toString()+"同步失败！");
				
				}
				channelApiLogMapper.insert(channelApiLog); //日志记录	  
				break;
			}
			BGproductGoodsExample productExample = new BGproductGoodsExample();
			com.work.shop.bean.bgcontentdb.BGproductGoodsExample.Criteria criteria = productExample.or();
			criteria.limit(start,pageSize);
			list = productGoodsMapper.selectByExampleWithBLOBs(productExample); //分页查询导入商品
            page = page+1;
			start = (page)*pageSize;  //继续读取下一页	
		    if(list == null || list.size()==0){
		    	System.out.println("start:"+start+"第"+(page+1)+"页");
		    	continue; 
		    }
			for(int z=0;z<list.size();z++){
				if(list.get(z)==null){
					continue;
				}
				ChannelGoodsExample channelGoodsExample = new ChannelGoodsExample();
				com.work.shop.bean.ChannelGoodsExample.Criteria channelGoodsCriteria = channelGoodsExample.or();
				channelGoodsCriteria.andChannelCodeEqualTo(shopCode);
				if(!StringUtil.isNotNull(list.get(z).getGoodsSn())){ //商品编号为空继续下次处理
					continue;
				}
				channelGoodsCriteria.andGoodsSnEqualTo(list.get(z).getGoodsSn());
				List<ChannelGoods> listChannelGoods = channelGoodsMapper.selectByExample(channelGoodsExample);
				ChannelGoods channelGoods = new ChannelGoods();
			try{
				if(listChannelGoods != null && listChannelGoods.size()>0){  //已经存在的数据做修改操作
				//	channelGoods = listChannelGoods.get(0);
//					Long addTime = TimeUtil.parseDateToNumeric(new Date());
//					channelGoods.setChannelCode(listChannelGoods.get(0).getChannelCode());
//					channelGoods.setGoodsSn(listChannelGoods.get(0).getGoodsSn());
					channelGoods.setId(listChannelGoods.get(0).getId());
					channelGoods.setAddTime(listChannelGoods.get(0).getAddTime());
					channelGoods.setPresellOrder((short)0);
					if(list.get(z).getAllowSearch() != null){
						channelGoods.setAllowSearch(list.get(z).getAllowSearch()==1?1:0);
					}else{
						channelGoods.setAllowSearch(0);
					}
					
					if(list.get(z).getBestOrder() != null){
					channelGoods.setBestOrder(list.get(z).getBestOrder().shortValue());
					}else{
					channelGoods.setBestOrder((short)0);	
					}
					channelGoods.setCardPicture(list.get(z).getCardPicture());
					channelGoods.setChannelPrice(list.get(z).getMarketPrice());
					if(list.get(z).getGoodsName() != null){
						channelGoods.setGoodsName(list.get(z).getGoodsName());
					}else{
						channelGoods.setGoodsName("");
					}
					channelGoods.setGoodsSn(list.get(z).getGoodsSn());
					if(list.get(z).getGoodsTitle()==null){
						channelGoods.setGoodsTitle("");
					}else{
						channelGoods.setGoodsTitle(list.get(z).getGoodsTitle());
					}
					
					channelGoods.setHotNumber(0);
					if(list.get(z).getHotOrder() != null){
					channelGoods.setHotOrder(list.get(z).getHotOrder().shortValue());	
					}else{
						channelGoods.setHotOrder((short)0);		
					}
					
					if(list.get(z).getIsBest() != null){
						channelGoods.setIsBest(list.get(z).getIsBest().byteValue());
					}else{
						channelGoods.setIsBest((byte) 0);
					}
					
					if(list.get(z).getIsHot() != null){
						channelGoods.setIsHot(list.get(z).getIsHot().byteValue());
					}else{
						channelGoods.setIsHot((byte)0);
					}
					
					if(list.get(z).getIsNew() != null){
						 channelGoods.setIsNew(list.get(z).getIsNew().byteValue());
					}else{
						channelGoods.setIsNew((byte)0);
					}
					
					if(list.get(z).getIsOnSell() != null){
						channelGoods.setIsOnSell(list.get(z).getIsOnSell().byteValue());	
					}else{
						channelGoods.setIsOnSell((byte)0);
					}
					if(list.get(z).getIsOutlets() != null){
						channelGoods.setIsOutlet(list.get(z).getIsOutlets().byteValue());
	
					}else{
						channelGoods.setIsOutlet((byte)0);
					}
					channelGoods.setIsPresale((byte)0);
					channelGoods.setIsPresell(0);
					channelGoods.setIsSecondSale((byte)0);
					channelGoods.setIsUpdate((byte)0);
					channelGoods.setKeywords(list.get(z).getKeywords());
					channelGoods.setLastUpdate(TimeUtil.parseString2Date(TimeUtil.getDate())); //修改时间
					Long lastUpdateTime = TimeUtil.parseDateToNumeric(new Date());
					channelGoods.setLastUpdateTime(lastUpdateTime.intValue());
					if(list.get(z).getNewOrder() != null){
						channelGoods.setNewOrder(list.get(z).getNewOrder().shortValue());
					}else{
						channelGoods.setNewOrder((short)0);
					}
					channelGoods.setPlatformPrice(list.get(z).getMarketPrice());
					if(list.get(z).getSalePoint() != null && list.get(z).getSalePoint().length()>250){
						channelGoods.setSalePoint(list.get(z).getSalePoint().substring(0,250));
					}else{
						channelGoods.setSalePoint(list.get(z).getSalePoint());
					}
					
					channelGoods.setSizePicture(list.get(z).getSizePicture());
					if(list.get(z).getSortOrder() != null){
					    channelGoods.setSortOrder(list.get(z).getSortOrder().shortValue());
						}else{
						channelGoods.setSortOrder((short)0);
						}
					if(list.get(z).getWarnNumber() !=null){
							channelGoods.setWarnNumber(list.get(z).getWarnNumber());	
						}else{
							channelGoods.setWarnNumber(0);
						}
					channelGoods.setGoodsDesc(list.get(z).getGoodsDesc());
				    
					channelGoodsMapper.updateByPrimaryKeySelective(channelGoods);
				}else{              //新增操作
					channelGoods.setChannelCode(shopCode);
					Long addTime = TimeUtil.parseDateToNumeric(new Date());
					if(addTime != null){
						channelGoods.setAddTime(Integer.parseInt(addTime.toString()));
					}
					channelGoods.setPresellOrder((short)0);
					if(list.get(z).getAllowSearch() != null){
						channelGoods.setAllowSearch(list.get(z).getAllowSearch()==1?1:0);
					}else{
						channelGoods.setAllowSearch(0);
					}
					if(list.get(z).getBestOrder() != null){
					channelGoods.setBestOrder(list.get(z).getBestOrder().shortValue());
					}else{
						channelGoods.setBestOrder((short)0);	
						}
					channelGoods.setCardPicture(list.get(z).getCardPicture());
					channelGoods.setChannelPrice(list.get(z).getMarketPrice());
					if(list.get(z).getGoodsName() != null){
						channelGoods.setGoodsName(list.get(z).getGoodsName());
					}else{
						channelGoods.setGoodsName("");
					}
					
					channelGoods.setGoodsSn(list.get(z).getGoodsSn());
					if(list.get(z).getGoodsTitle()==null){
						channelGoods.setGoodsTitle("");
					}else{
						channelGoods.setGoodsTitle(list.get(z).getGoodsTitle());
					}
					channelGoods.setHotNumber(0);
					
					if(list.get(z).getHotOrder() != null){
						channelGoods.setHotOrder(list.get(z).getHotOrder().shortValue());	
						}else{
							channelGoods.setHotOrder((short)0);		
						}
						
						if(list.get(z).getIsBest() != null){
							channelGoods.setIsBest(list.get(z).getIsBest().byteValue());
						}else{
							channelGoods.setIsBest((byte) 0);
						}
						
						if(list.get(z).getIsHot() != null){
							channelGoods.setIsHot(list.get(z).getIsHot().byteValue());
						}else{
							channelGoods.setIsHot((byte)0);
						}
						
						if(list.get(z).getIsNew() != null){
							 channelGoods.setIsNew(list.get(z).getIsNew().byteValue());
						}else{
							channelGoods.setIsNew((byte)0);
						}
						
						if(list.get(z).getIsOnSell() != null){
							channelGoods.setIsOnSell(list.get(z).getIsOnSell().byteValue());	
						}else{
							channelGoods.setIsOnSell((byte)0);
						}
						if(list.get(z).getIsOutlets() != null){
							channelGoods.setIsOutlet(list.get(z).getIsOutlets().byteValue());
		
						}else{
							channelGoods.setIsOutlet((byte)0);
						}
					channelGoods.setIsPresale((byte)0);
					channelGoods.setIsPresell(0);
					channelGoods.setIsSecondSale((byte)0);
					channelGoods.setIsUpdate((byte)0);
					channelGoods.setKeywords(list.get(z).getKeywords());
//					channelGoods.setLastUpdate(TimeUtil.parseString2Date(TimeUtil.getDate())); //修改时间
//					Long lastUpdateTime = TimeUtil.parseDateToNumeric(new Date());
					channelGoods.setLastUpdateTime(0);
					if(list.get(z).getNewOrder() != null){
						channelGoods.setNewOrder(list.get(z).getNewOrder().shortValue());
					}else{
						channelGoods.setNewOrder((short)0);
					}
					channelGoods.setPlatformPrice(list.get(z).getMarketPrice());
					if(list.get(z).getSalePoint() != null && list.get(z).getSalePoint().length()>250){
						channelGoods.setSalePoint(list.get(z).getSalePoint().substring(0,250));
					}else{
						channelGoods.setSalePoint(list.get(z).getSalePoint());
					}
					channelGoods.setSizePicture(list.get(z).getSizePicture());
					if(list.get(z).getSortOrder() != null){
				    channelGoods.setSortOrder(list.get(z).getSortOrder().shortValue());
					}else{
					channelGoods.setSortOrder((short)0);
					}
					if(list.get(z).getWarnNumber() !=null){
						channelGoods.setWarnNumber(list.get(z).getWarnNumber());	
					}else{
						channelGoods.setWarnNumber(0);
					}
					
					channelGoods.setGoodsDesc(list.get(z).getGoodsDesc());
					channelGoodsMapper.insert(channelGoods);
				
				}
			 }catch(Exception e){
				 System.out.println(list.get(z).getIsBest()+"  goodsSn = " + list.get(z).getGoodsSn());
			    	e.printStackTrace();
                    sb.append("["+list.get(z).getGoodsSn()+"],");
			    }
				
			}
			/**********每1000条记录一次日志*********/
			ChannelApiLog channelApiLog = new ChannelApiLog();
			channelApiLog.setChannelCode(channelCode);
			channelApiLog.setShopCode(shopCode);
			channelApiLog.setMethodName("渠道商品生成");
			channelApiLog.setParamInfo("记录数从第"+(page-1)*pageSize+"条，到"+(page)*pageSize+"条,参数：channelCode:"+channelCode+"ShopCode:"+shopCode);
			channelApiLog.setIpAddress("");
			channelApiLog.setUser("");
			channelApiLog.setRequestTime(TimeUtil.parseString2Date(TimeUtil.getDate()));
			if("".equals(sb.toString())){
				channelApiLog.setReturnCode("0"); //成功
				channelApiLog.setReturnMessage("渠道商品生成成功！");
				
			}else{
				channelApiLog.setReturnCode("1"); //失败
				channelApiLog.setReturnMessage("编号："+sb.toString()+"同步失败！");
			
			}
			channelApiLogMapper.insert(channelApiLog); //日志记录	  
		}
		
		
	
	}

	@Override
	public Paging getProductGoodsList(BGproductGoods model, PageHelper helper) {
		// TODO Auto-generated method stub
		//ProductGoodsExample
		BGproductGoodsExample example = new BGproductGoodsExample();
		com.work.shop.bean.bgcontentdb.BGproductGoodsExample.Criteria criteria = example.or();
		criteria.limit(helper.getStart(), helper.getLimit());
	
		Map<String,Object> map = new HashMap<String,Object>();
		if (StringUtil.isNotEmpty(model.getGoodsSn())) {
			criteria.andGoodsSnEqualTo(model.getGoodsSn());
			map.put("goodsSn", model.getGoodsSn());
		}
		
		if (StringUtil.isNotEmpty(model.getGoodsName())) {
			criteria.andGoodsNameLike("%"+model.getGoodsName()+"%");
			map.put("goodsName",model.getGoodsName());
		}
		/*if (StringUtil.isNotEmpty(model.getChannelCode())) {
			criteria.andChannelCodeEqualTo(model.getChannelCode());
			map.put("channelCode",model.getChannelCode());
		}*/

		map.put("start", helper.getStart());
		map.put("limit",  helper.getLimit());
	
		List<ProductGoodsVo> list = goodsPropertyMapper.selectProductGoods(map);
		
		for(ProductGoodsVo phannelGoodsVo : list){
			if (StringUtil.isNotEmpty(phannelGoodsVo.getGoodsSn())) {
			BGproductBarcodeListExample productBarcodeListExample = new BGproductBarcodeListExample();
			Criteria pbCriteria = productBarcodeListExample.or();
			pbCriteria.andGoodsSnEqualTo(phannelGoodsVo.getGoodsSn());
			List <BGproductBarcodeList> pbList = productBarcodeListMapper.selectByExample(productBarcodeListExample);
			phannelGoodsVo.setBarcodeChild(pbList);
		  }
		}

		Paging paging = new Paging(productGoodsMapper.countByExample(example), list);
		return paging;
	}
	
	@Override
	public BGproductGoods selectByGoodsSn(String goodsSn) {
		BGproductGoodsExample example = new BGproductGoodsExample();
		com.work.shop.bean.bgcontentdb.BGproductGoodsExample.Criteria criteria = example
				.or();
		if (StringUtil.isNotNull(goodsSn)) {
			criteria.andGoodsSnEqualTo(goodsSn);
		}
		BGproductGoods pg = productGoodsMapper.selectByPrimaryKey(goodsSn);
		return pg;
	}

}
