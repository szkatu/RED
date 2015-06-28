package org.robotframework.ide.core.testData.text.lexer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.robotframework.ide.core.testData.text.lexer.RobotTokenType.HELPER;


/**
 * 
 * @author wypych
 * @since JDK 1.7 update 74
 * @version Robot Framework 2.9 alpha 2
 * 
 * @see RobotTokenType
 */
public class RobotTokenTypeTest {

    @Test
    public void test_getTokenType_zeroCharacters_shouldReturn_UNKNOWN() {
        assertThat(RobotTokenType.UNKNOWN.getTokenType("")).isEqualTo(
                RobotTokenType.UNKNOWN);
        assertThat(RobotTokenType.UNKNOWN.getTokenType(new StringBuilder("")))
                .isEqualTo(RobotTokenType.UNKNOWN);
    }


    @Test
    public void test_getTokenType_nullAsParam_shouldReturn_UNKNOWN() {
        String text = null;
        assertThat(RobotTokenType.UNKNOWN.getTokenType(text)).isEqualTo(
                RobotTokenType.UNKNOWN);
        StringBuilder text2 = null;
        assertThat(RobotTokenType.UNKNOWN.getTokenType(text2)).isEqualTo(
                RobotTokenType.UNKNOWN);
    }


    @Test
    public void test_getTokenType_moreThanOneChar_shouldReturn_UNKNOWN() {
        assertThat(RobotTokenType.UNKNOWN.getTokenType("...")).isEqualTo(
                RobotTokenType.UNKNOWN);
        assertThat(
                RobotTokenType.UNKNOWN.getTokenType(new StringBuilder("...")))
                .isEqualTo(RobotTokenType.UNKNOWN);
    }


    @Test
    public void test_typeSINGLE_QUOTE_MARK() {
        char tokenChar = '"';
        RobotTokenType type = RobotTokenType.SINGLE_QUOTE_MARK;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_DOT() {
        char tokenChar = '.';
        RobotTokenType type = RobotTokenType.SINGLE_DOT;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_COLON() {
        char tokenChar = ':';
        RobotTokenType type = RobotTokenType.SINGLE_COLON;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_POSSITION_INDEX_END_SQUARE_BRACKET() {
        char tokenChar = ']';
        RobotTokenType type = RobotTokenType.SINGLE_POSSITION_INDEX_END_SQUARE_BRACKET;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_POSSITION_INDEX_BEGIN_SQUARE_BRACKET() {
        char tokenChar = '[';
        RobotTokenType type = RobotTokenType.SINGLE_POSSITION_INDEX_BEGIN_SQUARE_BRACKET;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_VARIABLE_END_CURLY_BRACKET() {
        char tokenChar = '}';
        RobotTokenType type = RobotTokenType.SINGLE_VARIABLE_END_CURLY_BRACKET;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_VARIABLE_BEGIN_CURLY_BRACKET() {
        char tokenChar = '{';
        RobotTokenType type = RobotTokenType.SINGLE_VARIABLE_BEGIN_CURLY_BRACKET;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_EQUAL() {
        char tokenChar = '=';
        RobotTokenType type = RobotTokenType.SINGLE_EQUAL;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_DICTIONARY_BEGIN_AMPERSAND() {
        char tokenChar = '&';
        RobotTokenType type = RobotTokenType.SINGLE_DICTIONARY_BEGIN_AMPERSAND;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_ENVIRONMENT_BEGIN_PROCENT() {
        char tokenChar = '%';
        RobotTokenType type = RobotTokenType.SINGLE_ENVIRONMENT_BEGIN_PROCENT;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_LIST_BEGIN_AT() {
        char tokenChar = '@';
        RobotTokenType type = RobotTokenType.SINGLE_LIST_BEGIN_AT;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_SCALAR_BEGIN_DOLLAR() {
        char tokenChar = '$';
        RobotTokenType type = RobotTokenType.SINGLE_SCALAR_BEGIN_DOLLAR;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_ESCAPE_BACKSLASH() {
        char tokenChar = '\\';
        RobotTokenType type = RobotTokenType.SINGLE_ESCAPE_BACKSLASH;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_COMMENT_HASH() {
        char tokenChar = '#';
        RobotTokenType type = RobotTokenType.SINGLE_COMMENT_HASH;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_ASTERISK() {
        char tokenChar = '*';
        RobotTokenType type = RobotTokenType.SINGLE_ASTERISK;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_PIPE() {
        char tokenChar = '|';
        RobotTokenType type = RobotTokenType.SINGLE_PIPE;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_SPACE() {
        char tokenChar = ' ';
        RobotTokenType type = RobotTokenType.SINGLE_SPACE;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeSINGLE_TABULATOR() {
        char tokenChar = '\t';
        RobotTokenType type = RobotTokenType.SINGLE_TABULATOR;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeLINE_FEED() {
        char tokenChar = '\n';
        RobotTokenType type = RobotTokenType.LINE_FEED;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeCARRIAGE_RETURN() {
        char tokenChar = '\r';
        RobotTokenType type = RobotTokenType.CARRIAGE_RETURN;

        assertThat(type.getThisTokenChar()).isEqualTo(tokenChar);
        assertThat(type.toWrite()).isEqualTo("" + tokenChar);
    }


    @Test
    public void test_typeEND_OF_LINE() {
        RobotTokenType type = RobotTokenType.END_OF_LINE;

        assertThat(type.getThisTokenChar()).isEqualTo(HELPER.UNWRITABLE_CHARS);
        assertThat(type.toWrite()).isEmpty();
    }


    @Test
    public void test_typeUNKNOWN() {
        RobotTokenType type = RobotTokenType.UNKNOWN;

        assertThat(type.getThisTokenChar()).isEqualTo(HELPER.UNWRITABLE_CHARS);
        assertThat(type.toWrite()).isEmpty();
    }


    @Test
    public void test_getToken_checkIfMapOfRobotWordTypesIsCoherent() {
        // prepare
        RobotTokenType[] tokenTypes = RobotTokenType.values();

        // execute & verify
        assertThat(tokenTypes).isNotNull();
        assertThat(tokenTypes).hasSize(22);

        for (RobotTokenType type : tokenTypes) {
            char thisTokenChar = type.getThisTokenChar();
            if (thisTokenChar == HELPER.UNWRITABLE_CHARS) {
                assertThat(RobotTokenType.getToken(thisTokenChar)).isEqualTo(
                        RobotTokenType.UNKNOWN);
                assertThat(((RobotType) type).isWriteable()).isFalse();
                assertThat(type.getTokenType("" + thisTokenChar)).isEqualTo(
                        RobotTokenType.UNKNOWN);
                assertThat(
                        type.getTokenType(new StringBuilder("" + thisTokenChar)))
                        .isEqualTo(RobotTokenType.UNKNOWN);
            } else {
                assertThat(RobotTokenType.getToken(thisTokenChar)).isEqualTo(
                        type);
                assertThat(((RobotType) type).isWriteable()).isTrue();
                assertThat(type.getTokenType("" + thisTokenChar)).isEqualTo(
                        type);
                assertThat(
                        type.getTokenType(new StringBuilder("" + thisTokenChar)))
                        .isEqualTo(type);
            }
        }
    }
}
