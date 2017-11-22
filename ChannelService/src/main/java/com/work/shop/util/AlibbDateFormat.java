package com.work.shop.util;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by colin on 15-12-14.
 */
public class AlibbDateFormat extends SimpleDateFormat {


	public AlibbDateFormat() {
		super("yyyyMMddHHmmss");
	}

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		return super.format(date, toAppendTo, fieldPosition);
	}

	@Override
	public Date parse(String source, ParsePosition pos) {
		//20151214210356000+0800
		String _source;

		if (source != null && source.matches("^\\d17+0800$")) {
			_source = source.substring(0, 14);
		} else {
			_source = source;
		}
		assert _source != null;
		return super.parse(_source, pos);
	}

}