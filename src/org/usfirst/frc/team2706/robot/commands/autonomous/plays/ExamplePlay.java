package org.usfirst.frc.team2706.robot.commands.autonomous.plays;



import org.usfirst.frc.team2706.robot.commands.autonomous.movements.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ExamplePlay extends CommandGroup {
	/* This class is an example class that shows how you would combine
	 * multiple movements to create a play, this one moves forward
	 * and then turns right and then moves forwards again.
	 */
	public ExamplePlay() {
		//Adds a movement one after another instead of at the same time
		this.addSequential(new StraightDriveWithTime(0.7,2));
		this.addSequential(new RotateDriveWithGyro(0.7,90,100));
	}
}
