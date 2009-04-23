package com.thedomokun.common;

import com.thedomokun.gui.Alert;

/**
 * Alert the user about an error
 * 
 * @author Joshua Gerrish
 *
 */
public class AlertErrorHandler extends ErrorHandler {
    Alert alert;

    /**
     * Create an AlertErrorHandler with an Alert object
     * @param alert alert object that displays the alert to the user
     */
    public AlertErrorHandler(Alert alert) {
        this.alert = alert;
    }

    /* (non-Javadoc)
     * @see com.thedomokun.common.ErrorHandler#alert(java.lang.String)
     */
    public void alert(String str) {
        alert.alert(str);
    }

}
