package com.work.shop.vo;
/**
 * 用于渠道同步库存中心的工具类
 * @author Administrator
 *
 */
public class ScChannelInfo {
	/**
	 * 操作标准位 ‘0’insert；‘1’update；‘2’active
	 */
	private int operateFlag;
	/**
	 * 渠道所属
	 */
	private String insert_channel_platform;
	/**
	 * 渠道号
	 */
	private String insert_channel_code;
	/**
	 * 父级渠道
	 */
	private String parent_channel_code;
	/**
	 * 渠道名称
	 */
	private String insert_channel_name;
    /**
     * 
     * 是否激活0：未激活 1：激活
     */
	private int sync_flag;
	
	/**
	 * 操作人
	 */
	private String action_user;
	
	public int getOperateFlag() {
		return operateFlag;
	}
	public void setOperateFlag(int operateFlag) {
		this.operateFlag = operateFlag;
	}
	public String getInsert_channel_platform() {
		return insert_channel_platform;
	}
	public void setInsert_channel_platform(String insert_channel_platform) {
		this.insert_channel_platform = insert_channel_platform;
	}
	public String getInsert_channel_code() {
		return insert_channel_code;
	}
	public void setInsert_channel_code(String insert_channel_code) {
		this.insert_channel_code = insert_channel_code;
	}
	public String getParent_channel_code() {
		return parent_channel_code;
	}
	public void setParent_channel_code(String parent_channel_code) {
		this.parent_channel_code = parent_channel_code;
	}
	public String getInsert_channel_name() {
		return insert_channel_name;
	}
	public void setInsert_channel_name(String insert_channel_name) {
		this.insert_channel_name = insert_channel_name;
	}
	public int getSync_flag() {
		return sync_flag;
	}
	public void setSync_flag(int sync_flag) {
		this.sync_flag = sync_flag;
	}
	public String getAction_user() {
		return action_user;
	}
	public void setAction_user(String action_user) {
		this.action_user = action_user;
	}
}
