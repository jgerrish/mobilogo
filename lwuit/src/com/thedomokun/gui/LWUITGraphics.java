/**
 * 
 */
package com.thedomokun.gui;

/**
 * @author josh
 *
 */
public class LWUITGraphics extends Graphics {
    private com.sun.lwuit.Graphics g;
    /**
     * 
     */
    public LWUITGraphics(com.sun.lwuit.Graphics g2) {
        this.g = g2;
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

        setColor(rgb);
    }
    
    public void drawImage(Image img, int x, int y) {
        g.drawImage(((LWUITImage)img).image, x, y);
    }

    public void fillRect(int x, int y, int width, int height) {
        g.fillRect(x, y, width, height);        
    }

    public int getClipHeight() {
        return g.getClipHeight();
    }

    public int getClipWidth() {
        return g.getClipWidth();
    }

    public int getClipX() {
        return g.getClipX();
    }

    public int getClipY() {
        return g.getClipY();
    }

    public int getColor() {
        return g.getColor();
    }
    
    public void setFont(Font font) {
        g.setFont(((LWUITFont)font).font);
    }

    public void fillArc(int i, int j, int k, int l, int m, int n) {
        g.fillArc(i, j, k, l, m, n);
    }

    public void drawChar(char c, int i, int j) {
        g.drawChar(c, i, j);
    }

    public void drawChar(char c, int i, int j, int f) {
        g.drawChar(c, i, j);
    }

    public void drawString(String label, int x, int y) {
        g.drawString(label, x, y);
    }

    public void drawString(String label, int x, int y, int f) {
        g.drawString(label, x, y);
    }
}
