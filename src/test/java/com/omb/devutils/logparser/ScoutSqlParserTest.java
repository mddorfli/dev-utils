package com.omb.devutils.logparser;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoutSqlParserTest {

	private static ScoutSqlParser parser;

	@BeforeAll
	static void setup() {
		parser = new ScoutSqlParser();
	}

	@Test
	void testMatchLogLine_BIGINT_11001() {
		List<Pair<String, Param>> result = parser.matchLogLine("IN  :preferredLanguageUid => ? [BIGINT 11001]");
		assertEquals(1, result.size());
		assertEquals(":preferredLanguageUid", result.get(0).getKey());
		assertEquals(ParamType.BIGINT, result.get(0).getValue().getType());
		assertEquals("11001", result.get(0).getValue().getValue());
	}

	@Test
	void testMatchLogLine_INTEGER_null() {
		List<Pair<String, Param>> result = parser.matchLogLine("IN  :maxPermissionLevel => ? [INTEGER null]");
		assertEquals(1, result.size());
		assertEquals(":maxPermissionLevel", result.get(0).getKey());
		assertEquals(ParamType.INTEGER, result.get(0).getValue().getType());
		assertEquals("null", result.get(0).getValue().getValue());
	}

	@Test
	void testMatchLogLine_BOOLEAN_false() {
		List<Pair<String, Param>> result = parser.matchLogLine("IN  :ownedByCurrentUser => ? [BOOLEAN false]");
		assertEquals(1, result.size());
		assertEquals(":ownedByCurrentUser", result.get(0).getKey());
		assertEquals(ParamType.BOOLEAN, result.get(0).getValue().getType());
		assertEquals("false", result.get(0).getValue().getValue());
	}

	@Test
	void testMatchLogLine_BIGINT_minus3() {
		List<Pair<String, Param>> result = parser.matchLogLine("IN  :key => ? [BIGINT -3]");
		assertEquals(1, result.size());
		assertEquals(":key", result.get(0).getKey());
		assertEquals(ParamType.BIGINT, result.get(0).getValue().getType());
		assertEquals("-3", result.get(0).getValue().getValue());
	}
}
