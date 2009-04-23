/**
 * 
 */
package com.thedomokun.gui;

/**
 * @author josh
 *
 */
public class MIDPFont extends Font {
    public javax.microedition.lcdui.Font font;

    /**
     * 
     */
    public MIDPFont(javax.microedition.lcdui.Font font) {
        this.font = font;
    }
    
    //public static createSystemFont();
    public int getHeight() {
        return font.getHeight();
    }

    public int charWidth(char c) {
        return font.charWidth(c);
    }

    public int stringWidth(String string) {
        return font.stringWidth(string);
    }
    public static Font createSystemFont(int face, int style, int size) {
        return new MIDPFont(javax.microedition.lcdui.Font.getFont(face,
                                                                  style, size));
    }

    public static Font getFont(int face, int style, int size) {
        return new MIDPFont(javax.microedition.lcdui.Font.getFont(face,
                                                                  style, size));
    }

    public int getBaseline() {
        return font.getBaselinePosition();
    }
}
