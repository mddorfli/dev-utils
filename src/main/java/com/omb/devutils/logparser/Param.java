package com.omb.devutils.logparser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Param {
	// Thu Jan 01 01:00:00 CET 1970
	private static final DateFormat LOG_DATEFORMAT_LONG = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
	// 2020-09-06 00:00:00.0
	private static final DateFormat LOG_DATEFORMAT_SHORT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	// 1970-01-01 00:00
	private static final DateFormat PSQL_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	ParamType type;
	String value;

	public Param(ParamType type, String value) {
		super();
		this.type = type;
		this.value = value;
	}

	public String getQuerySql() {
		String result;
		switch (type) {
		case BOOLEAN:
			result = value;
			break;
		case TIMESTAMP:
			Date date = parseLogTimestamp(value);
			result = date == null ? null : "TIMESTAMP '" + PSQL_DATEFORMAT.format(date) + "'";
			break;
		case VARCHAR:
			result = '\'' + value + '\'';
			break;
		case BIGINT:
		case INTEGER:
			result = value;
			break;
		default:
			result = null;
			break;
		}
		return result;
	}

	@Override
	public String toString() {
		return type + ":" + value;
	}

	static Date parseLogTimestamp(String value) {
		Date result = null;
		try {
			result = LOG_DATEFORMAT_LONG.parse(value);
		} catch (ParseException e) {
			// try alternative
			try {
				result = LOG_DATEFORMAT_SHORT.parse(value);
			} catch (ParseException e1) {
				// give up
			}
		}

		return result;
	}
}