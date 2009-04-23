/**
 * 
 */
package com.thedomokun.gui;

import net.rim.device.api.ui.FontFamily;

/**
 * @author josh
 *
 */
public class BBFont extends Font {
    public net.rim.device.api.ui.Font font;

    /**
     * 
     */
    public BBFont(net.rim.device.api.ui.Font font) {
        this.font = font;
    }
    
    //public static createSystemFont();
    public int getHeight() {
        return font.getHeight();
    }

    public int charWidth(char c) {
        return font.getAdvance(c);
    }

    public int stringWidth(String string) {
        return font.getAdvance(string);
    }

    // TODO: Fix createSystemFont for BB
    public static Font createSystemFont(int face, int style, int size) {
        try {
            FontFamily fontFam = FontFamily.forName("SYSTEM");
            net.rim.device.api.ui.Font f =
                fontFam.getFont(style, size);
            return new BBFont(f);
        } catch (ClassNotFoundException e) {
            return new BBFont(net.rim.device.api.ui.Font.getDefault());
        }        
    }

    // TODO: Fix getFont for BB
    public static Font getFont(int face, int style, int size) {
        try {
            FontFamily fontFam = FontFamily.forName("SYSTEM");
            net.rim.device.api.ui.Font f =
                fontFam.getFont(style, size);
            return new BBFont(f);
        } catch (ClassNotFoundException e) {
            return new BBFont(net.rim.device.api.ui.Font.getDefault());
        }        
    }

    public int getBaseline() {
        return this.font.getBaseline();
    }
}
