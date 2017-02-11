package org.usfirst.frc.team2706.robot.commands.autonomous.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.QuickRotate;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.plays.DrivePlaceGear;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SideStartSideGear extends CommandGroup {

    /**
     * Start at the side of the field, grab the side gear and head on out to the launch pad
     * 
     * @param rightSide Are we starting on the right or left?
     * @param speed What speeed are we travelling at?
     * @param fromWallDistance How far from the wall are we traveling?
     * @param toGearDistance How far is it from our position to the gear?
     * @param turnDegrees How far do we turn to face the gear?
     * @param reverseDistance How far back do we go after placing the gear?
     * @param toLaunchPadDistance How far is it to the launch pad from our location?
     */
    public SideStartSideGear(boolean rightSide, double speed, double fromWallDistance,
                    double toGearDistance, double turnDegrees, double reverseDistance,
                    double toLaunchPadDistance) {
        this.addSequential(new StraightDriveWithEncoders(speed, fromWallDistance, 25));
        this.addSequential(new QuickRotate(rightSide ? -turnDegrees : turnDegrees));
        this.addSequential(new DrivePlaceGear(speed, toGearDistance, reverseDistance));
        this.addSequential(new QuickRotate(rightSide ? turnDegrees : -turnDegrees));
        this.addSequential(new StraightDriveWithEncoders(speed, toLaunchPadDistance, 25));
    }
}
