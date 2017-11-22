package com.work.shop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.work.shop.bean.ChannelShop;
import com.work.shop.bean.ChannelShopExample;
import com.work.shop.bean.GiftsGoodsList;
import com.work.shop.bean.GiftsGoodsListExample;
import com.work.shop.bean.GroupGoods;
import com.work.shop.bean.GroupGoodsExample;
import com.work.shop.bean.GroupGoodsList;
import com.work.shop.bean.GroupGoodsListExample;
import com.work.shop.bean.PromotionsInfo;
import com.work.shop.bean.PromotionsInfoExample;
import com.work.shop.bean.PromotionsLimitMoney;
import com.work.shop.bean.PromotionsLimitMoneyExample;
import com.work.shop.bean.PromotionsLimitMoneyGoods;
import com.work.shop.bean.PromotionsLimitMoneyGoodsExample;
import com.work.shop.bean.PromotionsLimitSn;
import com.work.shop.bean.PromotionsLimitSnExample;
import com.work.shop.bean.PromotionsListGoods;
import com.work.shop.bean.PromotionsListGoodsExample;
import com.work.shop.bean.PromotionsListInfor;
import com.work.shop.bean.PromotionsListInforExample;
import com.work.shop.bean.PromotionsLog;
import com.work.shop.dao.BgChannelDbTableMapper;
import com.work.shop.dao.ChannelShopMapper;
import com.work.shop.dao.GiftsGoodsListMapper;
import com.work.shop.dao.GroupGoodsListMapper;
import com.work.shop.dao.GroupGoodsMapper;
import com.work.shop.dao.PromotionsInfoMapper;
import com.work.shop.dao.PromotionsLimitMoneyGoodsMapper;
import com.work.shop.dao.PromotionsLimitMoneyMapper;
import com.work.shop.dao.PromotionsLimitSnMapper;
import com.work.shop.dao.PromotionsListGoodsMapper;
import com.work.shop.dao.PromotionsListInforMapper;
import com.work.shop.dao.PromotionsLogMapper;
import com.work.shop.service.PromotionService;
import com.work.shop.util.Constants;
import com.work.shop.util.DateTimeUtils;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TimeUtil;
import com.work.shop.vo.GroupGoodsListSearchVO;
import com.work.shop.vo.GroupGoodsVO;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.PromotionInfoSearchVO;
import com.work.shop.vo.PromotionVO;
import com.work.shop.vo.PromotionsLimitMoneyVO;

@Service("promotionServiceImpl")
public class PromotionServiceImpl implements PromotionService {

	private final static Logger log = Logger.getLogger(PromotionServiceImpl.class);

	/** @Fields promotionsInfoMapper */
	@Resource(name = "promotionsInfoMapper")
	private PromotionsInfoMapper promotionsInfoMapper;

	/** @Fields promotionsLimitMoneyMapper */
	@Resource(name = "promotionsLimitMoneyMapper")
	private PromotionsLimitMoneyMapper promotionsLimitMoneyMapper;

	/** @Fields groupGoodsMapper */
	@Resource(name = "groupGoodsMapper")
	private GroupGoodsMapper groupGoodsMapper;

	/** @Fields groupGoodsListMapper */
	@Resource(name = "groupGoodsListMapper")
	private GroupGoodsListMapper groupGoodsListMapper;

	/** @Fields promotionsLimitSnMapper */
	@Resource(name = "promotionsLimitSnMapper")
	private PromotionsLimitSnMapper promotionsLimitSnMapper;

	/** @Fields promotionsListInforMapper */
	@Resource(name = "promotionsListInforMapper")
	private PromotionsListInforMapper promotionsListInforMapper;

	/** @Fields promotionsListGoodsMapper */
	@Resource(name = "promotionsListGoodsMapper")
	private PromotionsListGoodsMapper promotionsListGoodsMapper;

	/** @Fields giftsGoodsListMapper */
	@Resource(name = "giftsGoodsListMapper")
	private GiftsGoodsListMapper giftsGoodsListMapper;

	/** @Fields channelShopMapper */
	@Resource(name = "channelShopMapper")
	private ChannelShopMapper channelShopMapper;

	/** @Fields promotionsLogMapper */
	@Resource(name = "promotionsLogMapper")
	private PromotionsLogMapper promotionsLogMapper;
	/** @Fields promotionsLimitMoneyMapper */
	@Resource
	private PromotionsLimitMoneyGoodsMapper promotionsLimitMoneyGoodsMapper;
	@Resource
	private BgChannelDbTableMapper bgChannelDbTableMapper;

	@Override
	public JsonResult searchPromotionsInfo(PromotionInfoSearchVO searchVO) {
		if (searchVO == null) {
			return null;
		}
		JsonResult jsonResult = new JsonResult();

		PromotionsInfoExample promotionsInfoExample = new PromotionsInfoExample();
		com.work.shop.bean.PromotionsInfoExample.Criteria criteria = promotionsInfoExample.or();
		if (StringUtils.isNotEmpty(searchVO.getPromCode())) {
			criteria.andPromCodeLike("%" + searchVO.getPromCode() + "%");
		}
		if (StringUtils.isNotEmpty(searchVO.getShopCode())) {
			criteria.andShopCodeEqualTo(searchVO.getShopCode());
		}
		if (StringUtils.isNotEmpty(searchVO.getPromTitle())) {
			criteria.andPromTitleLike("%" + searchVO.getPromTitle() + "%");
		}
		if (searchVO.getBeginTime() != null) {
			criteria.andBeginTimeGreaterThanOrEqualTo(searchVO.getBeginTime());
		}
		if (searchVO.getEndTime() != null) {
			criteria.andEndTimeLessThanOrEqualTo(searchVO.getEndTime());
		}
		if (searchVO.getPromType() != null) {
			criteria.andPromTypeEqualTo(searchVO.getPromType());
		}
		if (searchVO.getPromStatus() != null) {
			criteria.andPromStatusEqualTo(searchVO.getPromStatus());
		}

		if (searchVO.getStart() != null && searchVO.getLimit() != null) {
			criteria.limit(searchVO.getStart(), searchVO.getLimit());
		}
		jsonResult.setTotalProperty(promotionsInfoMapper.countByExample(promotionsInfoExample));
		jsonResult.setData(promotionsInfoMapper.selectByExample(promotionsInfoExample));
		jsonResult.setIsok(true);
		return jsonResult;
	}

	@Override
	public JsonResult searchGroupGoods(GroupGoodsVO searchVO) {
		if (searchVO == null) {
			return null;
		}
		JsonResult jsonResult = new JsonResult();
		GroupGoodsExample example = new GroupGoodsExample();
		com.work.shop.bean.GroupGoodsExample.Criteria criteria = example.or();
		if (StringUtils.isNotEmpty(searchVO.getGroupCode())) {
			criteria.andGroupCodeEqualTo(searchVO.getGroupCode());
		}
		if (searchVO.getStart() != null && searchVO.getLimit() != null) {
			criteria.limit(searchVO.getStart(), searchVO.getLimit());
		}
		jsonResult.setTotalProperty(groupGoodsMapper.countByExample(example));
		jsonResult.setData(groupGoodsMapper.selectByExample(example));
		jsonResult.setIsok(true);
		return jsonResult;
	}

	@Override
	public JsonResult searchGroupGoodsList(GroupGoodsListSearchVO searchVO) {
		if (searchVO == null) {
			return null;
		}
		JsonResult jsonResult = new JsonResult();
		GroupGoodsListExample groupGoodsListExample = new GroupGoodsListExample();
		com.work.shop.bean.GroupGoodsListExample.Criteria criteria = groupGoodsListExample.or();
		if (StringUtils.isNotEmpty(searchVO.getGroupCode())) {
			criteria.andGroupCodeEqualTo(searchVO.getGroupCode());
		}
		// if (searchVO.getStart() != null && searchVO.getLimit() != null) {
		// criteria.limit(searchVO.getStart(), searchVO.getLimit());
		// }
		jsonResult.setTotalProperty(groupGoodsListMapper.countByExample(groupGoodsListExample));
		jsonResult.setData(groupGoodsListMapper.selectByExample(groupGoodsListExample));
		jsonResult.setIsok(true);
		return jsonResult;
	}

