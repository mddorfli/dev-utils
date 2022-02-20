package com.omb.devutils.logparser.replacement;

import com.omb.devutils.logparser.Param;

import java.util.List;
import java.util.Map;

public class NamedBindReplacementStrategy implements BindReplacementStrategy<String> {

    @Override
    public String process(String sql, List<String> logMessages, Map<String, Param> binds) {
        StringBuilder result = new StringBuilder(sql);
        for (Map.Entry<String, Param> entry : binds.entrySet()) {
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
}
