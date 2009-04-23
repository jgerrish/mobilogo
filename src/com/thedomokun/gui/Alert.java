package com.thedomokun.gui;

/**
 * Abstract class to handle application alerts.
 * 
 * @author Joshua Gerrish
 *
 */
public abstract class Alert {
    /**
     * Alert the user about an exceptional situation.
     * 
     * @param str the string to show the user
     */
    public abstract void alert(String str);
    public abstract void alert(String str, String str2);
}
