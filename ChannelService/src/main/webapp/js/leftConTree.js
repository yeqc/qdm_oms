var add_S=false;//添加
var modify_S=false;//修改
var delete_S=false;//删除
var publish_S=false;//发布
var lookable=false;//查看
var setpermissionable=false;//设置权限
var siteMenu = new Ext.menu.Menu();	// 合成预览站点菜单
 
var expdate = new Date();
var systemStr = $("#system").val();
var shopCodeStr = $("#shopCode").val();
var menuType = $("#menuType").val();
var channelCodeStr = $("#channelCode").val();

/*var shopCodeStr =  getGlobalValue("shopCode");
var systemStr =  getGlobalValue("system");
var channelCodeStr =  getGlobalValue("channelCode");*/

SetCookie("shopCode", shopCodeStr);
SetCookie("system", systemStr);
// 内容导航树
ConTree = function(){
	var con = null;
	var conEditor = null;
	var loader = null;
	var root = null;
	var removeFlag = false;
	var titleChangeFlag = false;
	var nodeSelected = null;
	var mgr = null;
	return {
		filter: function(paramValue){
			con.getRootNode().reload();
		},
		init : function(){
			if(!loader){
				loader = new Ext.tree.TreeLoader({
					url : basePath + 'custom/index/getTree.spmvc'
				});
				loader.on('beforeload', function(treeloader, node) {
					treeloader.baseParams = {
						id : node.id,
						system:systemStr,
						shopCode:shopCodeStr,
						channelCode:channelCodeStr,
						menuType:menuType,
						method : 'tree'
					};
				}, this);
			}
			if(!root){
				root = new Ext.tree.AsyncTreeNode({
					id : '-1',
					text : "功能管理"
				});
			}
			if(!con){
				//Ext.Msg.alert("33","33");
				con = new Ext.tree.TreePanel({
					title:"内容管理",
					border:false,
					split: true,
			        width:232,
			        autoWidth:true,
			        expanded:true,
			        margins: '0 0 0 5',
			        cmargins:'0 0 0 0',
			        autoWith:true,
			        height:480,
			        autoScroll:true,
					draggable:true,
					animate : true,
					loader : loader,
					root : root,
					listeners : {
						'click' : function(node, event) {
							if(node.attributes.items[1].text == '2'){
								if(node.id!=-1){
									var url=node.attributes.items[0].text;
									commonMenu(node,url,'con'+node.id);
								}
								return;
							}
						}
					}
				});
				// 添加右键菜单
				con.on("contextmenu", this.showTreeMenu);
				// 当节点文本改变时触发事件
				con.on("textchange", function(node, newText, oldText) {
					if (!titleChangeFlag && newText != oldText) {
						mgr.ajaxHasSameData(node.id,newText,function(success){
						if (success) {
									Ext.Msg.show({
											title : "操作失败！",
											msg : "该节点下有同名节点,无法修改！",
											buttons : Ext.Msg.OK,
											icon : Ext.MessageBox.ERROR
								});
								node.parentNode.reload();////////////////////////////////baocuo
						}else{
							mgr.ajaxUpdateConTitle(node.id, newText, function(success) {
								if (!success) {
									Ext.Msg.show({
										title : "操作失败！",
										msg : "修改失败！",
										buttons : Ext.Msg.OK,
										icon : Ext.MessageBox.ERROR
									});
									titleChangeFlag = true;
									node.setText(oldText);
									titleChangeFlag = false;
								}
							});
							
						}
						});
						
					}
				});
				// 当节点移动时触发事件
				con.on("movenode", function(tree, node, oldParent, newParent, index) {
					mgr.ajaxMoveNode(node.id, oldParent.id, newParent.id, index);
				});
				// 当节点删除时触发事件
				con.on("remove", function(tree, parentNode, node) {
					if (removeFlag) {
						mgr.ajaxRemoveConNode(node.id,function(success){
							if(success){
								alertMsg("操作结果","删除成功！！！");
							}else{
								Ext.Msg.show({
										title : "操作失败！",
										msg : "无法删除该节点！！！",
										buttons : Ext.Msg.OK,
										icon : Ext.MessageBox.ERROR
									});
								
								}
						});
					}
				});
			}
			if(!conEditor){
				conEditor = new Ext.tree.TreeEditor(con, {
					allowBlank : false,
					ignoreNoChange : true,
					blankText : '标题内容不能为空',
					selectOnFocus : true
				});
				conEditor.on("beforestartedit", function(treeEditer) {
						return false;
	            });
			}
			//this.setLeafMenu();
			//this.setDirMenu();
		},
		
		createTreeEditor : function (){
		    var treeEditor = new Ext.tree.TreeEditor(con, {   
		            allowBlank : false,   
		            ignoreNoChange : true,             
		            selectOnFocus : true  
		    });   
		    return treeEditor;   
		},
		
		//复制到
		copyTo:function(){
			FormEditWin.showAddDirWin("popWins",basePath+"page/content/SelectCopyConTreeNode.jsp?pid="+nodeSelected.id,"infoWinID","该栏目下内容复制到 --> 以下节点",300,400);
		
		},
		//移动到
		moveTo:function(){
			FormEditWin.showAddDirWin("popWins",basePath+"page/content/SelectMoveConTreeNode.jsp?pid="+nodeSelected.id,"infoWinID","该栏目下移动到 --> 以下节点",300,400);
		
		},
		showTreeMenu : function(node, e){
		//alert(node.attributes.items[0].text);	
		if(no!='admin'&&node.id!='-1'){ 
			if(node.attributes.items[0].text=="1")
				  add_S=false;//添加
		    else  add_S=true;
		    if(node.attributes.items[1].text=="1")
				  modify_S=false;//修改
		    else  modify_S=true;
		    if(node.attributes.items[2].text=="1")
				  delete_S=false;//删除
			else delete_S=true;
			if(node.attributes.items[3].text=="1")
				  publish_S=false;//发布
		    else  publish_S=true;
		    if(node.attributes.items[4].text=="1")
				  lookable=false;//查看
		 	else  lookable=true;
		 	if(node.attributes.items[5].text=="1")
				  setpermissionable=false;//设置权限
			else setpermissionable=true;
		}
		//alert(add_S);
		
			nodeSelected = node;
			nodeSelected.select();
			
		},
		delTreeItemComfirm : function(){
			Ext.Msg.confirm("确认删除", "确定要删除所选节点吗？", function(btn) {
				if (btn == "yes") {
					ConTree.delTreeItem();
				}
			});
		},
		
		templetSel:function(){
			//FormEditWin.showAddDirWin(null,basePath+"modules/content/templetSet.ct?pid="+nodeSelected.id,"temSelWinID","模板属性",600,240);			
			FormEditWin.showAddDirWinS(null,basePath+"modules/content/templetSet.ct?pid="+nodeSelected.id,"temSelWinID","模板属性",600,500);			
		},
		
		dataSyn:function(){
			commonMenu(this,'page/task/list.action?channleId='+nodeSelected.id,'数据同步');
		},
		delTreeItem : function(){
			if (nodeSelected != con.getRootNode()) {
				mgr.ajaxHasData(nodeSelected.id,function(success){
					if (success) {
								Ext.Msg.show({
										title : "操作失败！",
										msg : "该节点中含有数据，无法删除！",
										buttons : Ext.Msg.OK,
										icon : Ext.MessageBox.ERROR
							});
					}else{
						removeFlag = true;
						nodeSelected.remove();
						removeFlag = false;
					}
				});
				
			} else {
				Ext.Msg.alert("警告", "不能删除树的根节点！");
			}
		},//栏目顺序调整
		folderSeq:function(){
				FormEditWin.showAddDirWinS(nodeSelected,basePath+"modules/content/contreeNodeSeq.ct?pid="+nodeSelected.id,"temSelWinID","该栏目子节点排序 - 按序号",600,400);
		},
		show : function(){
			Ext.get('temtree').dom.style.display='none';
			Ext.get('contree').dom.style.display='';
			con.render("contree");
			//con.getRootNode().toggle();
			//展开所有节点
			con.getRootNode().expand(true);
		},
		returnCurrNode:function(){
			return nodeSelected;
		}
		
	};
}();


function runContree(){
//	if(typeof(TreeDWR)=="undefined"){
//			Ext.Msg.alert("警告提示","请先设置DWR，并实例化TreeDWR");
//		}else{
//			
			//alert(1);
			//alert(2);
			ConTree.init();
			//alert(3);
			ConTree.show();
//		}
}
