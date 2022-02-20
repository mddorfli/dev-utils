package com.omb.devutils.logparser;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.List;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

class PostgresSqlParserTest {

    private static PostgresSqlParser parser;

    @BeforeAll
    static void setup() {
        parser = new PostgresSqlParser();
    }

    @Test
    void testPattern_int() {
        Matcher matcher = PostgresSqlParser.BIND_PATTERN.matcher("$1 = '10070'");
        assertTrue(matcher.matches());
        assertEquals("1", matcher.group("bind"));
        assertEquals("10070", matcher.group("value"));
    }

    @Test
    void testPattern_varchar() {
        Matcher matcher = PostgresSqlParser.BIND_PATTERN.matcher("$3 = 'READ'");
        assertTrue(matcher.matches());
        assertEquals("3", matcher.group("bind"));
        assertEquals("READ", matcher.group("value"));
    }

    @Test
    void testPattern_negativeInt() {
        Matcher matcher = PostgresSqlParser.BIND_PATTERN.matcher("$6 = '-1'");
        assertTrue(matcher.matches());
        assertEquals("6", matcher.group("bind"));
        assertEquals("-1", matcher.group("value"));
    }

    @Test
    void testPattern_emptyVarchar() {
        Matcher matcher = PostgresSqlParser.BIND_PATTERN.matcher("$12 = ''");
        assertTrue(matcher.matches());
        assertEquals("12", matcher.group("bind"));
        assertEquals("", matcher.group("value"));
    }

    @Test
    void testPattern_specialChars() {
        Matcher matcher = PostgresSqlParser.BIND_PATTERN.matcher("$15 = ',%'");
        assertTrue(matcher.matches());
        assertEquals("15", matcher.group("bind"));
        assertEquals(",%", matcher.group("value"));
    }

    @Test
    void testPattern_Date() {
        Matcher matcher = PostgresSqlParser.BIND_PATTERN.matcher("$49 = '2022-02-16 14:39:20.806+01'");
        assertTrue(matcher.matches());
        assertEquals("49", matcher.group("bind"));
        assertEquals("2022-02-16 14:39:20.806+01", matcher.group("value"));
    }

    @Test
    void testDATE_TIME_FORMATTER() {
        TemporalAccessor result = PostgresSqlParser.DATE_TIME_FORMATTER.parse("1970-01-01 01:00:00+01");
        assertEquals(1970, result.get(ChronoField.YEAR));
        assertEquals(01, result.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(01, result.get(ChronoField.DAY_OF_MONTH));
        assertEquals(01, result.get(ChronoField.HOUR_OF_DAY));
        assertEquals(00, result.get(ChronoField.MINUTE_OF_HOUR));
        assertEquals(00, result.get(ChronoField.SECOND_OF_MINUTE));
        assertEquals(3600, result.get(ChronoField.OFFSET_SECONDS));
    }

    @Test
    void testMatchLogLine_multiple_list() {
        List<Pair<Integer, Param>> result = parser.matchLogLine("2022-02-16 14:39:42.545 CET [8739] DETAIL:  parameters: $1 = '10070', $2 = '10070', $3 = 'READ', $4 = '10070', $5 = '4582', $6 = '-1', $7 = '6244', $8 = '10070', $9 = 'f', $10 = '10070', $11 = '', $12 = '', $13 = '', $14 = '', $15 = ',%', $16 = '', $17 = '', $18 = '%, ', $19 = '', $20 = '', $21 = '%, ', $22 = ',%', $23 = '10070', $24 = '', $25 = '', $26 = '', $27 = '', $28 = ',%', $29 = '', $30 = '', $31 = '%, ', $32 = '', $33 = '', $34 = '%, ', $35 = ',%', $36 = 'RECORD_READ_ROLE', $37 = '10070', $38 = '4582', $39 = '-1', $40 = '6244', $41 = 't', $42 = '10070', $43 = '10070', $44 = '0', $45 = '9', $46 = 'f', $47 = '1', $48 = '1970-01-01 01:00:00+01', $49 = '2022-02-16 14:39:20.806+01', $50 = 'f', $51 = 't', $52 = '10070', $53 = '10070', $54 = '2147483647'");
        for (int i = 0; i < result.size(); i++) {
            Pair<Integer, Param> bind = result.get(i);
            assertEquals(i+1, bind.getKey().intValue(), "bind "+(i+1)+" has been missed");
        }
    }
}