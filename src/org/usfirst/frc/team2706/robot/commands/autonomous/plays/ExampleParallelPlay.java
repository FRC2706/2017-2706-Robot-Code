package org.usfirst.frc.team2706.robot.commands.autonomous.plays;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;
import org.usfirst.frc.team2706.robot.commands.camera.SearchForTarget;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ExampleParallelPlay extends CommandGroup {
	// Uses camera to search for a target for 5 seconds at the same time as driving forward
	public ExampleParallelPlay() {	
		this.addParallel(new StraightDriveWithTime(0.4,5));
		this.addParallel(new SearchForTarget(),5);
	}
}
