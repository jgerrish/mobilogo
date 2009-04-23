/**
 * 
 */
package com.thedomokun.common;

/**
 * Abstract LOGO turtle class.
 * @author josh
 */
public abstract class Turtle {

    /**
     * Move the turtle forward
     * @param dist the distance to move forward
     */
    public abstract void forward(int dist);
    /**
     * Move the turtle backwards
     * @param dist the distance to move backwards
     */
    public abstract void backward(int dist);
    /**
     * Turn the turtle right
     * @param dist the amount to turn right in degrees
     */
    public abstract void right(int dist);
    /**
     * Turn the turtle left
     * @param dist the amount to turn left in degrees
     */
    public abstract void left(int dist);
}
