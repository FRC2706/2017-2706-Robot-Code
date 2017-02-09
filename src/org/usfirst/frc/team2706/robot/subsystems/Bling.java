package org.usfirst.frc.team2706.robot.subsystems;


import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team2706.robot.controls.StickRumble;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Bling extends Subsystem {
SerialPort blingPort = new SerialPort(9600, SerialPort.Port.kMXP);
int pixels = 120; //The number of pixels on one LED strip

//Let's make the colour and command codes
Map<String, String> colours = new HashMap<String, String>(){{
put("RED", "0xff0000");
put("GREEN", "0x008000");
put("YELLOW", "0xffff00");
put("PURPLE", "0x800080");
put("ORANGE", "0xffa500");
put("BLUE", "0x0000ff");
put("VIOLET", "0xee82ee");
put("MERGE", "0x5200cc");
put("TAN", "0xffd9b3");
put("PINK", "0xD60C9F");
put("WHITE", "0xFFFFFF");
put("TURQUOISE", "0x00FFFF");
put("BLACK", "0x000000");
}};
	public Bling(){
	    blingPort.setTimeout(0.5); //Will wait a max of half a second.
	    blingPort.writeString("I");
		String readString = blingPort.readString(); //Get output from the arduino.
		blingPort.writeString("E2Z");
		System.out.println(readString + "\nThe previous line contained the output from the arduino.");
		
		StickRumble rumbler = new StickRumble(0.5, 0.3, 2);
		rumbler.start();
		
	}
	
	/**
	 * This function should be run at the beginning of autonomous to get
	 * the proper light pattern.
	 */
	public void auto(){ //Will run during autonomous period
		blingPort.writeString("I");
		if (blingPort.readString() == "R")blingPort.writeString("E0Z");
	}
	
	/**
	 * This function runs to initialize the teleop patterns.
	 */
	public void startTelOp(){ //Start the teleop period for the bling
		blingPort.writeString("I");
	}
	
	/**
	 * This function will run whenever we want to display the
	 * battery voltage output. Will run automatically at startup.
	 * @param percent
	 * The current voltage percent reading from the battery.
	 */
	public void batteryInd(double percent){ 
	    
    	/*Display the battery voltage of the robot's battery
    	*Voltage will be a double representing what the battery is outputting. 
    	*/
	    blingPort.writeString("I"); //Clear the LED strip
	    String bColour;
	    if (percent <= 0.25) bColour = "RED";
	    else if (percent <= 0.5) bColour = "YEL";
	    else if (percent <= 0.75) bColour = "PUR";
	    else bColour = "GRN";
	    blingPort.writeString("F6C" + bColour + "P" + Math.round(percent * pixels) + "E6Z");
	}
	/**
	 * Show a distance indication on the LED strip out of 3 metres.
	 * @param distance
	 * : The current distance
	 */
	public void showDistance(double distance){
	    blingPort.writeString("I");
	    if (distance > 3.0) return; //Only showing 3 metres from the object.
	    double percentDist = distance/3;
	    String dColour;
	    if (distance > 2) dColour = "RED";
	    else if (distance > 1) dColour = "YEL";
	    else dColour = "GRN";
	    blingPort.writeString("F7C" + dColour + "P" + Math.round(percentDist * pixels) + "E7Z"); //Colour flash with different colours
	}
	/**
	 * This function lets you show whether or not the robot is ready
	 * to receive a gear.
	 * @param ready
	 * : A boolean that indicates whether or not the robot is ready. True if yes.
	 */
	public void showReadyToReceiveGear(boolean ready){
	    if (ready) display("Green", 3, -1, 2, 100);
	}
	/**
	 * This is used to display a basic pattern on the bling
	 * LED lights. 
	 * @param colour
	 * : Colour, either as a present such as "RED", "GREEN", "WHITE" (either caps or no caps)
	 * or in RGB888 (eg. "0x0000FF" (Solid blue)).
	 * <a href = "http://www.barth-dev.de/online/rgb565-color-picker/">Colour Picker</a>  
	 * 
	 * Presets: GREEN, RED, BLUE, YELLOW, ORANGE, PURPLE, TAN, VIOLET, MERGE, PINK, WHITE, TURQUOISE, BLACK
	 * @param pattern
	 * : The type of motion or animation pattern you would like to display.
	 * @param duration
	 * : The amount of seconds the whole thing lasts.
	 * @param delay
	 * : The delay between animation segments in seconds, if applicable.
	 */
	public void display(String colour, int pattern, double duration, double delay, int brightness){
	    String gColour = colour.replace(" ", ""); //Get rid of all the spaces
	    gColour = gColour.toUpperCase(); //Make sure that any letters are uppercase.
	    
	    if ((gColour.charAt(0)) != '0'){ //Preset colour that we need to convert to RGB888.
	     gColour = colours.get(gColour);
	    }
	    blingPort.writeString("I");
	    blingPort.writeString("F" + pattern + "C" + gColour + "B" + brightness + "D" + duration + "E" + pattern + "Z");
	}

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub
        
    }
}
