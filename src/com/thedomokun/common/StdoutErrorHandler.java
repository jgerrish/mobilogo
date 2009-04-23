package com.thedomokun.common;

/**
 * Alert the user via stdout messages
 * 
 * @author Joshua Gerrish
 *
 */
public class StdoutErrorHandler extends ErrorHandler {

    /* (non-Javadoc)
     * @see com.thedomokun.common.ErrorHandler#alert(java.lang.String)
     */
    public void alert(String str) {
        System.out.println(str);
    }

}
