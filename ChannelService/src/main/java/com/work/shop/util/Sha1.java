
package com.work.shop.util;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1
{

	private static String hexDigits[] = {
		"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", 
		"a", "b", "c", "d", "e", "f"
	};

	public Sha1()
	{
	}

	public static String sha1(byte b[])
		throws NoSuchAlgorithmException
	{
		MessageDigest md5 = MessageDigest.getInstance("SHA1");
		md5.update(b, 0, b.length);
		return byteArrayToHexString(md5.digest());
	}

	public static String sha1File(File file)
		throws NoSuchAlgorithmException, IOException
	{
		MessageDigest md = MessageDigest.getInstance("SHA1");
		InputStream in = new FileInputStream(file);
		byte buffer[] = new byte[8192];
		for (int len = -1; (len = in.read(buffer)) != -1;)
			md.update(buffer, 0, len);

		return byteArrayToHexString(md.digest());
	}

	private static String byteArrayToHexString(byte b[])
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
		{
			sb.append(hexDigits[b[i] >>> 4 & 0xf]);
			sb.append(hexDigits[b[i] & 0xf]);
		}
		return sb.toString();
	}
}
