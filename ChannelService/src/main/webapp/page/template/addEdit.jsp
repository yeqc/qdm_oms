<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp" %>
<html>
	<head>
		<title>新增模板模块</title>
	</head>

<body>
	<div id='form'></div>
	<input type="hidden" id="ticketCodeId"  value="${cgtVo.ticketCode}"/>
	<input type="hidden" id="shopCodeId"  value="${cgtVo.shopCode}"/>
</body>
	
<script type="text/javascript">
		var schedingType = "";
		var basePath = '<%=basePath%>'; 
		Ext.namespace('Ext.ux','Ext.ux.plugins');
		Ext.ux.plugins.HtmlEditorImageInsert=function(config) {

		config=config||{};

		Ext.apply(this,config);

		this.init=function(htmlEditor) {
		this.editor=htmlEditor;
		this.editor.on('render',onRender,this);
	};

	this.imageInsertConfig={
		popTitle: config.popTitle||'Image URL',
		popMsg: config.popMsg||'Please select the URL of the image you want to insert:',
		popWidth: config.popWidth||350,
		popValue: config.popValue||''
	}

	this.imageInsert=function() {
		var range;
		if(Ext.isIE)
			range=this.editor.doc.selection.createRange();
		var msg=Ext.MessageBox.show({
			title: this.imageInsertConfig.popTitle,
			msg: this.imageInsertConfig.popMsg,
			width: this.imageInsertConfig.popWidth,
			buttons: Ext.MessageBox.OKCANCEL,
			prompt: true,
			value: this.imageInsertConfig.popValue,
			scope: this,
			fn: function(btn,text) {
				if(btn=='ok') {
					if(Ext.isIE)
						range.select();
					this.editor.relayCmd('insertimage',text);
				}
			}
		});
		var win=msg.getDialog()
		win.show.defer(200,win)
	}

	function onRender(){
		if( ! Ext.isSafari){
			this.editor.tb.add({
				itemId : 'htmlEditorImage',
				cls : 'x-btn-icon x-edit-insertimage',
				enableToggle : false,
				scope : this,
				handler : function(){
					this.imageInsert();
				},
				clickEvent : 'mousedown',
				tooltip : config.buttonTip || 
				{
					title : '插入图片',
					text : '插入图片到编辑器',
					cls : 'x-html-editor-tip'
				},
				tabIndex :- 1
			});
		}
	}
}
	</script>
	
	<script type="text/javascript">
Ext.onReady(function(){
	Ext.QuickTips.init();

	new Ext.FormPanel({
		renderTo: 'form',
		defaultType: 'textfield',
		items: [{
			xtype:'htmleditor',
			   // fieldLabel:'some label',
				width: 650,
				height: 350,
				plugins: new Ext.ux.plugins.HtmlEditorImageInsert({
					popTitle: 'Image url?',
					popMsg: 'Please insert an image URL...',
					popWidth: 400,
					popValue: 'http://www.google.gr/intl/en_com/images/logo_plain.png'
				})
			}
		]
	});
}); 
</script>
</html>