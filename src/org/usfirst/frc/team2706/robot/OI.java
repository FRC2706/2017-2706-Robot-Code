package org.usfirst.frc.team2706.robot;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
		// Joystick for driving the robot around
		private Joystick driverStick;
		
		// Joystick for controlling the mechanisms of the robot
		private Joystick controlStick;
		
	    public Joystick getDriverJoystick() {
	        return driverStick;
	    }	    public Joystick getOperatorJoystick() {
	        return controlStick;
	    }
	    
	    public OI() {

			// Joystick for driving the robot around
			driverStick = new Joystick(0);
			
			// Joystick for controlling the mechanisms of the robot
			controlStick = new Joystick(1);
	    	JoystickButton LB = new JoystickButton(driverStick,5);
	    	StraightDriveWithEncoders sdwe = new StraightDriveWithEncoders(0.75,-8/12.0,5,1);
	    	LB.whenPressed(sdwe);
	    }
}

