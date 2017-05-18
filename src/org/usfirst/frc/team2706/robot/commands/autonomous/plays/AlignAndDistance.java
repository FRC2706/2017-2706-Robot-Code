package org.usfirst.frc.team2706.robot.commands.autonomous.plays;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.QuickStraightDriveWithDistanceSensor;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Aligns with the wall, and then drives to a certain distance from the wall
 */
public class AlignAndDistance extends CommandGroup {

    /**
     * Aligns with the wall, and then drives to a certain distance from the wall
     * 
     * @param distance Distance from the wall
     */
    public AlignAndDistance(double distance) {
        this.addSequential(new StraightDriveWithTime(0.0, 250));
        this.addSequential(new QuickStraightDriveWithDistanceSensor(0.4, distance - 1, distance + 1,
                        25, 0.8));
    }
}

