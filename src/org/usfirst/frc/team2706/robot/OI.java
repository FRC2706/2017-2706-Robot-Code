package org.usfirst.frc.team2706.robot;

import org.usfirst.frc.team2706.robot.commands.autonomous.plays.AlignAndDistance;

import edu.wpi.first.wpilibj.Joystick;

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
	    }
	    
	    public Joystick getOperatorJoystick() {
	        return controlStick;
	    }
	    
	    public OI() {

			// Joystick for driving the robot around
			driverStick = new Joystick(0);
			
			EJoystickButton backLeftButton = new EJoystickButton(driverStick, 5);	
			backLeftButton.runWhileHeld(new AlignAndDistance(24));
			
			// Joystick for controlling the mechanisms of the robot
			controlStick = new Joystick(1);
	    }
}

