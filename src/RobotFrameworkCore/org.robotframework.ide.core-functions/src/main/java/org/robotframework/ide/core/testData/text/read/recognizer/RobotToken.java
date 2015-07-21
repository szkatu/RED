package org.robotframework.ide.core.testData.text.read.recognizer;

public class RobotToken {

    public static final int NOT_SET = -1;
    private int lineNumber = NOT_SET;
    private int startColumn = NOT_SET;
    private StringBuilder text = new StringBuilder();
    private RobotTokenType type = RobotTokenType.UNKNOWN;


    public int getLineNumber() {
        return lineNumber;
    }


    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }


    public int getStartColumn() {
        return startColumn;
    }


    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }


    public StringBuilder getText() {
        return text;
    }


    public void setText(StringBuilder text) {
        this.text = text;
    }


    public int getEndColumn() {
        return startColumn + text.length();
    }


    public RobotTokenType getType() {
        return type;
    }


    public void setType(RobotTokenType type) {
        this.type = type;
    }

    public static enum RobotTokenType {
        /**
         * 
         */
        UNKNOWN,
        /**
         * 
         */
        EMPTY_CELL,
        /**
         * 
         */
        SETTINGS_TABLE_HEADER,
        /**
         * 
         */
        VARIABLES_TABLE_HEADER,
        /**
         * 
         */
        TEST_CASES_TABLE_HEADER,
        /**
        * 
        */
        KEYWORDS_TABLE_HEADER;
    }


    @Override
    public String toString() {
        return String.format(
                "RobotToken [lineNumber=%s, startColumn=%s, text=%s, type=%s]",
                lineNumber, startColumn, text, type);
    }

}
