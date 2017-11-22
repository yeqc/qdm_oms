package com.work.shop.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.work.shop.bean.ChannelApiName;
import com.work.shop.dao.ChannelApiNameMapper;
import com.work.shop.service.ChannelApiNameService;
import com.work.shop.util.extjs.PageHelper;
import com.work.shop.util.extjs.Pagination;
import com.work.shop.util.extjs.Paging;

@Service("channelApiNameService")
public class ChannelApiNameServiceImpl implements ChannelApiNameService{
	
	@Resource(name="channelApiNameMapper")
	private ChannelApiNameMapper channelApiNameMapper;
	
	
	/**
	 * 渠道接口查询
	 * @param channelApiName
	 * @return
	 */
	@Override
	public List<ChannelApiName> getChannelApi(ChannelApiName channelApiName){
	//	List<ChannelApiName> list = channelApiNameMapper.selectByChannelApiName(channelApiName);
		return null;
	}
	
	@Override
	public Paging getChannelApiList(ChannelApiName channelApiName,Pagination pagination){
		List<ChannelApiName> list = new ArrayList<ChannelApiName>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start",pagination.getCurrentPage() );
		map.put("pageSize", pagination.getPageSize());
		map.put("sort_col", pagination.getSorts());
		map.put("sort_type", "desc");
	//	list = channelApiNameMapper.selectByChannelApiName(channelApiName);
		pagination.setData(list);
		return PageHelper.getPaging(pagination);
	}
	
	/**
	 * 渠道接口列表记录数量查询
	 * @param channelApiName
	 * @return
	 */
	@Override
	public int count(ChannelApiName channelApiName){
		
		int record =0;
		//channelApiNameMapper.countByExample(channelApiName);
		return record;
		
	}
	/**
	 * 添加渠道接口
	 * @param channelApiName
	 * @return
	 */
	@Override
	public Boolean addChannelApiName(ChannelApiName channelApiName){
		
		int f = channelApiNameMapper.insert(channelApiName);
		if(f>0){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 修改渠道接口
	 * @param channelApiName
	 * @return
	 */
   @Override
   public Boolean updateChannelApiName(ChannelApiName channelApiName){
	   int f = channelApiNameMapper.updateByPrimaryKey(channelApiName);
	   if(f>0){
			return true;
		}else{
			return false;
		}
   }
   
	/**
	 * 是否重名渠道接口
	 * @param channelApiName
	 * @return
	 */
  @Override
  public Boolean isSameName(ChannelApiName channelApiName){
	  
	  List<ChannelApiName> list = null;
	  //channelApiNameMapper.selectByChannelApiName(channelApiName);
	  if(list != null && list.size() > 0){		  
		  return true;		  
	  }else{
		  return false;
	  }
  }
   
   
   
   
}
