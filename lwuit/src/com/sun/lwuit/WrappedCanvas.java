/**
 * 
 */
package com.sun.lwuit;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 * @author josh
 *
 */
public abstract class WrappedCanvas extends Canvas {
    protected int width;
    protected int height;

    /**
     * 
     */
    public WrappedCanvas() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see javax.microedition.lcdui.Canvas#paint(javax.microedition.lcdui.Graphics)
     */
    public abstract void paint(Graphics arg0);
    

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public abstract void keyPressed(int key);
    public abstract void keyReleased(int key);
}
