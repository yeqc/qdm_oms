Ext.define('MyDesktop.App', {
	extend: 'Ext.ux.desktop.App',
	requires: [
		'Ext.ux.desktop.ShortcutModel',
		'My_Root.Product_app',
		'My_Root.Import_app',
		'My_Root.ProImport_app',
		'My_Root.System_app',
		'My_Root.Cms_app',
		'My_Root.Chart_app',
		'My_Root.Search_app',
		'My_Root.Mission_app',
		'My_Root.Promotion_app',
		'My_Root.Test_app',
		'MyDesktop.Settings'//修改桌面
	],
	init: function() {
	    var me = this;
	    me.menus = [];
	    Ext.Ajax.request({
		    url:'/Admin/index.php/Public/getDisplayGroup',
			method:'POST',
			timeout:2000,
			async:false,
			success:function(response,opts){
				var ret = eval('('+response.responseText+')');
				for(i in ret){
					var item= ret[i];
					var arg = '';
					if(item.name.indexOf('-') > 0){  //区分CMS 项目
						var args = item.name.split('-');
						item.name = args[0];
						arg = args[1];
					}
					me.menus.push({name:item.title,module:item.name,iconCls:item.cls ||'accordion-shortcut',arg:arg});
					Sys.module[item.name] = true;
				}
			}
	    });
		me.callParent();
	},
	getModules : function(){
		return [
				new My_Root.Product_app(),
				new My_Root.Import_app(),
				new My_Root.ProImport_app(),
				new My_Root.System_app(),
				new My_Root.Cms_app(),
				new My_Root.Search_app(),
				new My_Root.Chart_app(),
				new My_Root.Mission_app(),
				new My_Root.Promotion_app(),
				new My_Root.Test_app()
			   ];
	},
	getDesktopConfig: function () {
		var me = this, ret = me.callParent();	
		return Ext.apply(ret, {
			contextMenuItems: [{
				text: '设置背景',
				handler: me.onSettings,
				scope: me
			}],
			//右键菜单
			shortcuts: Ext.create('Ext.data.Store', {
				model: 'Ext.ux.desktop.ShortcutModel',
				data : me.menus
			}),
			wallpaper: Sys.wallpaper,
			wallpaperStretch: false
		});
	},
	// config for the start menu
	getStartConfig : function() {
		var me = this, ret = me.callParent();
		return Ext.apply(ret, {
			title: '欢迎,'+Sys.user_name,
			iconCls: 'user',
			height: 300,
			toolConfig: {
				width: 100,
				items: [{
					text:'设置',
					iconCls:'settings',
					handler: me.onSettings,
					scope: me
				},{
					text:'修改密码',
					iconCls:'user',
					handler: me.changePassword,
					scope: me
				},'-',{
					text:'退出登录',
					iconCls:'logout',
					handler: me.onLogout,
					scope: me
				}]
			}//开始系统菜单
		});
	},
	getTaskbarConfig: function () {
		var ret = this.callParent();
		return Ext.apply(ret, {
			quickStart: [
				{ name: '清除域名(网站)Varnish缓存', iconCls: 'accordion', module: 'acc-win' },
				{ name: '清除商品图片(6位码)Varnish缓存', iconCls: 'icon-grid', module: 'grid-win'}
			],                                    //快捷菜单
			trayItems: [{ xtype: 'trayclock', flex: 1 }]
		});
	},
	onLogout: function () {
		Ext.Msg.confirm('^_^', '您确定要退出登录吗?',function(btn){
			if(btn == 'yes'){
				Ext.Ajax.request({
					url: APP+'/Public/logout',
					success: function(response){
						var ret = eval("("+response.responseText+")");
						location.href = ret.message;
					}
				});
			}
		});
	},
	onSettings: function () {
		var dlg = new MyDesktop.Settings({
			desktop: this.desktop
		});
		dlg.show();
	},
	changePassword:function(){
		Ext.MessageBox.prompt('Name', '输入新密码:', function(o,text){
			   if(o == 'ok'){
				   Ext.Ajax.request({
					    url:'/Admin/index.php/Public/resetPwd',
						params:{
								  id:Sys.user_id,
								  password:text
							    },
						method:'POST',
						timeout:2000,
						success:function(response,opts){
							var ret = eval('('+response.responseText+')');
							Ext.Msg.alert("提示",ret.message);
						}
				   })
			   }
		 });
	},
	clearSiteVanish:function(){ alert(111)},
	clearImgVanish :function(){alert(022)}
});
