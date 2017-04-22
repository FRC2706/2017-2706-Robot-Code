package org.usfirst.frc.team2706.robot.commands.autonomous.curvecreator;

/**
 * Simple class that stores your x and y position for the equationcreator to increase simplicity in
 * that class
 */
public class Coordinates {

    /**
     * Your positions
     */
    double x, y;

    /**
     * Creates coordinates with x and y values
     * 
     * @param x The x part of the coordinate
     * @param y The y part of the coordinate
     */
    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Rotates you 90 degrees right into the bottom right quadrant, by going from (x,y) to (y,-x)
     */
    public void rotateRight() {
        double tempX = x;
        x = y;
        y = -tempX;
    }
}
