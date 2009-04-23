/**
 * 
 */
package com.thedomokun.gui;

import com.thedomokun.mobilogo.BBMobiLogo;

import net.rim.device.api.ui.component.Dialog;

/**
 * @author josh
 *
 */
public class BBImage extends Image {
    public net.rim.device.api.system.Bitmap image;
    public BBGraphics graphics;

    /**
     * 
     */
    public BBImage(net.rim.device.api.system.Bitmap image) {
        this.image = image;
        net.rim.device.api.ui.Graphics g =
            new net.rim.device.api.ui.Graphics(image);
        graphics = new BBGraphics(g);
    }

    public static BBImage createImage(int width, int height) throws IllegalArgumentException {
        return new BBImage(new net.rim.device.api.system.Bitmap(width, height));
    }
    
    public int getWidth() {
        return image.getWidth();
    }
    
    public int getHeight() {
        return image.getHeight();
    }
    /* (non-Javadoc)
     * @see com.thedomokun.gui.Image#getGraphics()
     */
    public Graphics getGraphics() {
        // TODO Auto-generated method stub
        return graphics;
    }

}
