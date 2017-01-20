package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class TeleopPneumaticControl extends Command {

	@SuppressWarnings("unused")
	private GenericHID driverJoystick = Robot.oi.getDriverJoystick();
	private GenericHID joystick = Robot.oi.getOperatorJoystick();
	
	//FloatControl floatControl = new FloatControl(true);
	@Override
	protected void initialize() {
	}
	@Override
	protected void execute() {
		
		//Example mechanism controlling
		
	//	System.out.println(controlButtonLB);
	//	boolean controlButtonRB = joystick.getRawButton(6);
		
		
		//new StraightDriveWithEncoders(0.75,-8/12.0,5)
		}
		/*
		if(controlButtonRB) {
			Robot.driveTrain.back_right_motor.set(0.5);
		}*/
	

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
		end();
	}
	
	public void setJoystick(GenericHID joystick) {
		this.joystick = joystick;
	}

}
