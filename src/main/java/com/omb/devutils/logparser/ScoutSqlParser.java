package com.omb.devutils.logparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ScoutSqlParser extends AbstractSqlLogParser<String> {
	// IN :preferredLanguageUid => ? [BIGINT 11001]
	static Pattern REGEX = Pattern.compile("^IN  (?<bind>:\\w+) => \\? \\[(?<type>\\w+) (?<value>-?\\w+)\\]$");

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
	public String process(String sql, List<String> logMessages) {
		StringBuilder result = new StringBuilder(sql);
		for (Entry<String, Param> entry : binds.entrySet()) {
			String bind = entry.getKey();
			Param value = entry.getValue();
			for (int i = 0; (i = result.indexOf(bind, i)) != -1;) {
				String replacement = "/*" + bind + "*/" + value.getQuerySql();
				if (logMessages != null) {
					logMessages.add(String.format("replacing bind %s with %s.\n", bind, replacement));
				}
				result.replace(i, i + bind.length(), replacement);
				i += replacement.length();
			}
		}
		return result.toString();
	}

	@Override
	protected boolean isValidSQL(String line) {
		return !"*** UNPARSED ***".equals(StringUtils.trim(line));
	}

	public static void main(String[] args) throws IOException {
		ScoutSqlParser me = new ScoutSqlParser();
		ArrayList<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.println("Paste SQL:");
			String line;
			while ((line = br.readLine()) != null && !line.isEmpty()) {
				lines.add(line);
			}
		}

		String sql = me.parseParameterBindings(lines);

		List<String> logMessages = new ArrayList<>();
		String result = me.process(sql, logMessages);
		logMessages.forEach(System.out::println);
		System.out.println();
		System.out.println(result);
	}
}
