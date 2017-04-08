package org.usfirst.frc.team2706.robot.subsystems;

import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team2706.robot.bling.BlingPeriodic2;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * @author eAUE (Kyle Anderson)
 */
public class Bling extends Subsystem {

    /**
     * Will be true if the bling system is working properly (so if the arduino is not plugged in, it
     * will be false). defaults to false to keep everything working.
     */
    public static boolean connected = false;

    public static SerialPort blingPort;
       
    Map<String, String> lastCommand = new HashMap<String,String>() {
        private static final long serialVersionUID = 1L;

        {
        put("pattern", "0");
        put("colour", "MERGE");
        put("delay", "150");
        put("startPoint", "0");
        put("endPoint", "1");
        put("brightness", "100");
        
        }
    };
    
    /* In case we get complaints of bling subsystem being distracting
       We will have the ability to turn off flashy stuff */
    public static boolean flashyOff = false;

    // We only want to show distances if this is true.
    public static boolean showDistance = true;
    
    /* Used to tell the bling command whether or not to display green 
    or nothing during autonomous */
    private static String specialState = "";
        
    // The command, in a ready state to send.
    protected static String command = "";
    
    protected static String previousCommand = "E0Z";
        
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
            clear();
            connected = true;

        }
        
        catch (Exception e){
            DriverStation.getInstance();
            DriverStation.reportWarning("Cannot connect to arduino :(", false);
        }
    }
    /**
     * Tells other subsystems if the bling system has displayed a battery 
     * critical sigh yet.    
     * @return True if battery is critical, false otherwise.
     */
    public boolean getBatteryCriticality() {
        return batCritical;  
    }
    
    /**
     * Call this to change the boolean state of autonomous.
     * @param good Send a true to display green, false to display nothing.
     */
    public void toggleAutoState(boolean good) {
        if (good) { 
            specialState = "autoTrue";
            auto();
        }
        
        else {
            clear();
        }
        
    }
    
    /**
     * Returns the value of autostate to tell bling command
     * whether or not to display a green during autonomous.
     * @return True if autonomous is good, false otherwise.
     */
    public String getSpecialState() {
        String returnable = specialState;
        specialState = "";
        return returnable;
    }
    /**
     * Simple function that toggles the value of the distance
     * shower variable.
     */
    public void toggleDistanceShower() {
        showDistance = !showDistance;
    }
    
    /**
     * Returns the value of the distance shower variable.
     * Used to determine whether or not the drivers want to
     * see distance when lining up.
     * @return A boolean with the value of whether or not we want
     * to show distance.
     */
    public boolean getDistanceShower() {
        return showDistance;
    }
    
    /**
     * This command will toggle if we're displaying flashy patterns
     * in case of possible complaints, a button on the driver joystick will
     * call this to toggle it.
     */
    public void toggleFlashiness() {
        
        specialState = "flashiness";
        
        if (!flashyOff) {
            flashyOff = true;
            
            if (lastCommand.get("pattern") == "0")
                return;
            
            // If the command was lower than 9, show a command 6.
            else if (Integer.parseInt(lastCommand.get("pattern")) <= 9)
                command = ("F6C" + lastCommand.get("colour") + "D" + lastCommand.get("delay")
                + "B" + lastCommand.get("brightness") + "P" + lastCommand.get("startPoint") + 
                "Q" + lastCommand.get("endPoint") + "R500E6Z");
            
            else
                command = ("F12C" + lastCommand.get("colour") + "D" +
                                lastCommand.get("delay") + "B" + lastCommand.get("brightness")
                                + "R500E12Z");
        }
        else {
            flashyOff = false;
            
            if (lastCommand.get("pattern") == "0")
                return;
            
            else if (Integer.parseInt(lastCommand.get("pattern")) <= 9)
                command = ("F" + lastCommand.get("pattern") + "C" + lastCommand.get("colour") + "D" + lastCommand.get("delay")
                + "B" + lastCommand.get("brightness") + "P" + lastCommand.get("startPoint") + 
                "Q" + lastCommand.get("endPoint") + "R500E" + lastCommand.get("pattern") + "Z");
            
            else
                command = ("F" + lastCommand.get("pattern") + "C" + lastCommand.get("colour") + "D" +
                                lastCommand.get("delay") + "B" + lastCommand.get("brightness")
                                + "R500E" + lastCommand.get("pattern") + "Z");
            
        }   
        DriverStation.getInstance();
        DriverStation.reportWarning("Bling Flashiness toggled. Is now " + flashyOff, false);
        send();
    }
    /**
     * Used to show if there is an error.
     * Will be red if there is, green otherwise.
     * @param error True if there is an error, false otherwise.
     */
    public void showError(boolean error) {
        if (error) 
            customDisplay("red", 7, 50, 100, 0, 1);
        else
            customDisplay("green", 7, 350, 100, 0, 1);
    }
    
    /**
     * This function should be run at the beginning of autonomous to get the proper light pattern.
     */
    public void auto() {
        customDisplay("green", 6, 50, 100, 0, 1);
    }

    /**
     * This function initializes teleop period
     */
    public void teleopInit(){
        customDisplay("MERGE", 7, 400, 100, 0, 1);
    }
    /**
     * Just displays a fun rainbow of colours during teleop when we're not doing anything.
     */
    public void funDisplay() {
        customDisplay("Blue", 4, 50, 100, 0, 1);
    }
    
    /**
     * Just a fun pattern to show while climbing
     */
    public void climbingDisplay() {
        customDisplay("merge", 7, 75, 100, 0, 1);
    }
    
    
    /**
     * This command just quickly clear the LED Strip.
     */
    public void clear() {
        // Clear the LED strip
        command = ("E0Z");
        send();
        
        lastCommand.put("pattern", "0");
    }

    /**
     * This function will run whenever we want to display the battery voltage output. Will run
     * automatically at startup.
     * 
     * @param percent The current voltage percent reading from the battery.
     * @param criticalStatus Needs to be true if the battery level is below 20%.
     */
    public void batteryInd(double percent, boolean criticalStatus) {

        batCritical = criticalStatus;
      
        String bColour;
        if (percent <= 0.25)
            bColour = "red";
        else if (percent <= 0.5)
            bColour = "yellow";
        else if (percent <= 0.75)
            bColour = "purple";
        else
            bColour = "green";

        // Use multi-colour display
        customDisplay(bColour, 12, (int) Math.round(percent * 100), 100, 0, 1);
    }

    /**
     * Show a distance indication on the LED strip.
     * 
     * @param distance The current distance
     * @param pegIn Will be true if the peg is in 
     * the hole, false otherwise.
     */
    public void showDistance(boolean pegIn) {

        String dColour;

        // Peg in is true if the peg is going through the gear hole
        if (pegIn) {
            dColour = "green";
        }
        else {
            dColour = "red";
        }
        // Multi-colour wipe
        customDisplay(dColour, 7, 100, 100, 0, 1);

    }

    /**
     * This function lets you show whether or not the robot is ready to receive a gear.
     * 
     * @param ready A boolean that indicates whether or not the robot is ready. True if yes.
     */
    public void showReadyToReceiveGear(int stateOfReadiness) {

        String colour;
        // Show a theatre chase
        if (!connected || !showDistance)
            return;
        // Just right
        else if (stateOfReadiness == 1) {
            colour = "green";
        }
        // Too close
        else if (stateOfReadiness == 0) {
            colour = "white";
        }
        // Too far
        else {
            colour = "red";
        }
        
        customDisplay(colour, 3, 100, 100, 0, 1);
    }
    
    /**
     * Basic command that shows a green signal when we get a gear.
     */
    public void showGotGear() {
        customDisplay("Green", 6, 100, 100, 0, 1);
    }
    
    /**
     * This function lets you show whether or not the robot is ready to climb.
     *
     * @param ready A boolean that indicate true for ready or false for not ready.
     */
    public void showReadyToClimb(boolean ready) {  
        if (ready) 
            customDisplay("White", 11, 25, 100, 0, 100);   
        
    }
    
    /**
     * Basic command that tells the arduino to get ready for a command.
     */
    public void getReady() {
        blingPort.writeString("I");
    }

    /**
     * This is used to display a basic pattern on the bling LED lights.
     * Note that for functions above 9, pixelStart and pixelEnd and delay will do nothing.
     * 
     * @param pattern The type of motion or animation pattern you would like to display. Patterns range from 1-12. <p>
     * 1 : Colour wipe <p>
     * 2 : Colour wipe with blank period <p>
     * 3 : Theatre chase<p>
     * 4 : Rainbow<p>
     * 5 : Theatre chase rainbow<p>
     * 6 : Color bar<p>
     * 7 : Color bar flash<p>
     * 8 : Bounce<p>
     * 9 : Bounce wipe<p>
     * 10: Multi bounce<p>
     * 11: Multi bouce wipe<p>
     * 12: Multi colour wipe<p>
     * @param colour Colour, either as a preset such as "RED", "GREEN", "WHITE" (either caps or
     *        no caps) or in decimal format. Use a programmer calculator to determine decimal
     *        format. <p>
     * 
     *        Presets: GREEN, RED, BLUE, YELLOW, ORANGE, PURPLE, TAN, VIOLET, MERGE, PINK, WHITE,
     *        TURQUOISE, BLACK, GOLD, SILVER 
     * @param delay The delay between animation segments in seconds, if applicable.
     * @param brightness The brightness of the LED pattern as an integer between 0 and 100.
     * @param pixelStart The percent of the bar where the pixel pattern will start in decimal format.
     * @param pixelEnd The percent of the bar where the pattern will end in decimal format.
     */
    public void customDisplay(String colour, int pattern, double tDelay,
                    int brightness, int pixelStart, int pixelEnd) {
        
        int delay = (int) Math.round(tDelay);
        
        // Get rid of all the spaces
        String gColour = colour.replace(" ", "");

        // Preset colour that we need to convert to proper format
        try {
            Integer.parseInt(gColour);
        }
        catch (Exception e) {
            // In case the colour is not in the Map.
            try { 
                // Make sure that any letters are uppercase.
                gColour = gColour.toUpperCase();
                gColour = colours.get(gColour);
            }
            
            catch (Exception f) {
                return;
            }
        }

        int startPixel = Math.round(pixelStart * pixels);
        int endPixel = Math.round(pixelEnd * pixels);
       
        // Record all of what is inputted
        lastCommand.put("pattern", Integer.toString(pattern));
        lastCommand.put("brightness", Integer.toString(brightness));
        lastCommand.put("colour", gColour);
        lastCommand.put("delay", Double.toString(delay));
        lastCommand.put("startPoint", Integer.toString(startPixel));
        lastCommand.put("endPoint", Integer.toString(endPixel));
        
        if (flashyOff && pattern <= 9) 
            pattern = 6;
        else if (flashyOff && pattern > 9)
            pattern = 12;        
        
        String LEDStripRange = "";
        if (pixelStart != 0 || pixelEnd != 1) {
            LEDStripRange = "P" + startPixel + "Q" + endPixel;            
        }
        
        // Let's make it easy for rainbow displaying
        if (pattern == 4) {
            command = ("E4Z");
        }
        
        else if (pattern <= 9) {
            command = ("F" + pattern + "C" + gColour + "B" + brightness + "D" + delay
                                   + LEDStripRange + "E" + pattern + "Z");
        }
        else {
            command = ("F" + pattern + "C" + gColour + "D" + delay + "B" + brightness + "R10000E" + 
                                   pattern + "Z");
        }
        send();
    }
    public void send() {
        // IF THE BLINGPORT FAILED, DON'T CAUSE ERRORS
        if (!(previousCommand.equalsIgnoreCase(command)) && connected) {
            blingPort.writeString("I" + command);
        }
        
        // Making sure we do not send the same command twice.
        previousCommand = command;
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
            defaultCommand = new BlingPeriodic2();
        }
        return defaultCommand;
    }
}
