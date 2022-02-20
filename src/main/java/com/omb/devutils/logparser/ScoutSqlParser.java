package com.omb.devutils.logparser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.omb.devutils.logparser.replacement.NamedBindReplacementStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ScoutSqlParser extends AbstractSqlLogParser<String> {
	// IN :preferredLanguageUid => ? [BIGINT 11001]
	private static Pattern BIND_PATTERN = Pattern.compile("^IN  (?<bind>:\\w+) => \\? \\[(?<type>\\w+) (?<value>-?\\w+)\\]$");

	public ScoutSqlParser() {
		super(new NamedBindReplacementStrategy());
	}

	@Override
	public List<Pair<String, Param>> matchLogLine(String line) {
		Pair<String, Param> result = null;
		Matcher matcher = BIND_PATTERN.matcher(line);
		if (matcher.matches()) {
			String bind = matcher.group("bind");
			String type = matcher.group("type");
			String value = matcher.group("value");
			Param param = new Param(ParamType.valueOf(type), value);
			result = Pair.of(bind, param);
		}
		return result == null ? Collections.emptyList() : Collections.singletonList(result);
	}

	@Override
	protected boolean isValidSQL(String line) {
		return !"*** UNPARSED ***".equals(StringUtils.trim(line));
	}

	public static void main(String[] args) throws IOException {
		AbstractSqlLogParser.main(new ScoutSqlParser());
	}
}
