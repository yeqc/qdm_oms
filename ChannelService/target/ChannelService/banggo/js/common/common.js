function checkFormat(result) {

	var re = /^(([1-9]\d*)|0)(\.\d{1,2})?$/;
	
	if(re.test(result)) {
		return true;
	} else {
		return false;
	}
}

function getById(id) {
	var value = Ext.fly(id).getValue().trim();
	Ext.getCmp(id).setValue(value);
	return value;
}

// 获取金额显示格式
function getMoneyFormate(value) {
	value = value.toString();
	if (value) {
		return "<div style='text-align:right;'>￥"+getMoney(value)+"</div>";
	} else {
		return "";
	}
}

function getPercentFormate(value) {
	value = value.toString();
	if (value) {
		return "<div style='text-align:right;'>"+getMoney(value)+"%</div>";
	} else {
		return "";
	}
}

// 获取左对齐格式
function getLeftFormate(value) {
	return "<div style='text-align:left;'>"+value+"</div>";
}

// 获取右对齐格式
function getRightFormate(value) {
	return "<div style='text-align:right;'>"+value+"</div>";
}

// 获取字符日期
function getStringDate(value) {
	if (value) {
		value = value.substring(0, 19);
	}
	return value;
}

function formatDate(millisecond){
	if (millisecond) {
		var dt = new Date(millisecond);
		return dt.format('Y-m-d');
	}
}

function formatDateformate(millisecond, formate){
	if (millisecond) {
		var dt = new Date(millisecond);
		return dt.format(formate);
	}
}

// 获取2位小数点四舍五入
function changeTwoDecimal(x) {  
	var f_x = parseFloat(x);  
	if (isNaN(f_x)) {  
		return 0.00;  
	}  
	var f_x = Math.round(x*100)/100;  
	return f_x;  
} 

function numFixed(num, n){
	n = n || 2;
	var number = new Number(num);
	return number.toFixed(n);
}

// 获取2位金额
function getMoney(value) {
	if (isNaN(value)) {
		return value;
	} else {
		var f = parseFloat(value);
		var f = numFixed(f, 2);
		
		return f;
	}
	/*var s = value.toString();
	
	var index = s.indexOf(".");
	
	if (index == -1) {
		if (s) {
			s =  s + ".00";
		} else {
			s = "0.00"
		}
	} else {
		var leng = s.substring(index + 1);
		
		if (leng.length == 1) {
			s = s + "0";
		} else {
			s = s.substring(0, index + 3);
		}
	}
	
	return s;*/
}

// 设置序号
var record_start = 0;

function getRecordStart() {
	return record_start;
}

function setRecordStart(start) {
	return record_start = start;
}

// 为了与后台的Page分页对象一致才设置
function setDefaultPage(start, limit, store) {
	setRecordStart(start);
	store.setBaseParam("start", start);
	store.setBaseParam("limit", limit);
	
	/*var pageNo = 1;
	if (start > 0) {
		pageNo = start / limit + 1;
	}
	
    store.setBaseParam("cp",pageNo);
    store.setBaseParam("pp",limit);
    store.setBaseParam("page.pageNo", pageNo);
	store.setBaseParam("page.pageSize", limit);*/
}

// 创建JsonStore
function createJsonStore(root, fields,url) {
	var data = {};
	data.root = root;
	data.fields = fields;
	
	if (url) {
		data.url = url;
	}
	return new Ext.data.JsonStore(data);
}

// 创建ComboBox
function createComboBox(store, id, displayField, valueField, mode, fieldLabel, width, name) {
	return new Ext.form.ComboBox({
		store: store,
		id: id,
		name: name,
   		displayField: displayField,
		valueField: valueField, 
		mode: mode,
		width:width ? width : 150,
		triggerAction: 'all',
		selectOnFocus:true,
       	allowBlank:true,
       	forceSelection:true,
    	fieldLabel: fieldLabel,
    	hiddenName: name,
		editable: false // 不可输入
	});
}

// 创建ComboBox
function createComboBoxNoHidden(store, id, displayField, valueField, mode, fieldLabel, width, name) {
	return new Ext.form.ComboBox({
		store: store,
		id: id,
		name: name,
   		displayField: displayField,
		valueField: valueField, 
		mode: mode,
		width:width ? width : 150,
		triggerAction: 'all',
		selectOnFocus:true,
       	allowBlank:true,
       	forceSelection:true,
    	fieldLabel: fieldLabel,
		editable: false // 不可输入
	});
}

