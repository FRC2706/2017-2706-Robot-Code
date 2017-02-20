package org.usfirst.frc.team2706.robot.commands.autonomous.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.CurveDrive;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.QuickRotate;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SideGearCurve extends CommandGroup {

    /**
     * Start at the side of the field, grab the side gear and head on out to the launch pad
     * 
     * @param rightSide Are we starting on the right or left?
     * @param speed What speeed are we travelling at?
     * @param fromWallDistance How far from the wall are we traveling?
     * @param turnDegrees How far do we turn to face the gear?
     * @param toGearDistance How far is it from our position to the gear?
     * @param reverseDistance How far back do we go after placing the gear?
     * @param toLaunchPadDistance How far is it to the launch pad from our location?
     */
    public SideGearCurve(double speed, double xCurve, double yCurve, double endAngle,
                    double reverseDistance,
                    double toLaunchPadDistance) {
        this.addSequential(new CurveDrive(xCurve,yCurve,endAngle,speed));
        this.addSequential(new StraightDriveWithEncoders(0.45,0,0.2));
        this.addSequential(new StraightDriveWithEncoders(-0.5, -4, 0.2));
        this.addSequential(new QuickRotate(-endAngle));
        this.addSequential(new StraightDriveWithEncoders(0.5,toLaunchPadDistance,0.2));
    }
}
