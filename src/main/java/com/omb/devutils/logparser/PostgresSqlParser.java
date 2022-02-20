package com.omb.devutils.logparser;

import com.omb.devutils.logparser.replacement.NumberedBindReplacementStrategy;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostgresSqlParser extends AbstractSqlLogParser<Integer> {
    // $1 = '10070'
    static final Pattern BIND_PATTERN = Pattern.compile("\\$(?<bind>\\d+) = '(?<value>.*)'");

    // 1970-01-01 01:00:00+01
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]X");

    public PostgresSqlParser() {
        super(new NumberedBindReplacementStrategy('$'));
    }

    @Override
    protected List<Pair<Integer, Param>> matchLogLine(String line) {
        List<Pair<Integer, Param>> result = new ArrayList<>();
        if (line != null && line.contains(" DETAIL: ") && line.contains("parameters:")) {
            String[] tokens = StringUtils.substringAfter(line, "parameters: ").split(", ");
            String lastToken = null;
            for (String token : tokens) {
                if (lastToken != null) {
                    // join the last token if so specified
                    token = lastToken + ", " + token;
                    lastToken = null;
                }
                if (!token.isEmpty() && token.charAt(token.length()-1) != '\'') {
                    // expect the last character to be a quote. If not, join this token to the next.
                    lastToken = token;
                    continue;
                }
                Matcher matcher = BIND_PATTERN.matcher(token);
                if (matcher.matches()) {
                    Integer bind = Integer.valueOf(matcher.group("bind"));
                    String value = matcher.group("value");
                    ParamType type = null;

                    if ("t".equals(value) || "f".equals(value)) {
                        type = ParamType.BOOLEAN;
                        value = BooleanUtils.toBooleanObject(value).toString().toUpperCase();
                    }
                    else if (value.matches("-?\\d+")) {
                        type = ParamType.INTEGER;
                    }
                    else {
                        try {
                            DATE_TIME_FORMATTER.parse(value);
                            type = ParamType.TIMESTAMP;
                        } catch (DateTimeParseException e) {
                            type = ParamType.VARCHAR;
                        }
                    }

                    if (type != null) {
                        result.add(Pair.of(bind, new Param(type, value)));
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        AbstractSqlLogParser.main(new PostgresSqlParser());
    }
}
