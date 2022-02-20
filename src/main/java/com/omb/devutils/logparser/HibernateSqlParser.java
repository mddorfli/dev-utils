package com.omb.devutils.logparser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.omb.devutils.logparser.replacement.NumberedBindReplacementStrategy;
import org.apache.commons.lang3.tuple.Pair;

public class HibernateSqlParser extends AbstractSqlLogParser<Integer> {

	// binding parameter [1] as [VARCHAR] - [READ]
	private static Pattern BIND_PATTERN = Pattern
			.compile(".*binding parameter \\[(?<bind>\\d+)\\] as \\[(?<type>[A-Z]+)\\] - \\[(?<value>.*)\\]");

	public HibernateSqlParser() {
		super(new NumberedBindReplacementStrategy('?'));
	}

	public static void main(String[] args) throws IOException {
		AbstractSqlLogParser.main(new HibernateSqlParser());
	}

	@Override
	public List<Pair<Integer, Param>> matchLogLine(String s) {
		Pair<Integer, Param> kv = null;
		Matcher matcher = BIND_PATTERN.matcher(s);
		if (matcher.matches()) {
			String bind = matcher.group("bind");
			String type = matcher.group("type");
			String value = matcher.group("value");
			Param param = new Param(ParamType.valueOf(type), value);
			kv = Pair.of(Integer.valueOf(bind), param);
		}
		return kv == null ? Collections.emptyList() : Collections.singletonList(kv);
	}
}
