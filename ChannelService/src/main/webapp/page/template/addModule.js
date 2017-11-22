Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var url = basePath+'/custom/channelTemplate/addModule.spmvc?method=add';
	var id = $("#moduleId").val();
	if (id != null && id != '') {
		url = basePath+'/custom/channelTemplate/addModule.spmvc?method=update';
	}
	//模块类型选择框
	var mTypeSelectOption = new Ext.form.ComboBox({
			id : 'channelTemplateModule.moduleType',
			store : new Ext.data.SimpleStore( {
				data : [ [ '1', '商品信息' ], [ '2', '模特展示' ] , [ '3', '编辑推荐' ] , [ '4', '模特信息' ], [ '5', '细节展示' ], 
					[ '6', '颜色选择' ], [ '7', '尺码规格' ]], 
				fields : [ 'text', 'filed' ]
			}),
			xtype : 'combo',
			valueField : 'text',
			displayField : 'filed',
			mode : 'local',
			forceSelection : true,
			blankText : '请选择模块类型',
			emptyText : '模块类型',
			name : 'moduleType',
			editable : false,
			hiddenName : 'moduleType',
			triggerAction : 'all', 
			fieldLabel : '模块类型',
			width : 150
	});
	
	//模板大小
/*	var moduleSizeSelectOption = new Ext.form.ComboBox({
		id : 'channelTemplateModule.moduleSize',
		store : new Ext.data.SimpleStore( {
			data : [ 
			         [ '0', '电脑版' ], [ '1', '移动版' ]
			       ], 
			fields : [ 'text', 'filed' ]
		}),
		xtype : 'combo',
		valueField : 'text',
		displayField : 'filed',
		mode : 'local',
		forceSelection : true,
		blankText : '请选择应用设备类型',
		emptyText : '应用设备类型',
		name : 'moduleSize',
		editable : false,
		hiddenName : 'moduleSize',
		triggerAction : 'all', 
		fieldLabel : '应用设备类型',
		width : 150
	});
	moduleSizeSelectOption.setValue(0);*/
	
	var addModule = new Ext.FormPanel({
		labelleft: 'top',
		frame:true,
		bodyStyle:'padding:5px 5px 0',
		height:490,
		renderTo: 'addModule-panel',
		autoScroll:true,
		width:780,
		url: url,
		layout:'form',
		labelAlign: "right",
		columnWidth:.8,
		items: [{
			columnWidth:.8,
			layout: 'column',
			items: [{
				layout: 'form',
				items: [{
					xtype : 'textfield',
					fieldLabel : '模块名称',
					id:'module.moduleName',
					name : 'moduleName',
					allowBlank : false,
					width : '200'
				} , {
					xtype : 'textfield',
					fieldLabel : '应用店铺',
					id:'module.shopTitle',
					readOnly : true,
					value : parent.getGlobalValue('shopTitle'),
					width : '200'
				} , {
					xtype : 'textfield',
					fieldLabel : '所属渠道',
					id:'module.channelTitle',
					readOnly : true,
					value : parent.getGlobalValue('channelTitle'),
					width : '200'//,
					//value:'${scs.shopCode}'
				},
				mTypeSelectOption
				//,moduleSizeSelectOption
				]
			}, {
				layout: 'form',
				items: [{
					xtype : 'textarea',
					fieldLabel : '淘宝模块名称',
					id:'module.tbModuleName',
					name : 'tbModuleName',
					value : $("#tbModuleName").val(),
					width : 250,
					height: 120
				}]
			}]
		}, {
				xtype : 'textarea',
				fieldLabel : '模块内容',
				id:'module.moduleContent',
				name : 'moduleContent',
				value: $("#moduleContent").val(),
				allowBlank : true,
				width: 580,
				height: 300
			} , {
				xtype:'textfield',
				fieldLabel: '模块ID',
				hidden: true,
				id:'module.id',
				name: 'id',
				value: id,
				width:200
			} , {
				xtype:'textfield',
				fieldLabel: '渠道编码',
				hidden: true,
				id:'module.channelCode',
				name: 'channelCode',
				value:$("#channelCode").val(),
				width:200
			} , {
				xtype:'textfield',
				fieldLabel: '店铺编号',
				hidden: true,
				id:'module.shopCode',
				name: 'shopCode',
				value: parent.getGlobalValue('shopCode'),
				width:200
			} , {
				xtype:'textfield',
				fieldLabel: '渠道编号',
				hidden: true,
				id:'module.channelCode',
				name: 'channelCode',
				value: parent.getGlobalValue('channelCode'),
				width:200
			} , {
				xtype:'textfield',
				fieldLabel: '模块大小',
				hidden: true,
				id:'module.moduleSize',
				name: 'moduleSize',
				value: 0,
				width:200
			}],
		buttons: [{
			text: '保存',
			handler:function(){
				if(!addModule.getForm().isValid()){
					alertMsg("验证", "请检查数据是否校验！");
					return;
				}
				var content = Ext.getCmp('module.moduleContent').getValue();
				
				if (content.indexOf('"') != -1) {
					alertMsg("验证", "模板内容不能含有双引号");
					return 
				}
				var json = {
					success: function(addModule, action){
						//alertMsg("结果", action.result.msg);
						Ext.Msg.alert('结果',action.result.msg,function(xx){ 
								parent.ChannelTemplateModulePage.refresh();
								parent.FormEditWin.close();
							});
					},
					failure: function(addModule, action){
						errorMsg("结果", action.result.msg);
					}
					,waitMsg:'Loading...'
				};
				addModule.getForm().submit(json);
			}
		},{
			text: '关闭',
			handler:function(){
				//parent.parent.channelTemplateMGrid.loadStore();
				addModule.destroy();
				parent.FormEditWin.close();
			}
		}]
	});
//	var addModulePanel = new Ext.Panel({
//		renderTo : 'addModule-panel',
//		layout : 'column',
//		items: [addModule, new Ext.Panel({
//			width: 400,
//			contentEl : 'velocityDesc'
//		})]
//	});
//	addModule.render(document.body);
	
	//$("input[name='showTitle'][value="+$("#showTitle").val()+"]").attr("checked",true);
//	$("input[name='moduleSize'][value="+$("#moduleSize").val()+"]").attr("checked",true);
//	Ext.getCmp("module.shopCode").setValue($("#shopCode").val());
//	shopStore.on('load', function(store, record, opts) {
//		Ext.getCmp("channelTemplateModule.shopCode").setValue($("#shopCode").val());
//	});
	
//	Ext.getCmp("module.channelCode").setValue($("#channelCode").val());

	
	if (id != null && id != '') {
		Ext.getCmp("module.moduleName").setValue($("#moduleName").val());
		Ext.getCmp("channelTemplateModule.moduleType").setValue($("#moduleType").val());
		//Ext.getCmp("channelTemplateModule.moduleSize").setValue($("#moduleSize").val());
	}

});