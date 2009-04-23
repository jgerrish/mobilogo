/**
 * 
 */
package com.thedomokun.gui;

/**
 * Abstract base class to wrap an image
 * @author josh
 *
 */
public abstract class Image {

    /**
     * Return a graphics object that can be used to write on the image
     * 
     * @return the graphics object
     */
    public abstract Graphics getGraphics();
}
