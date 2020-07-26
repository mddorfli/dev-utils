package com.omb.devutils.logparser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class HibernateSqlLogParserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      doPost(req, resp);
    }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		  ServletOutputStream out = resp.getOutputStream();
		  out.println("<html><body>");
	        
	        String logs = req.getParameter("logs");
	        String result = null;
	        if (StringUtils.isNotBlank(logs)) {
	        	result = process(logs);
	        }
	        
	        if (StringUtils.isBlank(result)) {
	        	out.println("No logs defined.<br/>");
	        } else {
	        	out.println("<div style='font-weight: bold;'>SQL query:</div>");
	        	out.println("<div style='font-family: monospace; white-space: pre;'>"+StringEscapeUtils.escapeHtml4(result)+"</div>");
	        }
	      
	        out.println("</body></html>");
	        
	        out.flush();
	        out.close();
	}
	
	
	private String process(String logs) {
		Map<Integer,Param> binds = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		for (String line : logs.split("\n")) {
			line = StringUtils.trimToEmpty(line);
			if (StringUtils.containsIgnoreCase(line, "select ") && sql.length() == 0 && binds.isEmpty()) {
				sql.append(line);
				continue;
			}
			
			Pair<Integer, Param> kv = HibernateSqlParser.matchLogLine(line);
			if (kv != null) {
				binds.put(kv.getKey(), kv.getValue());
			}
		}
		
		return HibernateSqlParser.process(sql.toString(), binds);
	}
    
}