//创建ComboBox
function createComboBox2(store, id, displayField, valueField, mode, fieldLabel,name) {
	return new Ext.form.ComboBox({
		store: store,
		id: id,
		name: name,
   		displayField: displayField,
		valueField: valueField, 
		mode: mode,
		triggerAction: 'all',
		selectOnFocus:true,
       	allowBlank:true,
       	forceSelection:true,
    	fieldLabel: fieldLabel,
    	hiddenName: name,
		editable: false // 不可输入
	});
}

// 创建分页
function createPagingToolbar(store, limit) {
	return new Ext.PagingToolbar({
		pageSize: limit,
		store: store,
		displayInfo: true,
		displayMsg: '显示 {0} - {1}条记录 共 {2}条',
		style:"padding:0 15px",
		emptyMsg: "没有记录可以显示",
		beforePageText:"页数",
		afterPageText : '共 {0}页',
		firstText : '第一页',
		prevText : '上一页',
		nextText : '下一页',
		lastText : '最后一页',
		refreshText : '刷新',
        doLoad : function(start){
  			record_start = start;
  		 	setDefaultPage(start, limit, store);
   			store.load();
  		}
	});
}

// 创建分页
function createLocalPagingToolbar(store, limit) {
	return new Ext.PagingToolbar({
		pageSize: limit,
		store: store,
		displayInfo: true,
		displayMsg: '显示 {0} - {1}条记录 共 {2}条',
		style:"padding:0 15px",
		emptyMsg: "没有记录可以显示",
		beforePageText:"页数",
		afterPageText : '共 {0}页',
		firstText : '第一页',
		prevText : '上一页',
		nextText : '下一页',
		lastText : '最后一页',
		refreshText : '刷新',
		doLoad : function(start){
        	setDefaultPage(start, limit, this.store);
  			record_start = start;
   			getPageList(start);
  		},
	    getParams : function(){
    		return this.paramNames || this.store.baseParams;
		},
        onLoad:function (store,r,o)//重写OnLoad
        {
        	if(!this.rendered){
	            this.dsLoaded = [store, r, o];
	            return;
	        }
	        var p = this.getParams();
	        //this.cursor = (o.params && o.params[p.start]) ? o.params[p.start] : 0;
	        this.cursor = p.start ? p.start : 0;
	        var d = this.getPageData(), ap = d.activePage, ps = d.pages;
	
	        this.afterTextItem.setText(String.format(this.afterPageText, d.pages));
	        this.inputItem.setValue(ap);
	        this.first.setDisabled(ap == 1);
	        this.prev.setDisabled(ap == 1);
	        this.next.setDisabled(ap == ps);
	        this.last.setDisabled(ap == ps);
	        this.refresh.enable();
	        this.updateInfo();
	        this.fireEvent('change', this, d);
        }
	});
}

// 创建ajax请求
function createAjax(url, successfun, failurefun, params, timeout) {

	if (!timeout) {
		// 默认超时10分钟
		timeout = 600000;
	}
	
	Ext.Ajax.request({
		url: url,
		timeout:timeout,
		success: function(response,opts){
			if (successfun) {
				successfun(response, opts);	
			}			
		},
		failure: function(response,opts){
			if (failurefun) {
				failurefun(response,opts);
			} else {
				createAlert("提示信息", "请求数据失败!");
			}
		},
		//scriptTag: true,
		params: params
	});
}

/**
 * id : id
 * title : 标题
 * url : 地址
 * width : 宽度
 * height : 高度
 */
function showWindow(id,title,url, w, h, x,y) {
	w = w ? w : 800;
	h = h ? h : 500;
	x = x ? x : Ext.getBody().getWidth()/2 - 300;
	y = y ? y : Ext.getBody().getHeight()/2 -200;
	
	return new Ext.Window( {
		id:id,
		title : title,
		width : w,
		height: h,
		x:x,
		y:y,
		iconCls: 'tabs',
   		shim:false,
   		animCollapse:false,
   		modal : true,     //window窗体后面变灰,不可编辑
   		resizable:false,
     	border:false,
     	//maximizable:true,
       	constrainHeader:true,
      	layout: 'fit',
      	html:'<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="ifr'+id+'" src="'+url+'" width="100%" height="100%"></iframe>'
	});
}

