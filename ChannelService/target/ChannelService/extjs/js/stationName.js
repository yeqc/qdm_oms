var stationCode = "";
// 省store
function createProvinceStore(path){
    var provinceStore = new Ext.data.JsonStore({
        fields:['region_id','region_name','region_type'],
        url:path+'/getRegionsValue.action?parentId=1&regionType=1',
        root:'regions',
        autoLoad:true
    });
    return provinceStore;
}

//创建省Combobox
function createProvince(id, provinceStore,selFun){
    return  new Ext.form.ComboBox({
        fieldLabel:"省",
        id: id,
        //xtype:"combo",
        hiddenName:"regsion",
        displayField:'region_name',
        inputType:"text",
        valueField:'region_name',
        emptyText:'--选择省--',
        width:150,
        mode:"local",
        editable :false,
        triggerAction:"all",
        layout:"form",
        forceSelection:true,
        store:provinceStore,
        listeners:{
            'select':selFun
        }
    });
}



/****************************************************************/
//市store
function createCityStore(path){
    var cityStore = new Ext.data.JsonStore({
       // storeId:"cityStore",
        fields:['region_id','region_name','region_type'],
        url:path+'/getRegionsValue.action',
        root:'regions'
        //autoLoad:true,
    });
    return cityStore;
}

function createCity(id,cityStore,selFun){
    return  new Ext.form.ComboBox({
       	id:id,
       	fieldLabel:"市",
        xtype:"combo",
        hiddenName:"city",
        displayField:'region_name',
        inputType:"text",
        valueField:'region_name',
        emptyText:'--选择市--',
        mode:"local",
        editable :false,
        width:150,
        triggerAction:"all",
        forceSelection:true,
        store:cityStore,
        listeners:{
            'select':selFun
        }
    });
}

/***********************************************************/


//区县store
function createAreaStore(path){
    var areaStore = new Ext.data.JsonStore({
       // storeId:'areaStore',
        fields:['region_id','region_name','region_type'],
        url:path+'/getRegionsValue.action',
        root:'regions'
    });
    return areaStore;
}
function createArea(id,areaStore,selFun){
    return new Ext.form.ComboBox({
       	id:id,
       	fieldLabel:"区县",
        xtype:"combo",
        hiddenName:"area",
        displayField:'region_name',
        inputType:"text",
        valueField:'region_name',
        emptyText:'--选择区县--',
        mode:"local",
        editable :false,
        width:150,
        triggerAction:"all",
        forceSelection:true,
        store:areaStore,
        listeners:{
            'select':selFun
        }
    });
}
/*******************************************************************/
//站点stort
/*function createStatStore(){
  var statStore = new Ext.data.JsonStore({
      storeId:'stationStore',
      fields:['stationCode','stationName','stationId'],
      url:'getStationValue',
      root:'showSystemStation',
      baseParams:{"province":-1,"city":-1,"area":-1},
      autoLoad:true
  });
  return statStore;
}
function createStation(statStore,selFun){
  return new Ext.form.ComboBox({
      //	id:"station",
	  fieldLabel:"站点"+cf,
      xtype:"combo",
      hiddenName:"station",
      displayField:'stationName',
      inputType:"text",
      valueField:'stationId',
      emptyText:'--全部--',
      width:150,
      mode:"local",
      editable :false,
      triggerAction:"all",
      forceSelection:true,
      store:statStore,
      listeners:{
          // scope: yourScope,
          'select':selFun
      }
  });
}

function buildStationCode(records, options, success){
  stationCode = "";
  for(var i = 0 ; i < records.length; i++){
      stationCode += ","+records[i].get('stationCode');
  }
}
*/