Ext.onReady(function(){
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var url = basePath+'/custom/channelTemplate/addTemplate.spmvc?method=add';
	var id = $("#templateId").val();
	if (id != null && id != '') {
		url = basePath+'/custom/channelTemplate/addTemplate.spmvc?method=update';
	}
	var showStatus = new Ext.form.RadioGroup({
		name: "showTitle",
		fieldLabel: "显示标题",
		width: 150,
		items: [
			{
				name: "showTitle",
				inputValue: "1",//实际值
				boxLabel: "不显示"//显示值
			},
			{
				name: "showTitle",
				inputValue: "0",//实际值
				boxLabel: "显示",//显示值
				checked: true //默认
			}
		],
		listeners : {
			change : function(radiofield){//这事件是当radiogroup的值发生改变时进入
				if (radiofield.getValue().inputValue == '0') {
					$("#moduleTitle").removeAttr("readonly");//去除input元素的readonly属性
				} else {
					$("#moduleTitle").attr("readonly","readonly");
				}
			}
		}
	});

	var addTemplate = new Ext.FormPanel({
		title: '模板基础信息',
		frame:true,
		bodyStyle:'padding:5px 5px 0',
		height: 250,
		autoScroll:true,
		width:500,
		url: url,
		params: '',
		layout:'form',
		labelAlign: "right",
		lableWidth : 80,
		columnWidth:.8,
		items: [{
				columnWidth:.8,
				layout: 'column',
				items: [{
					layout: 'form',
					items: [{
						xtype : 'textfield',
						fieldLabel : '模版编号',
						id:'channelTemplate.templateCode',
						name : 'templateCode',
						value : $("#templateCode").val(),
						readOnly:true,
						allowBlank : false,
						width : '124'
					} , {
						xtype : 'textfield',
						fieldLabel : '模版名称',
						id:'channelTemplate.templateName',
						name : 'templateName',
						value : $("#templateName").val(),
						allowBlank : false,
						blankText : '模版名称不能为空',
						width : '124'//,
						
					},
					{
						xtype:'textfield',
						fieldLabel: '模块大小',
						value:'790',
						id:'channelTemplate.width',
						width:50,
						listeners:{
							'blur':function(combo){
							//	var xx = Ext.getCmp("xx").getValue();
								var templateWidth = Ext.getCmp('channelTemplate.width').getValue();
								//$(".gridly").width(combo.value);
								$(".gridly").css("width",templateWidth+"px");
								//$(".tempArea").css("width",templateWidth+"px");
								$(".tempArea").css("width",templateWidth+"px");
							//	$(".tempSmallArea").css("width",templateWidth+"px");
								
							//	$(".tempArea").width(templateWidth);
							}
					    }
					}
			
					]
				} , {
					layout: 'form',
					items: [{
						xtype : 'textfield',
						fieldLabel : '应用店铺',
						id:'channelTemplate.shopTitle',
						readOnly : true,
						value : parent.getGlobalValue('shopTitle'),
						width : '124'
					} , {
						xtype : 'textfield',
						fieldLabel : '所属渠道',
						id:'channelTemplate.channelTitle',
						readOnly : true,
						value : parent.getGlobalValue('channelTitle'),
						width : '124'
					
					},{
						xtype : 'textfield',
						//fieldLabel : '所属渠道',
						id:'channelTemplate.templateType',
				
						name:'templateType',
						value : '1',
						hidden:true,
						width : '124'
					}]
					
					
				}]
			} ,new Ext.form.TextArea ({
			
				id:'channelTemplate.backup',
				fieldLabel: "模版备注",
				name: "backup",
				value : $("#backup").val(),
				grow: true,
				anchor: "98%",
				growMin:80,//显示的高度
				growMax:100//最大的显示高度
			}) , {
				xtype:'textfield',
				fieldLabel: '模板内容',
				hidden: true,
				id:'channelTemplate.content',
				name: 'content',
			
				width:180
			} , {
				xtype:'textfield',
				fieldLabel: '编辑模板内容',
				hidden: true,
				id:'channelTemplate.editTemplateContent',
				name: 'editTemplateContent',
				
				width:180
			} , {
				xtype:'textfield',
				fieldLabel: '模板子模块内容列表',
				hidden: true,
				id:'channelTemplate.moduleContent',
				name: 'contents',
				//value:'${synRule.synRuleActoDate}',
				width:180
			} , {
				xtype:'textfield',
				fieldLabel: '模板ID',
				hidden: true,
				id:'channelTemplate.id',
				name: 'id',
				value:$("#templateId").val(),
				width:180
			} , {
				xtype:'textfield',
				fieldLabel: '店铺编号',
				hidden: true,
				id:'channelTemplate.shopCode',
				name: 'shopCode',
				value: parent.getGlobalValue('shopCode'),
				width:180
			} , {
				xtype:'textfield',
				fieldLabel: '渠道编号',
				hidden: true,
				id:'addChannelTemplate.channelCode',
				name: 'channelCode',
				value:  parent.getGlobalValue('channelCode'),
				width:180
			}],
		buttons: [{
			text: '保存',
			handler:function(){
				if(!addTemplate.getForm().isValid()){
					alertMsg("验证", "请检查数据是否校验！");
					return;
				}
				
				
				// 编辑模板 需要保留编辑删除功能 
//				var editHtml = $('.gridly').html();
//				Ext.getCmp('channelTemplate.editTemplateContent').setValue(encodeURI(editHtml));
				var arr = $('.gridly').find('.brick');
				$('.gridly').css('height', '');
				var code = Ext.getCmp('channelTemplate.templateCode').getValue();
				
				
				var templateWidth = Ext.getCmp('channelTemplate.width').getValue();
				
				if(null == templateWidth || templateWidth.length==0){
					
					alertMsg("验证", "请输入模板宽度！");
					return;
				}
				
				 var reg = new RegExp("^[0-9]*$"); 
				 if(!reg.test(templateWidth)){  

					alertMsg("验证", "模板宽度请输入数字！");
				    return;
				 }  
				
				//$(".gridly").width(TemplateWidth);
				 
				var moduleTemplate ="";
				var template = "";
				var editTemplate = "";
				if(arr != null && arr.length > 0) {
					var editTempArr = new Array(); // 编辑模板内容
					var tempArr = new Array(); // 模板内容
					var moduleIdArr = new Array(); // 模板内容
					$.each(arr,function(j,obj){//div换位
						j++;
						$this = $(obj);
						var topNum = $this.css("top").replaceAll('px', '');
						var num = Math.round(parseInt(topNum)/160);
						editTempArr[num] = $this[0].outerHTML;
						$this.find(".editTemplate").remove(); // 移除 编辑功能html代码
						$this.find(".delTemplate").remove(); // 移除 删除功能html代码
						$this.removeAttr("style");
//						$this.css('width', templateWidth+"px");
						$this.css('width', "100.0%");
						$this.css('font-family', 'Tahoma, Geneva');
						$this.css('font-size', '12px');
						$this.css('overflow', 'hidden');
						var type = $this[0].id.split('_');
						moduleIdArr[num] = type[1];
						tempArr[num] = $this;
					});
//					template = "<div class='gridly' style='width:  "+templateWidth+"px;position: relative;font-family: Tahoma, Geneva, sans-serif; font-size: 12px; overflow: hidden;'>\n";
					template = "<div class='gridly' style='width:100.0%;position: relative;font-family: Tahoma, Geneva, sans-serif; font-size: 12px; overflow: hidden;'>\n";
					editTemplate = '';
					for (var i=0;i<tempArr.length;i++) {
						//子模块
						template += tempArr[i].context.outerHTML+"\n";
						//编辑子模块
						editTemplate += editTempArr[i] + +"\n";
						// 模板子模块
						var module = tempArr[i].context.outerHTML;
						
					/*	if( typeof module === 'string'){
							alert("11");
						}
				*/
	
//						var moduleNoDiv  = "<div style='width: 740px; '>"+ tempArr[i].find("textarea").html() +"</div>";
						var moduleNoDiv  =  tempArr[i].find("textarea").html();

						
					/*	module = module.replaceAll('<textarea class="tempArea">', '');
						module = module.replaceAll("<textarea class='tempArea'>", '');
						module = module.replaceAll('<TEXTAREA class=tempArea>', '');
						module = module.replaceAll('</TEXTAREA>', '');
						module = module.replaceAll('</textarea>', '');
						module = module.replaceAll("&amp;", '&');
						module = module.replaceAll("&lt;", '<');
						module = module.replaceAll("&gt;", '>');
						module = module.replaceAll('"', "'");*/
						
						moduleNoDiv = moduleNoDiv.replaceAll('<textarea class="tempArea">', '');
						moduleNoDiv = moduleNoDiv.replaceAll("<textarea class='tempArea'>", '');
						moduleNoDiv = moduleNoDiv.replaceAll('<TEXTAREA class=tempArea>', '');
						moduleNoDiv = moduleNoDiv.replaceAll('</TEXTAREA>', '');
						moduleNoDiv = moduleNoDiv.replaceAll('</textarea>', '');
						moduleNoDiv = moduleNoDiv.replaceAll("&amp;", '&');
						moduleNoDiv = moduleNoDiv.replaceAll("&lt;", '<');
						moduleNoDiv = moduleNoDiv.replaceAll("&gt;", '>');
						moduleNoDiv = moduleNoDiv.replaceAll('"', "'");
						
						j = i + 1;
			
						if (tempArr.length==1) {
							moduleTemplate += '[{"templateCode":"'+code+'","orderNum":'+j+',"moduleContent":"'+moduleNoDiv+'","moduleId":'+moduleIdArr[i]+'}]';
						} else if (tempArr.length > 1) {
							if (j==1) {
								moduleTemplate += '[{"templateCode":"'+code+'","orderNum":'+j+',"moduleContent":"'+moduleNoDiv+'","moduleId":'+moduleIdArr[i]+'},';
							} else if (arr.length == j) {
								moduleTemplate += '{"templateCode":"'+code+'","orderNum":'+j+',"moduleContent":"'+moduleNoDiv+'","moduleId":'+moduleIdArr[i]+'}]';
							}else if (arr.length > 1 || arr.length < j) {
								moduleTemplate += '{"templateCode":"'+code+'","orderNum":'+j+',"moduleContent":"'+moduleNoDiv+'","moduleId":'+moduleIdArr[i]+'},';
							}
						}
					}
					template += '</div>';
					editTemplate += '</div>';
//				$.each(arr,function(j,obj){//有数据
//					j++;
//					$this = $(obj);
//					
//					$this.find(".editTemplate").remove(); // 移除 编辑功能html代码
//					$this.find(".delTemplate").remove(); // 移除 删除功能html代码
//					$this.removeAttr("style");
//					var type = $this[0].id.split('_');
//					var modele = $this[0].outerHTML;
//					modele = modele.replaceAll('<textarea class="tempArea">', '');
//					modele = modele.replaceAll("<textarea class='tempArea'>", '');
//					modele = modele.replaceAll('<TEXTAREA class=tempArea>', '');
//					modele = modele.replaceAll('</TEXTAREA>', '');
//					modele = modele.replaceAll('</textarea>', '');
//					modele = modele.replaceAll("&amp;", '&');
//					modele = modele.replaceAll("&lt;", '<');
//					modele = modele.replaceAll("&gt;", '>');
//					if (arr.length==1) {
//						content = '[{"templateCode":"'+code+'","orderNum":'+j+',"moduleContent":"'+encodeURI(modele)+'","moduleType":'+type[0]+'}]';}
//					else if (arr.length > 1) {
//						if (j==1) {
//							content += '[{"templateCode":"'+code+'","orderNum":'+j+',"moduleContent":"'+encodeURI(modele)+'","moduleType":'+type[0]+'},';
//						} else if (arr.length == j) {
//							content += '{"templateCode":"'+code+'","orderNum":'+j+',"moduleContent":"'+encodeURI(modele)+'","moduleType":'+type[0]+'}]';
//						}else if (arr.length >1 || arr.length < j) {
//							content += '{"templateCode":"'+code+'","orderNum":'+j+',"moduleContent":"'+encodeURI(modele)+'","moduleType":'+type[0]+'},';
//						}
//					}
//				});
				}
//				var html = $('.example').html();
				template = template.replaceAll('<textarea class="tempArea">', '');
				template = template.replaceAll("<textarea class='tempArea'>", '');
				template = template.replaceAll('<TEXTAREA class=tempArea>', '');
				
				template = template.replaceAll('<textarea class="tempSmallArea">', '');
				template = template.replaceAll("<textarea class='tempSmallArea'>", '');
				template = template.replaceAll('<TEXTAREA class=tempSmallArea>', '');
				template = template.replaceAll('class="brick large"', '');
				template = template.replaceAll("class='brick large'", '');
				
				template = template.replaceAll('</TEXTAREA>', '');
				template = template.replaceAll('</textarea>', '');
				template = template.replaceAll("&amp;", '&');
				template = template.replaceAll("&lt;", '<');
				template = template.replaceAll("&gt;", '>');

				editTemplate = editTemplate.replaceAll("&amp;", '&');
				editTemplate = editTemplate.replaceAll("&lt;", '<');
				editTemplate = editTemplate.replaceAll("&gt;", '>');
				
//				html = html.replaceAll("z-index: 800;", '');
//				html = html.replaceAll('style="border:1px #ddd solid;"', '');
//				html = html.replaceAll("style='border:1px #ddd solid;'", '');
				Ext.getCmp('channelTemplate.content').setValue(encodeURI(template));
				Ext.getCmp('channelTemplate.editTemplateContent').setValue(encodeURI(editTemplate));
				Ext.getCmp('channelTemplate.moduleContent').setValue(moduleTemplate);
				var json = {
					success: function(addTemplate, action){
						//alertMsg("结果", action.result.msg);
						Ext.Msg.alert('结果',action.result.msg,function(xx){	
							parent.ChannelTemplatePage.refresh();
							parent.FormEditWin.close();});
					},
					failure: function(addTemplate, action){
						errorMsg("结果", action.result.msg);
					}
					,waitMsg:'Loading...'
				};
				addTemplate.getForm().submit(json);
			}
		},{
			text: '关闭',
			handler:function(){
				//parent.parent.contentListGrid.loadStore();
				addTemplate.destroy();
				parent.FormEditWin.close();
			}
		}]
	});
	addTemplate.render("template_form");
	$("#moduleTitle").val($("#moduleName").val());

});