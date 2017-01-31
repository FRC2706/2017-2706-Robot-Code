package org.usfirst.frc.team2706.robot;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.QuickStraightDriveWithDistanceSensor;

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
			
			EJoystickButton LB = new EJoystickButton(driverStick, 5);	
			// TODO: Tune stopCycles and speed
			QuickStraightDriveWithDistanceSensor sdwe = new QuickStraightDriveWithDistanceSensor(0.5, 13.0, 15.0, 4, 0.8);
			LB.whenPressed(sdwe);
			LB.cancelWhenReleased(sdwe);

			// Joystick for controlling the mechanisms of the robot
			controlStick = new Joystick(1);
	    }
}

