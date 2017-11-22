/**
 * 表单验证扩展
 */		
//ip验证
Ext.apply(Ext.form.VTypes, {
			IPAddress : function(v) {
				return /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/.test(v);
			},
			IPAddressText : 'ip地址必须为数字加字符.'
		});

//日期验证
Ext.apply(Ext.form.VTypes, {
	dateTimeValid : function(v) {
		return /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/.test(v);
	},
	dateTimeValidText : '日期格式不正确。正确格式例如:2013-09-22 11:34:45'
});