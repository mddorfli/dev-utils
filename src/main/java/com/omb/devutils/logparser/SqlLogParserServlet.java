package com.omb.devutils.logparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class SqlLogParserServlet extends HttpServlet {

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
		String logType = req.getParameter("logType");

		AbstractSqlLogParser<?> parser = null;
		if ("scout".equals(logType)) {
			parser = new ScoutSqlParser();
		} else if ("hibernate".equals(logType)) {
			parser = new HibernateSqlParser();
		}

		if (parser == null) {
			out.println("No log type defined.");
		} else {
			out.print("<div>Parsing " + logType + " log:</div>");
			Cookie cookie = new Cookie("lastLogType", logType);
			resp.addCookie(cookie);
			String result = null;
			List<String> logMessages = new ArrayList<>();
			if (StringUtils.isNotBlank(logs)) {
				String sql = parser.parseParameterBindings(Stream.of(logs.split("\n"))
						.map(String::trim)
						.collect(Collectors.toList()));
				result = parser.process(sql, logMessages);
			}

			if (StringUtils.isBlank(result)) {
				out.println("No logs defined.<br/>");
			} else {
				out.println("<div style='font-family: monospace; white-space: pre;'>");
				for (String logMessage : logMessages) {
					out.println(StringEscapeUtils.escapeHtml4(logMessage));
				}
				out.println("</div>");
				out.print("<hr/>");
				out.println(
						"<div style='font-family: monospace; white-space: pre-wrap; width: 120em; word-wrap: break-word;'>"
								+ StringEscapeUtils.escapeHtml4(result) + "</div>");
			}
		}

		out.println("</body></html>");

		out.flush();
		out.close();
	}
}
