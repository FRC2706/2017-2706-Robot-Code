package org.usfirst.frc.team2706.robot.commands.autonomous.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.DriveWhileWaiting;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.RotateDriveWithGyro;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithCamera;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.plays.DrivePlaceGear;
import org.usfirst.frc.team2706.robot.commands.mechanismcontrol.CloseGearMechanism;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SideStartSideGear extends CommandGroup {

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
    public SideStartSideGear(boolean rightSide, double speed, double fromWallDistance,
                    double turnDegrees, double toGearDistance, double reverseDistance,
                    double toLaunchPadDistance) {
        this.addSequential(new CloseGearMechanism());
        this.addSequential(new StraightDriveWithEncoders(speed, fromWallDistance, 0.2, 5),4);
        this.addSequential(new StraightDriveWithTime(0.0,300));
        this.addSequential(new RotateDriveWithGyro(0.6,rightSide ? -turnDegrees : turnDegrees,7),4);
        this.addSequential(new StraightDriveWithCamera(0.6, 25, 3),4);
        this.addSequential(new DriveWhileWaiting(0.5));
        this.addSequential(new DrivePlaceGear(0.6, 0, reverseDistance));
        this.addSequential(new StraightDriveWithTime(0.0,300));
        this.addSequential(new RotateDriveWithGyro(0.6,rightSide ? turnDegrees : -turnDegrees,7),4);
        this.addSequential(new StraightDriveWithEncoders(speed, toLaunchPadDistance, 2, 1));
    }
}
