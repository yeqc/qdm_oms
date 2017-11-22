/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
Ext.onReady(function(){
    var xd = Ext.data;

	var img = {
   			"images":[
      		{
         		"name":"back.jpg",
         		"url":desktop_path + "wallpapers\/back.jpg"
      		},
      		{
         		"name":"background.jpg",
         		"url":desktop_path + "wallpapers\/background.jpg"
      		},
      		{
         		"name":"blue.jpg",
         		"url":desktop_path + "wallpapers\/blue.jpg"
      		},
      		{
         		"name":"desk.jpg",
         		"url":desktop_path + "wallpapers\/desk.jpg"
      		},
      		/*{
         		"name":"desktop.jpg",
         		"url":"../wallpapers\/desktop.jpg"
      		},
      		{
         		"name":"desktop2.jpg",
         		"url":"../wallpapers\/desktop2.jpg"
      		},
      		{
         		"name":"ext.gif",
         		"url":"../wallpapers\/ext.gif"
      		},*/
      		{
         		"name":"shiny.gif",
         		"url":desktop_path + "wallpapers\/shiny.gif"
      		},
      		{
         		"name":"sky.jpg",
         		"url":desktop_path + "wallpapers\/sky.jpg"
      		}
   			]
		} 

    var store = new Ext.data.JsonStore({
        //url: 'get-images.php',
        root: 'images',
        fields: ['name', 'url']
    });
    //store.load();
    store.loadData(img);
    var tpl = new Ext.XTemplate(
		'<tpl for=".">',
            '<div class="thumb-wrap" id="{name}">',
		    '<div class="thumb"><img src="{url}" title="{name}"></div>',
		    '<span class="x-editable">{shortName}</span></div>',
        '</tpl>',
        '<div class="x-clear"></div>'
	);

    var panel = new Ext.Panel({
        id:'images-view',
        frame:true,
        width:535,
        autoHeight:true,
        //collapsible:true,
        //layout:'fit',
        title:'背景选择器',

        items: dataview = new Ext.DataView({
            store: store,
            tpl: tpl,
            autoHeight:true,
            singleSelect:true,
            multiSelect:false,
            overClass:'x-view-over',
            itemSelector:'div.thumb-wrap',
            emptyText: '没有背景图片',

            plugins: [
                new Ext.DataView.DragSelector()
                //,new Ext.DataView.LabelEditor({dataIndex: 'name'})
            ],

            prepareData: function(data){
                data.shortName = Ext.util.Format.ellipsis(data.name, 15);
                //data.sizeString = Ext.util.Format.fileSize(data.size);
                //data.dateString = data.lastmod.format("m/d/Y g:i a");
                return data;
            },
            
            listeners: {
            	selectionchange: {
            		fn: function(dv,nodes){
            			//var l = nodes.length;
            			//var s = l != 1 ? 's' : '';
            			//panel.setTitle('Simple DataView ('+l+' item'+s+' selected)');
            		}
            	},
            	click : {
            		fn: function(dv, index){
            			
            		}
            	}
            }

		})
    });
	panel.render(Ext.get("img-div"));
   
	var button = new Ext.Button({  
            renderTo: Ext.get("button-div"),  
            text: "确定",  
            minWidth: 50,  
            handler: function(){  
               if (dataview.getSelectedNodes()[0]) {
		        	var name = dataview.getSelectedNodes()[0].id;
		         			
		         	if (parent) {
			        	parent.setBackGroundToCookie(name,parent);
			         	closeWin();
		         	}
		      	} else {
		        	Ext.Msg.alert('提示信息', '您没有选择背景图片!');
		     	}  
            }  
        });  
	/*,buttons:[
			{text:"确定",handler:function(){
		         		
		 		if (dataview.getSelectedNodes()[0]) {
		        	var name = dataview.getSelectedNodes()[0].id;
		         			
		         	if (parent) {
			        	var url = parent.path + "/desktop/wallpapers/"+name;
			         	// 设置到cookie
			       		var expdate = new Date();         
		  				//当前时间加上两周的时间         
						expdate.setTime(expdate.getTime() + 14 * (24 * 60 * 60 * 1000));         
						parent.SetCookie("BackGround", url, expdate); 
			         	parent.document.body.style.background = "#3d71b8 url("+url+") left top";
			         	closeWin();
		         	}
		      	} else {
		        	Ext.Msg.alert('提示信息', '您没有选择背景图片!');
		     	}
		    	   		
			},width : 100},
			{text:"关闭",handler:function(){
		    	closeWin();
		 		},width : 100
			},
		],
		buttonAlign:"center"*/
		
	window.onresize=function(){
		
		if (panel) {
      		panel.setWidth(0);
      		panel.setWidth(Ext.get("img-div").getWidth());
      		panel.setHeight(0);
			panel.setHeight(Ext.get("img-div").getHeight());
      	}
	}

});

function closeWin() {
	var win = parent.MyDesktop.getDesktop().getWindow('dataview');
	win.close();
}