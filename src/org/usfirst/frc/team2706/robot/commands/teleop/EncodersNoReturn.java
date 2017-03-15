package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Does a full stop on the spot as best as possible and rights itself it you go too far too quick
 *
 */
public class EncodersNoReturn extends CommandGroup {
    
    public EncodersNoReturn() {
        this.addSequential(new StraightDriveWithEncoders(0.5,0.0,0.2));
        this.addParallel(new HandBrake(true));
    }
}
