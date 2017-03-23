package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Realign extends CommandGroup {
    public Realign(double speed, double distance) {
        this.addSequential(new StraightDriveWithEncoders(speed,-distance,3));
        this.addSequential(new StraightDriveWithCamera(0.6,25,3));
        this.addSequential(new StraightDriveWithTime(0.6,1500));
    }
}
