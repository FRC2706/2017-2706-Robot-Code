package org.usfirst.frc.team2706.robot.commands.autonomous.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.QuickRotate;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.plays.DrivePlaceGear;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterToLaunch extends CommandGroup {

	public CenterToLaunch(boolean rightSide, double speed, double gearDistance, double reverseDistance, double turnAmount, double sideDistance, double toLaunchPad) {
		this.addSequential(new DrivePlaceGear(speed,gearDistance,reverseDistance));
		this.addSequential(new QuickRotate(rightSide ? turnAmount : -turnAmount));
		this.addSequential(new StraightDriveWithEncoders(speed,sideDistance,25,5));
		this.addSequential(new QuickRotate(rightSide ? -turnAmount : turnAmount));
		this.addSequential(new StraightDriveWithEncoders(speed,toLaunchPad,25,5));
	}
}
