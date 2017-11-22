Ext.onReady(initPage);

function initPage() {

	// 执行类型选择框
	var isTimingSelectOption = new Ext.form.ComboBox({
		id : 'channelGoodsTicket.isTiming',
		store : new Ext.data.SimpleStore({
			data : [ [ '0', '否' ], [ '1', '是' ] ],
			fields : [ 'text', 'filed' ]
		}),
		disabled : true,
		allowBlank : false,
		xtype : 'combo',
		valueField : 'text',
		displayField : 'filed',
		mode : 'local',
		forceSelection : true,
		blankText : '请选择执行类型',
		emptyText : '请选择是否定时执行',
		name : 'channelGoodsTicket.isTiming',
		editable : false,
		triggerAction : 'all',
		fieldLabel : '是否定时执行',
		anchor : '70%'
	});
	
	isTimingSelectOption.on('load',function(){Ext.getCmp("channelGoodsTicket.isTiming").setValue(0);});

	var formPanel = new Ext.FormPanel(
			{
				frame : true,
				forceFit : true,
//				autoScroll : false,
//				autoWidth : true,
//				autoHeight : true,
				bodyStyle:'padding:10px 10px 0 20px',
				width: 500,
				height: 320,
				labelAlign : "left",
				waitMsgTarget : true,
				bodyBorder : false,
				applyTo : 'channel_good_form',
				border : true,
				layout : 'form',
				items : [{
					id : 'channelGoodsTicket.ticketCode',
					xtype : 'textfield',
					name : 'ticketCode',
					anchor : '70%',
					readOnly : true,
					fieldLabel : '调整单号'
				},{
					id : 'channelGoodsTicket.channelCode',
					xtype : 'textfield',
					anchor : '70%',
					fieldLabel : '调整类型',
					readOnly : true,
					width : '150'
				} , {
					id : 'channelGoodsTicket.shopCode',
					xtype : 'textfield',
					fieldLabel : '经营店铺',
					anchor : '70%',
					readOnly : true,
					width : '150'
				} , isTimingSelectOption , {
					anchor : '70%',
					disabled : true,
					fieldLabel : '执行时间',
					html : '<input type="text" size="30" id="channelGoodsTicket_excuteTime" readonly="true" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm:ss\'})"/>'
				} , {
					id : 'ticketInfo.goodsSn',
					xtype : 'textfield',
					name : 'goodsSn',
					anchor : '70%',
					fieldLabel : '商品款号',
					allowBlank : false
				} , {
					id : 'ticketInfo.goodsTitle',
					xtype : 'textfield',
					fieldLabel : '商品名称',
					name : 'goodsTitle',
					anchor : '70%',
					allowBlank : false
				} , {
					id : 'channelGoodsTicket.id',
					xtype : 'hidden',
					name : 'id'
				}],
				buttonAlign : 'center',
				buttons : [ {
					id : 'channelGoodsTicket.saveButton',
					text : '保存',
					handler : clickSaveButton
				}, {
					text : '关闭',
					handler : function() {
						parent.FormEditWin.close();
					}
				} ]
			});

	if (channelGoodsTicket) {
		channelGoodsTicket = Ext.util.JSON.decode(channelGoodsTicket);
		Ext.getCmp("channelGoodsTicket.id").setValue(channelGoodsTicket.id);
		Ext.getCmp("channelGoodsTicket.ticketCode").setValue(
				channelGoodsTicket.ticketCode);
		if(channelGoodsTicket.execTime){
			$("#channelGoodsTicket_excuteTime").val(
					new Date(channelGoodsTicket.execTime)
					.Format("yyyy-MM-dd hh:mm:ss"));
		}

		Ext.getCmp('channelGoodsTicket.channelCode').setValue(
				parent.getGlobalValue('channelTitle'));
		Ext.getCmp('channelGoodsTicket.shopCode').setValue(
				parent.getGlobalValue('shopTitle'));

		Ext.getCmp("channelGoodsTicket.isTiming").setValue(
				channelGoodsTicket.isTiming);
		
		if(channelGoodsTicket.ticketStatus == '2' || channelGoodsTicket.ticketStatus == '3' ){
			Ext.getCmp('channelGoodsTicket.saveButton').setDisabled(true);
		}
	}

	if (ticketInfo) {
		ticketInfo = Ext.util.JSON.decode(ticketInfo);
		Ext.getCmp("ticketInfo.goodsSn").setValue(ticketInfo.goodsSn);
		Ext.getCmp("ticketInfo.goodsTitle").setValue(ticketInfo.goodsTitle);

	}
	function clickSaveButton() {
		if (!formPanel.getForm().isValid()) {
			alertMsg("验证", "请检查数据是否校验！");
			return;
		}
		var params = {};
		params["id"] = getValueById("channelGoodsTicket.id");
		params["ticketCode"] = getValueById("channelGoodsTicket.ticketCode");
		params["execTime"] = $("#channelGoodsTicket_excuteTime").val();
		params["channelCode"] = parent.getGlobalValue('channelCode');
		params["shopCode"] = parent.getGlobalValue('shopCode');
		params["isTiming"] = getValueById("channelGoodsTicket.isTiming");

		params["goodsSn"] = getValueById("ticketInfo.goodsSn");
		params["goodsTitle"] = getValueById("ticketInfo.goodsTitle");

		var myMask = new Ext.LoadMask(Ext.getBody(), {
			msg : '正在保存，请稍后！',
			removeMask : true
		// 完成后移除
		});
		myMask.show();

		Ext.Ajax.request({
			waitMsg : '请稍等.....',
			url : basePath + '/custom/channelGoodsInfo/doUpdate.spmvc',
			params : params,
			method : "post",
			success : function(response) {
				myMask.hide();
				var json = Ext.util.JSON.decode(response.responseText);
				Ext.Msg.alert('结果', json.message, function(xx) {
					parent.Ext.getCmp("addcouponForm_gridss_id").getStore()
							.reload();
					parent.FormEditWin.close();
				});

			},
			failure : function(response) {
				myMask.hide();
				var json = Ext.util.JSON.decode(response.responseText);
				errorMsg("结果", json.message);
			}
		});
	}
}
