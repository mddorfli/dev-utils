package com.omb.devutils.logparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import com.omb.devutils.logparser.replacement.BindReplacementStrategy;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractSqlLogParser<KEY> {

	private BindReplacementStrategy<KEY> replacementStrategy;

	protected AbstractSqlLogParser(BindReplacementStrategy<KEY> replacementStrategy) {
		this.replacementStrategy = replacementStrategy;
	}

	protected enum LineType {
		SQL, BIND, INVALID
	}

	protected Map<KEY, Param> binds = new HashMap<>();

	/**
	 * @return true if this line contains a bind parameter
	 */
	protected LineType parseLine(String line) {
		LineType result;
		List<Pair<KEY, Param>> kvs = matchLogLine(line);
		if (kvs != null && !kvs.isEmpty()) {
			kvs.forEach(kv -> binds.put(kv.getKey(), kv.getValue()));
			result = LineType.BIND;
		} else {
			result = isValidSQL(line) ? LineType.SQL : LineType.INVALID;
		}
		return result;
	}

	protected boolean isValidSQL(String line) {
		return true;
	}

	protected String process(String sql, List<String> logMessages) {
		return replacementStrategy.process(sql, logMessages, binds);
	}

	protected abstract List<Pair<KEY, Param>> matchLogLine(String line);

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

	public static void main(AbstractSqlLogParser parserImpl) throws IOException {
		StringBuilder sql = new StringBuilder();
		Map<Integer, Param> binds = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.println("Paste SQL:");
			String s;
			do {
				s = br.readLine();
				if (s.toLowerCase().contains("select ") && sql.length() == 0 && binds.isEmpty()) {
					sql.append(s);
					continue;
				}

				parserImpl.parseLine(s);

			} while (s != null && !s.isEmpty());
		}

		List<String> logMessages = new ArrayList<>();
		String result = parserImpl.process(sql.toString(), logMessages);

		logMessages.forEach(System.out::println);
		System.out.println();
		System.out.println(result);
	}
}
