package com.work.shop.service.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.work.shop.bean.ChannelApiLog;
import com.work.shop.bean.ChannelGoods;
import com.work.shop.bean.ChannelGoodsExample;
import com.work.shop.bean.ChannelGoodsExtension;
import com.work.shop.bean.ChannelGoodsExtensionExample;
import com.work.shop.bean.ChannelTemplate;
import com.work.shop.bean.ChannelTemplateContent;
import com.work.shop.bean.ChannelTemplateContentExample;
import com.work.shop.bean.ChannelTemplateContentExample.Criteria;
import com.work.shop.bean.ChannelTemplateExample;
import com.work.shop.bean.ChannelTemplateModule;
import com.work.shop.bean.ChannelTemplateModuleExample;
import com.work.shop.bean.ChannelTemplateWithBLOBs;
import com.work.shop.bean.GoodsProperty;
import com.work.shop.bean.ProductGoodsGalleryVo;
import com.work.shop.bean.TicketInfo;
import com.work.shop.bean.bgcontentdb.BGproductAttrValues;
import com.work.shop.bean.bgcontentdb.BGproductAttrValuesExample;
import com.work.shop.dao.BgGoodsPropertyMapper;
import com.work.shop.dao.ChannelApiLogMapper;
import com.work.shop.dao.ChannelGoodsExtensionMapper;
import com.work.shop.dao.ChannelGoodsMapper;
import com.work.shop.dao.ChannelTemplateContentMapper;
import com.work.shop.dao.ChannelTemplateMapper;
import com.work.shop.dao.ChannelTemplateModuleMapper;
import com.work.shop.dao.bgcontentdb.BGproductAttrValuesMapper;
import com.work.shop.service.ChannelTemplateService;
import com.work.shop.util.ExceptionStackTraceUtil;
import com.work.shop.util.SqlUtils;
import com.work.shop.util.StringUtil;
import com.work.shop.util.TimeUtil;
import com.work.shop.util.VelocityUtil;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Pagination;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.DescModule;
import com.work.shop.vo.QueueManagerVo;


@Service("channelTemplateService")
public class ChannelTemplateServiceImpl implements ChannelTemplateService{
	
	private static final Logger logger = Logger.getLogger(ChannelTemplateServiceImpl.class);

	@Resource(name = "channelTemplateMapper")
	private ChannelTemplateMapper channelTemplateMapper;

	@Resource(name = "channelTemplateModuleMapper")
	private ChannelTemplateModuleMapper channelTemplateModuleMapper;

	@Resource(name = "channelTemplateContentMapper")
	private ChannelTemplateContentMapper channelTemplateContentMapper;

	@Resource(name = "channelGoodsMapper")
	private ChannelGoodsMapper channelGoodsMapper;

	@Resource(name = "bgGoodsPropertyMapper")
	private BgGoodsPropertyMapper goodsPropertyMapper;
	
	@Resource(name = "channelGoodsExtensionMapper")
	private ChannelGoodsExtensionMapper channelGoodsExtensionMapper;
	
	@Resource(name= "channelApiLogMapper")
	private ChannelApiLogMapper channelApiLogMapper;
	
//	@Resource(name= "ticketInfoMapper")
//	private TicketInfoMapper ticketInfoMapper;
	
	@Resource(name= "bGproductAttrValuesMapper")
	private BGproductAttrValuesMapper productAttrValuesMapper;
	
	@Override
	public List<ChannelTemplate> getChannelTemplateList(ChannelTemplate channelTemplate) {
		com.work.shop.bean.ChannelTemplateExample example = new com.work.shop.bean.ChannelTemplateExample();
		com.work.shop.bean.ChannelTemplateExample.Criteria criteria = example.or();
		if (StringUtil.isNotNull(channelTemplate.getShopCode())) {
			criteria.andShopCodeEqualTo(channelTemplate.getShopCode());
		}
		if (StringUtil.isNotNull(channelTemplate.getChannelCode())) {
			criteria.andChannelCodeEqualTo(channelTemplate.getChannelCode());
		}
		if (null != channelTemplate.getTemplateType()) {
			criteria.andTemplateTypeEqualTo(channelTemplate.getTemplateType());
		}
		return channelTemplateMapper.selectByExample(example);
	}

