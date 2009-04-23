package com.thedomokun.common;

import com.thedomokun.common.Turtle;


public class TestTurtle extends Turtle {
    public int x, y;
    public double facing;

    public void reset() {
        x = 0;
        y = 0;
        facing = 0;
    }

    public void backward(int dist) {
        x -= dist;
    }

    public void forward(int dist) {
        x += dist;
    }

    public void left(int dist) {
        facing -= dist;
    }

    public void right(int dist) {
        facing += dist;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append("Position: ");
        buf.append(x);
        buf.append("\nFacing: ");
        buf.append(facing);

        return buf.toString();
    }
}
