/**
 * 
 */
package com.thedomokun.common;

/**
 * @author josh
 *
 */
public class LogoParseException extends Exception {
    String error;

    public LogoParseException(String msg) {
        error = msg;
    }
    
    public String toString() {
        return this.error;
    }
}
