package org.usfirst.frc.team2706.robot.commands.autonomous.plays;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.QuickStraightDriveWithDistanceSensor;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightenWithDistanceSensor;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AlignAndDistance extends CommandGroup {
	/**
	 * Aligns with the wall, and then drives to a certain distance from the wall
	 * 
	 * @param distance Distance from the wall
	 */
	public AlignAndDistance(double distance) {
		this.addSequential(new StraightenWithDistanceSensor(0.6,0.8,0.65,0.25,25,false,25, 0.025));
		this.addSequential(new StraightDriveWithTime(0.0,250));
		this.addSequential(new QuickStraightDriveWithDistanceSensor(0.5, distance - 1, distance + 1, 25, 0.8));
	}
}
