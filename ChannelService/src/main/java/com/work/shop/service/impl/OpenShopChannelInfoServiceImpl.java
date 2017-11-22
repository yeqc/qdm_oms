package com.work.shop.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.work.shop.bean.OmsChannelInfo;
import com.work.shop.bean.OmsChannelInfoExample;
import com.work.shop.dao.OmsChannelInfoMapper;
import com.work.shop.service.OpenShopChannelInfoService;
import com.work.shop.util.SqlUtils;
import com.work.shop.util.StringUtil;
import com.work.shop.util.extjs.Paging;
import com.work.shop.vo.JsonResult;
import com.work.shop.vo.OpenShopChannelInfoVO;

@Service("openShopChannelInfoService")
public class OpenShopChannelInfoServiceImpl implements OpenShopChannelInfoService {

	@Resource
	private OmsChannelInfoMapper omsChannelInfoMapper;

	@Override
	public JsonResult searchOpenShopChannelInfoList(OpenShopChannelInfoVO searchVO) {
		JsonResult jsonResult = new JsonResult();
		if (searchVO == null) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("检索条件为空!");
			return jsonResult;
		}
		try {
			Paging page = new Paging();
			OmsChannelInfoExample example1 = new OmsChannelInfoExample();
			OmsChannelInfoExample example2 = new OmsChannelInfoExample();
			com.work.shop.bean.OmsChannelInfoExample.Criteria criteria1 = example1.or();
			com.work.shop.bean.OmsChannelInfoExample.Criteria criteria2 = example2.or();
			if (searchVO != null) {
				if (StringUtil.isNotEmpty(searchVO.getParentChannelCode())) {
					criteria1.andParentChannelCodeEqualTo(searchVO.getParentChannelCode());
					criteria2.andParentChannelCodeEqualTo(searchVO.getParentChannelCode());
				}
				if (StringUtil.isNotEmpty(searchVO.getChannelCode())) {
					criteria1.andChannelCodeEqualTo(searchVO.getChannelCode());
					criteria2.andChannelCodeEqualTo(searchVO.getChannelCode());
				}
				if (StringUtil.isNotEmpty(searchVO.getChannelName())) {
					criteria1.andChannelNameLike(SqlUtils.getLike(searchVO.getChannelName()));
					criteria2.andChannelNameLike(SqlUtils.getLike(searchVO.getChannelName()));
				}
			}
			page.setTotalProperty(omsChannelInfoMapper.countByExample(example1));

			criteria2.limit(searchVO.getStart(), searchVO.getLimit());
			page.setRoot(omsChannelInfoMapper.selectByExample(example2));
			jsonResult.setIsok(true);
			jsonResult.setData(page);
			return jsonResult;
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			jsonResult.setMessage("获取渠道信息列表异常!" + e.getMessage());
		}
		return jsonResult;
	}

	@Override
	public JsonResult searchOnlyChannelInfo(OmsChannelInfo searchVo) {
		JsonResult jsonResult = new JsonResult();
		if (searchVo == null || ((searchVo.getChannelId() == null || searchVo.getChannelId() == 0) && StringUtil.isEmpty(searchVo.getChannelCode()))) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("检索条件为空!");
			return jsonResult;
		}
		try {
			OmsChannelInfoExample example = new OmsChannelInfoExample();
			com.work.shop.bean.OmsChannelInfoExample.Criteria criteria = example.or();
			if (searchVo.getChannelId() != null && searchVo.getChannelId() != 0) {
				criteria.andChannelIdEqualTo(searchVo.getChannelId());
			}
			if (StringUtil.isNotEmpty(searchVo.getChannelCode())) {
				criteria.andChannelCodeEqualTo(searchVo.getChannelCode());
			}

			List<OmsChannelInfo> list = omsChannelInfoMapper.selectByExample(example);
			if (list == null || list.size() != 1) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("没有检索到数据!");
				return jsonResult;
			}

			jsonResult.setIsok(true);
			jsonResult.setData(list.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			jsonResult.setMessage("获取渠道信息列表异常!" + e.getMessage());
		}
		return jsonResult;
	}

	@Override
	public JsonResult searchOpenShopChannelInfo(int id) {
		JsonResult jsonResult = new JsonResult();
		try {
			OmsChannelInfo omsChannelInfo = omsChannelInfoMapper.selectByPrimaryKey(id);
			jsonResult.setIsok(true);
			jsonResult.setData(omsChannelInfo);
		} catch (Exception e) {
			jsonResult.setIsok(false);
			jsonResult.setMessage("根据ID获取渠道信息异常!" + e.getMessage());
		}
		return jsonResult;
	}

	@Override
	public JsonResult addOrUpdateOpenShopChannelInfo(OmsChannelInfo omsChannelInfo) {
		JsonResult jsonResult = new JsonResult();
		try {
			// 判断父店铺编号是否存在
			String parentChannelCode = omsChannelInfo.getParentChannelCode();
			if (StringUtil.isNotEmpty(parentChannelCode) && !parentChannelCode.equals(omsChannelInfo.getChannelCode())) {
				OmsChannelInfoExample example = new OmsChannelInfoExample();
				example.or().andChannelCodeEqualTo(parentChannelCode);
				int count = omsChannelInfoMapper.countByExample(example);
				if (count != 1) {
					jsonResult.setIsok(false);
					jsonResult.setMessage("父渠道编号[" + parentChannelCode + "]不存在！");
					return jsonResult;
				}
			}

			if (omsChannelInfo.getChannelId() != null && omsChannelInfo.getChannelId() > 0) {
				// 数据修改
				OmsChannelInfo chanelInfoBean = omsChannelInfoMapper.selectByPrimaryKey(omsChannelInfo.getChannelId());
				if (!omsChannelInfo.getChannelCode().equals(chanelInfoBean.getChannelCode())) {
					// channel_code 属性被修改，判断新channel_code是否已经存在
					if (null != omsChannelInfo.getChannelCode() && !"".equals(omsChannelInfo.getChannelCode())) {
						OmsChannelInfoExample example = new OmsChannelInfoExample();
						example.or().andChannelCodeEqualTo(omsChannelInfo.getChannelCode());
						int count = omsChannelInfoMapper.countByExample(example);
						if (count != 0) {
							jsonResult.setIsok(false);
							jsonResult.setMessage("该渠道编号已经存在！");
							return jsonResult;
						}
					}
				}
				OmsChannelInfoExample updateExample = new OmsChannelInfoExample();
				updateExample.or().andChannelIdEqualTo(omsChannelInfo.getChannelId());
				omsChannelInfoMapper.updateByExampleSelective(omsChannelInfo, updateExample);
				jsonResult.setIsok(true);
				jsonResult.setMessage("修改渠道信息成功！");
			} else {
				// 数据添加
				// 判断新增数据channel_code是否已经存在
				if (StringUtil.isNotEmpty(omsChannelInfo.getChannelCode())) {
					OmsChannelInfoExample example = new OmsChannelInfoExample(); // 用于查询渠道编号
					example.or().andChannelCodeEqualTo(omsChannelInfo.getChannelCode());
					int count = omsChannelInfoMapper.countByExample(example);
					if (count != 0) {
						jsonResult.setIsok(false);
						jsonResult.setMessage("该渠道编号已经存在！");
						return jsonResult;
					}
				}
				omsChannelInfoMapper.insertSelective(omsChannelInfo);
				jsonResult.setIsok(true);
				jsonResult.setMessage("新增渠道信息成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			jsonResult.setMessage("修改/新增渠道信息异常!" + e.getMessage());
		}
		return jsonResult;
	}

	@Override
	public JsonResult deleteOpenShopChannelInfo(List<String> channelCodeList) {
		JsonResult jsonResult = new JsonResult();
		try {
			if (channelCodeList == null || channelCodeList.size() == 0) {
				jsonResult.setIsok(false);
				jsonResult.setMessage("数据channelCodes为空!");
				return jsonResult;
			}

			OmsChannelInfoExample example = new OmsChannelInfoExample();
			example.or().andChannelCodeIn(channelCodeList);

			int record = omsChannelInfoMapper.deleteByExample(example);
			if (channelCodeList != null && record == channelCodeList.size()) {
				jsonResult.setIsok(true);
				jsonResult.setMessage("数据删除成功！");
			} else {
				jsonResult.setIsok(false);
				jsonResult.setMessage("数据删除失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setIsok(false);
			jsonResult.setMessage("删除渠道信息异常!" + e.getMessage());
		}
		return jsonResult;
	}

	@Override
	public OmsChannelInfo findOmsChannelInfoBychannelcode(String channelcode) {
		OmsChannelInfo omsChannelInfo = new OmsChannelInfo();
		try {
			OmsChannelInfoExample channelExample = new OmsChannelInfoExample();
			channelExample.or().andChannelCodeEqualTo(channelcode);
			List<OmsChannelInfo> omsChannelInfos = omsChannelInfoMapper.selectByExample(channelExample);
			if (!StringUtil.isListNotNull(omsChannelInfos)) {
				return null;
			}
			omsChannelInfo = omsChannelInfos.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return omsChannelInfo;
	}

	@Override
	public JsonResult addOpenShopChannelInfo(List<OmsChannelInfo> omsChannelInfos) {
		JsonResult jsonResult = new JsonResult();
		StringBuffer error = new StringBuffer("");
		for (OmsChannelInfo omsChannelInfo : omsChannelInfos) {
			try {
				omsChannelInfoMapper.insertSelective(omsChannelInfo);
			} catch (Exception e) {
				if(StringUtil.isEmpty(error.toString())){
					error.append(omsChannelInfo.getChannelCode());
				}else{
					error.append(",").append(omsChannelInfo.getChannelCode());
				}
			}
		}
		if(StringUtil.isEmpty(error.toString())){
			jsonResult.setIsok(true);
			jsonResult.setMessage("保存成功！");
		}else{
			jsonResult.setIsok(false);
			jsonResult.setMessage("[" + error.toString() + "]数据添加失败!");
		}
		return jsonResult;
	}
}
