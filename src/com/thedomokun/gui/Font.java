/**
 * 
 */
package com.thedomokun.gui;

/**
 * Abstract class representing a font.
 * 
 * @author Joshua Gerrish
 *
 */
public abstract class Font {

    public static final int FACE_PROPORTIONAL = 64;
    public static final int STYLE_PLAIN = 0;
    public static final int SIZE_MEDIUM = 0;
    public static final int STYLE_BOLD = 1;
    public static final int SIZE_SMALL = 8;

    /**
     * Static method to create a system font
     * 
     * @param face the font face to use
     * @param style the font style
     * @param size the font size
     * @return the system font specified by the parameters
     */
    public static Font createSystemFont(int face, String style, String size) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Get the font specified by the parameters
     * @param face the font face
     * @param style the font style
     * @param size the font size
     * @return
     */
    public static Font getFont(int face, int style, int size) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Gets the height of this font.
     * 
     * @return the font height
     */
    public abstract int getHeight();
    /**
     * Returns the width of a character
     * 
     * @param c the character
     * @return the width of the character
     */
    public abstract int charWidth(char c);
    /**
     * Returns the width of a string
     * 
     * @param string the string to check
     * @return the width of the string
     */
    public abstract int stringWidth(String string);
    /**
     * Return the font baseline
     * @return the font baseline
     */
    public abstract int getBaseline();
}
