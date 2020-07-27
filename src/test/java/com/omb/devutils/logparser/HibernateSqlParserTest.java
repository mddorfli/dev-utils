package com.omb.devutils.logparser;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

class HibernateSqlParserTest {

	@Test
	public void matchLogLineTest_BIGINTParam_144() {
		Pair<Integer, Param> bind = HibernateSqlParser.matchLogLine("17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [1] as [BIGINT] - [144]");
		assertNotNull(bind);
		assertEquals(1, bind.getKey().intValue());
		assertEquals(ParamType.BIGINT, bind.getValue().type);
		assertEquals("144", bind.getRight().getQuerySql());
	}

	@Test
	public void matchLogLineTest_BOOLEANParam_false() {
		Pair<Integer, Param> bind = HibernateSqlParser.matchLogLine("17:50:13.427 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [5] as [BOOLEAN] - [false]");
		assertNotNull(bind);
		assertEquals(5, bind.getKey().intValue());
		assertEquals(ParamType.BOOLEAN, bind.getValue().type);
		assertEquals("false", bind.getRight().getQuerySql());
	}

	@Test
	public void matchLogLineTest_INTEGERParam_1() {
		Pair<Integer, Param> bind = HibernateSqlParser.matchLogLine("17:50:13.427 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [6] as [INTEGER] - [1]");
		assertNotNull(bind);
		assertEquals(6, bind.getKey().intValue());
		assertEquals(ParamType.INTEGER, bind.getValue().type);
		assertEquals("1", bind.getRight().getQuerySql());
	}
	
	
	@Test
	public void matchLogLineTest_TIMESTAMPParam_0100_01011970() {
		Pair<Integer, Param> bind = HibernateSqlParser.matchLogLine("17:50:13.427 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [7] as [TIMESTAMP] - [Thu Jan 01 01:00:00 CET 1970]");
		assertNotNull(bind);
		assertEquals(7, bind.getKey().intValue());
		assertEquals(ParamType.TIMESTAMP, bind.getValue().type);
		assertEquals("TIMESTAMP '1970-01-01 01:00'", bind.getRight().getQuerySql());
	}
	
	@Test
	public void matchLogLineTest_TIMESTAMPParam_1750_26072020() {
		Pair<Integer, Param> bind = HibernateSqlParser.matchLogLine("17:50:13.428 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [8] as [TIMESTAMP] - [Sun Jul 26 17:50:13 CEST 2020]");
		assertNotNull(bind);
		assertEquals(8, bind.getKey().intValue());
		assertEquals(ParamType.TIMESTAMP, bind.getValue().type);
		assertEquals("TIMESTAMP '2020-07-26 17:50'", bind.getRight().getQuerySql());
	}
			
	@Test
	public void matchLogLineTest_BOOLEANParam_true() {
		Pair<Integer, Param> bind = HibernateSqlParser.matchLogLine("17:50:13.428 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [9] as [BOOLEAN] - [true]");
		assertNotNull(bind);
		assertEquals(9, bind.getKey().intValue());
		assertEquals(ParamType.BOOLEAN, bind.getValue().type);
		assertEquals("true", bind.getRight().getQuerySql());
	}
}
