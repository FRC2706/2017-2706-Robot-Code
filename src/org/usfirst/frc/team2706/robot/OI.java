package org.usfirst.frc.team2706.robot;

import java.lang.reflect.Field;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
		// Joystick for driving the robot around
		private final Joystick driverStick;
		
		// Joystick for controlling the mechanisms of the robot
		private final Joystick controlStick;
		
	    public Joystick getDriverJoystick() {
	        return driverStick;
	    }	    public Joystick getOperatorJoystick() {
	        return controlStick;
	    }
	    
	    public OI() {
	    	this(new Joystick(0), new Joystick(1));
	    }
	    
	    public OI(Joystick driverStick, Joystick controlStick)  {
			// Joystick for driving the robot around
			this.driverStick = driverStick;
			
			// Joystick for controlling the mechanisms of the robot
			this.controlStick = controlStick;
	    	
			JoystickButton LB = new JoystickButton(controlStick,5);
			StraightDriveWithEncoders sdwe = new StraightDriveWithEncoders(0.75,-8/12.0,5,1);
			LB.whenPressed(sdwe);
	    }
	    
	    public void destroy() {
	    	try {
	    		Field f = Scheduler.getInstance().getClass().getDeclaredField("m_buttons");
	    		f.setAccessible(true);
	    		f.set(Scheduler.getInstance(), null);
	    		f.setAccessible(false);
	    	}
	    	catch(IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
	    }
}

