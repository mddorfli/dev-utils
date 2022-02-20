package com.omb.devutils.logparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class HibernateSqlParserTest {

	private static HibernateSqlParser parser;

	@BeforeAll
	static void setup() {
		parser = new HibernateSqlParser();
	}

	@Test
	void matchLogLineTest_BIGINTParam_144() {
		List<Pair<Integer, Param>> bind = parser.matchLogLine(
				"17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [1] as [BIGINT] - [144]");
		assertEquals(1, bind.size());
		assertEquals(1, bind.get(0).getKey().intValue());
		assertEquals(ParamType.BIGINT, bind.get(0).getValue().getType());
		assertEquals("144", bind.get(0).getRight().getQuerySql());
	}

	@Test
	void matchLogLineTest_BOOLEANParam_false() {
		List<Pair<Integer, Param>> bind = parser.matchLogLine(
				"17:50:13.427 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [5] as [BOOLEAN] - [false]");
		assertEquals(1, bind.size());
		assertEquals(5, bind.get(0).getKey().intValue());
		assertEquals(ParamType.BOOLEAN, bind.get(0).getValue().getType());
		assertEquals("false", bind.get(0).getRight().getQuerySql());
	}

	@Test
	void matchLogLineTest_INTEGERParam_1() {
		List<Pair<Integer, Param>> bind = parser.matchLogLine(
				"17:50:13.427 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [6] as [INTEGER] - [1]");
		assertEquals(1, bind.size());
		assertEquals(6, bind.get(0).getKey().intValue());
		assertEquals(ParamType.INTEGER, bind.get(0).getValue().getType());
		assertEquals("1", bind.get(0).getRight().getQuerySql());
	}

	@Test
	void matchLogLineTest_TIMESTAMPParam_0100_01011970() {
		List<Pair<Integer, Param>> bind = parser.matchLogLine(
				"17:50:13.427 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [7] as [TIMESTAMP] - [Thu Jan 01 01:00:00 CET 1970]");
		assertEquals(1, bind.size());
		assertEquals(7, bind.get(0).getKey().intValue());
		assertEquals(ParamType.TIMESTAMP, bind.get(0).getValue().getType());
		assertEquals("TIMESTAMP '1970-01-01 01:00'", bind.get(0).getRight().getQuerySql());
	}

	@Test
	void matchLogLineTest_TIMESTAMPParam_1750_26072020() {
		List<Pair<Integer, Param>> bind = parser.matchLogLine(
				"17:50:13.428 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [8] as [TIMESTAMP] - [Sun Jul 26 17:50:13 CEST 2020]");
		assertEquals(1, bind.size());
		assertEquals(8, bind.get(0).getKey().intValue());
		assertEquals(ParamType.TIMESTAMP, bind.get(0).getValue().getType());
		assertEquals("TIMESTAMP '2020-07-26 17:50'", bind.get(0).getRight().getQuerySql());
	}

	@Test
	void matchLogLineTest_BOOLEANParam_true() {
		List<Pair<Integer, Param>> bind = parser.matchLogLine(
				"17:50:13.428 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [9] as [BOOLEAN] - [true]");
		assertEquals(1, bind.size());
		assertEquals(9, bind.get(0).getKey().intValue());
		assertEquals(ParamType.BOOLEAN, bind.get(0).getValue().getType());
		assertEquals("true", bind.get(0).getRight().getQuerySql());
	}

	@Test
	void matchLogLineTest_TIMESTAMPParam_0000_06092020() {
		List<Pair<Integer, Param>> bind = parser.matchLogLine(
				"16:27:34.055 [http-nio-8080-exec-6] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [9] as [TIMESTAMP] - [2020-09-06 00:00:00.0]");
		assertEquals(1, bind.size());
		assertEquals(9, bind.get(0).getKey().intValue());
		assertEquals(ParamType.TIMESTAMP, bind.get(0).getValue().getType());
		assertEquals("TIMESTAMP '2020-09-06 00:00'", bind.get(0).getRight().getQuerySql());
	}
}
