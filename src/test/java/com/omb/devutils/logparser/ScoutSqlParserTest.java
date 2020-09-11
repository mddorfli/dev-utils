package com.omb.devutils.logparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ScoutSqlParserTest {

	private static ScoutSqlParser parser;

	@BeforeAll
	static void setup() {
		parser = new ScoutSqlParser();
	}

	@Test
	void testMatchLogLine_BIGINT_11001() {
		Pair<String, Param> result = parser.matchLogLine("IN  :preferredLanguageUid => ? [BIGINT 11001]");
		assertNotNull(result);
		assertEquals(":preferredLanguageUid", result.getKey());
		assertEquals(ParamType.BIGINT, result.getValue().getType());
		assertEquals("11001", result.getValue().getValue());
	}

	@Test
	void testMatchLogLine_INTEGER_null() {
		Pair<String, Param> result = parser.matchLogLine("IN  :maxPermissionLevel => ? [INTEGER null]");
		assertNotNull(result);
		assertEquals(":maxPermissionLevel", result.getKey());
		assertEquals(ParamType.INTEGER, result.getValue().getType());
		assertEquals("null", result.getValue().getValue());
	}

	@Test
	void testMatchLogLine_BOOLEAN_false() {
		Pair<String, Param> result = parser.matchLogLine("IN  :ownedByCurrentUser => ? [BOOLEAN false]");
		assertNotNull(result);
		assertEquals(":ownedByCurrentUser", result.getKey());
		assertEquals(ParamType.BOOLEAN, result.getValue().getType());
		assertEquals("false", result.getValue().getValue());
	}

	@Test
	void testMatchLogLine_BIGINT_minus3() {
		Pair<String, Param> result = parser.matchLogLine("IN  :key => ? [BIGINT -3]");
		assertNotNull(result);
		assertEquals(":key", result.getKey());
		assertEquals(ParamType.BIGINT, result.getValue().getType());
		assertEquals("-3", result.getValue().getValue());
	}
}
