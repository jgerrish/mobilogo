package com.thedomokun.common;


import com.thedomokun.gui.Graphics;
import com.thedomokun.gui.Image;
import com.thedomokun.gui.Point;

public class GUITurtle extends Turtle {
    // Current facing of the turtle in degrees
    // zero degree means pointing straight up
    protected double facing;
    protected boolean penUp;
    protected Point currentPos;
    protected int size = 6;
    Graphics g;
    Image offscreenBuffer;

    public GUITurtle(Image img) {
        facing = 0;
        penUp = false;
        //this.g = g;
        this.offscreenBuffer = img;
        this.g = this.offscreenBuffer.getGraphics();
    }

    public GUITurtle(Image b, Point start) {
        this(b);
        //System.out.println("Creating turtle");
        currentPos = start;
        //System.out.println("start point: " + start);
    }

    public void reset(Point p) {
        facing = 0;
        penUp = false;
        currentPos = p;
    }

    protected Point getCorner(double a) {
        Point p = new Point();
        p.setX(Math.cos(Math.toRadians(a)) * size + currentPos.getX());
        // Could use Pythagorean theorem instead to calculate length of y2
        p.setY(Math.sin(Math.toRadians(a)) * size + currentPos.getY());
        
        return p;
    }
    
    public void penUp() {
        penUp = true;
    }

    public void penDown() {
        penUp = false;
    }
    
    public void right(int amount) {
        this.facing = (this.facing + amount) % 360;
    }
    public void left(int amount) {
        this.facing = (this.facing - amount) % 360;
    }

    public void draw(Graphics g) {
        Graphics saved = g;
        
        if( offscreenBuffer != null ){
            g = offscreenBuffer.getGraphics();
        }

        g.setColor(0x000000);

        // Find where the end-points of the triangle should be
        // TODO: optimize
        double a1 = facing - 90;
        Point p1 = getCorner(a1);
        double a2 = facing + 30;
        Point p2 = getCorner(a2);
        double a3 = facing + 150;
        Point p3 = getCorner(a3);
        
        g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        g.drawLine(p2.getX(), p2.getY(), p3.getX(), p3.getY());
        g.drawLine(p3.getX(), p3.getY(), p1.getX(), p1.getY());
        
        //System.out.println("p1x: " + p1.getX() + ", p1y: " + p1.getY() +
        //        ", p2x: " + p2.getX() + ", p2y: " + p2.getY() +
        //        ", p3x: " + p3.getX() + ", p3y: " + p3.getY());
        if( g != saved ){
            saved.drawImage(offscreenBuffer, 0, 0);
        }
    }

    public void forward(int dist) {
        //System.out.println("Forward " + dist);
        int new_x = currentPos.getX()
                    + (int)(dist * Math.cos(Math.toRadians(facing - 90)));
        int new_y = currentPos.getY()
                    + (int)(dist * Math.sin(Math.toRadians(facing - 90)));

        //System.out.println("old_x: " + currentPos.getX() + ", old_y: " + currentPos.getY() +
        //                   ", new_x: " + new_x + ", new_y: " + new_y);
        if (!penUp)
            g.drawLine(currentPos.getX(), currentPos.getY(), new_x, new_y);

        currentPos.setX(new_x);
        currentPos.setY(new_y);
        //draw(g);
    }

    public void backward(int dist) {
        int new_x = currentPos.getX() - (int)(dist * Math.cos(Math.toRadians(facing - 90)));
        int new_y = currentPos.getY() - (int)(dist * Math.sin(Math.toRadians(facing - 90)));

        if (!penUp)
            g.drawLine(currentPos.getX(), currentPos.getY(), new_x, new_y);

        currentPos.setX(new_x);
        currentPos.setY(new_y);

        //draw(g);
    }

}
