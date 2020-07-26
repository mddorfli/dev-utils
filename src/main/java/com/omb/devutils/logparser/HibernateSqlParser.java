package com.omb.devutils.logparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

public class HibernateSqlParser {
	
	// binding parameter [1] as [VARCHAR] - [READ]
	private static Pattern REGEX = Pattern.compile(".*binding parameter \\[(?<bind>\\d+)\\] as \\[(?<type>[A-Z]+)\\] - \\[(?<value>.*)\\]");

	public static void main(String[] args) throws IOException {
		StringBuilder sql = new StringBuilder();
		Map<Integer,Param> binds = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.println("Paste SQL:");
	        String s;
	        do {
	        	s = br.readLine();
	        	if (s.toLowerCase().contains("select ") && sql.length() == 0 && binds.isEmpty()) {
	        		sql.append(s);
	        		continue;
	        	}
	        	
	        	Pair<Integer, Param> kv = matchLogLine(s);
	        	if (kv != null) {
	        		binds.put(kv.getKey(), kv.getValue());
	        		continue;
	        	}
	        	
	        } while (s != null && !s.isEmpty());
		}
		
		String result = process(sql.toString(), binds);
		
		System.out.println(result);
	}

	static String process(String sql, Map<Integer, Param> binds)  {
		StringBuilder result = new StringBuilder(sql);
		Param param = null;
		int i = 1, idx = 0;
		while(true) {
			param = binds.get(i);
			idx = result.indexOf("?", idx+1);
			
			if (param == null || idx == -1) {
				break;
			}
			
			String replacement = "/*?" + i + "*/" + param.getQuerySql();
			System.out.printf("replacing bind %d with %s.\n", i, replacement);
			result.replace(idx, idx+1, replacement);
			idx += replacement.length();
			i++;
		}
		return result.toString();
	}

	static Pair<Integer, Param> matchLogLine(String s) {
		Pair<Integer, Param> kv = null;
		Matcher matcher = REGEX.matcher(s);
    	if (matcher.matches()) {
    		String bind = matcher.group("bind");
    		String type = matcher.group("type");
    		String value = matcher.group("value");
    		Param param = new Param(ParamType.valueOf(type), value);
			kv = Pair.of(Integer.valueOf(bind), param);
    	}
		return kv;
	}
}