function showWindowUrl(url,id, title, x, y, w, h, button) {
	
	var data = {
		id:id,
		title: title,
  	 	layout:'fit',
  		plain: true,
		modal:true,
		resizable:false,
		closeAction: 'hide',
		buttonAlign:"center",
  	 	buttons: button,
		html:'<iframe frameborder="no" scrolling="auto" border="0" framespacing="0" id="ifr'+id+'" src="'+url+'" width="100%" height="100%"></iframe>'
	}
	
	w = w ? w : 800;
	h = h ? h : 500;
	x = x ? x : Ext.getBody().getWidth()/2 - 300;
	y = y ? y : Ext.getBody().getHeight()/2 -200;
	
	data.x = x;
	data.y = y;
	data.width = w;
	data.height = h;
	
	return new Ext.Window(data);
}

function showWindowForm(title, x, y, w, button) {
	w = w ? w : 800;
	//height = height ? height : 500;
	x = x ? x : Ext.getBody().getWidth()/2 - 300;
	y = y ? y : Ext.getBody().getHeight()/2 -200;
	return new Ext.Window( {
		title: title,
		x:x,
		y:y,
  	 	layout:'fit',
  	  	width:w,
  	 	autoHeight:true,
  	  	closeAction:'hide',
  		plain: true,
		modal:true,
		resizable:false,
		buttonAlign:"center",
  	 	buttons: button
	});
}

/**
 * 创建表格 附带分页条
 * id
 * title
 */
function createGridPanel(id, title, columns,store,limit, renderTo, sm, tb, tbar) {
	return createGridPanelByModel(getGridPanelModel(id, title, columns,store,limit, renderTo, sm, tb, tbar));
}

/**
 * 创建编辑表格 附带分页条
 * id
 * title
 */
function createGridPanel(id, title, cm,store,limit, renderTo, sm, tb, tbar) {
	return createEditGridPanelByModel(getGridPanelModel(id, title, cm,store,limit, renderTo, sm, tb, tbar));
}

// 创建表格,传入对象
function createGridPanelByModel(model) {
	return new Ext.grid.GridPanel(model);
}

// 创建编辑表格,传入对象
function createEditGridPanelByModel(model) {
	return new Ext.grid.EditorGridPanel(model);
}

function getGridPanelModel(id, title, columns,store,limit, renderTo, sm, tb, tbar) {
	var model = {
		id:id,
		title:title,
		height:renderTo.getHeight(),
		columns:columns,
		store:store,
		//loadMask: true,  //读取数据时的遮罩和提示功能
		//enableColumnMove: false, //禁止拖放列
		//enableColumnResize: false,  //禁止改变列的宽度
		stripeRows: true,  //斑马线效果
		columnLines : true,
		viewConfig : {
			//forceFit:true,
			layout : function() {
				if (!this.mainBody) {     
					return; // not rendered     
			    }     
			    var g = this.grid;     
			    var c = g.getGridEl();     
			    var csize = c.getSize(true);     
			    var vw = csize.width;     
			    if (!g.hideHeaders && (vw < 20 || csize.height < 20)) { 
			        return;     
			    }     
			    if (g.autoHeight) {     
			        if (this.innerHd) {     
			            this.innerHd.style.width = (vw) + 'px';     
			        }     
			    } else {     
			        this.el.setSize(csize.width, csize.height);     
			        var hdHeight = this.mainHd.getHeight();     
			        var vh = csize.height - (hdHeight);     
			        this.scroller.setSize(vw, vh);     
			        if (this.innerHd) {     
			            this.innerHd.style.width = (vw) + 'px';     
			        }     
			    }     
			    if (this.forceFit) {     
			        if (this.lastViewWidth != vw) {     
			            this.fitColumns(false, false);     
			            this.lastViewWidth = vw;     
			        }     
			    } else {     
			        this.autoExpand();     
			        this.syncHeaderScroll();     
			    }     
			    this.onLayout(vw, vh);
			},
			getColumnStyle : function(colIndex, isHeader) {
            	var colModel  = this.cm,
				colConfig = colModel.config,
				style = isHeader ? '' : colConfig[colIndex].css || '',
				align = colConfig[colIndex].align;
            	if(Ext.isChrome){
                	style += String.format("width: {0};", parseInt(this.getColumnWidth(colIndex))-2+'px');
            	}else{
                	style += String.format("width: {0};", this.getColumnWidth(colIndex));
            	}
            	if (colModel.isHidden(colIndex)) {
                	style += 'display: none; ';
            	}
            	if (align) {
                	style += String.format("text-align: {0};", align);
            	}
            	return style;
        	}
		},
		renderTo:renderTo
	}
	if (sm) {
		model.sm = sm;
	}
	if (tb) {
		model.bbar = tb
	} else {
		if (limit > 0 ) {
			model.bbar = createPagingToolbar(store, limit);
		}
	}
	if (tbar) {
		model.tbar = tbar;
	}
	return model;
}

