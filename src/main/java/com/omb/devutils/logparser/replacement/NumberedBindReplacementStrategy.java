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
        int bindNr = 1, idx = 0;
        while (true) {
            param = binds.get(bindNr);
            idx = result.indexOf(String.valueOf(bindChar), idx + 1);

            if (param == null || idx == -1) {
                break;
            }

            String replacement = "/*" + bindChar + bindNr + "*/" + param.getQuerySql();
            if (logMessages != null) {
                logMessages.add(String.format("replacing bind %d with %s.\n", bindNr, replacement));
            }
            int bindCharCount = (int) Math.log10(bindNr) + 2;
            result.replace(idx, idx + bindCharCount, replacement);
            idx += replacement.length();
            bindNr++;
        }
        return result.toString();
    }

}
