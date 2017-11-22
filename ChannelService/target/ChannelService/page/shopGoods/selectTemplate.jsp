<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<html>
	<head>
		<title>模板生成</title>
	</head>
	<body>
 <div id='createTemplate'></div>
   <input type="hidden" id="ticketCode" value="${ticketCode}" />
   <input type="hidden" id="seletTemplateTicketType" value="${ticketType}" />
   </body>
	
	<script type="text/javascript">
	

	
	
	var basePath = '<%=basePath%>';
	Ext.onReady(initPage);
	var ticketCode = $('#ticketCode').val();
	
/* 	var ticketType = $("#seletTemplateTicketType").val();
	var templateType = "";
	if(2==ticketType){
		templateType = "1";
	} else if(12== ticketType){
		templateType = "2";
	} */
	var channelCode = parent.getGlobalValue('channelCode');
	var shopCode = parent.getGlobalValue('shopCode');
	function initPage() {
		//模板选择
		var tempSelectOption = new Ext.form.ComboBox( {
				fieldLabel : '模板选择',
				id : 'editChannelDetail.template',
				store : new Ext.data.Store({
					proxy : new Ext.data.HttpProxy({
					/* 	url : basePath + '/custom/channelTemplate/channelTemplateSelect.spmvc?shopCode='+ shopCode +
							'&channelCode='+ channelCode+"&templateType="+templateType, */
						url : basePath + '/custom/channelTemplate/channelTemplateSelect.spmvc?shopCode='+ shopCode +
							'&channelCode='+ channelCode,	
						
						method : 'GET'
					}),
					reader : new Ext.data.JsonReader({
						fields : [ 'templateCode', 'templateName' ]
					})
				}),
				xtype : 'combo',
				valueField : 'templateCode',
				displayField : 'templateName',
				mode : 'remote',
				forceSelection : true,
				emptyText : '请选择',
				name : 'templateCode',
				editable : false,
				hiddenName : 'templateCode',
				triggerAction : 'all', 
				width : 150
		});
		
	/* 	var typeOptionCombo = new Ext.form.ComboBox( {
			id : 'editChannelDetail.templateType',
			store : new Ext.data.SimpleStore( {
				data : [ [ '1', 'pc模板' ], [ '2', '手机模板' ]],
				fields : [ 'text', 'filed' ]
			}),
			xtype : 'combo',
			valueField : 'text',
			displayField : 'filed',
			labelAlign : 'left',
			mode : 'local',
			value:'1',
			forceSelection : true,
			//emptyText : '请选择执行类型',
			name : 'templateType',
			editable : false,
			//editable : false,
			hiddenName : 'templateType',
			triggerAction : 'all', 
			fieldLabel : '执行类型',
			width : 150
		}); */
	
		var formPanel = new Ext.FormPanel(
				{
					frame : true,
					forceFit : true,
					bodyStyle:'padding:10px 10px 0 20px',
					width: 320,
					height: 220,
					labelAlign : "left",
					waitMsgTarget : true,
					bodyBorder : false,
					applyTo : 'createTemplate',
					border : true,
					layout : 'form',
					items : [tempSelectOption ,
					             {
						id : 'channelGoodsTicket.id',
						xtype : 'hidden',
						name : 'id',
						value : ticketCode
					}],
					buttonAlign : 'center',
					buttons : [ {
						id : 'channelGoodsTicket.saveButton',
						text : '生成模板',
						handler : clickSaveButton
					}, {
						text : '关闭',
						handler : function() {
							parent.FormEditWin.close();
						}
					} ]
				});
		function clickSaveButton() {
			if (!formPanel.getForm().isValid()) {
				alertMsg("验证", "请检查数据是否校验！");
				return;
			}
			var params = {};
			var ids = [];
			ids.push(ticketCode);
			var templateCode = Ext.getCmp('editChannelDetail.template').getValue();
			if (templateCode == null || templateCode == '') {
				errorMsg("错误", "请选择详情模板！");
				return ;
			}
			params["ids"] = ids;
			params["templateCode"] = templateCode;
			params["channelCode"] = channelCode;
			params["shopCode"] = shopCode;
			Ext.Ajax.request({
				waitMsg : '请稍等.....',
				url : basePath + '/custom/channelTemplate/createGoodsDetailByGoodsSn.spmvc',
				params : params,
				method : "post",
				success : function(response) {
					var json = Ext.util.JSON.decode(response.responseText);
					Ext.Msg.alert('结果', json.message, function(xx) {
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
	</script>
</html>