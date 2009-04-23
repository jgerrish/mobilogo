package com.thedomokun.gui;

import net.rim.device.api.system.Bitmap;

/**
 * @author josh
 *
 */
public class BBGraphics extends Graphics {
    private net.rim.device.api.ui.Graphics g;
    private static final int TOP_LEFT =
        net.rim.device.api.ui.Graphics.TOP | net.rim.device.api.ui.Graphics.LEFT;
    /**
     * 
     */
    public BBGraphics(net.rim.device.api.ui.Graphics  g) {
        this.g = g;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        // TODO Auto-generated method stub
        g.drawLine(x1, y1, x2, y2);
    }
    
    public void setColor(int color) {
        g.setColor(color);
    }

    public void setColor(int r, int g, int b) {
        int rgb = b; //0XRRGGBB
        rgb += g << 8;
        rgb += r << 16;

        this.g.setColor(rgb);
    }
    
    public void drawImage(Image img, int x, int y) {
        g.drawBitmap(x, y, ((BBImage)img).getWidth(),
                     ((BBImage)img).getHeight(),
                     ((BBImage)img).image, 0, 0);
    }

    public void fillRect(int x, int y, int width, int height) {
        g.fillRect(x, y, width, height);        
    }

    public int getClipHeight() {
        return g.getClippingRect().height;
    }

    public int getClipWidth() {
        return g.getClippingRect().width;
    }

    public int getClipX() {
        return g.getClippingRect().x;
    }

    public int getClipY() {
        return g.getClippingRect().y;
    }

    public int getColor() {
        return g.getColor();
    }
    
    public void setFont(Font font) {
        g.setFont(((BBFont)font).font);
    }

    public void fillArc(int i, int j, int k, int l, int m, int n) {
        g.fillArc(i, j, k, l, m, n);
    }

    public void drawChar(char c, int i, int j) {
        g.drawText(c, i, j, TOP_LEFT, 20);
    }

    public void drawChar(char c, int i, int j, int f) {
        g.drawText(c, i, j, f, 20);
    }

    public void drawString(String label, int x, int y) {
        g.drawText(label, x, y, TOP_LEFT, 100);
    }

    public void drawString(String label, int x, int y, int f) {
        g.drawText(label, x, y, f, 100);
    }

}
