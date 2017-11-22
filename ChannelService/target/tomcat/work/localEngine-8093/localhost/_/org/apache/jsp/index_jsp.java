package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(2);
    _jspx_dependants.add("/page/page.jsp");
    _jspx_dependants.add("/page/script.jsp");
  }

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd\">\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
 
	String url = request.getRequestURI();
	String url2 = url.substring(url.lastIndexOf("/") + 1, url.length());

      out.write('\r');
      out.write('\n');

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);

      out.write("\r\n");
      out.write("<base href=\"");
      out.print(basePath);
      out.write("\">\r\n");
      out.write("\r\n");
      out.write("<input type=\"hidden\" id=\"system\" name=\"system\"  value=\"");
      out.print(request.getParameter("system"));
      out.write("\" />\r\n");
      out.write("<input type=\"hidden\" id=\"channelCode\" name=\"channelCode\"  value=\"");
      out.print(request.getParameter("channelCode"));
      out.write("\" />\r\n");
      out.write("<input type=\"hidden\" id=\"shopCode\" name=\"shopCode\"  value=\"");
      out.print(request.getParameter("shopCode"));
      out.write("\" />\r\n");
      out.write("<input type=\"hidden\" id=\"menuType\" name=\"menuType\"  value=\"");
      out.print(request.getParameter("menuType"));
      out.write("\" />\r\n");
      out.write("<META http-equiv=Content-Type content=\"text/html; charset=utf-8\">\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
      out.print(basePath);
      out.write("/js/ext3.4/resources/css/ext-all.css\" />\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
      out.print(basePath);
      out.write("/js/ext3.4/resources/shared/icons/silk.css\" />\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
      out.print(basePath);
      out.write("/js/ext3.4/ux/css/MultiSelect.css\"/>\r\n");
      out.write("\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("js/jquery/jquery-1.6.2.min.js\"></script >\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/ext3.4/adapter/ext/ext-base.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/ext3.4/ext-all.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/ext3.4/ux/TabCloseMenu.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/ext3.4/ux/RowExpander.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/iss.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/banggo/js/common/cookie.js\"></script>\r\n");
      out.write("\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/leftConTree.js\"></script>\t\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/ext3.4/ux/MultiSelect.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/ext3.4/ux/ItemSelector.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/codepress/codepress.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/codepress/Ext.ux.codepress.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("js/ext3.4/ux/treegrid/TreeGridSorter.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("js/ext3.4/ux/treegrid/TreeGridColumnResizer.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("js/ext3.4/ux/treegrid/TreeGridNodeUI.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("js/ext3.4/ux/treegrid/TreeGridLoader.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("js/ext3.4/ux/treegrid/TreeGridColumns.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("js/ext3.4/ux/treegrid/TreeGrid.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/ext3.4/ux/CheckColumn.js\"></script>\r\n");
      out.write("\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      out.print(basePath);
      out.write("/js/my97/WdatePicker.js\"></script>\r\n");
      out.write("\r\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("<!--\n");
      out.write("Ext.BLANK_IMAGE_URL = 'js/ext3.4/resources/images/default/s.gif';\n");
      out.write("//-->\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\t<title>Channel</title>\r\n");
      out.write("\t<style type=\"text/css\">\r\n");
      out.write("\t\thtml, body {\r\n");
      out.write("\t\t\t/**font:normal 12px verdana;*/\r\n");
      out.write("\t\t\tfont:normal 12px ;\r\n");
      out.write("\t\t\tmargin:0;\r\n");
      out.write("\t\t\tpadding:0;\r\n");
      out.write("\t\t\tborder:0 none;\r\n");
      out.write("\t\t\toverflow:hidden;\r\n");
      out.write("\t\t\theight:100%;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t</style>\r\n");

 String no = "admin";

      out.write("\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("// 全局路径\r\n");
      out.write("var basePath = \"");
      out.print(basePath);
      out.write("\";\r\n");
      out.write("var no = \"");
      out.print(no);
      out.write("\";\r\n");
      out.write("var cnode;\r\n");
      out.write("Ext.BLANK_IMAGE_URL = 'js/ext3.4/resources/images/default/s.gif';\r\n");
      out.write("var pageSize = 20;//分页-每页数据条数\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("function shop(systemStr,shopCode, channelCode, channelTitle,shopTitle){\r\n");
      out.write("\tvar obj=new Object();\r\n");
      out.write("\tobj.system=systemStr;\r\n");
      out.write("\tobj.shopCode=shopCode;\r\n");
      out.write("\tobj.channelCode=channelCode;\r\n");
      out.write("\tobj.channelTitle=channelTitle;\r\n");
      out.write("\tobj.shopTitle=shopTitle;\r\n");
      out.write("\treturn obj; \r\n");
      out.write("}\r\n");
      out.write("var shopObj = new shop('");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${system}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      out.write('\'');
      out.write(',');
      out.write('\'');
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${channelShopVo.shopCode}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      out.write("', '");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${channelShopVo.channelCode}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      out.write("', '");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${channelShopVo.channelTitle}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      out.write('\'');
      out.write(',');
      out.write('\'');
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${channelShopVo.shopTitle}", java.lang.String.class, (PageContext)_jspx_page_context, null, false));
      out.write("');\r\n");
      out.write("\r\n");
      out.write("function getGlobalValue(key){\r\n");
      out.write("\tif (shopObj != null) {\r\n");
      out.write("\t\tvar value = shopObj[key];\r\n");
      out.write("\t\treturn value;\r\n");
      out.write("\t}\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("function logout(){\r\n");
      out.write("\tExt.Msg.confirm(\"退出\", \"确定要退出吗？\", function(btn) {\r\n");
      out.write("\t\tif (btn == \"yes\") {\r\n");
      out.write("\t\t\tlocation.href=\"");
      out.print(basePath);
      out.write("page/user/userLogout.ct\";\r\n");
      out.write("\t\t}\r\n");
      out.write("\t});\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("//头部菜单\r\n");
      out.write("var issMainMenu = new Ext.Toolbar({\r\n");
      out.write("\tenableOverflow: true,\r\n");
      out.write("\theight: 30\r\n");
      out.write("});\r\n");
      out.write("issMainMenu.add(\r\n");
      out.write("\tnew Ext.Toolbar.Fill({id:'fill'}),\r\n");
      out.write("\tnew Ext.Toolbar.TextItem(no+\",欢迎您\"),\r\n");
      out.write("\t'-',\r\n");
      out.write("\t{\r\n");
      out.write("\t\tid: 'aad',\r\n");
      out.write("\t\ttext:'资料修改',\r\n");
      out.write("\t\thandler: function(){\r\n");
      out.write("\t\t\tFormEditWin.showAddDirWin(\"popWins\",\"");
      out.print(basePath);
      out.write("/page/user/userUpdate.ct?no=");
      out.print(no );
      out.write("\",\"userWinID\",\"编辑用户\",550,420);\r\n");
      out.write("\t\t}\r\n");
      out.write("\t},'-',\r\n");
      out.write("\t{\r\n");
      out.write("\t\tid: 'ee',\r\n");
      out.write("\t\ttext:'退出',\r\n");
      out.write("\t\t\thandler: function(){\r\n");
      out.write("\t\t\tlogout();\r\n");
      out.write("\t\t\t//Ext.Msg.alert(\"提示\",\"你已安全退出\");\r\n");
      out.write("\t\t\t//location.href=\"");
      out.print(basePath);
      out.write("login.html\";\r\n");
      out.write("\t\t}\r\n");
      out.write("\t}\r\n");
      out.write(");\r\n");
      out.write("\r\n");
      out.write("//主页面\r\n");
      out.write("IssmainCenterTabs = function() {\r\n");
      out.write("\tIssmainCenterTabs.superclass.constructor.call(this, {\r\n");
      out.write("\t\tid: 'issmainCenterTabs',\r\n");
      out.write("\t \tregion: 'center', // a center region is ALWAYS required for border layout\r\n");
      out.write("\t\tdeferredRender: false,///一次性将选项卡内容全部加载,不推荐\r\n");
      out.write("\t\tactiveTab: 0,\r\n");
      out.write("\t\tresizeTabs:true, // turn on tab resizing\r\n");
      out.write("\t\tminTabWidth: 115,\r\n");
      out.write("\t\ttabWidth:135,\r\n");
      out.write("\t\tenableTabScroll:true,\r\n");
      out.write("\t\twidth:600,\r\n");
      out.write("\t\theight:250,\r\n");
      out.write("\t\tmargins: '0 5 0 0',\r\n");
      out.write("\t\tdefaults: {autoScroll:true},\r\n");
      out.write("\t\tplugins: new Ext.ux.TabCloseMenu()\r\n");
      out.write("\t}); \r\n");
      out.write("};\r\n");
      out.write("\t\r\n");
      out.write("Ext.extend(IssmainCenterTabs, Ext.TabPanel, {\r\n");
      out.write("\tloadClass2 : function(href, cls){\r\n");
      out.write("\t\tvar id = 'iss-' + cls;\r\n");
      out.write("\t\tvar tab = this.getComponent(id);\r\n");
      out.write("\t\tif(tab){\r\n");
      out.write("\t\t\tthis.setActiveTab(tab);\r\n");
      out.write("\t\t\t/*if(cls.id=='iss-内容管理  '){\r\n");
      out.write("\t\t\t var autoLoad = {url: href, scripts: true};\r\n");
      out.write("\t\t\t tab.autoLoad=autoLoad;\r\n");
      out.write("\t\t\t}*/\r\n");
      out.write("\t\t}else{\r\n");
      out.write("\t\t\tvar autoLoad = {url: href, scripts: true};\r\n");
      out.write("\t\t\tvar p = this.add({\r\n");
      out.write("\t\t\t\tid: id,\r\n");
      out.write("\t\t\t\ttitle: cls,\r\n");
      out.write("\t\t\t\tframe : true,\r\n");
      out.write("\t\t\t\tautoScroll : true,\r\n");
      out.write("\t\t\t\tautoShow : true,\r\n");
      out.write("\t\t\t\tautoLoad: autoLoad,\r\n");
      out.write("\t\t\t\tclosable:true\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\tthis.setActiveTab(p);\r\n");
      out.write("\t\t}\r\n");
      out.write("\t},\r\n");
      out.write("\tloadClass : function(href, cls, idx){\r\n");
      out.write("\t\tvar id = 'iss-' + idx;\r\n");
      out.write("\t\tvar tab = this.getComponent(id);\r\n");
      out.write("\t\tif(tab){\r\n");
      out.write("\t\t\tthis.setActiveTab(tab);\r\n");
      out.write("\t\t\t/*if(cls.id=='iss-内容管理  '){\r\n");
      out.write("\t\t\t var autoLoad = {url: href, scripts: true};\r\n");
      out.write("\t\t\t tab.autoLoad=autoLoad;\r\n");
      out.write("\t\t\t}*/\r\n");
      out.write("\t\t}else{\r\n");
      out.write("\t\t\tvar autoLoad = {url: href, scripts: true};\r\n");
      out.write("\t\t\tvar p = this.add({\r\n");
      out.write("\t\t   \t\tid: id,\r\n");
      out.write("\t\t\t\ttitle: cls,\r\n");
      out.write("\t\t\t\tframe : true,\r\n");
      out.write("\t\t\t\tautoScroll : true,\r\n");
      out.write("\t\t\t\tautoShow : true,\r\n");
      out.write("\t\t\t\tautoLoad: autoLoad,\r\n");
      out.write("\t\t\t\tclosable:true\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t\tthis.setActiveTab(p);\r\n");
      out.write("\t\t}\r\n");
      out.write("\t}\r\n");
      out.write("});\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("var issmainCenterTabs = new IssmainCenterTabs();\r\n");
      out.write("\r\n");
      out.write("//共通菜单跳转函数\r\n");
      out.write("function commonMenu(item, url,idx){\r\n");
      out.write("\tissmainCenterTabs.loadClass(url,item.text,idx);\t\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("function initPage(){\r\n");
      out.write("\trunContree();\r\n");
      out.write("\t/**\r\n");
      out.write("\tif(!issMainMenu.findById('2')) {\r\n");
      out.write("\t\tvar treepanel =  Ext.getCmp('tNode_20110714');\r\n");
      out.write("\t\tif(treepanel) {\r\n");
      out.write("\t\t\ttreepanel.collapse();\r\n");
      out.write("\t\t\ttreepanel.hide();\r\n");
      out.write("\t\t}\r\n");
      out.write("\t} else {\r\n");
      out.write("\t\trunContree();\r\n");
      out.write("\t}\r\n");
      out.write("\t**/\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("//页面初始化\r\n");
      out.write("Ext.onReady(function(){\r\n");
      out.write("\t\t//\t\t\tcouponPanel = new Ext.Panel({\r\n");
      out.write("\t\t//\t\t\t\trenderTo : 'coupon-grid',\r\n");
      out.write("\r\n");
      out.write("\t//\t\t\t\t\tlayout : 'column',\r\n");
      out.write("\t\t//\t\t\t\titems : [ outChannelShopForm, couponGrid ]\r\n");
      out.write("\t\t\t//\t\t}).show();\r\n");
      out.write("\t\t\t\r\n");
      out.write("\r\n");
      out.write("\t//issmainCenterTabs.add({\r\n");
      out.write("\t//\tid:'iss-welcome',\r\n");
      out.write("\t//\ttitle: 'welcome',\r\n");
      out.write("\t//html: '统一渠道',\r\n");
      out.write("\t//loader: new Ext.tree.TreeLoader({dataUrl:basePath+ \"custom/channelArea/shopGoodsUpDownPage.spmvc\"}),\r\n");
      out.write("\t\t//closable:true\r\n");
      out.write("\t//}).show();\r\n");
      out.write("\t\r\n");
      out.write("\tvar panel = new Ext.Panel({\r\n");
      out.write("\t\tlayout : 'border',\r\n");
      out.write("\t\titems : [{\r\n");
      out.write("\t\t\tid:'tNode_20110714',\r\n");
      out.write("\t\t\tlayout:'fit',\r\n");
      out.write("\t\t\tregion: 'west',\r\n");
      out.write("\t\t\tsplit: true,\r\n");
      out.write("\t\t\tcontentEl:'treeNode',\r\n");
      out.write("\t\t\twidth: 232,\r\n");
      out.write("\t\t\tautoHeight:true,\r\n");
      out.write("\t\t\theight:500\r\n");
      out.write("\t\t} ,\r\n");
      out.write("\t\tissmainCenterTabs]\r\n");
      out.write("\t});\r\n");
      out.write("\r\n");
      out.write("\tvar viewport = new Ext.Viewport({\r\n");
      out.write("\t\tlayout:'fit',\r\n");
      out.write("\t\titems: [panel]\r\n");
      out.write("\t});\r\n");
      out.write("\tinitPage();\r\n");
      out.write("\t});\r\n");
      out.write("\t</script>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("\t\r\n");
      out.write("\t<div id=\"south\" class=\"x-hide-display\">\r\n");
      out.write("\t\t<table>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td align=\"left\" width=\"800\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t\r\n");
      out.write("\t</div>\r\n");
      out.write("\t<div id=\"treeNode\"  class=\"x-hide-display\" style=\"over-flow:scroll-y;\">\r\n");
      out.write("\t\t<div id=\"temtree\"></div>\r\n");
      out.write("\t\t<div id=\"contree\"></div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else log(t.getMessage(), t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
