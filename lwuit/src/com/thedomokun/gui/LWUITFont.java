/**
 * 
 */
package com.thedomokun.gui;

/**
 * @author josh
 *
 */
public class LWUITFont extends Font {
    public com.sun.lwuit.Font font;

    /**
     * 
     */
    public LWUITFont(com.sun.lwuit.Font font) {
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

    public static Font createSystemFont(int face, int style,
                                        int size) {
        return new LWUITFont(com.sun.lwuit.Font.createSystemFont(face, style, size));
    }

    public int getBaseline() {
        // TODO: Get the baseline in LWUIT
        return 0;
    }

}
