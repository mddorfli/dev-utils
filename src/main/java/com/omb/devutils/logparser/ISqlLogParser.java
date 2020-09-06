package com.omb.devutils.logparser;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

public interface ISqlLogParser<KEY> {

	Pair<KEY, Param> matchLogLine(String line);

	String process(String sql, Map<KEY, Param> binds, List<String> logMessages);

}
