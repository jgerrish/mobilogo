package com.thedomokun.gui;

/**
 * Simple class used to represent a point.
 * 
 * @author Joshua Gerrish
 *
 */
public class Point {
    private int x, y;
    
    /**
     * Point constructor.  Initializes x and y to zero.
     */
    public Point() {
        x = 0;
        y = 0;
    }
    /**
     * Construct a point with the given x and y coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Get the x coordinate of this point
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate of this point
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Set the x coordinate
     * @param x the x coordinate
     */
    public void setX(double x) {
        this.x = (int)x;
    }

    /**
     * Set the x coordinate
     * @param x the x coordinate
     */
    public void setY(double y) {
        this.y = (int)y;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("x: ");
        buf.append(x);
        buf.append(", y: ");
        buf.append(y);

        return buf.toString();
    }
}
