Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var url = basePath + '/custom/channelTemplate/editTemplateContent.spmvc?method=update';
	var id = $("#templateId").val();
	if (id != null && id != '') {
		url = basePath + '/custom/channelTemplate/editTemplateContent.spmvc?method=update';
	}

	var editTemplate = new Ext.FormPanel({
		labelleft: 'top',
		frame:true,
		bodyStyle:'padding:5px 5px 0',
		height: 520,
		autoScroll:true,
		width: 820,
		url: url,
		params: '',
		layout:'form',
		labelAlign: "right",
		columnWidth:.8,
		items: [ {
				xtype : 'textarea',
				fieldLabel : '模块内容',
				id:'template.templateContentTemp',
				name : 'templateContentTemp',
				value: decodeURI($("#templateContent").val()),
				allowBlank : true,
				width: 660,
				height: 440
			} , {
				xtype:'textfield',
				fieldLabel: '模板名称',
				hidden: true,
				id:'template.templateName',
				name: 'templateName',
				value: $("#templateName").val(),
				width:200
			} , {
				xtype:'textfield',
				fieldLabel: '模板备注',
				hidden: true,
				id:'template.backup',
				name: 'backup',
				value: $("#backup").val(),
				width:200
			} , {
				xtype:'textfield',
				fieldLabel: '编辑模板内容',
				hidden: true,
				id:'template.editTemplateContent',
				name: 'editTemplateContent',
				value: $("#editTemplateContent").val(),
				width:200
			} , {
				xtype:'textfield',
				fieldLabel: '模板CODE',
				hidden: true,
				id:'template.templateCode',
				name: 'templateCode',
				value: $("#templateCode").val(),
				width:200
			} , {
				xtype:'textfield',
				fieldLabel: '模板ID',
				hidden: true,
				id:'template.id',
				name: 'id',
				value:$("#templateId").val(),
				width:200
			} , {
				xtype : 'textfield',
				fieldLabel : '模块内容',
				hidden: true,
				id:'template.templateContent',
				name : 'content',
				width: 580
			}],
		buttons: [{
			text: '保存',
			handler:function(){
				var content = Ext.getCmp('template.templateContentTemp').getValue();
				Ext.getCmp('template.templateContent').setValue(encodeURI(content.trim()));
				var json = {
					success: function(editTemplate, action){
						//alertMsg("结果", action.result.msg);
						Ext.Msg.alert('结果',action.result.msg,function(xx){parent.FormEditWin.close();});
					},
					failure: function(editTemplate, action){
						errorMsg("结果", action.result.msg);
					}
					,waitMsg:'Loading...'
				};
				editTemplate.getForm().submit(json);
			}
		},{
			text: '关闭',
			handler:function(){
				//parent.parent.contentListGrid.loadStore();
				editTemplate.destroy();
				parent.FormEditWin.close();
			}
		}]
	});
	editTemplate.render("edit_template_form");
//	$("#moduleTitle").val($("#moduleName").val());

});