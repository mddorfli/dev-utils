package com.omb.devutils.logparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;

class ParamTest {

	@Test
	void testParseLogTimestamp_shortTimeFormat_expectParsedTimestamp() {
		Date result = Param.parseLogTimestamp("2020-09-06 00:00:00.0");
		assertNotNull(result);
		assertEquals(Date.from(LocalDateTime.of(2020, 9, 6, 0, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant()),
				result);
	}

	@Test
	void testParseLogTimestamp_longTimeFormat_expectParsedTimestamp() {
		Date result = Param.parseLogTimestamp("Sun Jul 26 17:50:13 CEST 2020");
		assertNotNull(result);
		assertEquals(Date.from(LocalDateTime.of(2020, 7, 26, 17, 50, 13, 0).atZone(ZoneId.systemDefault()).toInstant()),
				result);
	}

}