	@Override
	public int getChannelTemplateCount(ChannelTemplateExample example) {
		return channelTemplateMapper.countByExample(example);
	}

	@Override
	public Paging getChannelTemplatePage(ChannelTemplate model, PageHelper helper) throws Exception {
		ChannelTemplateExample example = new ChannelTemplateExample();
		com.work.shop.bean.ChannelTemplateExample.Criteria criteria = example.or();
		criteria.limit(helper.getStart(), helper.getLimit());
		if (null != model) {
			if (StringUtil.isNotEmpty(model.getTemplateName())) {
				criteria.andTemplateNameLike(SqlUtils.getLike(model.getTemplateName()));
			}
			if (null != model.getId()) {
				criteria.andIdEqualTo(model.getId());
			}
			if (StringUtil.isNotEmpty(model.getShopCode())) {
				criteria.andShopCodeEqualTo(model.getShopCode());
			}
			if (StringUtil.isNotEmpty(model.getChannelCode())) {
				criteria.andChannelCodeEqualTo(model.getChannelCode());
			}
			if (StringUtil.isNotEmpty(model.getTemplateCode())) {
				criteria.andTemplateCodeEqualTo(model.getTemplateCode());
			}
		}
		Paging paging = new Paging(channelTemplateMapper.countByExample(example), channelTemplateMapper.selectByExample(example));
		return paging;
	}

	@Override
	public int getChannelTemplateModuleCount(ChannelTemplateModuleExample example) {
		return this.channelTemplateModuleMapper.countByExample(example);
	}

	@Override
	public List<ChannelTemplateModule> getChannelTemplateModuleList(ChannelTemplateModule record) {
		ChannelTemplateModuleExample example = new ChannelTemplateModuleExample();
		com.work.shop.bean.ChannelTemplateModuleExample.Criteria criteria = example.or();
		if(StringUtil.isNotNull(record.getShopCode())) {
			criteria.andShopCodeEqualTo(record.getShopCode());
		}
		if(StringUtil.isNotNull(record.getChannelCode())) {
			criteria.andChannelCodeEqualTo(record.getChannelCode());
		}
		
/*		if(0==record.getModuleSize() || 1==record.getModuleSize()){
			criteria.andModuleSizeEqualTo(record.getModuleSize());
		}*/
		
		return this.channelTemplateModuleMapper.selectByExample(example);
	}

	@Override
	public Paging getChannelTemplateModulePage(ChannelTemplateModule model, PageHelper helper) throws Exception {
		ChannelTemplateModuleExample example = new ChannelTemplateModuleExample();
		com.work.shop.bean.ChannelTemplateModuleExample.Criteria criteria = example.or();
		criteria.limit(helper.getStart(), helper.getLimit());
		if (StringUtil.isNotNull(model.getModuleName())) {
			criteria.andModuleNameLike(SqlUtils.getLike(model.getModuleName()));
		}
		if (StringUtil.isNotNull(model.getShopCode())) {
			criteria.andShopCodeEqualTo(model.getShopCode());
		}
		if (StringUtil.isNotNull(model.getChannelCode())) {
			criteria.andChannelCodeEqualTo(model.getChannelCode());
		}
		Paging paging = new Paging(channelTemplateModuleMapper.countByExample(example), 
				channelTemplateModuleMapper.selectByExample(example));
		return paging;
	}

	@Override
	public int deleteModuleById(String ids) throws Exception {
		String[] arr = ids.split(",");
		int result = 1;
		for (String id : arr) {
			result = this.channelTemplateModuleMapper.deleteByPrimaryKey(Integer.valueOf(id));
			if (result ==0 ) {
				throw new Exception();
			}
		}
		return result;
	}

	@Override
	public int deleteTemplateById(String ids) throws Exception {
		String[] arr = ids.split(",");
		int result = 1;
		for (String id : arr) {
			result = this.channelTemplateMapper.deleteByPrimaryKey(Integer.valueOf(id));
			if (result ==0 ) {
				throw new Exception();
			}
		}
		return result;
	}

