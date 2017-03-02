package org.usfirst.frc.team2706.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team2706.robot.commands.teleop.BlingTeleop;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author eAUE (Kyle Anderson)
 * @see <a href = "https://docs.google.com/drawings/d/1JQYcLj3Sdf0h_-DD0J7ceG14csBJ6LMhIWoIXoaeYVE/edit?usp=sharing"> 
 * Explanation of the light patterns</a> 
 */
public class Bling extends Subsystem {

    /**
     * Will be true if the bling system is working properly (so if the arduino is not plugged in, it
     * will be false). defaults to false to keep everything working.
     */
    public static boolean connected = false;

    public static SerialPort blingPort;
    
    // The number of pixels on one LED strip
    int pixels = 120;

    /**
     * Will be true if the battery level is critical, in which case it will override all other
     * signals to display the critical battery warning
     */
    public static boolean batCritical = false;

    // Let's make the colour and command codes
    Map<String, String> colours = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("RED", "16711680");
            put("GREEN", "32768");
            put("YELLOW", "16775680");
            put("PURPLE", "8388736");
            put("ORANGE", "16753920");
            put("BLUE", "255");
            put("VIOLET", "15631086");
            put("MERGE", "6160762");
            put("TAN", "16767411");
            put("PINK", "14027935");
            put("WHITE", "16777215");
            put("TURQUOISE", "65535");
            put("BLACK", "0");
            put("GOLD", "16766720");
            put("SILVER", "12632256");
        }
    };

    private Command defaultCommand;
    
    public Bling() {
        try {
            blingPort = new SerialPort(9600, SerialPort.Port.kMXP);
            // Will wait a max of half a second.
            blingPort.setTimeout(0.8);
            // Tell arduino we're sending a command.
            blingPort.writeString("I");
            // Clear the LED strip.
            blingPort.writeString("E0Z");
            connected = true;

        }
        
        catch (Exception e){
            DriverStation.getInstance();
            DriverStation.reportWarning("Can not connect to arduino :(", false);
        }
    }

    /**
     * This function should be run at the beginning of autonomous to get the proper light pattern.
     */
    public void auto() {
        // IF THE BLINGPORT FAILED, DON'T CAUSE MORE ERRORS
        if (!connected)
            return;

        blingPort.writeString("I");
        blingPort.writeString("E0Z"); // Clear the LED strip
        blingPort.writeString("F1C" + colours.get("MERGE") + "D150E1Z");
    }

    /**
     * This function initializes teleop period
     */
    public void teleopInit(){
        blingPort.writeString("I");
        blingPort.writeString("F7C" + colours.get("YELLOW") + "B100D100E7Z");
    }
    
    
    /**
     * This command just quickly clear the LED Strip.
     */
    public void clear() {
        if (!connected)
            return;

        blingPort.writeString("I");
        // Clear the LED strip
        blingPort.writeString("E0Z");
    }

    /**
     * This function will run whenever we want to display the battery voltage output. Will run
     * automatically at startup.
     * 
     * @param percent The current voltage percent reading from the battery.
     * @param criticalStatus Needs to be true if the battery level is below 20%.
     */
    public void batteryInd(double percent, boolean criticalStatus) {
        if (!connected)
            return;

        batCritical = criticalStatus;
      
        blingPort.writeString("I"); // Let them know we need to send another command
      
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
     * @param distance The current distance
     * @param pegIn Will be true if the peg is in 
     * the hole, false otherwise.
     */
    public void showDistance(double distance, boolean pegIn) {
        if (!connected)
            return;

        blingPort.writeString("I");
        // Clear the LED strip
        blingPort.writeString("E0Z");

        // Only showing 3 metres from the object.
        if (distance > 3.0)
            return;

        double percentDist = distance / 3;
        System.out.println(Math.round(percentDist * pixels));
        String dColour;

        // Peg in is true if the peg is going through the gear hole
        if (distance < 1.5 && !pegIn)
            dColour = colours.get("YELLOW");
        
        else if (distance <1.5 && pegIn)
            dColour = colours.get("GREEN");
        else
            dColour = colours.get("RED");

        // Colour flash
        blingPort.writeString(
                        "F7C" + dColour + "P0" + "Q" + Math.round(percentDist * pixels) + "E7Z");
    }

    /**
     * This function lets you show whether or not the robot is ready to receive a gear.
     * 
     * @param ready A boolean that indicates whether or not the robot is ready. True if yes.
     */
    public void showReadyToReceiveGear(boolean ready) {
        if (!connected)
            return;

        // Do not interfere with critical battery warning.
        // Show a theatre chase
        if (ready && !batCritical)
            customDisplay("Orange", 3, -1, 100, 0, 100);
    }
    
    /**
     * This function lets you show whether or not the robot is ready to climb.
     *
     * @param ready A boolean that indicate true for ready or false for not ready.
     */
    public void showReadyToClimb(boolean ready) {  
        if (ready) customDisplay("White", 11, 250, 60, 0, 100);   
    }

    /**
     * This is used to display a basic pattern on the bling LED lights.
     * Note that for functions above 9, pixelStart and pixelEnd and delay will do nothing.
     * 
     * @param pattern The type of motion or animation pattern you would like to display. Patterns range from 1-12.
     * 1 : Color wipe
     * 2 : Colour wipe with blank period
     * 3 : Theatre chase
     * 4 : Rainbow
     * 5 : Theatre chase rainbow
     * 6 : Color bar
     * 7 : Color bar flash
     * 8 : Bounce
     * 9 : Bounce wipe
     * 10: Multi bounce
     * 11: Multi bouce wipe
     * 12: Multi colour wipe
     * @param colour Colour, either as a preset such as "RED", "GREEN", "WHITE" (either caps or
     *        no caps) or in decimal format. Use a programmer calculator to determine decimal
     *        format.
     * 
     *        Presets: GREEN, RED, BLUE, YELLOW, ORANGE, PURPLE, TAN, VIOLET, MERGE, PINK, WHITE,
     *        TURQUOISE, BLACK, GOLD, SILVER
     * @param delay The delay between animation segments in seconds, if applicable.
     * @param brightness The brightness of the LED pattern as an integer between 0 and 100.
     * @param pixelStart The percent of the bar where the pixel pattern will start in decimal format.
     * @param pixelEnd The percent of the bar where the pattern will end in decimal format.
     */
    public void customDisplay(String colour, int pattern, double delay,
                    int brightness, int pixelStart, int pixelEnd) {
        if (!connected)
            return;

        // Get rid of all the spaces
        String gColour = colour.replace(" ", "");

        // Make sure that any letters are uppercase.
        gColour = gColour.toUpperCase();

        // Preset colour that we need to convert to RGB888.
        if ((gColour.charAt(0)) != '0') {
            gColour = colours.get(gColour);
        }

        int startPixel = Math.round(pixelStart * pixels);
        int endPixel = Math.round(pixelEnd * pixels);

        blingPort.writeString("I");
        
        if (pattern <= 9)
            blingPort.writeString("F" + pattern + "C" + gColour + "B" + brightness + "D" + delay
                                   + "P" + startPixel + "Q" + endPixel + "E" + pattern + "Z");
        else 
            blingPort.writeString("F" + pattern + "C" + gColour + "B" + brightness + "E" + 
                                   pattern + "Z");
    }

    /**
     * When no other command is running let the operator drive around using the Xbox joystick.
     */
    @Override
    public void initDefaultCommand() {
        if (defaultCommand == null) {
            getDefaultCommand();
        }
        setDefaultCommand(defaultCommand);
    }

    @Override
    public Command getDefaultCommand() {
        if (defaultCommand == null) {
            defaultCommand = new BlingTeleop();
        }
        return defaultCommand;
    }
}
