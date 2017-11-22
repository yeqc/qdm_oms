package com.work.shop.vo;

public class ApiInfoVo {
	
	private int id;
	private String channel_title;//渠道名称 ，淘宝，京东等
	private String api_type; //接口类型 add，update..
    private String api_property; //接口参数
    private String api_property_type ; //接口参数类型
    private int  is_need; //是否是必填项   
    private String channel_goods_property; //对应的channelGoods表字段
    private String api_property_desc ; //接口参数说明
    
    private String api_url; //接口地址
    
    
	public String getApi_url() {
		return api_url;
	}
	public void setApi_url(String api_url) {
		this.api_url = api_url;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getChannel_title() {
		return channel_title;
	}
	public void setChannel_title(String channel_title) {
		this.channel_title = channel_title;
	}
	public String getApi_type() {
		return api_type;
	}
	public void setApi_type(String api_type) {
		this.api_type = api_type;
	}
	public String getApi_property() {
		return api_property;
	}
	public void setApi_property(String api_property) {
		this.api_property = api_property;
	}
	public String getApi_property_type() {
		return api_property_type;
	}
	public void setApi_property_type(String api_property_type) {
		this.api_property_type = api_property_type;
	}
	public int getIs_need() {
		return is_need;
	}
	public void setIs_need(int is_need) {
		this.is_need = is_need;
	}
	public String getChannel_goods_property() {
		return channel_goods_property;
	}
	public void setChannel_goods_property(String channel_goods_property) {
		this.channel_goods_property = channel_goods_property;
	}
	public String getApi_property_desc() {
		return api_property_desc;
	}
	public void setApi_property_desc(String api_property_desc) {
		this.api_property_desc = api_property_desc;
	}
    
	
    
    
}  
