package org.usfirst.frc.team2706.robot.commands.autonomous.curvecreator;

/**
 * Actually does the math to create the equation from two points and a tangent. Warning: Complicated
 *
 */
public class EquationCreator {

    /**
     * Creates the equation
     * 
     * @param x amount right (any unit)
     * @param y amount up(any unit)
     * @param theta ending angle(degrees)
     * @return the cubic equation that it creates
     */
    public static CubicEquation MakeCubicEquation(double x, double y, double theta) {

        // Makes a new coordinate points and rotates it into the bottom right quadrant
        Coordinates c = new Coordinates(x, y);
        c.rotateRight();

        // Calculates the slope of your triangle at the angle you want, in the bottom right quadrant
        double tangent = Math.tan(Math.toRadians(360 - theta));

        // Instead of making another equation class for each I just store each polynomial in its own double
        double eq1a = Math.pow(c.x, 3);
        double eq1b = Math.pow(c.x, 2);
        double eq1y = c.y;

        // The derivative equation that will be compared to the first equation, basically y`(x)
        double eq2a = Math.pow(c.x, 2) * 3;
        double eq2b = c.x * 2;

        // Figures out a similar b value to each equation so they can be subtracted away
        double multiplier = eq1b / eq2b;

        // Multiplies all of the values in equation b cause algebra
        eq2a *= multiplier;
        eq2b *= multiplier;
        double mTan = tangent * multiplier;

        // Figures out your final a and b value that is the coefficient
        double a = (eq1y - mTan) / (eq1a - eq2a);
        double b = ((eq1y - (eq1a * a)) / eq1b);

        // Creates a new cubicequation with the values to be returned.
        return new CubicEquation(-a, -b, 0, 0);
    }

}
