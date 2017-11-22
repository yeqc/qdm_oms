Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	
	var authCommit = new Ext.FormPanel({
		labelleft: 'top',
		frame:true,
		bodyStyle:'padding:5px 5px 0',
		height:490,
//		renderTo: 'addModule-panel',
		autoScroll:true,
		width:780,
		url: authUrl,
		layout:'form',
		labelAlign: "right",
		columnWidth:.8,
		items: [{
			xtype:'textfield',
			fieldLabel: '授权类型',
//			hidden: true,
			id:'auth.grant_type',
			name: 'grant_type',
			value: 'authorization_code',
			width:200
		} , {
			xtype:'textfield',
			fieldLabel: '授权请求返回的授权码',
//			hidden: true,
			id:'auth.code',
			name: 'code',
			value: code,
			width:200
		} , {
			xtype:'textfield',
			fieldLabel: '应用的回调地址',
//			hidden: true,
			id:'auth.redirect_uri',
			name: 'redirect_uri',
			value: redirectUri,
			width:200
		} , {
			xtype:'textfield',
			fieldLabel: '应用Appkey',
//			hidden: true,
			id:'auth.client_id',
			name: 'client_id',
			value: clientId,
			width:200
		} , {
			xtype:'textfield',
			fieldLabel: '应用Appsecret',
//			hidden: true,
			id:'auth.client_secret',
			name: 'client_secret',
			value: clientSecret,
			width:200
		} , {
			xtype:'textfield',
			fieldLabel: '状态参数',
//			hidden: true,
			id:'auth.state',
			name: 'state',
			value: state,
			width:200
		} , {
			xtype:'textfield',
			fieldLabel: 'view (淘宝天猫平台使用)',
//			hidden: true,
			id:'auth.view',
			name: 'view',
			value: view,
			width:200
		}],
		buttons: [{
			text: '保存',
			handler:function(){
				if(!authCommit.getForm().isValid()){
					alertMsg("验证", "请检查数据是否校验！");
					return;
				}
				var json = {
					success: function(authCommit, action){
						Ext.Msg.alert('结果',action,function(xx){
							alert(action);
							// 提交至后台保存
						});
					},
					failure: function(authCommit, action){
						errorMsg("结果", action.response.statusText);
					}
					,waitMsg:'Loading...'
				};
				authCommit.getForm().submit(json);
			}
		}]
	});
	authCommit.render(document.body);
});