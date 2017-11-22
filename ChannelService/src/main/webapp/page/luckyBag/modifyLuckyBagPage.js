Ext.onReady(modifyLuckyBag);
function modifyLuckyBag(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	
	var delSubsetCodes = ',';
	var luckyBagSkuValue = '';
		
	var model = Ext.data.Record.create([
		{ name : 'id' },
		{ name : 'luckyBagSku' },
		{ name : 'subsetCode' },
		{ name: 'configGoodsNumber'}
	]);
	
	var reader = new Ext.data.ArrayReader(model);
	
	var dataStore = new Ext.data.GroupingStore({
		reader: reader
	});
	
	var toolbar = new Ext.Toolbar({
		items : [ "菜单栏：", "-", {
			id : 'modifyLuckyBag.add',
			text : '添加子集',
			iconCls : 'add',
			handler : addLuckyBagSubset
		}, {
			id : 'modifyLuckyBag.delete',
			text : '移除子集',
			iconCls : 'delete',
			handler : deleteLuckyBagSubset
		}]
	});
	
	var cb = new Ext.grid.CheckboxSelectionModel();
	
	var modifyLuckyBagSubsetGrid = new Ext.grid.GridPanel({
		id : 'modifyLuckyBag.grid',
		loadMask : true,
		height : 240,
		autoScroll : true,
		frame : true,
		tbar : toolbar,
		store : dataStore,
		autoScroll : true,
		viewConfig : {
			autoFill : true
		},
		sm : cb,
		columns : [ 
		    cb,
		    { header : "id", hidden: true, width : 50, dataIndex : 'id' },
		    { header : "福袋编码", hidden: true,  width : 50, dataIndex : 'luckyBagSku' },
			{ header : "子集编码", width : 50, dataIndex : 'subsetCode' },
			{ header : "子集抽取商品数", width : 50, dataIndex : 'configGoodsNumber' },
		],
		listeners : {
			'rowdblclick' : function(grid, rowIndex, e) {
				var record = grid.getSelectionModel().getSelected();
				modifyLuckyBagSubset(record);
			}
		}
	});
	
	var modifyLuckyBagPanelForm = new Ext.FormPanel({
		frame : true,
		forceFit : true,
		autoScroll : false,
		autoWidth : true,
		autoHeight : true,
		labelAlign : "right",
		waitMsgTarget : true,
		bodyBorder : false,
		applyTo : 'modify_luckybag_panel_div',
		border : true,
		items : [{
			xtype:'fieldset', 
			title:'福袋基本信息', 
			collapsible:false, 
			autoHeight:true, 
			autoWidth:true,
			items : [{
				layout : 'column',
				border : false,
				items : [{
					columnWidth : .33,
					layout : 'form',
					items : [ {
						id : 'modifyLuckyBag.warehouseId',
						xtype : 'textfield',
						maxLength : 11,
						anchor : '100%',
						name : 'warehouseId',
						readOnly : true,
						fieldLabel : '仓库ID'
					}]
				},{
					columnWidth : .34,
					layout : 'form',
					items : [ {
						id : 'modifyLuckyBag.luckyBagSku',
						xtype : 'textfield',
						anchor : '100%',
						name : 'luckyBagSku',
						readOnly : true,
						fieldLabel : '福袋SKU'
					}]
				},{
					columnWidth : .33,
					layout : 'form',
					items : [ {
						id : 'modifyLuckyBag.luckyBagPrice',
						xtype : 'numberfield',
						anchor : '100%',
						allowDecimals: true,
				        decimalPrecision: 2,
						name : 'luckyBagPrice',
						fieldLabel : '福袋售价'
					}]
				}]
			}]
		},{
			xtype:'fieldset', 
			title:'福袋子集信息', 
			collapsible:false, 
			autoHeight:true, 
			autoWidth:true,
			items : [modifyLuckyBagSubsetGrid]
		}],
		buttonAlign : "center",
		buttons : [ {
			text : '保存',
			handler : saveLuckyBag
		}, {
			text : '关闭',
			handler : function() {
				parent.FormEditWin.close();
			}
		} ]
	});	
	
	
	function modifyLuckyBagSubset(record){
		var oriCode = record.get('subsetCode');
		var oriGoodsNumber = record.get('configGoodsNumber');
		// 创建弹出窗口
		var win = new Ext.Window({
			layout : 'fit',
			width : 750,
			closeAction : 'hide',
			height : 150,
			resizable : false,
			shadow : true,
			modal : true,
			closable : true,
			bodyStyle : 'padding:5 5 5 5',
			animCollapse : true,
			items : [{
				xtype: "form",
				border: false,
				frame: true,
				labelAlign : "right",
				items: [{
					layout : 'column',
					border : false,
					items : [{
						columnWidth : .5,
						layout : 'form',
						items : [ {
							id : 'modifyLuckyBag.modify.subsetCode',
							xtype : 'textfield',
							maxLength : 11,
							anchor : '100%',
							name : 'subsetCode',
							fieldLabel : '子集编码',
							readOnly : true,
							value : oriCode
						}]
					},{
						columnWidth : .5,
						layout : 'form',
						items : [ {
							id : 'modifyLuckyBag.modify.configGoodsNumber',
							xtype : 'numberfield',
							anchor : '100%',
							allowDecimals: false,
							name : 'configGoodsNumber',
							fieldLabel : '子集抽取商品数',
							value : oriGoodsNumber
						}]
					}]
				}],
				buttonAlign : "center",
				buttons : [ {
					text : '提交',
					handler : function() {
						var subsetCode = getValueById("modifyLuckyBag.modify.subsetCode").replace(/\s/g, "");
						var configGoodsNumber = getValueById("modifyLuckyBag.modify.configGoodsNumber");
						if(!subsetCode||!configGoodsNumber){
							Ext.Msg.alert("提示", '子集编码和子集抽取商品数必填！');
							return;
						}
						if(configGoodsNumber&&configGoodsNumber>5){
							Ext.Msg.alert("提示", '子集抽取商品数不能超过5件！');
							return;
						}
						var theStore =  modifyLuckyBagSubsetGrid.store;
						var index = 0;
						for(var i=0;i<theStore.getCount();i++){
							var thecode = theStore.getAt(i).get('subsetCode');
							if(subsetCode==thecode){
								index = i;
							}
						}
						theStore.getAt(index).set("configGoodsNumber",configGoodsNumber);
						win.close();					
					}
				}, {
					text : '关闭',
					handler : function() {
						win.close();
					}
				}]
			}]
		});
		win.show();
	}
	
	function deleteLuckyBagSubset(){
		var recs = modifyLuckyBagSubsetGrid.getSelectionModel().getSelections();
		var num = recs.length;
		if (num == 0) {
			Ext.MessageBox.alert('提示', '请选择要移除的数据!');
			return;
		}
		Ext.MessageBox.confirm("提示","删除已入库子集会同时删除该子集下的商品,确定删除吗？",function(btnId) {
			if (btnId == 'yes') {
				for ( var i = 0; i < recs.length; i++) {
					var subsetCode = recs[i].get('subsetCode');
					if(subsetCode!=''){
						delSubsetCodes=delSubsetCodes+subsetCode+',';
					}
					dataStore.remove(recs[i]);
				}
			}
		});
	}
	
	function addLuckyBagSubset(){
		if(modifyLuckyBagSubsetGrid.store.getCount()==4){
			Ext.Msg.alert("提示", '一个福袋最多四个子集！');
			win.close();
			return;
		}
		// 创建弹出窗口
		var win = new Ext.Window({
			layout : 'fit',
			width : 750,
			closeAction : 'hide',
			height : 150,
			resizable : false,
			shadow : true,
			modal : true,
			closable : true,
			bodyStyle : 'padding:5 5 5 5',
			animCollapse : true,
			items : [{
				xtype: "form",
				border: false,
				frame: true,
				labelAlign : "right",
				items: [{
					layout : 'column',
					border : false,
					items : [{
						columnWidth : .5,
						layout : 'form',
						items : [ {
							id : 'modifyLuckyBag.add.subsetCode',
							xtype : 'textfield',
							maxLength : 11,
							anchor : '100%',
							name : 'subsetCode',
							fieldLabel : '子集编码'
						}]
					},{
						columnWidth : .5,
						layout : 'form',
						items : [ {
							id : 'modifyLuckyBag.add.configGoodsNumber',
							xtype : 'numberfield',
							anchor : '100%',
							allowDecimals: false,
							name : 'configGoodsNumber',
							fieldLabel : '子集抽取商品数'
						}]
					}]
				}],
				buttonAlign : "center",
				buttons : [ {
					text : '提交',
					handler : function() {
						var subsetCode = getValueById("modifyLuckyBag.add.subsetCode").replace(/\s/g, "");
						var configGoodsNumber = getValueById("modifyLuckyBag.add.configGoodsNumber");
						if(!subsetCode||!configGoodsNumber){
							Ext.Msg.alert("提示", '子集编码和子集抽取商品数必填！');
							return;
						}
						if(configGoodsNumber&&configGoodsNumber>5){
							Ext.Msg.alert("提示", '子集抽取商品数不能超过5件！');
							return;
						}
						var theStore =  modifyLuckyBagSubsetGrid.store;
						for(var i=0;i<theStore.getCount();i++){
							var thecode = theStore.getAt(i).get('subsetCode');
							if(thecode==subsetCode){
								Ext.Msg.alert("提示", '子集编码重复！');
								return;
							}
						}
						if(delSubsetCodes.indexOf(','+subsetCode+',')>=0){
							Ext.Msg.alert("提示", '与已删除子集编码重复！');
							return;
						}
						//把数据添加到store中
						var data ={'id':'','luckyBagSku':luckyBagSkuValue,'subsetCode':subsetCode,'configGoodsNumber':configGoodsNumber};      
			            var rs = [new Ext.data.Record(data)];  
			            modifyLuckyBagSubsetGrid.store.insert(0,rs);
						win.close();					
					}
				}, {
					text : '关闭',
					handler : function() {
						win.close();
					}
				}]
			}]
		});
		win.show();
	}
	
	function saveLuckyBag(){
		var params = {};
		//获取值
		var warehouseId = getValueById("modifyLuckyBag.warehouseId");
		var luckyBagSku = getValueById("modifyLuckyBag.luckyBagSku");
		var luckyBagPrice = getValueById("modifyLuckyBag.luckyBagPrice");
		var theStore =  modifyLuckyBagSubsetGrid.store;
		var count = theStore.getCount();
		
		//验证
		if(!luckyBagPrice){
			Ext.Msg.alert("提示", '福袋售价必填！');
			return;
		}
		if(count<=0){
			Ext.Msg.alert("提示", '至少设置一个福袋子集！');
			return;
		}
		
		//拼装入参
		var array = new Array();
		theStore.each(function(record) {
			array.push(record.data);
		});
		
		Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url : basePath + '/custom/luckyBag/saveModifiedLuckyBag.spmvc',
			params : {
				jsonString : Ext.encode(array),
				warehouseId : warehouseId,
				luckyBagSku : luckyBagSku,
				luckyBagPrice : luckyBagPrice,
				delSubsetCodes : delSubsetCodes
			},
			method : "post",
			success : function(response) {
				var result = Ext.util.JSON.decode(response.responseText);
				if(result.code=='1'){
					parent.Ext.getCmp("luckyBag.grid").getStore().reload();
					parent.FormEditWin.close();
				}else{
					alertMsg("结果", result.msg);
				}
			},
			failure : function() {
				errorMsg("结果", "修改福袋失败！");
			}
		});
	}
	
	function initData(){
		if(luckyBagInfo){
			var luckyBagBean = Ext.util.JSON.decode(luckyBagInfo);
			luckyBagSkuValue = luckyBagBean.luckyBagSku;
			Ext.getCmp('modifyLuckyBag.warehouseId').setValue(luckyBagBean.warehouseId);
			Ext.getCmp('modifyLuckyBag.luckyBagSku').setValue(luckyBagBean.luckyBagSku);
			Ext.getCmp('modifyLuckyBag.luckyBagPrice').setValue(luckyBagBean.luckyBagPrice);
		}
		if(luckyBagSubsetList){
			var luckyBagSubsetData = Ext.util.JSON.decode(luckyBagSubsetList);
			for ( var i = 0; i < luckyBagSubsetData.length; i++) {
				var data ={'id':luckyBagSubsetData[i].id,'luckyBagSku':luckyBagSubsetData[i].luckyBagSku,'subsetCode':luckyBagSubsetData[i].subsetCode,'configGoodsNumber':luckyBagSubsetData[i].configGoodsNumber};      
	            var rs = [new Ext.data.Record(data)];
	            dataStore.add(rs);
			}
		}
	}
	initData();
	
}