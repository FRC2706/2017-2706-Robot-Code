package org.usfirst.frc.team2706.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
		// Joystick for driving the robot around
		private Joystick driverStick = new Joystick(0);
		
		// Joystick for controlling the mechanisms of the robot
		private Joystick controlStick = new Joystick(1);
		
	    public Joystick getDriverJoystick() {
	        return driverStick;
	    }
	    public Joystick getOperatorJoystick() {
	        return controlStick;
	    }
}

