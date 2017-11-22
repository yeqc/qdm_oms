package com.work.shop.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShopFilter  implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse hres = (HttpServletResponse) response;
		//String path = hreq.getContextPath();
		String url = hreq.getRequestURI();
		
		//Object user = hreq.getSession().getAttribute("shopName");	
		//String urlwrong = path+"/view/base/msg.jsp";
		/* String shop =  hreq.getParameter("shopCode");
		if(url.indexOf("/index.jsp")>0){
			hreq.getSession().setAttribute("shopCode", shop);
			chain.doFilter(request, response);
		}*/
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
