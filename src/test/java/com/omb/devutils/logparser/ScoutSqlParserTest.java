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
		System.out.println("before");
		parser = new ScoutSqlParser();
	}

	@Test
	void testMatchLogLine_Date_normal() {
		System.out.println("Testing: parser is " + parser);
		Pair<String, Param> result = parser.matchLogLine("IN :preferredLanguageUid => ? [BIGINT 11001]");
		assertNotNull(result);
		assertEquals(":preferredLanguageUid", result.getKey());
		assertEquals(ParamType.BIGINT, result.getValue().getType());
		assertEquals("11001", result.getValue().getValue());
	}
}
