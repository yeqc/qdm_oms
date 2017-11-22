package com.work.shop.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.work.shop.bean.ChannelApiName;
import com.work.shop.service.ChannelApiNameService;
import com.work.shop.util.extjs.Pagination;
import com.work.shop.util.extjs.Paging;

@Controller
@RequestMapping(value= "channelApiName")
public class ChannelApiNameController extends BaseController {

	@Resource(name="channelApiNameService")
	private ChannelApiNameService channelApiNameService;
	protected Pagination pagination;
		
	//查询渠道接口列表
	@RequestMapping(value= "/getChannelApiNameList.spmvc")
	public void list(ChannelApiName channelApiName,HttpServletResponse response){
		System.out.println(System.getProperty("user.dir"));
		if(channelApiName == null){
		  channelApiName = new ChannelApiName();
		}	
		try {
			Paging paging = this.channelApiNameService.getChannelApiList(channelApiName,pagination);
			int record = this.channelApiNameService.count(channelApiName);
			paging.setTotalProperty(record); //总得记录数
			writeJson(paging, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
	}
	
	//添加渠道接口
	@RequestMapping("/addChannelApiName.spmvc")
	public void addChannelApiName(ChannelApiName channelApiName,HttpServletResponse response){
		
		Boolean flag = this.channelApiNameService.isSameName(channelApiName);
		if(flag){
			writeMsg(false, "该渠道接口已经存在！",response);
			
		}else{
			flag = channelApiNameService.addChannelApiName(channelApiName);
			if(flag){ //成功
				writeMsg(true, "添加渠道接口成功！",response);
			}else{
				writeMsg(false, "添加渠道接口失败！",response);
			}
			
		}
		
	}
	
	//查询渠道接口对象信息
	@RequestMapping("selectChannelApiName.spmvc")
	public void  selectChannelApiName(ChannelApiName channelApiName,HttpServletResponse response){
		Paging paging = new Paging();
		try {
		List<ChannelApiName> list = channelApiNameService.getChannelApi(channelApiName);
		if(list != null && list.size() == 1){
			paging.setRoot(list);
			writeJson(paging, response);
			
		}else{
			writeMsg(false, "该渠道接口数据异常！",response);
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
}
