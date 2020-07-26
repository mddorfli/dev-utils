package com.omb.devutils.logparser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Param {
	// Thu Jan 01 01:00:00 CET 1970
	private DateFormat LOG_DATEFORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
	// TIMESTAMP '1970-01-01 00:00'
	private DateFormat PSQL_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	ParamType type;
	String value;
	public Param(ParamType type, String value) {
		super();
		this.type = type;
		this.value = value;
	}
	
	public String getQuerySql() {
		String result;
		try {
			switch(type) {
			case BOOLEAN:
				result = value;
				break;
			case TIMESTAMP:
				Date date = LOG_DATEFORMAT.parse(value);
				result = "TIMESTAMP '"+PSQL_DATEFORMAT.format(date)+"'";
				break;
			case VARCHAR:
				result = '\''+value+'\'';
				break;
			case BIGINT:
				result = value;
				break;
			default:
				result = null;
				break;
			}
		} catch (ParseException e) {
			result = null;
		}
		return result;
	}
	
	@Override
	public String toString() {
		return type+":"+value;
	}
}