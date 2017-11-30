package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Backs up and tries to drive into peg with vision
 */
public class Realign extends CommandGroup {
    
    /**
     * Creates CommandGroup
     * 
     * @param speed The speed to drive back
     * @param distance The distance to drive back
     */
    public Realign(double speed, double distance) {
        this.addSequential(new StraightDriveWithEncoders(speed, -distance, 3, 1));
        this.addSequential(new StraightDriveWithCamera(0.6, 25, 3));
        this.addSequential(new StraightDriveWithTime(0.6, 1500));
    }
}
