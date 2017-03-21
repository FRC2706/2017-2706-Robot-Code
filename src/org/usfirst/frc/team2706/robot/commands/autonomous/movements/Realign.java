package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Realign extends CommandGroup {
    public Realign(double speed, double distance) {
        requires(Robot.driveTrain);
        this.addSequential(new StraightDriveWithEncoders(speed,-distance,0.1));
        this.addSequential(new RotateDriveWithGyroDistanceSensorHybrid(0.45));
        this.addSequential(new StraightDriveWithTime(0.6,1500));
    }
}
