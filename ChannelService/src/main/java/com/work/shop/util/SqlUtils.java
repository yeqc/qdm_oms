package com.work.shop.util;

/**
 * 
 * @author lmqu
 * @since 1.0
 * @version 1.0
 */
public class SqlUtils {
	
	
	public static String getLike(String key){
		return "%" + key + "%";
	}
	
	public static String getLeftLike(String key){
		return "%" + key;
	}
	
	public static String getRightLike(String key){
		return key + "%";
	}
	
	/** 
	 * sql的in语句里的内容作成
	 * @param ids
	 * @return
	 */
	public static String getSqlIn(String fieldName, String[] inArray) {
		String inId = "";
		if (inArray == null || inArray.length <= 0) {
			return null;
		}
		
		for (int i=0; i<inArray.length; i++) {
			inId = inId + "'" + inArray[i] + "'";
			
			if (i < inArray.length-1) {
				inId += ",";
			}
		}
		
		return fieldName + " in (" +inId + ")";
	}
}
