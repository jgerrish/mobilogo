/**
 * 
 */
package com.thedomokun.gui;

/**
 * @author josh
 *
 */
public class LWUITImage extends Image {
    public com.sun.lwuit.Image image;
    public LWUITGraphics graphics;

    /**
     * 
     */
    public LWUITImage(com.sun.lwuit.Image image) {
        this.image = image;
        graphics = new LWUITGraphics(image.getGraphics());
    }

    public static LWUITImage createImage(int width, int height) {
        return new LWUITImage(com.sun.lwuit.Image.createImage(width, height));
    }
    
    /* (non-Javadoc)
     * @see com.thedomokun.gui.Image#getGraphics()
     */
    public Graphics getGraphics() {
        return graphics;
    }

}
