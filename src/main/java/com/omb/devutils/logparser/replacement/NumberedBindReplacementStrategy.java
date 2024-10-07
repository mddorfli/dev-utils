package com.omb.devutils.logparser.replacement;

import com.omb.devutils.logparser.Param;
import org.apache.commons.lang3.math.NumberUtils;

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
        int bindNr = 1, idx = 0;
        while (true) {
            Param param = binds.get(bindNr);
            idx = result.indexOf(String.valueOf(bindChar), idx + 1);

            if (param == null || idx == -1) {
                break;
            }

            short bindLen = 1;
            for (int bindIdx = idx + 1; Character.isDigit(result.charAt(bindIdx)); bindIdx++) {
                bindLen++;
            }

            String replacement = "/*" + bindChar + bindNr + "*/" + param.getQuerySql();
            if (logMessages != null) {
                logMessages.add(String.format("replacing bind %d with %s.\n", bindNr, replacement));
            }
            result.replace(idx, idx + bindLen, replacement);
            idx += replacement.length() - bindLen;
            bindNr++;
        }
        return result.toString();
    }

}
