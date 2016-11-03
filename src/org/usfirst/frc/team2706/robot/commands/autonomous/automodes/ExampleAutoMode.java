package org.usfirst.frc.team2706.robot.commands.autonomous.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.plays.ExampleParallelPlay;
import org.usfirst.frc.team2706.robot.commands.autonomous.plays.ExamplePlay;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ExampleAutoMode extends CommandGroup {
	public ExampleAutoMode() {
		/*
		 * Combines exampleplay and exampleparallelplay to move 
		 * forwards turn and then move forwards with camera
		 */	
		this.addSequential(new ExamplePlay());
		this.addSequential(new ExampleParallelPlay());
	}
}
