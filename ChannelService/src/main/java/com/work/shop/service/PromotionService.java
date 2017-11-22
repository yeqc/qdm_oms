package com.work.shop.service;

import java.util.List;

import com.work.shop.bean.GiftsGoodsList;
import com.work.shop.bean.GroupGoods;
import com.work.shop.bean.GroupGoodsList;
import com.work.shop.bean.PromotionsInfo;
import com.work.shop.bean.PromotionsLimitMoney;
import com.work.shop.bean.PromotionsLimitMoneyGoods;
import com.work.shop.bean.PromotionsLimitSn;
import com.work.shop.bean.PromotionsListGoods;
import com.work.shop.bean.PromotionsListInfor;
import com.work.shop.bean.PromotionsLog;
import com.work.shop.vo.GroupGoodsListSearchVO;
import com.work.shop.vo.GroupGoodsVO;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.PromotionInfoSearchVO;
import com.work.shop.vo.PromotionVO;
import com.work.shop.vo.PromotionsLimitMoneyVO;

public interface PromotionService {

	public JsonResult searchPromotionsInfo(PromotionInfoSearchVO searchVO);
	
	public JsonResult searchGroupGoods(GroupGoodsVO searchVO);

	public JsonResult searchGroupGoodsList(GroupGoodsListSearchVO searchVO);

	public JsonResult deletePromotionsInfo(List<Integer> ids, PromotionsLog promotionsLog);
	
	public JsonResult deleteGroupGoods(List<String> groupCodeList);

	public JsonResult deleteGroupGoodsList(List<Integer> ids);

	public JsonResult activatePromotionsInfo(List<Integer> ids, PromotionsLog promotionsLog);
	
	public JsonResult unactivatePromotionsInfo(List<Integer> ids, PromotionsLog promotionsLog);

	public JsonResult addPromotion(PromotionVO promotionVO, PromotionsLog promotionsLog);

	public JsonResult savePromotion(PromotionVO promotionVO, PromotionsLog promotionsLog);

	public PromotionsInfo searchPromotionsInfoById(Integer id);

	public List<PromotionsLimitMoney> searchPromotionsLimitMoneyByPromCode(String promCode);
	
	public GroupGoods searchGroupGoodsByGroupCode(String groupCode);

	public List<GroupGoodsList> searchGroupGoodsListByGroupCode(String groupCode);

	public List<PromotionsLimitSn> searchPromotionsLimitSnByPromCode(String promCode);

	public JsonResult addGroupGoods(PromotionVO promotionVO);

	public JsonResult saveGroupGoods(PromotionVO promotionVO);
	
	public JsonResult uplaodGroupGoods(List<GroupGoodsVO> groupGoodsVOs);

	public PromotionsListInfor searchPromotionsListInforByPromCode(String promCode);

	public List<PromotionsListGoods> searchPromotionsListGoodsByPromCode(String promCode);

	public List<GiftsGoodsList> searchGiftsGoodsListByPromCode(String promCode);
	
	public List<PromotionsLimitMoneyGoods> searchPromotionsLimitMoneyGoodsByPromCode(String promCode);
	
	public List<PromotionsLimitMoneyVO> searchPromotionsLimitMoneyVOByPromCode(String promCode);


}
