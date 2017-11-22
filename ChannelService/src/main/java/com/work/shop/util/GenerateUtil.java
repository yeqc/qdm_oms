package com.work.shop.util;

import java.util.Date;

public class GenerateUtil {

	private static String previousGroupCode = "";
	private static Integer index = 1;

	public static String generateGroupCode() {
		String dataTime = DateTimeUtils.format(new Date(), DateTimeUtils.YYYYMMDDHHmmss);
		String nextGroupCode = (Constants.GROUP_CODE_PREFIX + dataTime);
		if (StringUtil.isEmpty(previousGroupCode)) {
			index = 1;
			previousGroupCode = nextGroupCode;
			return fill(nextGroupCode);
		} else {
			if (previousGroupCode.equals(nextGroupCode)) {
				index++;
				return fill(nextGroupCode);
			} else {
				index = 1;
				previousGroupCode = nextGroupCode;
				return fill(nextGroupCode);
			}
		}
	}

	private static String fill(String groupCode) {
		if (index < 10) {
			return groupCode + "000" + index;
		}
		if (index < 100) {
			return groupCode + "00" + index;
		}
		if (index < 1000) {
			return groupCode + "0" + index;
		}
		return groupCode + index;
	}

}
