package com.work.shop.util;

import java.security.MessageDigest;

public class SNFXMD5 {
	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes("UTF-8"));
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return str;
	}
	/*public static void main(String[] args) {
	    String source = "{'USERNAME':'admin','PASSWORD':'1'}"+ApiConstant.MD5_KEY;
	    System.out.println(source);
		System.out.println(SNFXMD5.md5(source));
	}*/

}
