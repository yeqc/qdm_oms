Ext.onReady(addLuckyBag);
function addLuckyBag(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
			
	var model = Ext.data.Record.create([
		{ name : 'subsetCode' },
		{ name: 'configGoodsNumber'}
	]);
	
	var reader = new Ext.data.ArrayReader({idIndex: 0},model);
	
	var dataStore = new Ext.data.GroupingStore({
		reader: reader
	});
	
	var toolbar = new Ext.Toolbar({
		items : [ "菜单栏：", "-", {
			id : 'addLuckyBag.add',
			text : '添加子集',
			iconCls : 'add',
			handler : addLuckyBagSubset
		}, {
			id : 'addLuckyBag.delete',
			text : '移除子集',
			iconCls : 'delete',
			handler : deleteLuckyBagSubset
		}]
	});
	
	var cb = new Ext.grid.CheckboxSelectionModel();
	
	var addLuckyBagSubsetGrid = new Ext.grid.GridPanel({
		id : 'addLuckyBag.grid',
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
	
	var addLuckyBagPanelForm = new Ext.FormPanel({
		frame : true,
		forceFit : true,
		autoScroll : false,
		autoWidth : true,
		autoHeight : true,
		labelAlign : "right",
		waitMsgTarget : true,
		bodyBorder : false,
		applyTo : 'add_luckyBag_panel_div',
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
					columnWidth : .34,
					layout : 'form',
					items : [{
						id : 'addLuckyBag.panel.warehouseId',
						store : new Ext.data.Store(
								{
									proxy : new Ext.data.HttpProxy(
											{
												url : basePath+ '/custom/luckyBag/getWarehouseList.spmvc',
												method : 'POST'
											}),
									reader : new Ext.data.JsonReader({fields : ['code','name' ]})
								}),
						xtype : 'combo',
						valueField : 'code',
						displayField : 'name',
						name : 'warehouseId',
						mode : 'remote',
						forceSelection : true,
						emptyText : '--请选择--',
						editable : false,
						triggerAction : 'all',
						anchor : '100%',
						fieldLabel : '仓库'
					}]
				},{
					columnWidth : .33,
					layout : 'form',
					items : [ {
						id : 'addLuckyBag.panel.luckyBagSku',
						xtype : 'textfield',
						maxLength : 11,
						anchor : '100%',
						name : 'luckyBagSku',
						fieldLabel : '福袋SKU'
					}]
				},{
					columnWidth : .33,
					layout : 'form',
					items : [ {
						id : 'addLuckyBag.panel.luckyBagPrice',
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
			items : [addLuckyBagSubsetGrid]
		}],
		buttonAlign : "center",
		buttons : [ {
			text : '提交',
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
							id : 'modifyLuckyBagSubset.subsetCode',
							xtype : 'textfield',
							maxLength : 11,
							anchor : '100%',
							name : 'subsetCode',
							fieldLabel : '子集编码',
							value : oriCode
						}]
					},{
						columnWidth : .5,
						layout : 'form',
						items : [ {
							id : 'modifyLuckyBagSubset.configGoodsNumber',
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
						var subsetCode = getValueById("modifyLuckyBagSubset.subsetCode").replace(/\s/g, "");
						var configGoodsNumber = getValueById("modifyLuckyBagSubset.configGoodsNumber");
						if(!subsetCode||!configGoodsNumber){
							Ext.Msg.alert("提示", '子集编码和子集抽取商品数必填！');
							return;
						}
						if(configGoodsNumber&&configGoodsNumber>5){
							Ext.Msg.alert("提示", '子集抽取商品数不能超过5件！');
							return;
						}
						var theStore =  addLuckyBagSubsetGrid.store;
						var index = 0;
						for(var i=0;i<theStore.getCount();i++){
							var thecode = theStore.getAt(i).get('subsetCode');
							if(oriCode==thecode){
								index = i;
							}
							if(oriCode!=thecode&&thecode==subsetCode){
								Ext.Msg.alert("提示", '子集编码重复！');
								return;
							}
						}
						//把数据更新到store中
						theStore.getAt(index).set("subsetCode",subsetCode);
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
		var recs = addLuckyBagSubsetGrid.getSelectionModel().getSelections();
		var num = recs.length;
		if (num == 0) {
			Ext.MessageBox.alert('提示', '请选择要移除的数据!');
			return;
		}
		Ext.MessageBox.confirm("提示","确定删除吗？",function(btnId) {
			if (btnId == 'yes') {
				for ( var i = 0; i < recs.length; i++) {
					dataStore.remove(recs[i]);
				}
			}
		});
	}
	
	function addLuckyBagSubset(){
		if(addLuckyBagSubsetGrid.store.getCount()==4){
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
							id : 'addLuckyBagSubset.subsetCode',
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
							id : 'addLuckyBagSubset.configGoodsNumber',
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
						var subsetCode = getValueById("addLuckyBagSubset.subsetCode").replace(/\s/g, "");
						var configGoodsNumber = getValueById("addLuckyBagSubset.configGoodsNumber");
						if(!subsetCode||!configGoodsNumber){
							Ext.Msg.alert("提示", '子集编码和子集抽取商品数必填！');
							return;
						}
						if(configGoodsNumber&&configGoodsNumber>5){
							Ext.Msg.alert("提示", '子集抽取商品数不能超过5件！');
							return;
						}
						var theStore =  addLuckyBagSubsetGrid.store;
						for(var i=0;i<theStore.getCount();i++){
							var thecode = theStore.getAt(i).get('subsetCode');
							if(thecode==subsetCode){
								Ext.Msg.alert("提示", '子集编码重复！');
								return;
							}
						}
						//把数据添加到store中
						var data ={'subsetCode':subsetCode,'configGoodsNumber':configGoodsNumber};      
			            var rs = [new Ext.data.Record(data)];  
			            addLuckyBagSubsetGrid.store.insert(0,rs);
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
		//获取值
		var  warehouseId = getValueById("addLuckyBag.panel.warehouseId");
		var luckyBagSku = getValueById("addLuckyBag.panel.luckyBagSku");
		var luckyBagPrice = getValueById("addLuckyBag.panel.luckyBagPrice");
		var theStore =  addLuckyBagSubsetGrid.store;
		var count = theStore.getCount();
		
		//验证
		if(!warehouseId){
			Ext.Msg.alert("提示", '仓库必选！');
			return;
		}
		if(!luckyBagSku){
			Ext.Msg.alert("提示", '福袋SKU必填！');
			return;
		}
		var reg =/^866\d{8}$/;
		if(!reg.test(luckyBagSku)){
			Ext.Msg.alert("提示", '福袋SKU必须是866开头的11位纯数字！');
			return;
		}
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
			url : basePath + '/custom/luckyBag/addLuckyBag.spmvc',
			params : {
				jsonString : Ext.encode(array),
				warehouseId : warehouseId,
				luckyBagSku : luckyBagSku,
				luckyBagPrice : luckyBagPrice
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
				errorMsg("结果", "添加福袋失败！");
			}
		});
	}
	
	
}