	@Override
	public ChannelTemplateModule getModuleById(Integer id) throws Exception {
		ChannelTemplateModule module = this.channelTemplateModuleMapper.selectByPrimaryKey(id);
		if (null != module) {
//			module.setModuleContent(URLEncoder.encode(module.getModuleContent(),"UTF-8"));
		}
		return module;
	}

	@Override
	public ChannelTemplateWithBLOBs getTemplateById(Integer id) throws Exception {
		ChannelTemplateWithBLOBs template =  this.channelTemplateMapper.selectByPrimaryKey(id);
//		template.setEditTemplateContent(URLEncoder.encode(template.getEditTemplateContent(),"UTF-8"));
		return template;
	}

	@Override
	public int insertModule(ChannelTemplateModule record) throws Exception {
		record.setAddTime(TimeUtil.getTimestamp());
//		record.setModuleContent(URLDecoder.decode(record.getModuleContent()));
		record.setShowTitle(0);
		this.channelTemplateModuleMapper.insert(record);
		return  0;
	}

	@Override
	public int insertTemplate(ChannelTemplateWithBLOBs record, List<ChannelTemplateContent> contents) throws Exception {
		record.setAddTime(TimeUtil.getTimestamp());
		int count = this.channelTemplateMapper.insert(record);
		if (count == 0) {
			throw new Exception();
		}
		if (!StringUtil.isNotNullForList(contents)) {
			return count;
		}
		for(ChannelTemplateContent content : contents){
			content.setAddTime(TimeUtil.getTimestamp());
			content.setModuleType((byte) 0);
			count = this.channelTemplateContentMapper.insert(content);
			if (count == 0) {
				throw new Exception();
			}
		}
		return count;
	}

