package com.omb.devutils.logparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoutSqlParser {

	private static Pattern REGEX = Pattern
			.compile("^IN  (?<bind>:\\w+) => \\? \\[(?:BIGINT|INTEGER|VARCHAR|BOOLEAN) (?<value>\\w+)\\]$");

	public static void main(String[] args) throws IOException {
		StringBuilder sql = new StringBuilder();
		Map<String, String> binds = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.println("Paste SQL:");
			String s;
			do {
				s = br.readLine();
				if ("*** UNPARSED ***".equals(s)) {
					sql.deleteCharAt(sql.length() - 1);
					continue;
				}

				Matcher matcher = REGEX.matcher(s);
				if (matcher.matches()) {
					String bind = matcher.group("bind");
					String value = matcher.group("value");
					binds.put(bind, value);

				} else if (binds.isEmpty()) {
					sql.append(s).append('\n');
				}
			} while (s != null && !s.isEmpty());
		}

		for (Entry<String, String> entry : binds.entrySet()) {
			String bind = entry.getKey();
			String value = entry.getValue();
			for (int i = 0; (i = sql.indexOf(bind, i)) != -1;) {
				String replacement = "/*" + bind + "*/" + value;
				sql.replace(i, i + bind.length(), replacement);
				i += replacement.length();
			}
		}
		System.out.println(sql.toString());
	}

}
