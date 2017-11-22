Ext.namespace("ISS");//命名空间


ISS.data =function() {
    ISS.data.superclass.constructor.call(this, {
    	
    }); 
 };
 
Ext.extend(ISS.data, Ext.data, {
 			
});


ISS.grid =function() {
    ISS.grid.superclass.constructor.call(this, {
    	
    }); 
 };
 
Ext.extend(ISS.grid, Ext.grid, {
 			
});


//列模型，定义GridPanel的表头
ISS.grid.ColumnModel=function() {
    ISS.grid.ColumnModel.superclass.constructor.call(this, {
    	
    }); 
 };
 
Ext.extend(ISS.grid.ColumnModel, Ext.grid.ColumnModel, {
 			
});
 
Ext.reg('ISS.grid.ColumnModel', ISS.grid.ColumnModel);//注册

//数据源，负责为表格面板提供各种格式的数据
ISS.data.Store=function() {
    ISS.data.Store.superclass.constructor.call(this, {
    	
    }); 
 };
 
Ext.extend(ISS.data.Store, Ext.data.Store, {
 			
});

//数据代理
ISS.data.HttpProxy=function() {
    ISS.data.HttpProxy.superclass.constructor.call(this, {
    	
    }); 
 };
 
Ext.extend(ISS.data.HttpProxy, Ext.data.HttpProxy, {
 			
});

//Reader 读json中数据
ISS.data.JsonReader=function() {
    ISS.data.JsonReader.superclass.constructor.call(this, {
    	
    }); 
 };
 
ISS.extend(ISS.data.JsonReader, Ext.data.JsonReader, {
 			
});

//定义记录结果
ISS.data.Record = Ext.data.Record;

//表格
ISS.grid.GridPanel=function() {
    ISS.grid.GridPanel.superclass.constructor.call(this, {
    	
    }); 
 };
 
Ext.extend(ISS.grid.GridPanel, Ext.grid.GridPanel, {
 			
});


/////////////////
/**
//列模型，定义GridPanel的表头
 issColumnModel=function() {
    issColumnModel.superclass.constructor.call(this, {
    	
    }); 
 };
 
Ext.extend(issColumnModel, Ext.grid.ColumnModel, {
 			
});
 
//Ext.reg('ColumnModel',ISS.grid.ColumnModel);//注册

//数据源，负责为表格面板提供各种格式的数据
var IssStore=function() {
    IssStore.superclass.constructor.call(this, {
    	
    }); 
 };
 
Ext.extend(IssStore, Ext.data.Store, {
 			
});

//数据代理
var IssHttpProxy=Ext.data.HttpProxy;

//Reader 读json中数据
//var IssJsonReader = Ext.data.JsonReader;
var IssJsonReader=function() {
    IssJsonReader.superclass.constructor.call(this, {
    	
    }); 
 };
 
Ext.extend(IssJsonReader, Ext.data.JsonReader, {
 			
});

//定义记录结果
var IssRecord = Ext.data.Record;

//表格
var IssGridPanel=function() {
    IssGridPanel.superclass.constructor.call(this, {
    	
    }); 
 };
 
Ext.extend(IssGridPanel, Ext.grid.GridPanel, {
 			
});
 */
