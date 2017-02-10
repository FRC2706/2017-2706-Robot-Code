package org.usfirst.frc.team2706.robot.commands.autonomous.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.QuickRotate;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.plays.DrivePlaceGear;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterToLaunch extends CommandGroup {

    /**
     * Gets center gear, then heads over to the other team's launch area
     * 
     * @param rightSide Are we going right or left?
     * @param speed What speeed are we traveling at?
     * @param gearDistance How far away is the gear from our starting position
     * @param reverseDistance How far back are we going after placing the gear?
     * @param turnAmount How much are we turning to not hit the airship?
     * @param sideDistance How far are we going while angled?
     * @param toLaunchPad How far is it to the launch pad from our location?
     */
    public CenterToLaunch(boolean rightSide, double speed, double gearDistance,
                    double reverseDistance, double turnAmount, double sideDistance,
                    double toLaunchPad) {
        this.addSequential(new DrivePlaceGear(speed, gearDistance, reverseDistance));
        this.addSequential(new QuickRotate(rightSide ? turnAmount : -turnAmount));
        this.addSequential(new StraightDriveWithEncoders(speed, sideDistance, 25));
        this.addSequential(new QuickRotate(rightSide ? -turnAmount : turnAmount));
        this.addSequential(new StraightDriveWithEncoders(speed, toLaunchPad, 25));
    }
}
