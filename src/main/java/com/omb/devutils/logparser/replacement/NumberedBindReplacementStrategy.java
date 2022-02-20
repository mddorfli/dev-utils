package com.omb.devutils.logparser.replacement;

import com.omb.devutils.logparser.Param;

import java.util.List;
import java.util.Map;

public class NumberedBindReplacementStrategy implements BindReplacementStrategy<Integer> {

    private char bindChar;

    public NumberedBindReplacementStrategy(char bindChar) {
        this.bindChar = bindChar;
    }

    @Override
    public String process(String sql, List<String> logMessages, Map<Integer, Param> binds) {
        StringBuilder result = new StringBuilder(sql);
        Param param = null;
        int i = 1, idx = 0;
        while (true) {
            param = binds.get(i);
            idx = result.indexOf(String.valueOf(bindChar), idx + 1);

            if (param == null || idx == -1) {
                break;
            }

            String replacement = "/*" + bindChar + i + "*/" + param.getQuerySql();
            if (logMessages != null) {
                logMessages.add(String.format("replacing bind %d with %s.\n", i, replacement));
            }
            result.replace(idx, idx + 1, replacement);
            idx += replacement.length();
            i++;
        }
        return result.toString();
    }

}
