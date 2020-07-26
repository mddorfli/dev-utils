package com.omb.devutils.logparser;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

class HibernateSqlParserTest {

	@Test
	void matchLogLineTest_bigintParam_144() {
		Pair<Integer, Param> bind = HibernateSqlParser.matchLogLine("17:20:14.907 [MyoshWeb-BGTask-Thread] TRACE org.hibernate.type.descriptor.sql.BasicBinder - binding parameter [1] as [BIGINT] - [144]");
		assertNotNull(bind);
		assertEquals(1, bind.getKey().intValue());
		assertEquals(ParamType.BIGINT, bind.getValue().type);
		assertEquals("144", bind.getRight().getQuerySql());
	}

}
