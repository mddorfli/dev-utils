package com.omb.devutils.logparser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractSqlLogParser<KEY> {
	protected enum LineType {
		SQL, BIND, INVALID
	}

	protected Map<KEY, Param> binds = new HashMap<>();

	/**
	 * @return true if this line contains a bind parameter
	 */
	protected LineType parseLine(String line) {
		LineType result;
		Pair<KEY, Param> kv = matchLogLine(line);
		if (kv != null) {
			binds.put(kv.getKey(), kv.getValue());
			result = LineType.BIND;
		} else {
			result = isValidSQL(line) ? LineType.SQL : LineType.INVALID;
		}
		return result;
	}

	protected boolean isValidSQL(String line) {
		return true;
	}

	protected abstract Pair<KEY, Param> matchLogLine(String line);

	abstract String process(String sql, List<String> logMessages);

	public String parseParameterBindings(List<String> lines) {
		StringBuilder sql = new StringBuilder();
		for (Iterator<String> iter = lines.iterator(); iter.hasNext();) {
			String line = iter.next();
			switch (parseLine(line)) {
			case SQL:
				sql.append(line).append('\n');
				break;
			case BIND:
				// ignore
				break;
			case INVALID:
				sql.deleteCharAt(sql.length() - 1);
				break;
			}
		}
		return sql.toString();
	}
}
