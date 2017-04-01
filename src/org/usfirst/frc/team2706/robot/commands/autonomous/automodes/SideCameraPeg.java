package org.usfirst.frc.team2706.robot.commands.autonomous.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.CurveDrive;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithCamera;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.plays.DrivePlaceGear;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SideCameraPeg extends CommandGroup {

    /**
     * Start at the side of the field, grab the side gear and head on out to the launch pad
     * 
     * @param speed What speeed are we travelling at?
     * @param xCurve How far right do we go on the turn?
     * @param yCurve How far forward to we go on the turn?
     * @param endAngle What angle do we turn at to face the gear handler?
     * @param reverseDistance How far back do we go after placing the gear?
     * @param toLaunchPadDistance How far is it to the launch pad from our location?
     * @param isRight are we doing this from the right or from the left
     */
    public SideCameraPeg(double speed, double xCurve, double yCurve, double endAngle,
                    double reverseDistance, double toLaunchPadDistance, boolean isRight) {
        this.addSequential(new CurveDrive(xCurve, yCurve, endAngle, speed, isRight));
        this.addSequential(new StraightDriveWithTime(0.0,500));
        this.addSequential(new StraightDriveWithCamera(0.6, 25, 2));
     //   this.addSequential(new RotateDriveWithGyroDistanceSensorHybrid(0.4));
        this.addSequential(new StraightDriveWithTime(0.55, 1500));
        // this.addSequential(new RetryPegUntilComplete(10));
        this.addSequential(new DrivePlaceGear(0.55, 0, 4));
        this.addSequential(new RotateDriveWithGyro(0.6, -55, 1));
        this.addSequential(new StraightDriveWithTime(0.0, 300));
        this.addSequential(new StraightDriveWithEncoders(0.65, toLaunchPadDistance, 0.2, 1));
    }
}
