package org.apache.jsp.banggo.desktop;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.work.shop.united.client.filter.config.Config;
import com.work.shop.united.client.facade.UserStore;

public final class desktop_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

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
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
      out.write("<html>\r\n");
      out.write("\t<head>\r\n");
      out.write("\t\t");

			String path = request.getContextPath();
		
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		
		    String  exitPath =  Config.getAuthCenterUrl();
		    
			String userName = UserStore.get(request).getUserName();
		
      out.write("\r\n");
      out.write("\t\t<title>统一渠道系统</title>\r\n");
      out.write("\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n");
      out.write("\t\t<link rel=\"shortcut icon\" href=\"");
      out.print(path);
      out.write("/banggo/images/ico.ico\">\r\n");
      out.write("\t\t <script type=\"text/javascript\">\r\n");
      out.write("\t\t\tvar path = '");
      out.print(path);
      out.write("';\r\n");
      out.write("\t\t\tvar basePath = '");
      out.print(basePath);
      out.write("';\r\n");
      out.write("\t\t </script>\r\n");
      out.write("\t\t<script type=\"text/javascript\" src=\"");
      out.print(path);
      out.write("/banggo/js/common/ext.js\"></script>\r\n");
      out.write("\t\t<script type=\"text/javascript\" src=\"");
      out.print(path);
      out.write("/js/jquery/jquery-1.6.2.min.js\"></script>\r\n");
      out.write("\t    <script type=\"text/javascript\">\r\n");
      out.write("\t \t\t// 路径\r\n");
      out.write("\t\t\r\n");
      out.write("\t    \timportDesktopCss(\"desktop.css\");\r\n");
      out.write("\t    \tvar exitPath = '");
      out.print(exitPath);
      out.write("';\r\n");
      out.write("\t    \tvar userName = '");
      out.print(userName);
      out.write("';\r\n");
      out.write("\t    \r\n");
      out.write("\t    </script>\r\n");
      out.write("\t      \r\n");
      out.write("\t    <style type=\"text/css\">\r\n");
      out.write("\t    \r\n");
      out.write("\t    #x-shortcuts dt {\r\n");
      out.write("    clear: both;\r\n");
      out.write("    display: block;\r\n");
      out.write("    float: left;\r\n");
      out.write("    font: 12px tahoma,arial,verdana,sans-serif;\r\n");
      out.write("    height: 90px;\r\n");
      out.write("    margin: 0px 0 0 2px;\r\n");
      out.write("    padding-top: 10px;\r\n");
      out.write("    text-align: center;\r\n");
      out.write("    width: 82px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#x-shortcuts dt a {\r\n");
      out.write("    color: #000000;\r\n");
      out.write("    display: block;\r\n");
      out.write("    text-decoration: none;\r\n");
      out.write("    width: 90px;\r\n");
      out.write("}\r\n");
      out.write("\t    \r\n");
      out.write("\t    \r\n");
      out.write("\t    </style>\r\n");
      out.write("\t</head>\r\n");
      out.write("\r\n");
      out.write("\t<body scroll=\"no\">\r\n");
      out.write("\t\t<form action=\"\" method=\"POST\" name=\"form\">\r\n");
      out.write("\t\t\t<input type=\"hidden\" value=\"\" name=\"flag\" id=\"flag\"/>\r\n");
      out.write("\t\t\t<input type=\"hidden\" value=\"\" name=\"name\" id=\"name\"/>\r\n");
      out.write("\t\t</form>\r\n");
      out.write("\r\n");
      out.write("\t\t<script>\r\n");
      out.write("\t\r\n");
      out.write("\t\t\timportJsPath(\"cookie.js\");\r\n");
      out.write("\t    \timportJsDesPath(\"js/Menu.js?d=\"+new Date().getTime());\r\n");
      out.write("\t    \timportJsPath(\"right.js\");\r\n");
      out.write("\t\t</script>\r\n");
      out.write("\t\t<div id=\"x-desktop\">\r\n");
      out.write("\t\t\t<div id=\"x-commcon\">\r\n");
      out.write("\t\t\t   \t<script>\r\n");
      out.write("\t\t\t   \t\r\n");
      out.write("\t\t\t   \t\twrite('', '");
      out.print(request.getAttribute("flag"));
      out.write("');\r\n");
      out.write("\t\t\t   \t</script>\r\n");
      out.write("\t\t   \t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div id=\"ux-taskbar\">\r\n");
      out.write("\t\t\t<div id=\"ux-taskbar-start\"></div>\r\n");
      out.write("\t\t\t<div id=\"ux-taskbuttons-panel\"></div>\r\n");
      out.write("\t\t\t<div class=\"x-clear\"></div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<!-- DESKTOP -->\r\n");
      out.write("\t\t<script>\r\n");
      out.write("\t\t\timportJsDesPath(\"js/StartMenu.js\");\r\n");
      out.write("\t\t\timportJsDesPath(\"js/TaskBar.js\");\r\n");
      out.write("\t\t\timportJsDesPath(\"js/Desktop.js\");\r\n");
      out.write("\t\t\timportJsDesPath(\"js/App.js\");\r\n");
      out.write("\t\t\timportJsDesPath(\"js/Module.js\");\r\n");
      out.write("\t\t\timportJsDesPath(\"sample.js\");\r\n");
      out.write("\t\t</script>\r\n");
      out.write("\t</body>\r\n");
      out.write("</html>\r\n");
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
