package org.usfirst.frc.team2706.robot.commands.autonomous.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.QuickRotate;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.plays.DrivePlaceGear;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SideStartSideGear extends CommandGroup {
	public SideStartSideGear(boolean rightSide, double speed, double fromWallDistance, double toGearDistance, double turnDegrees, double reverseDistance, double toLaunchPadDistance) {
		this.addSequential(new StraightDriveWithEncoders(speed,fromWallDistance,25));
		this.addSequential(new QuickRotate(rightSide ? -turnDegrees : turnDegrees));
		this.addSequential(new DrivePlaceGear(speed,toGearDistance,reverseDistance));
		this.addSequential(new QuickRotate(rightSide ? turnDegrees : -turnDegrees));
		this.addSequential(new StraightDriveWithEncoders(speed,toLaunchPadDistance,25));
	}
}
