package com.omb.devutils.logparser.replacement;

import com.omb.devutils.logparser.Param;

import java.util.List;
import java.util.Map;

public interface BindReplacementStrategy<KEY> {

    String process(String sql, List<String> logMessages, Map<KEY, Param> binds);

}