// 获取editGrid对象
function getEditGridPanelModel(id, title, cm,store,limit, renderTo, sm, tb, tbar) {
	var model = {
		id:id,
		title:title,
		height:renderTo.getHeight(),
		cm:cm,
		store:store,
		clicksToEdit: 1,
		//loadMask: true,  //读取数据时的遮罩和提示功能
		//enableColumnMove: false, //禁止拖放列
		//enableColumnResize: false,  //禁止改变列的宽度
		stripeRows: true,  //斑马线效果
		columnLines : true,
		viewConfig : {
			//forceFit:true,
			layout : function() {
				if (!this.mainBody) {     
					return; // not rendered     
			    }     
			    var g = this.grid;     
			    var c = g.getGridEl();     
			    var csize = c.getSize(true);     
			    var vw = csize.width;     
			    if (!g.hideHeaders && (vw < 20 || csize.height < 20)) { 
			        return;     
			    }     
			    if (g.autoHeight) {     
			        if (this.innerHd) {     
			            this.innerHd.style.width = (vw) + 'px';     
			        }     
			    } else {     
			        this.el.setSize(csize.width, csize.height);     
			        var hdHeight = this.mainHd.getHeight();     
			        var vh = csize.height - (hdHeight);     
			        this.scroller.setSize(vw, vh);     
			        if (this.innerHd) {     
			            this.innerHd.style.width = (vw) + 'px';     
			        }     
			    }     
			    if (this.forceFit) {     
			        if (this.lastViewWidth != vw) {     
			            this.fitColumns(false, false);     
			            this.lastViewWidth = vw;     
			        }     
			    } else {     
			        this.autoExpand();     
			        this.syncHeaderScroll();     
			    }     
			    this.onLayout(vw, vh);
			},
			getColumnStyle : function(colIndex, isHeader) {
            	var colModel  = this.cm,
				colConfig = colModel.config,
				style = isHeader ? '' : colConfig[colIndex].css || '',
				align = colConfig[colIndex].align;
            	if(Ext.isChrome){
                	style += String.format("width: {0};", parseInt(this.getColumnWidth(colIndex))-2+'px');
            	}else{
                	style += String.format("width: {0};", this.getColumnWidth(colIndex));
            	}
            	if (colModel.isHidden(colIndex)) {
                	style += 'display: none; ';
            	}
            	if (align) {
                	style += String.format("text-align: {0};", align);
            	}
            	return style;
        	}
		},
		renderTo:renderTo
	}
	if (sm) {
		model.sm = sm;
	}
	if (tb) {
		model.bbar = tb
	} else {
		if (limit > 0 ) {
			model.bbar = createPagingToolbar(store, limit);
		}
	}
	if (tbar) {
		model.tbar = tbar;
	}
	return model;
}
// grid默认选中
function onChecked(grid, store, sm) {
	grid.getStore().on('load', function(s, records) {
    	for (i = 0; i < store.getCount(); i++) {
            sm.selectRow(i, true);
        }
    });
}

// 创建点击不取消其他选择的checkbox
function createMoreCheckboxSelectionModel() {
	return new Ext.grid.CheckboxSelectionModel({
	       	handleMouseDown: function(g, rowIndex, e){  
			    if(e.button !== 0 || this.isLocked()){  
			        return;  
			    }  
			    var view = this.grid.getView();  
			    if(e.shiftKey && !this.singleSelect && this.last !== false){  
			        var last = this.last;  
			        this.selectRange(last, rowIndex, e.ctrlKey);  
			        this.last = last;
			        view.focusRow(rowIndex);  
			    }
			} 
		});
}

function createAlert(msg, message, fn) {
	Ext.MessageBox.alert(msg, message, fn);
}

if  (!Ext.grid.GridView.prototype.templates) {  
    Ext.grid.GridView.prototype.templates = {};  
}  
Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(  
     '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,  
     '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,  
     '</td>'  
);

function createOpenUrl(id) {
	var url = "initExpressDetail?invoiceNo=" + id;
	// initExpressDetail "快件明细"
	var obj = parent.createNodeTab(id, id, url);
	parent.createMainTab(obj);
}

function createOpenUrlById(id, url) {
	url += id;
	var obj = parent.createNodeTab(id, id, url);
	parent.createMainTab(obj);
}

Array.prototype.contains = function(item){ 
	return RegExp(item).test(this); 
}

