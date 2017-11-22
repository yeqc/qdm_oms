Ext.onReady(function() {
	var ALLChannels = [ 'channelName1', 'channelName2', 'channelName3',
			'channelName4', 'channelName5', 'channelName6' ];
	var ALLColumnNams = [ 'Column1', 'Column2', 'Column2', 'Column4',
			'Column5', 'Column6', 'Column7' ];
	var fields = [];
	columnsA = [];
	data = [];
	titleGroupRow = [];

	function generateConfig() {
		var arr, numALLColumnNams = ALLColumnNams.length;
		Ext.iterate(ALLChannels, function(channelName) {
			titleGroupRow.push({
				header : channelName,
				align : 'center',
				colspan : numALLColumnNams
			});

			Ext.each(ALLColumnNams, function(columnTitle) {
				fields.push({
					type : 'int',
					name : columnTitle
				});

				columnsA.push({
					dataIndex : columnTitle,
					header : columnTitle,
					renderer : Ext.util.Format.usMoney
				});
			});

			arr = [];
			for ( var i = 0; i < 20; ++i) {
				arr.push((Math.floor(Math.random() * 11) + 1) * 100000);
			}

			data.push(arr);

		});
	}

	generateConfig();

	var group = new Ext.ux.grid.ColumnHeaderGroup({
		rows : [ titleGroupRow ]
	});

	var grid = new Ext.grid.GridPanel({
		renderTo : 'column-group-grid',
		title : 'Sales By Location',
		width : '100%',
		height : 400,
		store : new Ext.data.ArrayStore({
			fields : fields,
			data : data
		}),
		columns : columnsA,
		viewConfig : {
			forceFit : true
		},
		plugins : group
	});
});