	@Override
	public JsonResult deletePromotionsInfo(List<Integer> ids, PromotionsLog promotionsLog) {
		JsonResult jsonResult = new JsonResult();
		try {
			StringBuffer message = null;
			for (Integer id : ids) {
				message = new StringBuffer();
				PromotionsInfo promotionsInfo = promotionsInfoMapper.selectByPrimaryKey(id);
				String promCode = promotionsInfo.getPromCode();
				byte promType = promotionsInfo.getPromType();

				message.append("删除促销活动：" + promotionsInfo.toString()).append("\n");
				promotionsInfoMapper.deleteByPrimaryKey(id);

				// 促销信息 (满赠 ) 类型
				if (Constants.PROM_TYPE_0 == promType) {
					List<PromotionsLimitMoney> list = searchPromotionsLimitMoneyByPromCode(promCode);
					message.append("活动细则：" + list.toString()).append("\n");

					PromotionsLimitMoneyExample example = new PromotionsLimitMoneyExample();
					example.or().andPromCodeEqualTo(promCode);
					promotionsLimitMoneyMapper.deleteByExample(example);
				}

				// 促销信息 (买赠 ) 类型
				if (Constants.PROM_TYPE_2 == promType) {
					List<PromotionsLimitSn> list = searchPromotionsLimitSnByPromCode(promCode);
					message.append("活动细则：" + list.toString()).append("\n");

					PromotionsLimitSnExample example = new PromotionsLimitSnExample();
					example.or().andPromCodeEqualTo(promCode);
					promotionsLimitSnMapper.deleteByExample(example);
				}

				// 促销信息 (集合赠 ) 类型
				if (Constants.PROM_TYPE_3 == promType) {
					message.append("活动细则：").append("\n");
					PromotionsListInforExample promotionsListInforExample = new PromotionsListInforExample();
					promotionsListInforExample.or().andPromCodeEqualTo(promCode);
					List<PromotionsListInfor> listInfo = promotionsListInforMapper.selectByExample(promotionsListInforExample);
					message.append("1.集合赠基本信息：" + listInfo.toString()).append("\n");
					promotionsListInforMapper.deleteByExample(promotionsListInforExample);

					List<PromotionsListGoods> list1 = searchPromotionsListGoodsByPromCode(promCode);
					message.append("2.需购商品信息：" + list1.toString()).append("\n");
					PromotionsListGoodsExample promotionsListGoodsExample = new PromotionsListGoodsExample();
					promotionsListGoodsExample.or().andPromCodeEqualTo(promCode);
					promotionsListGoodsMapper.deleteByExample(promotionsListGoodsExample);

					List<GiftsGoodsList> list2 = searchGiftsGoodsListByPromCode(promCode);
					message.append("3.赠品信息：" + list2.toString()).append("\n");
					GiftsGoodsListExample giftsGoodsListExample = new GiftsGoodsListExample();
					giftsGoodsListExample.or().andPromCodeEqualTo(promCode);
					giftsGoodsListMapper.deleteByExample(giftsGoodsListExample);
				}
				promotionsLog.setShopCode(promotionsInfo.getShopCode());
				promotionsLog.setPromType(promotionsInfo.getPromType());
				promotionsLog.setPromCode(promotionsInfo.getPromCode());
				promotionsLog.setShopTitle(promotionsInfo.getShopTitle());
				promotionsLog.setMessage(message.toString());
				insertPromotionsLog(promotionsLog);
			}
			jsonResult.setIsok(true);
			jsonResult.setMessage("删除成功!");
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("删除失败!");
		}
		return jsonResult;
	}

