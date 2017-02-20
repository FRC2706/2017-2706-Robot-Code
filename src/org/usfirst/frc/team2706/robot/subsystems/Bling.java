package org.usfirst.frc.team2706.robot.subsystems;


import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Bling extends Subsystem {

    SerialPort blingPort = new SerialPort(9600, SerialPort.Port.kMXP);

    int pixels = 120; // The number of pixels on one LED strip

    /*
     * Will be true if the battery level is critical, in which case it will override all other
     * signals to display the critical battery warning
     */
    boolean batCritical = false;


    // Let's make the colour and command codes
    Map<String, String> colours = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("RED", "16711680");
            put("GREEN", "32768");
            put("YELLOW", "65535");
            put("PURPLE", "8388736");
            put("ORANGE", "16753920");
            put("BLUE", "255");
            put("VIOLET", "15631086");
            put("MERGE", "5374156");
            put("TAN", "16767411");
            put("PINK", "14027935");
            put("WHITE", "16777215");
            put("TURQUOISE", "65535");
            put("BLACK", "0");
        }
    };

    public Bling() {
        blingPort.setTimeout(0.5); // Will wait a max of half a second.
        blingPort.writeString("I");
        // TODO replace this with a better error catching system
        try {
            blingPort.readString(); // Get output from the arduino. 
        } catch(RuntimeException e) {
            System.out.println("Can not connect to arduino :(");
        }
    }

    /**
     * This function should be run at the beginning of autonomous to get the proper light pattern.
     */
    public void auto() { // Will run during autonomous period

        blingPort.writeString("I");
        blingPort.writeString("E0Z"); // Clear the LED strip
        blingPort.writeString("E1Z");
    }

    /**
     * This function runs to initialize the teleop patterns. Starts the teleop period for the bling
     */
    public void startTeleOp() {

        blingPort.writeString("I");
        blingPort.writeString("E0Z"); // Clear the LED strip
    }

    /**
     * This function will run whenever we want to display the battery voltage output. Will run
     * automatically at startup.
     * 
     * @param percent : The current voltage percent reading from the battery.
     */
    public void batteryInd(double percent) {

        blingPort.writeString("I"); // Let them know we need to send another command
        blingPort.writeString("E0Z"); // Clear the LED strip
        String bColour;
        if (percent <= 0.25)
            bColour = colours.get("RED");
        else if (percent <= 0.5)
            bColour = colours.get("YELLOW");
        else if (percent <= 0.75)
            bColour = colours.get("PURPLE");
        else
            bColour = colours.get("GREEN");

        // Use multi-colour display
        blingPort.writeString("F12C" + bColour + "D" + Math.round(percent * 100) + "E12Z");
    }

    /**
     * Show a distance indication on the LED strip out of 3 metres.
     * 
     * @param distance : The current distance
     */
    public void showDistance(double distance) {

        blingPort.writeString("I");
        blingPort.writeString("E0Z"); // Clear the LED strip
        if (distance > 3.0)
            return; // Only showing 3 metres from the object.
        double percentDist = distance / 3;
        System.out.println(Math.round(percentDist * pixels));
        String dColour;
        if (distance > 2)
            dColour = colours.get("RED");
        else if (distance > 1)
            dColour = colours.get("YELLOW");
        else
            dColour = colours.get("GREEN");
        
        // Colour flash
        blingPort.writeString("F7C" + dColour + "P0" + "Q" + Math.round(percentDist * pixels) + "E7Z");
    }

    /**
     * This function lets you show whether or not the robot is ready to receive a gear.
     * 
     * @param ready : A boolean that indicates whether or not the robot is ready. True if yes.
     */
    public void showReadyToReceiveGear(boolean ready) {
        // Do not interfere with critical battery warning.
        if (ready && !batCritical)
            customDisplay("Green", 3, -1, 2, 100, 0, 120);
    }

    /**
     * This is used to display a basic pattern on the bling LED lights.
     * 
     * @param pattern : The type of motion or animation pattern you would like to display.
     * @param colour : Colour, either as a present such as "RED", "GREEN", "WHITE" (either caps or
     *        no caps) or in decimal format. Use a programmer calculator to determine decimal
     *        format. <a href = "http://www.barth-dev.de/online/rgb565-color-picker/">Colour
     *        Picker</a>
     * 
     *        Presets: GREEN, RED, BLUE, YELLOW, ORANGE, PURPLE, TAN, VIOLET, MERGE, PINK, WHITE,
     *        TURQUOISE, BLACK
     * @param duration : The amount of seconds the whole thing lasts.
     * @param delay : The delay between animation segments in seconds, if applicable.
     * @param pixelStart : The percent of the bar where the pixel pattern will start.
     * @param pixelEnd : The percent of the bar where the pattern will end.
     */
    public void customDisplay(String colour, int pattern, double duration, double delay, int brightness,
                    int pixelStart, int pixelEnd) {

        String gColour = colour.replace(" ", ""); // Get rid of all the spaces
        gColour = gColour.toUpperCase(); // Make sure that any letters are uppercase.

        if ((gColour.charAt(0)) != '0') { // Preset colour that we need to convert to RGB888.
            gColour = colours.get(gColour);
        }

        int startPixel = Math.round((pixelStart * pixels) / 100);
        int endPixel = Math.round((pixelEnd * pixels) / 100);

        blingPort.writeString("I");
        blingPort.writeString("E0Z"); // Clear the LED strip
        blingPort.writeString("F" + pattern + "C" + gColour + "B" + brightness + "D" + duration
                        + "P" + startPixel + "Q" + endPixel + "E" + pattern + "Z");
    }

    @Override
    protected void initDefaultCommand() {}
}
