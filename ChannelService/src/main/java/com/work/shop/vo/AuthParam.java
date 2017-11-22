package com.work.shop.vo;

public class AuthParam {
	
	private String authUrl;						// 提交更新授权信息URL
	
	private String grantType;					// 授权类型
	
	private String code;						// 授权请求返回的授权码
	
	private String redirectUri;					// 应用的回调地址
	
	private String clientId;					// 即创建应用时的Appkey
	
	private String clientSecret;				// 即创建应用时的Appsecret
	
	private String state;						// 状态参数，由ISV自定义，颁发授权后会原封不动返回 为店铺code
	
	private String view;						// web 天猫淘宝平台使用
	
	public AuthParam(String authUrl, String grantType, String code,
			String redirectUri, String clientId, String clientSecret,
			String state, String view) {
		super();
		this.authUrl = authUrl;
		this.grantType = grantType;
		this.code = code;
		this.redirectUri = redirectUri;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.state = state;
		this.view = view;
	}
	public AuthParam() {
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	public String getAuthUrl() {
		return authUrl;
	}
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}
}
