/**
 * 
 */
package com.thedomokun.gui;

/**
 * Abstract class for a graphics context.  Subclasses of this class should
 * interface with the devices graphics API.
 * 
 * @author Joshua Gerrish
 *
 */
public abstract class Graphics {
    
    /**
     * Draw a line from a point to another point
     * 
     * @param x1 x coordinate of first point
     * @param y1 y coordinate of first point
     * @param x2 x coordinate of second point
     * @param y2 y coordinate of second point
     */
    public abstract void drawLine(int x1, int y1, int x2, int y2);
    /**
     * Set the current color
     * @param color the color to set, specified as a hexadecimal number:
     * 0xrrbbgg
     */
    public abstract void setColor(int color);
    /**
     * Set the current color
     * @param r the red level
     * @param g the green level
     * @param b the blue level
     */
    public abstract void setColor(int r, int g, int b);
    /**
     * Draw an image to this graphics context
     * 
     * @param img the image to draw
     * @param x upper left x coordinate
     * @param y upper left y coordinate
     */
    public abstract void drawImage(Image img, int x, int y);
    /**
     * Gets the clipping rectangle x coordinate
     * 
     * @return the x coordinate of the clipping rectangle
     */
    public abstract int getClipX();
    /**
     * Gets the clipping rectangle y coordinate
     * 
     * @return the y coordinate of the clipping rectangle
     */
    public abstract int getClipY();
    /**
     * Gets the clipping rectangle width
     * @return the width of the clipping rectangle
     */
    public abstract int getClipWidth();
    /**
     * Gets the clipping rectangle height
     * @return the height of the clipping rectangle
     */
    public abstract int getClipHeight();
    /**
     * Get the current color
     * 
     * @return the current color
     */
    public abstract int getColor();
    /**
     * Draw a filled rectangle
     * 
     * @param x x coordinate of the upper left point
     * @param y y coordinate of the upper left point
     * @param width rectangle width
     * @param height rectangle height
     */
    public abstract void fillRect(int x, int y, int width, int height);
    /**
     * Set the current font to use for drawing
     * 
     * @param font the font to use for drawing text
     */
    public abstract void setFont(Font font);
    public abstract void fillArc(int i, int j, int k, int l, int m, int n);

    /**
     * Draw a character in a certain position
     * 
     * @param c the character to draw
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public abstract void drawChar(char c, int x, int y);
    /**
     * Draw a character in a certain position
     * 
     * @param c the character to draw
     * @param x the x coordinate
     * @param y the y coordinate
     * @param f where to place the character relative to the point specified
     */
    public abstract void drawChar(char c, int x, int y, int f);

    /**
     * Draw a string
     * 
     * @param label the string to draw
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public abstract void drawString(String label, int x, int y);
    /**
     * Draw a string
     * 
     * @param label the string to draw
     * @param x the x coordinate
     * @param y the y coordinate
     * @param f where to place the string relative to the point specified
     */
    public abstract void drawString(String label, int x, int y, int f);

}