// 提交form
function doSubmit(url, succFun, params) {
	var bsForm = selectform.getForm();
	bsForm.url = url;
	bsForm.submit({
		params: params,
		timeout:600000,
  		success: function(frm, action) {
  			createAlert('信息提示', "操作成功!" );
  			if (succFun) {
  				succFun(frm, action);
  			}
  		},
		failure: function(frm, action) {
  			var msg = "操作失败!";
       		var result = action.result;
     		if (result) {
     			if (result.msg) {
     				msg = result.msg;
     			}
     		}
     		Ext.getBody().unmask();
       		createAlert('信息提示', msg);
       	}
	});
}

// 获取渠道
function getChannelCombox(name, value) {
	// 渠道
	var channelStore = createJsonStore('list', ['channelCode','channelName'], path + "/scChannel/getScChannelList.htm");
	channelStore.load();
	// 默认选择
	var channelRecord = Ext.data.Record.create([
		'channelCode',
		'channelName'
	]);
	
	var channelRecord_data = new channelRecord({channelCode: '',channelName: '请选择...'});
	channelStore.on("load", function(obj, record) {
		channelStore.insert(0, channelRecord_data);
		if (value) {
			channelComboBox.setValue(value);
		} else {
			channelComboBox.setValue("");
		}
		
	})
	
	var n = "渠道";
	if (name)
		n = name;
	else 
		n = "";
	// 渠道下拉框对象
	var channelComboBox = createComboBox(channelStore, 'channel', 'channelName', 'channelCode', 'local', n);
	
	return channelComboBox;
}
// 获取品牌
function getBrandCombox(name, value) {
	// 品牌
	var brandData = [
		['请选择',''],
		['Metersbonwe','MB'],
		['ME&CITY','MC'],
		['AMPM','B2C'],
		["CH'IN 祺",'CHIN'],
		['米喜迪','KIDS'],
		['MooMoo','MM']
	];
	var brandStore = new Ext.data.ArrayStore({
		fields: ['name', 'id'],
		data : brandData
	});
	// 品牌下拉框对象
	var brandComboBox = createComboBox(brandStore, 'brandCode', 'name', 'id', 'local', name);
	if (value) {
		brandComboBox.setValue(value);
	} else {
		brandComboBox.setValue("");
	}
	return brandComboBox;
}

//批量阀值设置
function setBatchValue(flag) {
	var value = Ext.fly('batchKValue').getValue();
	if (value == Ext.getCmp('batchKValue').emptyText) {
		value = "";
	}
	if (value || value.length > 0) {
		if (flag == 2) {
			var count = store.getCount();
			if (count > 0) {
				batchChangeStock = value;
				//var skuCount = localChangeSku.length;
				// 给记录设置值
				for (var i = 0; i < count; i++) {
					var record = store.getAt(i);
					var sku = record.get('sku');
            		/*var check = false;
            		if (skuCount > 0) {
            			for (var j = 0; j < skuCount; j++) {
            				if (sku == localChangeSku[j]) {
            					check = true;
            					record.set('changeStock', localChangeStock[j]);
            					break;
            				}
            			}
            		} 
					if (!check)*/
					record.set("changeStock", batchChangeStock);  
				}
				createAlert("信息提示", "批量设置成功!");
			} else {
				createAlert("信息提示", "无记录!");
			}
		} else {
			var count = tempUserData.length;
			if (count > 0) {
				for (var i = 0; i < count; i++) {
					tempUserData[i].changeStock = value;
				}
				getPageList(0);
				createAlert("信息提示", "批量设置成功!");
			} else {
				createAlert("信息提示", "无记录!");
			}
		}
	} else {
		createAlert("信息提示", "请输入调整值!");
	}
}

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
}

Ext.Ajax.on('requestcomplete',function(conn,response,options) {
	Ext.getBody().unmask();
	var res = Ext.util.JSON.decode(response.responseText);
	if (res) {
		var authCenter = res.banggo_auth;
		
		if (authCenter) {
			var code = authCenter.code;
			if(code == 401){
				createAlert('提示', '会话超时，请重新登录!', function(){
					var logUrl = path + "/index.htm";
					
					/*if (parent.parent.authPathLogin) {
						logUrl = parent.parent.authPathLogin;
					} else if (parent.parent.parent.authPathLogin) {
						logUrl = parent.parent.parent.authPathLogin;
					}*/
					window.top.location = logUrl; 
				});
			} else if (code == 403) {
				createAlert('提示', '您无权限操作!');
			}
		}
	}
});