	@Override
	public int updateModule(ChannelTemplateModule record)
			throws Exception {
		ChannelTemplateModule tempObj = this.channelTemplateModuleMapper.selectByPrimaryKey(record.getId());
		record.setUpdateTime(TimeUtil.getTimestamp());
		record.setAddTime(tempObj.getAddTime());
		record.setShowTitle(0);
		return this.channelTemplateModuleMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateTemplate(ChannelTemplateWithBLOBs record, List<ChannelTemplateContent> contents) throws Exception {
		ChannelTemplate tempObj = this.channelTemplateMapper.selectByPrimaryKey(record.getId());
		record.setAddTime(tempObj.getAddTime());
		record.setUpdateTime(TimeUtil.getTimestamp());
		int count =  this.channelTemplateMapper.updateByPrimaryKeySelective(record);
		if (count == 0) {
			throw new Exception();
		}
		if (!StringUtil.isNotNullForList(contents)) {
			return count;
		}
		ChannelTemplateContentExample example = new ChannelTemplateContentExample();
		Criteria criteria = example.or();
		criteria.andTemplateCodeEqualTo(record.getTemplateCode());
		this.channelTemplateContentMapper.deleteByExample(example);
		for(ChannelTemplateContent content : contents){
			content.setAddTime(TimeUtil.getTimestamp());
			content.setModuleType((byte) 0);
			count = this.channelTemplateContentMapper.insert(content);
			if (count == 0) {
				throw new Exception();
			}
		}
		return count;
	}

	@Override
	public List<ChannelTemplateContent> getChannelTemplateContentList(
			ChannelTemplateContentExample example) throws Exception {
		return this.channelTemplateContentMapper.selectByExample(example);
	}

	@Override
	public void createGoodsDetail(QueueManagerVo managerVo, List<TicketInfo> list) throws Exception{
		// 模板内容
		logger.info("生成商品详情  channelCode=" + managerVo.getChannelCode()+";shopCode="+ managerVo.getShopCode()+ "  templateCode="+managerVo.getTemplateCode());
		ChannelTemplateExample templateExample = new ChannelTemplateExample();
		com.work.shop.bean.ChannelTemplateExample.Criteria tempCriteria = templateExample.or();
		tempCriteria.andTemplateCodeEqualTo(managerVo.getTemplateCode());
		List<ChannelTemplateWithBLOBs> templates = this.channelTemplateMapper.selectByExampleWithBLOBs(templateExample);
		if (!StringUtil.isNotNullForList(templates)) {
			return ;
		}
		String goodsSnTicketCode = managerVo.getTicketInfo().getTicketCode() +"-"+ managerVo.getTicketInfo().getGoodsSn();
		String actionUser = managerVo.getOperUser();
		String shopCode = managerVo.getShopCode();
		String channelCode = managerVo.getChannelCode();
//		for (TicketInfo info : list) {
			TicketInfo info = managerVo.getTicketInfo();
			String goodsSn = info.getGoodsSn();
			String html = "";
			// 商品属性
			ChannelGoodsExample goodsExample = new ChannelGoodsExample();
			com.work.shop.bean.ChannelGoodsExample.Criteria goodsCriteria= goodsExample.or();
			goodsCriteria.andGoodsNameEqualTo(goodsSn);
			GoodsProperty goodsProperty = new GoodsProperty();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("channelCode", channelCode);
			params.put("shopCode", shopCode);
			params.put("goodsSn", goodsSn);
			goodsProperty = this.goodsPropertyMapper.selectGoodsProperties(params);
			if (null != goodsProperty) {
				// 商品图片列表
				List<ProductGoodsGalleryVo> galleries = this.goodsPropertyMapper.productGoodsGalleryList(params);
				// 生成商品详情Html代码
				String content = URLDecoder.decode(templates.get(0).getContent(), "utf-8");
				Map<String, Object> map = new HashMap<String, Object>();
				if (StringUtil.isNotNullForList(galleries)) {
					map = resultMapList(galleries);
				}
				BGproductAttrValuesExample attrValuesExample = new BGproductAttrValuesExample();
				attrValuesExample.setOrderByClause("sort_order DESC");
				com.work.shop.bean.bgcontentdb.BGproductAttrValuesExample.Criteria criteria = attrValuesExample.or();
				criteria.andGoodsSnEqualTo(goodsSn);
				List<BGproductAttrValues> attrValues = this.productAttrValuesMapper.selectByExample(attrValuesExample);
				map.put("attrValues", attrValues);
				try {
					if (StringUtil.isTaoBaoChannel(channelCode) ) { // 淘宝平台按  模块化解析
						params.put("goodsSn", goodsSn);
						ChannelTemplateContentExample contentExample = new ChannelTemplateContentExample();
						Criteria criteria2 = contentExample.or();
						criteria2.andTemplateCodeEqualTo(managerVo.getTemplateCode());
						List<ChannelTemplateContent> templateContents = channelTemplateContentMapper.selectByExampleWithBLOBs(contentExample);
						List<DescModule> descModules = new ArrayList<DescModule>();
						for (ChannelTemplateContent channelTemplateContent : templateContents) {
							DescModule descModule = new DescModule();
							descModule.setModuleId(channelTemplateContent.getModuleId());
							String subDetail = VelocityUtil.getParserHtml(channelTemplateContent.getModuleContent(), goodsProperty, map);
							descModule.setContent(subDetail);
							descModules.add(descModule);
						}
						html = JSON.toJSONString(descModules);
					} else {
						html = VelocityUtil.getParserHtml(content, goodsProperty, map);
					}
					// 保存模板详情
					if (StringUtil.isNotNull(html)) {
						ChannelGoodsExtensionExample extensionExample = new ChannelGoodsExtensionExample();
						com.work.shop.bean.ChannelGoodsExtensionExample.Criteria criteria3 = extensionExample.or();
						criteria3.andGoodsSnEqualTo(goodsSn);
						criteria3.andChannelCodeEqualTo(shopCode);
						List<ChannelGoodsExtension> extensions = this.channelGoodsExtensionMapper.selectByExampleWithBLOBs(extensionExample);
						ChannelGoodsExtension extension = new ChannelGoodsExtension();
						int count = 0;
						if (StringUtil.isNotNullForList(extensions)) {
							extension = extensions.get(0);
							extension.setTplName(templates.get(0).getTemplateName());
							
					//		if(2 == templates.get(0).getTemplateType()){
//								extension.setPhoneGoodsDesc(html);
				//			} else{
								extension.setGoodsDesc(html);
					//		}
					
							extension.setLastUpdateTime((int)TimeUtil.parseDateToNumeric(new Date()));
							count = this.channelGoodsExtensionMapper.updateByPrimaryKeyWithBLOBs(extension);
						} else {
							extension.setGoodsSn(goodsSn);
							extension.setChannelCode(shopCode);
							extension.setSortOrder((short)0);
							extension.setTplName(templates.get(0).getTemplateName());
					//		if(2 ==templates.get(0).getTemplateType()){
//								extension.	extension.setGoodsDesc(html);(html);
					//		} else{
								extension.setGoodsDesc(html);
					//		}
							
							extension.setAddTime((int)TimeUtil.parseDateToNumeric(new Date()));
							count = this.channelGoodsExtensionMapper.insertSelective(extension);
						}
						if (count == 1) {
							ChannelApiLog apiLog = new ChannelApiLog();
							apiLog.setChannelCode(channelCode);
							apiLog.setShopCode(shopCode);
							apiLog.setRequestTime(new Date());
							apiLog.setParamInfo(goodsSnTicketCode);
							apiLog.setMethodName("9");
							apiLog.setReturnCode("0");
							apiLog.setReturnMessage("生成宝贝详情成功！");
							apiLog.setUser(actionUser);
							channelApiLogMapper.insertSelective(apiLog);
						}
					}
					logger.info("调整单" + goodsSnTicketCode + "生成宝贝详情成功");
				} catch (Exception e) {
					logger.error("生成商品详情失败 "+ ExceptionStackTraceUtil.getExceptionTrace(e));
					ChannelApiLog apiLog = new ChannelApiLog();
					apiLog.setChannelCode(channelCode);
					apiLog.setShopCode(shopCode);
					apiLog.setRequestTime(new Date());
					apiLog.setParamInfo(goodsSnTicketCode);
					apiLog.setMethodName("9");
					apiLog.setReturnCode("1");
					apiLog.setReturnMessage("生成宝贝详情失败 " + e.getMessage());
					channelApiLogMapper.insertSelective(apiLog);
				}
			} else {
				ChannelApiLog apiLog = new ChannelApiLog();
				apiLog.setChannelCode(channelCode);
				apiLog.setShopCode(shopCode);
				apiLog.setRequestTime(new Date());
				apiLog.setParamInfo(goodsSnTicketCode);
				apiLog.setMethodName("9");
				apiLog.setReturnCode("1");
				apiLog.setReturnMessage("生成宝贝详情失败:goodsSn="+goodsSn+" 在product_goods表中不存在！");
				channelApiLogMapper.insertSelective(apiLog);
			}
//		}
	}
	
	/**
	 * 商品信息列表
	 * @param goods
	 * @param helper
	 * @return Pagination
	 * @throws Exception
	 */
	@Override
	public Pagination getChannelGoodsList(ChannelGoods goods, PageHelper helper) throws Exception {
		Pagination pagination = new Pagination(helper.getStart(), helper.getLimit());
		ChannelGoodsExample example = new ChannelGoodsExample();
		com.work.shop.bean.ChannelGoodsExample.Criteria criteria = example.or();
		criteria.limit(helper.getStart(), helper.getLimit());
		if (StringUtil.isNotEmpty(goods.getGoodsSn())) {
			criteria.andGoodsSnEqualTo(SqlUtils.getLike(goods.getGoodsSn()));
		}
		if (StringUtil.isNotEmpty(goods.getChannelCode())) {
			criteria.andChannelCodeEqualTo(goods.getChannelCode());
		}
		pagination.setData(this.channelGoodsMapper.selectByExample(example));
		pagination.setTotalSize(this.channelGoodsMapper.countByExample(example));
		return pagination;
	}
	
	
	/**
	 * 根据GoodsSn获取商品信息列表
	 * @param goods
	 * @param idList
	 * @return List<ChannelGoods>
	 * @throws Exception
	 */
	@Override
	public List<ChannelGoods> getGoodsListByGoodsSn(ChannelGoods goods, List<String> idList) throws Exception {
		ChannelGoodsExample example = new ChannelGoodsExample();
		com.work.shop.bean.ChannelGoodsExample.Criteria criteria = example.or();
		if (StringUtil.isNotNullForList(idList)) {
			criteria.andGoodsSnIn(idList);
		}
		if (StringUtil.isNotEmpty(goods.getChannelCode())) {
			criteria.andChannelCodeEqualTo(goods.getChannelCode());
		}
		return this.channelGoodsMapper.selectByExample(example);
	}

	
	
	@Override
	public void createTemplate(QueueManagerVo managerVo) {
		if (null == managerVo) {
			return ;
		}
		try {
			ChannelTemplate template = new ChannelTemplate();
			template.setTemplateCode(template.getTemplateCode());
			// 获取调整单信息
			createGoodsDetail(managerVo, null);
			/*for (String ticketCode : managerVo.getTicketList()) {
				// 调整单信息分页查询
				TicketInfoExample example = new TicketInfoExample();
				com.work.shop.bean.TicketInfoExample.Criteria criteria = example.or();
				if(StringUtil.isNotNull(ticketCode)){
					criteria.andTicketCodeVoEqualTo(ticketCode);
				} else {
					return ;
				}
				int totalSize = ticketInfoMapper.countByVo(example);
				int pageSize = 1000;
				int totalPages = (totalSize % pageSize == 0? totalSize/pageSize : totalSize/pageSize + 1);
				for (int i = 1; i <= totalPages; i++) {
					int start = (i-1)*pageSize;
					TicketInfoExample ticketInfoExample = new TicketInfoExample();
					com.work.shop.bean.TicketInfoExample.Criteria criteria2 = ticketInfoExample.or();
					criteria2.andTicketCodeEqualTo(ticketCode);
					criteria2.limit(start, pageSize);
					try {
						List<TicketInfo> ticketInfos = ticketInfoMapper.selectByExample(ticketInfoExample);
						createGoodsDetail(managerVo, ticketInfos);
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(e);
					}
				}
			}*/
		} catch (Exception e) {
			logger.error("创建宝贝详情异常" + ExceptionStackTraceUtil.getExceptionTrace(e));
		}
	}

	private Map<String, Object> resultMapList(List<ProductGoodsGalleryVo> galleries) {
		// 模特图列表
		List<ProductGoodsGalleryVo> modelImgs = new ArrayList<ProductGoodsGalleryVo>();
		// 细节正反面图列表
		List<ProductGoodsGalleryVo> detailPoNeImgs = new ArrayList<ProductGoodsGalleryVo>();
		// 细节图列表
		List<ProductGoodsGalleryVo> detailImgs = new ArrayList<ProductGoodsGalleryVo>();
		// 颜色图列表
		List<ProductGoodsGalleryVo> colorImgs = new ArrayList<ProductGoodsGalleryVo>();
		
		Map<String , Object> map = new HashMap<String, Object>();
		Map<String , Object> distMap = new HashMap<String, Object>();
		for (ProductGoodsGalleryVo gallery : galleries) {
			// 按静态图规则将图片分类
			try {
				if (null == gallery || StringUtil.isEmpty(gallery.getImgUrl())) {
					continue ;
				} else {
					String imgPath = gallery.getImgUrl().toUpperCase();
					if (!distMap.isEmpty() && null != distMap.get(imgPath)) {
						ProductGoodsGalleryVo tempObj = (ProductGoodsGalleryVo) distMap.get(imgPath);
						if (tempObj.getType() == gallery.getType()) {
							continue;
						}
					} else {
						distMap.put(imgPath, gallery);
					}
				}
				String imgUrl = gallery.getImgUrl();
				int preNum = imgUrl.lastIndexOf("/");
				int sufNum = imgUrl.lastIndexOf(".");
				if (preNum == -1 || sufNum ==-1) {
					continue;
				}
				String imgSize = imgUrl.substring(preNum+1, sufNum);
				if (imgSize.indexOf(gallery.getGoodsSn()) == -1) {
					continue ;
				}
				String[] arr = imgSize.split("_");
				if (arr.length == 2) {
					String size = StringUtil.isNotNull(arr[1]) ? arr[1].trim() : "";
					if (size.length() == 1) {
						detailImgs.add(gallery);
					} else {
						int num = Integer.valueOf(size);
						if (num >= 29 && num <= 99) {
							modelImgs.add(gallery);
						} else if (num >= 1 && num <= 2) {
							detailPoNeImgs.add(gallery);
						} else if (num >= 3 && num <= 8) {
							detailImgs.add(gallery);
						}
					}
				} else if (arr.length == 3) {
					colorImgs.add(gallery);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("goodsSn="+gallery.getGoodsSn()+" 中图片列表按名字定义格式放入不同数组中出错："+e);
			}
			// 按颜色Code（ColorCode）不为空为颜色图,类型（Type）为3的图为模特图，其他为细节图、其中后缀为goodsSn_01,goodsSn_02的图片为正反面图
//			try {
//				if (null == gallery || StringUtil.isEmpty(gallery.getImgUrl())) {
//					continue ;
//				} else {
//					String imgPath = gallery.getImgUrl().toUpperCase();
//					if (!distMap.isEmpty() && null != distMap.get(imgPath)) {
//						ProductGoodsGallery tempObj = (ProductGoodsGallery) distMap.get(imgPath);
//						if (tempObj.getType() == gallery.getType()) {
//							continue;
//						}
//					} else {
//						distMap.put(imgPath, gallery);
//					}
//				}
//				if (StringUtil.isNotEmpty(gallery.getColorCode())) {
//					colorImgs.add(gallery);
//				} else if (null != gallery.getType() && gallery.getType() == 3) {
//					modelImgs.add(gallery);
//				} else {
//					String imgUrl = gallery.getImgUrl();
//					int preNum = imgUrl.lastIndexOf("/");
//					int sufNum = imgUrl.lastIndexOf(".");
//					if (preNum == -1 || sufNum ==-1) {
//						continue;
//					}
//					String imgName = imgUrl.substring(preNum+1, sufNum);
//					if (imgName.equals(gallery.getGoodsSn()+"_01") ||
//							imgName.equals(gallery.getGoodsSn()+"_02")) {
//						detailPoNeImgs.add(gallery);
//					} else {
//						detailImgs.add(gallery);
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error("goodsSn="+gallery.getGoodsSn()+" 中图片列表按名字定义格式放入不同数组中出错："+e);
//			}
		}
		map.put("modelImgs", modelImgs);
		map.put("detailPoNeImgs", detailPoNeImgs);
		map.put("detailImgs", detailImgs);
		map.put("colorImgs", colorImgs);
		/**
		 *  邦购版模板与渠道版模板区分开，将模特图中的一张图片或者细节正反面图中的一张图片放在编辑推荐出展示（并且图片尺寸有别于模特图和细节正反面图）。
		**/
		List<ProductGoodsGalleryVo> modelImgsCh = new ArrayList<ProductGoodsGalleryVo>();
		ProductGoodsGalleryVo editModelImg = null;
		for (ProductGoodsGalleryVo gallery : modelImgs) {
			if (null == editModelImg) {
				editModelImg = gallery;
			} else {
				modelImgsCh.add(gallery);
			}
		}
		List<ProductGoodsGalleryVo> detailPoNeImgsCh = new ArrayList<ProductGoodsGalleryVo>();
		for (ProductGoodsGalleryVo gallery : detailPoNeImgs) {
			if (null == editModelImg) {
				editModelImg = gallery;
			} else {
				detailPoNeImgsCh.add(gallery);
			}
		}
		map.put("modelImgsCh", modelImgsCh);
		map.put("detailPoNeImgsCh", detailPoNeImgsCh);
		map.put("editModelImg", editModelImg);
		return map;
	}
}
