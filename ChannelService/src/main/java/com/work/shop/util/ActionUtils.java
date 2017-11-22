package com.work.shop.util;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

public class ActionUtils {
	
	/**
	 * 取得用户IP
	 * @return
	 */
	public static String getPosterIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip != null && ip.length() != 0) { 
			   while ((ip != null) && (ip.equals("unknow"))) { 

				   ip = request.getHeader("x-forworded-for"); 
			   } 
		} 

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}
	
	
	
	//反射机制
	
	 /* 
     *@param obj 操作的对象 
     *@param att 操作的属性 
     *@param value 设置的值 
     *@param type 参数的类型  int.class;string.class
     */  
    public static void setter(Object obj, String att, Object value, Class<?>type){  
        try {
        	if(att != null && !"".equals(att)){
        		Method met = obj.getClass().getMethod("set" + initStr(att), type);  
                met.invoke(obj, value);  
        	}
            
        }catch (Exception e){  
            e.printStackTrace();  
        }  
    }  
	
    public static String getter(Object obj, String att){  
    	String value = "";
        try {  
        	if(att != null && !"".equals(att)){
        		 Method met = obj.getClass().getMethod("get" + initStr(att));  
                 System.out.println(met.invoke(obj)); 
                 if(met.invoke(obj) != null){
                 	value =	met.invoke(obj).toString();
                 }
        	}
           
        }catch (Exception e){  
            e.printStackTrace();  
        } 
        return value;
    }  
    
    public static String initStr(String old){   // 将单词的首字母大写  
        String str = old.substring(0,1).toUpperCase() + old.substring(1) ;  
        return str ;  
    } 
	
	
	
	
}
