package com.thedomokun.common;



/**
 * Error handler base class.
 * Error handlers are used to inform the user that an error has occurred.
 * @author Joshua Gerrish
 *
 */
public abstract class ErrorHandler {
    /**
     * Alert the user about an error
     * @param str the error string
     */
    public abstract void alert(String str);
}
