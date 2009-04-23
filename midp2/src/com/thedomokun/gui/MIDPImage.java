/**
 * 
 */
package com.thedomokun.gui;

/**
 * @author josh
 *
 */
public class MIDPImage extends Image {
    public javax.microedition.lcdui.Image image;
    public MIDPGraphics graphics;

    /**
     * 
     */
    public MIDPImage(javax.microedition.lcdui.Image image) {
        this.image = image;
        graphics = new MIDPGraphics(image.getGraphics());
    }

    public static MIDPImage createImage(int width, int height) {
        return new MIDPImage(javax.microedition.lcdui.Image.createImage(width, height));
    }
    
    /* (non-Javadoc)
     * @see com.thedomokun.gui.Image#getGraphics()
     */
    public Graphics getGraphics() {
        // TODO Auto-generated method stub
        return graphics;
    }

}
