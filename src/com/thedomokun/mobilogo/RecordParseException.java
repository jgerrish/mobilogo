package com.thedomokun.mobilogo;

public class RecordParseException extends Exception {
    String msg = null;
    public RecordParseException(String string) {
        msg = string;
    }

    public String toString() {
        return msg;
    }
}
