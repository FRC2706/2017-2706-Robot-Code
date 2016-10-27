package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;

public class TeleopPneumaticControl extends Command {

	private GenericHID joystick = Robot.oi.getOperatorJoystick();
	
	//FloatControl floatControl = new FloatControl(true);
	@Override
	protected void initialize() {
	}
	@Override
	protected void execute() {
		
		//Example mechanism controlling
		boolean controlButtonLB = joystick.getRawButton(5);
		boolean controlButtonRB = joystick.getRawButton(6);
		
		if(controlButtonLB) {
			// Do something or run a command
		}
		if(controlButtonRB) {
			//Do something or run a command
		}
	}

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
