<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/page/page.jsp"%>
<%@ include file="/page/script.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script type="text/javascript">
Ext.onReady(function() {
	Ext.BLANK_IMAGE_URL = 'js/ext3.4/resources/images/default/s.gif';
	var tree = Ext.tree;
	var root = new tree.TreeNode({ text : "根节点", singleClickExpand : true//是否单击打开
	});
	var node = new tree.TreeNode({text : "一级节点" });
	var node1 = new tree.TreeNode({text : "二级节点" });
	var node2 = new tree.TreeNode({text : "三级节点" });
	var node3 = new tree.TreeNode({text : "一级节点子节点" });
	var node4 = new tree.TreeNode({text : "一级节点子节点" });
	var node5 = new tree.TreeNode({text : "一级节点子节点" });

	var node6 = new tree.TreeNode({text : "二级节点子节点"});
	var node7 = new tree.TreeNode({text : "二级节点子节点"});
	var node8 = new tree.TreeNode({text : "三级节点子节点"});
	node.appendChild([node3, node4, node5]);
	node1.appendChild([node6, node7]);
	node2.appendChild([node8]);
	root.appendChild([node,node1,node2]);
	var treepanel = new tree.TreePanel({
	title : "统一渠道",
	width : 500,
	height : 300,
	lines : true, // 设置为true 有线条
	animate : true
	});
	treepanel.setRootNode(root);
	treepanel.expand();
	treepanel.render("aa");
	});

</script>

<body>
<div id="aa"></div>
</body>
</html>