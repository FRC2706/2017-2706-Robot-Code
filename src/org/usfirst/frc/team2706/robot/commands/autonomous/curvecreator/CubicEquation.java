package org.usfirst.frc.team2706.robot.commands.autonomous.curvecreator;

import java.text.DecimalFormat;

/**
 * Stores and allows you to print the equation if neccesary, more useful for the console version but
 * still valuable
 */
public class CubicEquation {

    // The four coefficients that need to be saved
    public double a, b, c, d;

    public CubicEquation(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Prints the equation so it can be copied into desmos easily.
     */
    public String toString() {
        DecimalFormat df =
                        new DecimalFormat("############.#########################################");
        String aa = df.format(a);
        String bb = df.format(b);
        return "x = " + aa + "y^3 + " + bb + "y^2";
    }
}