	@Override
	public JsonResult deleteGroupGoods(List<String> groupCodeList) {
		JsonResult jsonResult = new JsonResult();
		try {
			GroupGoodsExample groupGoodsExample = new GroupGoodsExample();
			groupGoodsExample.or().andGroupCodeIn(groupCodeList);
			int count = groupGoodsMapper.deleteByExample(groupGoodsExample);
			if (count != groupCodeList.size()) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("删除失败!");
				return jsonResult;
			}

			GroupGoodsListExample groupGoodsListExample = new GroupGoodsListExample();
			groupGoodsListExample.or().andGroupCodeIn(groupCodeList);
			groupGoodsListMapper.deleteByExample(groupGoodsListExample);

			jsonResult.setMessage("删除成功!");
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("删除失败!");
		}
		return jsonResult;
	}

	@Override
	public JsonResult deleteGroupGoodsList(List<Integer> ids) {
		JsonResult jsonResult = new JsonResult();
		try {
			GroupGoodsListExample example = new GroupGoodsListExample();
			example.or().andIdIn(ids);
			int count = groupGoodsListMapper.deleteByExample(example);
			if (count != ids.size()) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("删除失败!");
				return jsonResult;
			}
			jsonResult.setMessage("删除成功!");
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("删除失败!");
		}
		return jsonResult;
	}

	@Override
	public JsonResult activatePromotionsInfo(List<Integer> ids, PromotionsLog promotionsLog) {
		JsonResult jsonResult = new JsonResult();
		try {
			PromotionsInfo promotionsInfo = null;
			int count = 0;
			for (Integer id : ids) {
				promotionsInfo = promotionsInfoMapper.selectByPrimaryKey(id);
				PromotionVO promotionVO = new PromotionVO();
				promotionVO.setId(String.valueOf(id));
				promotionVO.setBeginTime(TimeUtil.formatDate(promotionsInfo.getBeginTime()));
				promotionVO.setEndTime(TimeUtil.formatDate(promotionsInfo.getEndTime()));
				promotionVO.setPromType(promotionsInfo.getPromType() + "");
				promotionVO.setPromCode(promotionsInfo.getPromCode());
				promotionVO.setShopCode(promotionsInfo.getShopCode());
				jsonResult = validatePromotion(promotionVO);
				if (!jsonResult.isIsok()) {
					break;
				}
				promotionsInfo.setPromStatus(Constants.ACTIVATE_STATUS);
				promotionsInfoMapper.updateByPrimaryKeySelective(promotionsInfo);
				count++;
				promotionsLog.setShopCode(promotionsInfo.getShopCode());
				promotionsLog.setPromType(promotionsInfo.getPromType());
				promotionsLog.setPromCode(promotionsInfo.getPromCode());
				promotionsLog.setShopTitle(promotionsInfo.getShopTitle());
				promotionsLog.setMessage("激活促销活动[" + promotionsInfo.getPromCode() + "]");
				insertPromotionsLog(promotionsLog);
			}
			if (!jsonResult.isIsok()) {
				return jsonResult;
			}
			if (count != ids.size()) {
				throw new RuntimeException();
			} else {
				jsonResult.setMessage("激活成功!");
			}
		} catch (Exception e) {
			log.error("激活促销异常", e);
			jsonResult.setIsok(false);
			jsonResult.setMessage("激活促销异常" + e.getMessage());
		}
		return jsonResult;
	}

	@Override
	public JsonResult unactivatePromotionsInfo(List<Integer> ids, PromotionsLog promotionsLog) {
		JsonResult jsonResult = new JsonResult();
		try {
			PromotionsInfo promotionsInfo = null;
			int count = 0;
			for (Integer id : ids) {
				promotionsInfo = promotionsInfoMapper.selectByPrimaryKey(id);
				promotionsInfo.setPromStatus(Constants.NO_ACTIVATE_STATUS);
				promotionsInfoMapper.updateByPrimaryKeySelective(promotionsInfo);
				count++;
				promotionsLog.setShopCode(promotionsInfo.getShopCode());
				promotionsLog.setPromType(promotionsInfo.getPromType());
				promotionsLog.setPromCode(promotionsInfo.getPromCode());
				promotionsLog.setShopTitle(promotionsInfo.getShopTitle());
				promotionsLog.setMessage("失效促销活动[" + promotionsInfo.getPromCode() + "]");
				insertPromotionsLog(promotionsLog);
			}
			if (count != ids.size()) {
				throw new RuntimeException();
			} else {
				jsonResult.setMessage("失效成功!");
			}
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("删除失败!");
		}
		return jsonResult;
	}
	
	@Override
	public JsonResult addPromotion(PromotionVO promotionVO, PromotionsLog promotionsLog) {
		JsonResult jsonResult = new JsonResult();
		StringBuffer message = new StringBuffer();
		try {
//			jsonResult = validatePromotion(promotionVO);
//			if (!jsonResult.isIsok()) {
//				return jsonResult;
//			}
			String ss = promotionVO.getPromType();
			if (StringUtil.isEmpty(promotionVO.getPromType())) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("促销类型为空！");
				return jsonResult;
			}
			byte promType = Byte.valueOf(promotionVO.getPromType());
			int count = 0;
			// 促销信息 (满赠 ) 类型
			if (Constants.PROM_TYPE_0 == promType) {
				if (promotionVO.getPromotionsLimitMoneyList() != null) {
					jsonResult = checkLimitMoneyEffectiveGifts(promotionVO.getGiftsGoodsListList(),
							promotionVO.getLimitMoneyGoods(), promotionVO.getPromotionsLimitMoneyList());
					if (jsonResult != null && !jsonResult.isIsok()) {
						return jsonResult;
					}
					message.append("满赠活动细则：");
					for (PromotionsLimitMoney bean : promotionVO.getPromotionsLimitMoneyList()) {
						count = promotionsLimitMoneyMapper.insertSelective(bean);
						if (count != 1) {
							jsonResult.setIsok(false);
							jsonResult.setMessage("添加失败!");
							return jsonResult;
						}
					}
					message.append(promotionVO.getPromotionsLimitMoneyList().toString());
				}
				if (StringUtil.isListNotNull(promotionVO.getLimitMoneyGoods())) {
					message.append("满赠活动细则赠品设置：");
					for (PromotionsLimitMoneyGoods bean : promotionVO.getLimitMoneyGoods()) {
						count = promotionsLimitMoneyGoodsMapper.insertSelective(bean);
						if (count != 1) {
							jsonResult.setIsok(false);
							jsonResult.setMessage("添加失败!");
							return jsonResult;
						}
					}
					message.append(promotionVO.getPromotionsLimitMoneyList().toString());
				}
				
				if (promotionVO.getGiftsGoodsListList() != null) {
					message.append("赠品列表信息：");
					for (GiftsGoodsList bean : promotionVO.getGiftsGoodsListList()) {
						count = giftsGoodsListMapper.insertSelective(bean);
						if (count != 1) {
							jsonResult.setIsok(false);
							jsonResult.setMessage("添加失败!");
							return jsonResult;
						}
					}
					message.append(promotionVO.getGiftsGoodsListList().toString());
				}
			}

			// 促销信息 (买赠 ) 类型
			if (Constants.PROM_TYPE_2 == promType) {
				jsonResult = checkLimitSnEffectiveGifts(promotionVO.getGiftsGoodsListList(),
						promotionVO.getPromotionsLimitSnList());
				if (jsonResult != null && !jsonResult.isIsok()) {
					return jsonResult;
				}
				message.append("活动细则：");
				for (PromotionsLimitSn bean : promotionVO.getPromotionsLimitSnList()) {
					count = promotionsLimitSnMapper.insertSelective(bean);
					if (count != 1) {
						jsonResult.setIsok(false);
						jsonResult.setMessage("添加失败!");
						return jsonResult;
					}
				}
				message.append(promotionVO.getPromotionsLimitSnList().toString());
				message.append("赠品列表信息：");
				for (GiftsGoodsList bean : promotionVO.getGiftsGoodsListList()) {
					count = giftsGoodsListMapper.insertSelective(bean);
					if (count != 1) {
						jsonResult.setIsok(false);
						jsonResult.setMessage("添加失败!");
						return jsonResult;
					}
				}
				message.append(promotionVO.getGiftsGoodsListList().toString());
			}

			// 促销信息 (集合赠) 类型
			if (Constants.PROM_TYPE_3 == promType) {
				message.append("活动细则：\n");
				count = promotionsListInforMapper.insertSelective(promotionVO.getPromotionsListInfor());
				if (count != 1) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("添加失败!");
					return jsonResult;
				}
				message.append("1.集合赠基本信息：").append(promotionVO.getPromotionsListInfor().toString()).append("\n");

				if (promotionVO.getPromotionsListGoodsList() != null) {
					message.append("2.需购商品信息：");
					for (PromotionsListGoods bean : promotionVO.getPromotionsListGoodsList()) {
						count = promotionsListGoodsMapper.insertSelective(bean);
						if (count != 1) {
							jsonResult.setIsok(false);
							jsonResult.setMessage("添加失败!");
							return jsonResult;
						}
					}
					message.append(promotionVO.getPromotionsListGoodsList().toString()).append("\n");
				}

				if (promotionVO.getGiftsGoodsListList() != null) {
					message.append("3.赠品信息：");
					for (GiftsGoodsList bean : promotionVO.getGiftsGoodsListList()) {
						count = giftsGoodsListMapper.insertSelective(bean);
						if (count != 1) {
							jsonResult.setIsok(false);
							jsonResult.setMessage("添加失败!");
							return jsonResult;
						}
					}
					message.append(promotionVO.getGiftsGoodsListList().toString());
				}
			}
			PromotionsInfo promotionsInfo = new PromotionsInfo();
			promotionsInfo.setPromType(promType);
			promotionsInfo.setPromCode(promotionVO.getPromCode());
			promotionsInfo.setShopCode(promotionVO.getShopCode());
			promotionsInfo.setShopTitle(getShopTitle(promotionVO.getShopCode()));
			promotionsInfo.setPromTitle(promotionVO.getPromTitle());
			promotionsInfo.setBackup(promotionVO.getBackup());
			promotionsInfo.setBeginTime(DateTimeUtils.parseStr(promotionVO.getBeginTime(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			promotionsInfo.setEndTime(DateTimeUtils.parseStr(promotionVO.getEndTime(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss));
			promotionsInfo.setAddTime(new Date());
			promotionsInfo.setPromStatus(Byte.valueOf("0"));
			promotionsLog.setShopTitle(promotionsInfo.getShopTitle());
			count = promotionsInfoMapper.insertSelective(promotionsInfo);
			if (count != 1) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("添加失败!");
				return jsonResult;
			}
			message.append("添加促销活动 ： [" + promotionsInfo.toString() + "] \n");
			jsonResult.setIsok(true);
			jsonResult.setMessage("添加成功!");
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("添加失败!");
			log.error("添加促销活动异常", e);
		}
		promotionsLog.setMessage(message.toString());
		insertPromotionsLog(promotionsLog);
		return jsonResult;
	}

	@Override
	public JsonResult savePromotion(PromotionVO promotionVO, PromotionsLog promotionsLog) {
		JsonResult jsonResult = new JsonResult();
		StringBuffer sb = new StringBuffer();
		try {
//			jsonResult = validatePromotion(promotionVO);
//			if (!jsonResult.isIsok()) {
//				return jsonResult;
//			}

			boolean modifyFlag = false;

			PromotionsInfo promotionsInfo = promotionsInfoMapper.selectByPrimaryKey(Integer.valueOf(promotionVO.getId()));

			promotionsInfo.setPromType(Byte.valueOf(promotionVO.getPromType()));
			promotionsInfo.setPromStatus(Byte.valueOf(promotionVO.getPromStatus()));
			promotionsInfo.setPromCode(promotionVO.getPromCode());
			if (!promotionsInfo.getPromTitle().equals(promotionVO.getPromTitle())) {
				sb.append("促销名称[" + promotionsInfo.getPromTitle() + "]->[" + promotionVO.getPromTitle() + "]\n");
				promotionsInfo.setPromTitle(promotionVO.getPromTitle());
				modifyFlag = true;
			}
			promotionsInfo.setShopCode(promotionVO.getShopCode());
			promotionsInfo.setShopTitle(getShopTitle(promotionVO.getShopCode()));
			promotionsInfo.setBackup(promotionVO.getBackup());
			Date beginDateTime = DateTimeUtils.parseStr(promotionVO.getBeginTime(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss);
			if (!promotionsInfo.getBeginTime().equals(beginDateTime)) {
				sb.append("促销开始时间[" + DateTimeUtils.format(promotionsInfo.getBeginTime(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss) + "]->[" + promotionVO.getBeginTime() + "]\n");
				promotionsInfo.setBeginTime(beginDateTime);
				modifyFlag = true;
			}
			Date endDateTime = DateTimeUtils.parseStr(promotionVO.getEndTime(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss);
			if (!promotionsInfo.getEndTime().equals(endDateTime)) {
				sb.append("促销结束时间[" + DateTimeUtils.format(promotionsInfo.getEndTime(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss) + "]->[" + promotionVO.getEndTime() + "]\n");
				promotionsInfo.setEndTime(endDateTime);
				modifyFlag = true;
			}

			byte promType = promotionsInfo.getPromType();
			String promCode = promotionsInfo.getPromCode();
			int count = 0;

			promotionsLog.setShopTitle(promotionsInfo.getShopTitle());
			if (modifyFlag) {
				count = promotionsInfoMapper.updateByPrimaryKey(promotionsInfo);
				if (count != 1) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("保存失败!");
					return jsonResult;
				}
			}
			// 促销信息 (满赠 ) 类型
			if (Constants.PROM_TYPE_0 == promType) {
				// 细则变更
				// check 赠品细则商品是否在赠品列表中中含有
				jsonResult = checkLimitMoneyEffectiveGifts(promotionVO.getGiftsGoodsListList(),
						promotionVO.getLimitMoneyGoods(), promotionVO.getPromotionsLimitMoneyList());
				if (jsonResult != null && !jsonResult.isIsok()) {
					return jsonResult;
				}
				List<PromotionsLimitMoney> oldList = searchPromotionsLimitMoneyByPromCode(promCode);
				List<PromotionsLimitMoneyGoods> oldGoodsList = searchPromotionsLimitMoneyGoodsByPromCode(promCode);
				List<GiftsGoodsList> oldGiftGoodsList = searchGiftsGoodsListByPromCode(promCode);
				compareUpdatePromotionsLimitMoney(oldList, promotionVO.getPromotionsLimitMoneyList(), sb);
				compareUpdatePromotionsLimitMoneyGoods(oldGoodsList, promotionVO.getLimitMoneyGoods(), sb);
				compareUpdateGiftsGoodsList(oldGiftGoodsList, promotionVO.getGiftsGoodsListList(), sb);
			}
			// 促销信息 (买赠 ) 类型
			if (Constants.PROM_TYPE_2 == promType) {
				// 细则变更
				// check 赠品细则商品是否在赠品列表中中含有
				jsonResult = checkLimitSnEffectiveGifts(promotionVO.getGiftsGoodsListList(),
						promotionVO.getPromotionsLimitSnList());
				if (jsonResult != null && !jsonResult.isIsok()) {
					return jsonResult;
				}
				List<PromotionsLimitSn> oldList = searchPromotionsLimitSnByPromCode(promCode);
				List<GiftsGoodsList> oldGiftGoodsList = searchGiftsGoodsListByPromCode(promCode);
				compareUpdatePromotionsLimitSn(oldList, promotionVO.getPromotionsLimitSnList(), sb);
				compareUpdateGiftsGoodsList(oldGiftGoodsList, promotionVO.getGiftsGoodsListList(), sb);
			}

			// 促销信息 (集合赠) 类型
			if (Constants.PROM_TYPE_3 == promType) {
				modifyFlag = false;
				Integer id = promotionVO.getPromotionsListInfor().getId();
				PromotionsListInfor oldInfo = promotionsListInforMapper.selectByPrimaryKey(id);
				PromotionsListInfor newInfo = promotionVO.getPromotionsListInfor();
				if (!oldInfo.getGoodsCount().equals(newInfo.getGoodsCount())) {
					sb.append("需购商品数量:[" + oldInfo.getGoodsCount() + "]->[" + newInfo.getGoodsCount() + "]\n");
					modifyFlag = true;
				}
				int other = oldInfo.getLimitMoney().subtract(newInfo.getLimitMoney()).intValue();
				if (other != 0) {
					sb.append("需购商品金额:[" + oldInfo.getLimitMoney().intValue() + "]->[" + newInfo.getLimitMoney().intValue() + "]\n");
					modifyFlag = true;
				}
				if (!oldInfo.getGiftsCount().equals(newInfo.getGiftsCount())) {
					sb.append("赠品数量:[" + oldInfo.getGiftsCount() + "]->[" + newInfo.getGiftsCount() + "]\n");
					modifyFlag = true;
				}
				if (modifyFlag) {
					count = promotionsListInforMapper.updateByPrimaryKeySelective(promotionVO.getPromotionsListInfor());
					if (count != 1) {
						jsonResult.setIsok(false);
						jsonResult.setMessage("保存失败!");
						return jsonResult;
					}
				}

				List<PromotionsListGoods> oldList = searchPromotionsListGoodsByPromCode(promCode);
				Map<String, List<PromotionsListGoods>> data = getPromotionsListGoodsData(oldList, promotionVO.getPromotionsListGoodsList());
				if (StringUtil.isNotNullForList(promotionVO.getPromotionsListGoodsList())) {
					List<PromotionsListGoods> add = data.get("add");
					if (StringUtil.isNotNullForList(add)) {
						for (PromotionsListGoods addData : add) {
							count = promotionsListGoodsMapper.insertSelective(addData);
							if (count != 1) {
								jsonResult.setIsok(false);
								jsonResult.setMessage("保存失败!");
								return jsonResult;
							}
						}
						sb.append("新增需购商品细则：\n").append(add.toString()).append("\n");
					}
				}
				if (StringUtil.isNotNullForList(oldList)) {
					List<PromotionsListGoods> delete = data.get("delete");
					if (StringUtil.isNotNullForList(delete)) {
						for (PromotionsListGoods deleteData : delete) {
							count = promotionsListGoodsMapper.deleteByPrimaryKey(deleteData.getId());
							if (count != 1) {
								jsonResult.setIsok(false);
								jsonResult.setMessage("删除失败!");
								return jsonResult;
							}
						}
						sb.append("删除需购商品细则：\n").append(delete.toString()).append("\n");
					}
				}
				if (StringUtil.isNotNullForList(promotionVO.getPromotionsListGoodsList()) && StringUtil.isNotNullForList(oldList)) {
					List<PromotionsListGoods> modify = data.get("modify");
					if (StringUtil.isNotNullForList(modify)) {
						sb.append("修改需购商品细则：\n");
						for (PromotionsListGoods modifyData : modify) {
							PromotionsListGoods oldData = promotionsListGoodsMapper.selectByPrimaryKey(modifyData.getId());
							count = promotionsListGoodsMapper.updateByPrimaryKey(modifyData);
							if (count != 1) {
								jsonResult.setIsok(false);
								jsonResult.setMessage("修改失败!");
								return jsonResult;
							}
							sb.append(oldData.toString()).append(" -> ").append(modifyData.toString()).append("\n");
						}
					}
				}
				List<GiftsGoodsList> oldGiftGoodsList = searchGiftsGoodsListByPromCode(promCode);
				compareUpdateGiftsGoodsList(oldGiftGoodsList, promotionVO.getGiftsGoodsListList(), sb);
//				List<GiftsGoodsList> oldList1 = searchGiftsGoodsListByPromCode(promCode);
//				Map<String, List<GiftsGoodsList>> data1 = getGiftsGoodsListData(oldList1, promotionVO.getGiftsGoodsListList());
//				if (StringUtil.isNotNullForList(promotionVO.getGiftsGoodsListList())) {
//					List<GiftsGoodsList> add = data1.get("add");
//					if (StringUtil.isNotNullForList(add)) {
//						for (GiftsGoodsList addData : add) {
//							count = giftsGoodsListMapper.insertSelective(addData);
//							if (count != 1) {
//								jsonResult.setIsok(false);
//								jsonResult.setMessage("保存失败!");
//								return jsonResult;
//							}
//						}
//						sb.append("新增赠品细则：\n").append(add.toString()).append("\n");
//					}
//				}
//				if (StringUtil.isNotNullForList(oldList1)) {
//					List<GiftsGoodsList> delete = data1.get("delete");
//					if (StringUtil.isNotNullForList(delete)) {
//						for (GiftsGoodsList deleteData : delete) {
//							count = giftsGoodsListMapper.deleteByPrimaryKey(deleteData.getId());
//							if (count != 1) {
//								jsonResult.setIsok(false);
//								jsonResult.setMessage("删除失败!");
//								return jsonResult;
//							}
//						}
//						sb.append("删除赠品细则：\n").append(delete.toString()).append("\n");
//					}
//				}
//				if (StringUtil.isNotNullForList(promotionVO.getGiftsGoodsListList()) && StringUtil.isNotNullForList(oldList1)) {
//					List<GiftsGoodsList> modify = data1.get("modify");
//					if (StringUtil.isNotNullForList(modify)) {
//						sb.append("修改赠品细则：\n");
//						for (GiftsGoodsList modifyData : modify) {
//							GiftsGoodsList oldData = giftsGoodsListMapper.selectByPrimaryKey(modifyData.getId());
//							count = giftsGoodsListMapper.updateByPrimaryKey(modifyData);
//							if (count != 1) {
//								jsonResult.setIsok(false);
//								jsonResult.setMessage("修改失败!");
//								return jsonResult;
//							}
//							sb.append(oldData.toString()).append(" -> ").append(modifyData.toString()).append("\n");
//						}
//					}
//				}
			}
			jsonResult.setIsok(true);
			jsonResult.setMessage("保存成功!");
		} catch (Exception e) {
			log.debug("保存异常", e);
			jsonResult.setIsok(false);
			jsonResult.setMessage("保存失败!");
		}
		if (StringUtil.isNotEmpty(sb.toString())) {
			promotionsLog.setMessage("修改明细：\n" + sb.toString());
			insertPromotionsLog(promotionsLog);
		}
		return jsonResult;
	}

	/**
	 *  满赠细则设置
	 * 比对原纪录数据与新纪录数据差异
	 * 
	 * @param oldList
	 * @param newList
	 * @return
	 */
	private Map<String, List<PromotionsLimitMoney>> getPromotionsLimitMoneyData(List<PromotionsLimitMoney> oldList, List<PromotionsLimitMoney> newList) {
		Map<String, List<PromotionsLimitMoney>> data = new HashMap<String, List<PromotionsLimitMoney>>();
		if (StringUtil.isNotNullForList(oldList)) {
			List<PromotionsLimitMoney> addMoneys = new ArrayList<PromotionsLimitMoney>();
			List<PromotionsLimitMoney> modify = new ArrayList<PromotionsLimitMoney>();
			// 删除列表先copy一份原细则数据
			List<PromotionsLimitMoney> delete = new ArrayList<PromotionsLimitMoney>();
			delete.addAll(oldList);
			// 原纪录数据
			for (PromotionsLimitMoney oldObj : oldList) {
				for(PromotionsLimitMoney newObj : newList) {
					// 当初赠送商品量以外的条件都相同时
					if (newObj.equals(oldObj)) {
						if (!newObj.getGiftsGoodsCount().equals(oldObj.getGiftsGoodsCount())) {
							// 不相同时,更新细则列表
							modify.add(newObj);
						}
						// 移除原纪录数据
						delete.remove(oldObj);
					} else {
						addMoneys.add(newObj);
					}
				}
			}
			data.put("add", addMoneys);
			data.put("delete", delete);
			data.put("modify", modify);
		}
		
//		if (StringUtil.isNotNullForList(newList)) {
//			List<PromotionsLimitMoney> add = new ArrayList<PromotionsLimitMoney>();
//			for (PromotionsLimitMoney newData : newList) {
//				if (newData.getId() == null) {
//					add.add(newData);
//				}
//			}
//			data.put("add", add);
//		}
//		if (StringUtil.isNotNullForList(oldList)) {
//			List<PromotionsLimitMoney> delete = new ArrayList<PromotionsLimitMoney>();
//			if (StringUtil.isNotNullForList(newList)) {
//				for (PromotionsLimitMoney oldData : oldList) {
//					boolean flag = true;
//					for (PromotionsLimitMoney newData : newList) {
//						if (newData.getId() != null && newData.getId().equals(oldData.getId())) {
//							flag = false;
//							break;
//						}
//					}
//					if (flag) {
//						delete.add(oldData);
//					}
//				}
//			} else {
//				delete = oldList;
//			}
//			data.put("delete", delete);
//		}
//		if (StringUtil.isNotNullForList(oldList)) {
//			if (StringUtil.isNotNullForList(newList)) {
//				List<PromotionsLimitMoney> modify = new ArrayList<PromotionsLimitMoney>();
//				for (PromotionsLimitMoney newData : newList) {
//					if (newData.getId() != null) {
//						for (PromotionsLimitMoney oldData : oldList) {
//							if (newData.getId().equals(oldData.getId()) && !oldData.equals(newData)) {
//								modify.add(newData);
//							}
//						}
//					}
//				}
//				data.put("modify", modify);
//			}
//		}
		return data;
	}
	
	private JsonResult checkLimitMoneyEffectiveGifts(List<GiftsGoodsList> giftsGoodsLists,
			List<PromotionsLimitMoneyGoods> limitMoneyGoods, List<PromotionsLimitMoney> limitMoneys) {
		JsonResult result = new JsonResult();
		result.setIsok(false);
		if (!StringUtil.isListNotNull(limitMoneys)) {
			result.setMessage("满赠细则设置不能为空");
			return result;
		}
		if (!StringUtil.isListNotNull(limitMoneyGoods)) {
			result.setMessage("满赠细则商品设置不能为空");
			return result;
		}
		if (!StringUtil.isListNotNull(giftsGoodsLists)) {
			result.setMessage("满赠商品列表设置不能为空");
			return result;
		}
		boolean flag = true;
		Map<String, PromotionsLimitMoney> limitMoneyMap = new HashMap<String, PromotionsLimitMoney>();
		Map<String, PromotionsLimitMoney> limitMoneyMap2 = new HashMap<String, PromotionsLimitMoney>();
		for (PromotionsLimitMoney obj : limitMoneys) {
			String mapKey = obj.getPromCode() + obj.getLimitMoney().toString();
			PromotionsLimitMoney temp = limitMoneyMap.get(mapKey);
			if (null != temp) {
				result.setMessage("满赠细则设置[" + obj.getLimitMoney() + "]档重复,请检查后重新设置！");
				flag = false;
				break;
			} else {
				limitMoneyMap.put(mapKey, obj);
				limitMoneyMap2.put(obj.getPromDetailsCode(), obj);
			}
		}
		if (!flag) {
			return result;
		}
		Map<String, GiftsGoodsList> giftsGoodsMap = new HashMap<String, GiftsGoodsList>();
		for (GiftsGoodsList obj : giftsGoodsLists) {
			GiftsGoodsList goodsList = giftsGoodsMap.get(obj.getGoodsSn());
			if (null != goodsList) {
				result.setMessage("满赠商品列表SKU[" + obj.getGoodsSn() + "]重复,请检查后重新设置！");
				flag = false;
				break;
			} else {
				giftsGoodsMap.put(obj.getGoodsSn(), obj);
			}
		}
		if (!flag) {
			return result;
		}
		Map<String, PromotionsLimitMoneyGoods> limitMoneyGoodsMap = new HashMap<String, PromotionsLimitMoneyGoods>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (PromotionsLimitMoneyGoods obj : limitMoneyGoods) {
			// 检查赠品的有效性
			GiftsGoodsList goodsList = giftsGoodsMap.get(obj.getGiftsSkuSn());
			if (goodsList == null) {
				PromotionsLimitMoney money = limitMoneyMap2.get(obj.getPromDetailsCode());
				result.setMessage("满赠细则[" + money.getLimitMoney() + "]档商品SKU[" + obj.getGiftsSkuSn() + "] 不在满赠商品列表中,请检查后重新设置！");
				flag = false;
				break;
			}
			String mapKey = obj.getPromDetailsCode() + obj.getGiftsSkuSn();
			PromotionsLimitMoneyGoods temp = limitMoneyGoodsMap.get(mapKey);
			PromotionsLimitMoney money = limitMoneyMap2.get(obj.getPromDetailsCode());
			// 判断每档的股定赠商品赠送量是否大于活动的每单赠送量
			if (money != null && 0 == obj.getGiftsPriority().intValue()) {
				Integer num = map.get(obj.getPromDetailsCode());
				if (num != null) {
					if ((num + obj.getGiftsSkuCount()) > money.getGiftsGoodsCount()) {
						result.setMessage("满赠细则[" + money.getLimitMoney() + "]档固定赠【每单每款赠送数量】总和大于【每单赠送数量】,请检查后重新设置！");
						flag = false;
						break;
					} else {
						map.put(obj.getPromDetailsCode(), (num + obj.getGiftsSkuCount()));
					}
				} else {
					map.put(obj.getPromDetailsCode(), obj.getGiftsSkuCount());
				}
			}
			// 判断每一档是否有重复SKU记录
			if (null != temp) {
				if (money != null) {
					result.setMessage("满赠细则[" + money.getLimitMoney() + "]档商品SKU[" + obj.getGiftsSkuSn() + "]重复,请检查后重新设置！");
				}
				flag = false;
				break;
			} else {
				limitMoneyGoodsMap.put(mapKey, obj);
			}
		}
		if (!flag) {
			return result;
		} else {
			result.setIsok(true);
		}
		return result;
	}
	
	private JsonResult checkLimitSnEffectiveGifts(List<GiftsGoodsList> giftsGoodsLists,
			List<PromotionsLimitSn> limitSns) {
		JsonResult result = new JsonResult();
		result.setIsok(false);
		if (!StringUtil.isListNotNull(limitSns)) {
			result.setMessage("买赠细则设置不能为空");
			return result;
		}
		if (!StringUtil.isListNotNull(giftsGoodsLists)) {
			result.setMessage("满赠商品列表设置不能为空");
			return result;
		}
		boolean flag = true;
		Map<String, GiftsGoodsList> giftsGoodsMap = new HashMap<String, GiftsGoodsList>();
		for (GiftsGoodsList obj : giftsGoodsLists) {
			GiftsGoodsList goodsList = giftsGoodsMap.get(obj.getGoodsSn());
			if (null != goodsList) {
				result.setMessage("满赠商品列表SKU[" + obj.getGoodsSn() + "]重复,请检查后重新设置！");
				flag = false;
				break;
			} else {
				giftsGoodsMap.put(obj.getGoodsSn(), obj);
			}
		}
		if (!flag) {
			return result;
		}
		Map<String, PromotionsLimitSn> limitSnMap = new HashMap<String, PromotionsLimitSn>();
		for (PromotionsLimitSn obj : limitSns) {
			// 检查赠品的有效性
			GiftsGoodsList goodsList = giftsGoodsMap.get(obj.getGiftsGoodsSn());
			if (goodsList == null) {
				result.setMessage("买赠细则需够款[" + obj.getLimitGoodsSn() + "]中赠品款[" + obj.getGiftsGoodsSn() + "] 不在赠品列表列表中,请检查后重新设置！");
				flag = false;
				break;
			}
			PromotionsLimitSn limitSn = limitSnMap.get(obj.getLimitGoodsSn());
			// 判断每一档是否有重复SKU记录
			if (null != limitSn) {
				if (limitSn.equals(obj)) {
					result.setMessage("买赠细则需够款[" + obj.getLimitGoodsSn() + "]中赠品款[" + obj.getGiftsGoodsSn() + "] 重复设置,请检查后重新设置！");
					flag = false;
					break;
				}
			} else {
				limitSnMap.put(obj.getLimitGoodsSn(), obj);
			}
		}
		if (!flag) {
			return result;
		} else {
			result.setIsok(true);
		}
		return result;
	}
	/**
	 *  满赠细则设置
	 * 比对原纪录数据与新纪录数据差异
	 * 
	 * @param oldList
	 * @param newList
	 * @return
	 */
	private void compareUpdatePromotionsLimitMoney(List<PromotionsLimitMoney> oldList,
			List<PromotionsLimitMoney> newList, StringBuffer msg) {
		List<PromotionsLimitMoney> addList = new ArrayList<PromotionsLimitMoney>();
		List<PromotionsLimitMoney> modifyList = new ArrayList<PromotionsLimitMoney>();
		// 删除列表先copy一份原细则数据
		List<PromotionsLimitMoney> deleteList = new ArrayList<PromotionsLimitMoney>();
		if (StringUtil.isNotNullForList(oldList)) {
			Map<String, PromotionsLimitMoney> oldListMap = new HashMap<String, PromotionsLimitMoney>();
			for (PromotionsLimitMoney limitMoney : oldList) {
				oldListMap.put(limitMoney.getPromDetailsCode(), limitMoney);
				deleteList.add(limitMoney);
			}
			// 原纪录数据
			for(PromotionsLimitMoney newObj : newList) {
				// 当初赠送商品量以外的条件都相同时
				PromotionsLimitMoney oldObj = oldListMap.get(newObj.getPromDetailsCode());
				if ((null != newObj.getId() || oldObj != null) && newObj.equals(oldObj)) {
					if (!newObj.getGiftsGoodsCount().equals(oldObj.getGiftsGoodsCount())) {
						// 不相同时,更新细则列表
						modifyList.add(newObj);
					}
					// 移除原纪录数据
					deleteList.remove(oldObj);
				} else {
					newObj.setId(null);
					addList.add(newObj);
				}
			}
		} else {
			// 原纪录为null时，将新纪录放入addList中
			addList.addAll(newList);
		}
		
		int count = 0;
		if (StringUtil.isListNotNull(addList)) {
			for (PromotionsLimitMoney addData : addList) {
				count = promotionsLimitMoneyMapper.insertSelective(addData);
//				if (count != 1) {
//					jsonResult.setIsok(false);
//					jsonResult.setMessage("保存失败!");
//					return jsonResult;
//				}
			}
			msg.append("新增细则：\n").append(addList.toString()).append("\n");
		}
//		List<PromotionsLimitMoney> delete = data.get("delete");
		if (StringUtil.isNotNullForList(deleteList)) {
			for (PromotionsLimitMoney deleteData : deleteList) {
				count = promotionsLimitMoneyMapper.deleteByPrimaryKey(deleteData.getId());
//				if (count != 1) {
//					jsonResult.setIsok(false);
//					jsonResult.setMessage("删除失败!");
//					return jsonResult;
//				}
			}
			msg.append("删除细则：\n").append(deleteList.toString()).append("\n");
		}
		if (StringUtil.isNotNullForList(modifyList)) {
			msg.append("修改细则：\n");
			for (PromotionsLimitMoney modifyData : modifyList) {
				PromotionsLimitMoney oldData = promotionsLimitMoneyMapper.selectByPrimaryKey(modifyData.getId());
				count = promotionsLimitMoneyMapper.updateByPrimaryKey(modifyData);
//				if (count != 1) {
//					jsonResult.setIsok(false);
//					jsonResult.setMessage("修改失败!");
//					return jsonResult;
//				}
				msg.append(oldData.toString()).append(" -> ").append(modifyData.toString()).append("\n");
			}
		}
	}
	
	
	/**
	 * 满赠细则赠品设置
	 * 比对原纪录数据与新纪录数据差异并且更新相关纪录
	 * 
	 * @param oldList
	 * @param newList
	 * @return
	 */
	private void compareUpdatePromotionsLimitMoneyGoods(List<PromotionsLimitMoneyGoods> oldList,
			List<PromotionsLimitMoneyGoods> newList, StringBuffer msg) {
		List<PromotionsLimitMoneyGoods> addList = new ArrayList<PromotionsLimitMoneyGoods>();
		List<PromotionsLimitMoneyGoods> modifyList = new ArrayList<PromotionsLimitMoneyGoods>();
		// 删除列表先copy一份原细则数据
		List<PromotionsLimitMoneyGoods> deleteList = new ArrayList<PromotionsLimitMoneyGoods>();
		if (StringUtil.isNotNullForList(oldList)) {
			Map<String, PromotionsLimitMoneyGoods> oldListMap = new HashMap<String, PromotionsLimitMoneyGoods>();
			for (PromotionsLimitMoneyGoods limitMoney : oldList) {
				oldListMap.put(limitMoney.getPromDetailsCode() + limitMoney.getGiftsSkuSn(), limitMoney);
				deleteList.add(limitMoney);
			}
			// 原纪录数据
			for(PromotionsLimitMoneyGoods newObj : newList) {
				// 当初赠送商品量以外的条件都相同时
				PromotionsLimitMoneyGoods oldObj = oldListMap.get(newObj.getPromDetailsCode() + newObj.getGiftsSkuSn());
				if (oldObj != null && newObj.equals(oldObj)) {
					if (!newObj.getGiftsSkuCount().equals(oldObj.getGiftsSkuCount())
							|| !newObj.getGiftsPriority().equals(oldObj.getGiftsPriority())) {
						// 不相同时,更新细则列表
						modifyList.add(newObj);
					}
					// 移除原纪录数据
					deleteList.remove(oldObj);
				} else {
					addList.add(newObj);
				}
			}
		} else {
			// 原纪录为null时，将新纪录放入addList中
			addList.addAll(newList);
		}
		if (StringUtil.isNotNullForList(deleteList)) {
			for (PromotionsLimitMoneyGoods deleteData : deleteList) {
				PromotionsLimitMoneyGoodsExample example = new PromotionsLimitMoneyGoodsExample();
				example.or().andPromCodeEqualTo(deleteData.getPromCode()).
					andPromDetailsCodeEqualTo(deleteData.getPromDetailsCode()).
					andGiftsSkuSnEqualTo(deleteData.getGiftsSkuSn());
				promotionsLimitMoneyGoodsMapper.deleteByExample(example);
			}
			msg.append("删除细则：\n").append(deleteList.toString()).append("\n");
		}
		if (StringUtil.isNotNullForList(modifyList)) {
			msg.append("修改细则：\n");
			for (PromotionsLimitMoneyGoods modifyData : modifyList) {
				PromotionsLimitMoneyGoodsExample example = new PromotionsLimitMoneyGoodsExample();
				example.or().andPromCodeEqualTo(modifyData.getPromCode()).
					andPromDetailsCodeEqualTo(modifyData.getPromDetailsCode()).
					andGiftsSkuSnEqualTo(modifyData.getGiftsSkuSn());
				List<PromotionsLimitMoneyGoods> oldDatas = promotionsLimitMoneyGoodsMapper.selectByExample(example);
				promotionsLimitMoneyGoodsMapper.updateByExampleSelective(modifyData, example);
				msg.append(oldDatas.get(0).toString()).append(" -> ").append(modifyData.toString()).append("\n");
			}
		}
		if (StringUtil.isListNotNull(addList)) {
			for (PromotionsLimitMoneyGoods addData : addList) {
				promotionsLimitMoneyGoodsMapper.insertSelective(addData);
			}
			msg.append("新增细则：\n").append(addList.toString()).append("\n");
		}
	}
	
	/**
	 *  买赠细则设置
	 * 比对原纪录数据与新纪录数据差异
	 * 
	 * @param oldList
	 * @param newList
	 * @return
	 */
	private void compareUpdatePromotionsLimitSn(List<PromotionsLimitSn> oldList,
			List<PromotionsLimitSn> newList, StringBuffer msg) {
		List<PromotionsLimitSn> addList = new ArrayList<PromotionsLimitSn>();
		List<PromotionsLimitSn> modifyList = new ArrayList<PromotionsLimitSn>();
		// 删除列表先copy一份原细则数据
		List<PromotionsLimitSn> deleteList = new ArrayList<PromotionsLimitSn>();
		if (StringUtil.isNotNullForList(oldList)) {
			Map<String, PromotionsLimitSn> oldListMap = new HashMap<String, PromotionsLimitSn>();
			for (PromotionsLimitSn limitSn : oldList) {
				String key = limitSn.getLimitGoodsSn() + limitSn.getGiftsGoodsSn();
				oldListMap.put(key, limitSn);
				deleteList.add(limitSn);
			}
			// 原纪录数据
			for(PromotionsLimitSn newObj : newList) {
				// 当初赠送商品量以外的条件都相同时
				String key = newObj.getLimitGoodsSn() + newObj.getGiftsGoodsSn();
				PromotionsLimitSn oldObj = oldListMap.get(key);
				if ((null != newObj.getId() || oldObj != null) && newObj.equals(oldObj)) {
					newObj.setId(oldObj.getId());
					if (!newObj.getLimitCount().equals(oldObj.getLimitCount())
							|| !newObj.getGiftsGoodsCount().equals(oldObj.getGiftsGoodsCount())) {
						// 不相同时,更新细则列表
						modifyList.add(newObj);
					}
					// 移除原纪录数据
					deleteList.remove(oldObj);
				} else {
					newObj.setId(null);
					addList.add(newObj);
				}
			}
		} else {
			// 原纪录为null时，将新纪录放入addList中
			addList.addAll(newList);
		}
		if (StringUtil.isListNotNull(addList)) {
			for (PromotionsLimitSn addData : addList) {
				promotionsLimitSnMapper.insertSelective(addData);
			}
			msg.append("新增细则：\n").append(addList.toString()).append("\n");
		}
		if (StringUtil.isNotNullForList(deleteList)) {
			for (PromotionsLimitSn deleteData : deleteList) {
				promotionsLimitSnMapper.deleteByPrimaryKey(deleteData.getId());
			}
			msg.append("删除细则：\n").append(deleteList.toString()).append("\n");
		}
		if (StringUtil.isNotNullForList(modifyList)) {
			msg.append("修改细则：\n");
			for (PromotionsLimitSn modifyData : modifyList) {
				PromotionsLimitSn oldData = promotionsLimitSnMapper.selectByPrimaryKey(modifyData.getId());
				promotionsLimitSnMapper.updateByPrimaryKey(modifyData);
				msg.append(oldData.toString()).append(" -> ").append(modifyData.toString()).append("\n");
			}
		}
	}

	private Map<String, List<PromotionsListGoods>> getPromotionsListGoodsData(List<PromotionsListGoods> oldList, List<PromotionsListGoods> newList) {
		Map<String, List<PromotionsListGoods>> data = new HashMap<String, List<PromotionsListGoods>>();
		if (StringUtil.isNotNullForList(newList)) {
			List<PromotionsListGoods> add = new ArrayList<PromotionsListGoods>();
			for (PromotionsListGoods newData : newList) {
				if (newData.getId() == null) {
					add.add(newData);
				}
			}
			data.put("add", add);
		}
		if (StringUtil.isNotNullForList(oldList)) {
			List<PromotionsListGoods> delete = new ArrayList<PromotionsListGoods>();
			if (StringUtil.isNotNullForList(newList)) {
				for (PromotionsListGoods oldData : oldList) {
					boolean flag = true;
					for (PromotionsListGoods newData : newList) {
						if (newData.getId() != null && newData.getId().equals(oldData.getId())) {
							flag = false;
							break;
						}
					}
					if (flag) {
						delete.add(oldData);
					}
				}
			} else {
				delete = oldList;
			}
			data.put("delete", delete);
		}
		if (StringUtil.isNotNullForList(oldList)) {
			if (StringUtil.isNotNullForList(newList)) {
				List<PromotionsListGoods> modify = new ArrayList<PromotionsListGoods>();
				for (PromotionsListGoods newData : newList) {
					if (newData.getId() != null) {
						for (PromotionsListGoods oldData : oldList) {
							if (newData.getId().equals(oldData.getId()) && !oldData.equals(newData)) {
								modify.add(newData);
							}
						}
					}
				}
				data.put("modify", modify);
			}
		}
		return data;
	}

	/**
	 * 赠品列表设置
	 * 比对原纪录数据与新纪录数据差异
	 * 
	 * @param oldList
	 * @param newList
	 * @return
	 */
	private void compareUpdateGiftsGoodsList(
			List<GiftsGoodsList> oldList, List<GiftsGoodsList> newList, StringBuffer msg) {
		List<GiftsGoodsList> addList = new ArrayList<GiftsGoodsList>();
		List<GiftsGoodsList> modifyList = new ArrayList<GiftsGoodsList>();
		// 删除列表先copy一份原细则数据
		List<GiftsGoodsList> deleteList = new ArrayList<GiftsGoodsList>();
		if (StringUtil.isNotNullForList(oldList)) {
			Map<String, GiftsGoodsList> oldListMap = new HashMap<String, GiftsGoodsList>();
			for (GiftsGoodsList limitMoney : oldList) {
				oldListMap.put(limitMoney.getPromCode() + limitMoney.getGoodsSn(), limitMoney);
				deleteList.add(limitMoney);
			}
			// 原纪录数据
			for(GiftsGoodsList newObj : newList) {
				// 当初赠送商品量以外的条件都相同时
				GiftsGoodsList oldObj = oldListMap.get(newObj.getPromCode() + newObj.getGoodsSn());
				if (oldObj != null && newObj.equals(oldObj)) {
					if (!newObj.getGiftsSum().equals(oldObj.getGiftsSum())) {
						// 不相同时,更新细则列表
						newObj.setId(oldObj.getId());
						modifyList.add(newObj);
					}
					// 移除原纪录数据
					deleteList.remove(oldObj);
				} else {
					addList.add(newObj);
				}
			}
		} else {
			// 原纪录为null时，将新纪录放入addList中
			addList.addAll(newList);
		}
		if (StringUtil.isNotNullForList(deleteList)) {
			for (GiftsGoodsList deleteObj : deleteList) {
				giftsGoodsListMapper.deleteByPrimaryKey(deleteObj.getId());
			}
			msg.append("删除赠品细则：\n").append(deleteList.toString()).append("\n");
		}
		if (StringUtil.isNotNullForList(modifyList)) {
			msg.append("修改赠品细则：\n");
			for (GiftsGoodsList modifyData : modifyList) {
				GiftsGoodsList oldData = giftsGoodsListMapper.selectByPrimaryKey(modifyData.getId());
				giftsGoodsListMapper.updateByPrimaryKey(modifyData);
				msg.append(oldData.toString()).append(" -> ").append(modifyData.toString()).append("\n");
			}
		}
		if (StringUtil.isNotNullForList(addList)) {
			for (GiftsGoodsList addObj : addList) {
				giftsGoodsListMapper.insertSelective(addObj);
			}
			msg.append("新增赠品细则：\n").append(addList.toString()).append("\n");
		}
	}
	
	private Map<String, List<GiftsGoodsList>> getGiftsGoodsListData(List<GiftsGoodsList> oldList, List<GiftsGoodsList> newList) {
		Map<String, List<GiftsGoodsList>> data = new HashMap<String, List<GiftsGoodsList>>();
		if (StringUtil.isNotNullForList(newList)) {
			List<GiftsGoodsList> add = new ArrayList<GiftsGoodsList>();
			for (GiftsGoodsList newData : newList) {
				if (newData.getId() == null) {
					add.add(newData);
				}
			}
			data.put("add", add);
		}
		if (StringUtil.isNotNullForList(oldList)) {
			List<GiftsGoodsList> delete = new ArrayList<GiftsGoodsList>();
			if (StringUtil.isNotNullForList(newList)) {
				for (GiftsGoodsList oldData : oldList) {
					boolean flag = true;
					for (GiftsGoodsList newData : newList) {
						if (newData.getId() != null && newData.getId().equals(oldData.getId())) {
							flag = false;
							break;
						}
					}
					if (flag) {
						delete.add(oldData);
					}
				}
			} else {
				delete = oldList;
			}
			data.put("delete", delete);
		}
		if (StringUtil.isNotNullForList(oldList)) {
			if (StringUtil.isNotNullForList(newList)) {
				List<GiftsGoodsList> modify = new ArrayList<GiftsGoodsList>();
				for (GiftsGoodsList newData : newList) {
					if (newData.getId() != null) {
						for (GiftsGoodsList oldData : oldList) {
							if (newData.getId().equals(oldData.getId()) && !oldData.equals(newData)) {
								modify.add(newData);
							}
						}
					}
				}
				data.put("modify", modify);
			}
		}
		return data;
	}

	@Override
	public PromotionsInfo searchPromotionsInfoById(Integer id) {
		return promotionsInfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<PromotionsLimitMoney> searchPromotionsLimitMoneyByPromCode(String promCode) {
		PromotionsLimitMoneyExample example = new PromotionsLimitMoneyExample();
		example.or().andPromCodeEqualTo(promCode);
		return promotionsLimitMoneyMapper.selectByExample(example);
	}
	
	@Override
	public List<PromotionsLimitMoneyGoods> searchPromotionsLimitMoneyGoodsByPromCode(String promCode) {
		PromotionsLimitMoneyGoodsExample example = new PromotionsLimitMoneyGoodsExample();
		example.or().andPromCodeEqualTo(promCode);
		return promotionsLimitMoneyGoodsMapper.selectByExample(example);
	}

	@Override
	public GroupGoods searchGroupGoodsByGroupCode(String groupCode) {
		GroupGoodsExample example = new GroupGoodsExample();
		example.or().andGroupCodeEqualTo(groupCode);
		List<GroupGoods> list = groupGoodsMapper.selectByExample(example);
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<GroupGoodsList> searchGroupGoodsListByGroupCode(String groupCode) {
		GroupGoodsListExample example = new GroupGoodsListExample();
		example.or().andGroupCodeEqualTo(groupCode);
		return groupGoodsListMapper.selectByExample(example);
	}

	@Override
	public List<PromotionsLimitSn> searchPromotionsLimitSnByPromCode(String promCode) {
		PromotionsLimitSnExample example = new PromotionsLimitSnExample();
		example.or().andPromCodeEqualTo(promCode);
		return promotionsLimitSnMapper.selectByExample(example);
	}

	@Override
	public JsonResult addGroupGoods(PromotionVO promotionVO) {
		JsonResult jsonResult = new JsonResult();
		try {
			GroupGoods groupGoods = new GroupGoods();
			groupGoods.setGroupCode(promotionVO.getGroupCode());
			groupGoods.setPrice(promotionVO.getPrice());
			groupGoods.setAddTime(new Date());
			int count = groupGoodsMapper.insertSelective(groupGoods);
			if (count != 1) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("添加失败!");
				return jsonResult;
			}
			if (promotionVO.getGroupGoodsListList() != null) {
				for (GroupGoodsList groupGoodsList : promotionVO.getGroupGoodsListList()) {
					groupGoodsList.setAddTime(new Date());

					count = groupGoodsListMapper.insertSelective(groupGoodsList);

					if (count != 1) {
						jsonResult.setIsok(false);
						jsonResult.setMessage("添加失败!");
						return jsonResult;
					}
				}
			}
			jsonResult.setIsok(true);
			jsonResult.setMessage("添加成功!");
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("添加失败!");
		}
		return jsonResult;
	}

	@Override
	public JsonResult saveGroupGoods(PromotionVO promotionVO) {
		JsonResult jsonResult = new JsonResult();
		try {
			GroupGoods groupGoods = new GroupGoods();
			groupGoods.setId(Integer.valueOf(promotionVO.getId()));
			groupGoods.setGroupCode(promotionVO.getGroupCode());
			groupGoods.setPrice(promotionVO.getPrice());
			int count = groupGoodsMapper.updateByPrimaryKeySelective(groupGoods);

			if (count != 1) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("修改失败!");
				return jsonResult;
			}

			GroupGoodsListExample example = new GroupGoodsListExample();
			example.or().andGroupCodeEqualTo(promotionVO.getGroupCode());
			groupGoodsListMapper.deleteByExample(example);
			if (promotionVO.getGroupGoodsListList() != null) {
				for (GroupGoodsList groupGoodsList : promotionVO.getGroupGoodsListList()) {
					groupGoodsList.setAddTime(new Date());

					count = groupGoodsListMapper.insertSelective(groupGoodsList);

					if (count != 1) {
						jsonResult.setIsok(false);
						jsonResult.setMessage("修改失败!");
						return jsonResult;
					}
				}
			}

			jsonResult.setIsok(true);
			jsonResult.setMessage("修改成功!");
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("修改失败!");
		}
		return jsonResult;
	}

	@Override
	public JsonResult uplaodGroupGoods(List<GroupGoodsVO> groupGoodsVOs) {
		JsonResult jsonResult = new JsonResult();
		try {
			String groupCodeBak = "";
			int count = 0;
			for (GroupGoodsVO groupGoodsVO : groupGoodsVOs) {
				if (StringUtils.isEmpty(groupCodeBak)) {
					groupCodeBak = groupGoodsVO.getGroupCode();
					GroupGoods groupGoods = new GroupGoods();
					groupGoods.setGroupCode(groupGoodsVO.getGroupCode());
					groupGoods.setPrice(groupGoodsVO.getPrice());
					groupGoods.setAddTime(new Date());
					count = groupGoodsMapper.insertSelective(groupGoods);
					if (count != 1) {
						jsonResult.setIsok(false);
						jsonResult.setMessage("添加失败!");
						return jsonResult;
					}
				} else {
					if (!groupCodeBak.equals(groupGoodsVO.getGroupCode())) {
						groupCodeBak = groupGoodsVO.getGroupCode();
						groupCodeBak = groupGoodsVO.getGroupCode();
						GroupGoods groupGoods = new GroupGoods();
						groupGoods.setGroupCode(groupGoodsVO.getGroupCode());
						groupGoods.setPrice(groupGoodsVO.getPrice());
						groupGoods.setAddTime(new Date());
						count = groupGoodsMapper.insertSelective(groupGoods);
						if (count != 1) {
							jsonResult.setIsok(false);
							jsonResult.setMessage("添加失败!");
							return jsonResult;
						}
					}
				}

				GroupGoodsList groupGoodsList = new GroupGoodsList();
				groupGoodsList.setGroupCode(groupCodeBak);
				groupGoodsList.setGoodsSn(groupGoodsVO.getGoodsSn());
				groupGoodsList.setGoodsPrice(groupGoodsVO.getGoodsPrice());
				groupGoodsList.setGoodsCount(groupGoodsVO.getGoodsCount());
				groupGoodsList.setAddTime(new Date());
				count = groupGoodsListMapper.insertSelective(groupGoodsList);

				if (count != 1) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("添加失败!");
					return jsonResult;
				}
			}
			jsonResult.setIsok(true);
			jsonResult.setMessage("添加成功!");
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("添加失败!");
		}
		return jsonResult;
	}

	@Override
	public PromotionsListInfor searchPromotionsListInforByPromCode(String promCode) {
		PromotionsListInforExample example = new PromotionsListInforExample();
		example.or().andPromCodeEqualTo(promCode);
		List<PromotionsListInfor> list = promotionsListInforMapper.selectByExample(example);
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<PromotionsListGoods> searchPromotionsListGoodsByPromCode(String promCode) {
		PromotionsListGoodsExample example = new PromotionsListGoodsExample();
		example.or().andPromCodeEqualTo(promCode);
		return promotionsListGoodsMapper.selectByExample(example);
	}

	@Override
	public List<GiftsGoodsList> searchGiftsGoodsListByPromCode(String promCode) {
		GiftsGoodsListExample example = new GiftsGoodsListExample();
		example.or().andPromCodeEqualTo(promCode);
		return giftsGoodsListMapper.selectByExample(example);
	}

	private String getShopTitle(String shopCode) {
		ChannelShopExample example = new ChannelShopExample();
		example.or().andShopCodeEqualTo(shopCode);
		List<ChannelShop> list = channelShopMapper.selectByExample(example);
		if (list != null && list.size() == 1) {
			return list.get(0).getShopTitle();
		}
		return null;
	}

	private JsonResult validatePromotion(PromotionVO promotionVO) {
		JsonResult jsonResult = new JsonResult();
		boolean flag = StringUtils.isNotEmpty(promotionVO.getId());
		String shopCode = promotionVO.getShopCode();
		Date beginDate = DateTimeUtils.parseStr(promotionVO.getBeginTime(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss);
		Date endDate = DateTimeUtils.parseStr(promotionVO.getEndTime(), DateTimeUtils.YYYY_MM_DD_HH_mm_ss);
		PromotionsInfoExample selectExample = new PromotionsInfoExample();
		selectExample.or().andShopCodeEqualTo(shopCode).andPromTypeEqualTo(Byte.valueOf(promotionVO.getPromType()))
				.andPromStatusEqualTo((byte)1);
		List<PromotionsInfo> list = promotionsInfoMapper.selectByExample(selectExample);
		String promCodes = "";
		for (PromotionsInfo promotionsInfo : list) {
			if (flag && promotionVO.getId().equals(promotionsInfo.getId().toString())) {
				continue;
			}
			Date beforeDate = promotionsInfo.getBeginTime();
			Date afterDate = promotionsInfo.getEndTime();
			if (DateTimeUtils.isBetweenDate(beforeDate, afterDate, beginDate) || DateTimeUtils.isBetweenDate(beforeDate, afterDate, endDate)
					|| (beginDate.before(beforeDate) && endDate.after(afterDate))) {
				if (StringUtils.isNotBlank(promCodes)) {
					promCodes = promCodes + "," + promotionsInfo.getPromCode();
				} else {
					promCodes = promotionsInfo.getPromCode();
				}
			}
		}
		if (StringUtils.isNotBlank(promCodes)) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("促销[" + promotionVO.getPromCode() + "]与下列促销[" + promCodes + "]的促销时间存在重叠时间，请确认!");
		}
		return jsonResult;
	}

	private void insertPromotionsLog(PromotionsLog promotionsLog) {
		promotionsLog.setAddTime(new Date());
		if (StringUtil.isNotEmpty(promotionsLog.getMessage()) && promotionsLog.getMessage().length() > 65535) {
			promotionsLog.setMessage(promotionsLog.getMessage().substring(65535));
		}
		promotionsLogMapper.insertSelective(promotionsLog);
	}

	@Override
	public List<PromotionsLimitMoneyVO> searchPromotionsLimitMoneyVOByPromCode(
			String promCode) {
		return bgChannelDbTableMapper.selectPromotionsLimitMoneyGoods(promCode);
	}
}
