package com.omb.devutils.logparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

public class ScoutSqlParser implements ISqlLogParser<String> {
	// IN :preferredLanguageUid => ? [BIGINT 11001]
	static Pattern REGEX = Pattern.compile("^IN (?<bind>:\\w+) => \\? \\[(?<type>\\w+) (?<value>\\d+)\\]$");

	@Override
	public Pair<String, Param> matchLogLine(String line) {
		Pair<String, Param> result = null;
		Matcher matcher = REGEX.matcher(line);
		if (matcher.matches()) {
			String bind = matcher.group("bind");
			String type = matcher.group("type");
			String value = matcher.group("value");
			Param param = new Param(ParamType.valueOf(type), value);
			result = Pair.of(bind, param);
		}
		return result;
	}

	@Override
	public String process(String sql, Map<String, Param> binds, List<String> logMessages) {
		StringBuilder result = new StringBuilder(sql);
		for (Entry<String, Param> entry : binds.entrySet()) {
			String bind = entry.getKey();
			Param value = entry.getValue();
			for (int i = 0; (i = result.indexOf(bind, i)) != -1;) {
				String replacement = "/*" + bind + "*/" + value.getQuerySql();
				result.replace(i, i + bind.length(), replacement);
				i += replacement.length();
			}
		}
		return result.toString();
	}

	public static void main(String[] args) throws IOException {
		ScoutSqlParser me = new ScoutSqlParser();
		StringBuilder sql = new StringBuilder();
		Map<String, Param> binds = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.println("Paste SQL:");
			String s;
			do {
				s = br.readLine();
				if ("*** UNPARSED ***".equals(s)) {
					sql.deleteCharAt(sql.length() - 1);
					continue;
				}

				Pair<String, Param> kv = me.matchLogLine(s);
				if (kv != null) {
					binds.put(kv.getKey(), kv.getValue());
				} else if (binds.isEmpty()) {
					sql.append(s).append('\n');
				}
			} while (s != null && !s.isEmpty());
		}
		List<String> logMessages = new ArrayList<>();
		String result = me.process(sql.toString(), binds, logMessages);
		logMessages.forEach(System.out::println);
		System.out.println();
		System.out.println(result);
	}

}
