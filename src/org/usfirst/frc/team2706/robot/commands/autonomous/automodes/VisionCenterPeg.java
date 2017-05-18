package org.usfirst.frc.team2706.robot.commands.autonomous.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.DriveWhileWaiting;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithPeg;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;
import org.usfirst.frc.team2706.robot.commands.mechanismcontrol.CloseGearMechanism;
import org.usfirst.frc.team2706.robot.commands.mechanismcontrol.OpenGearMechanism;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Place the gear and then back up after
 */
public class VisionCenterPeg extends CommandGroup {

    /**
     * Place the gear and then back up after
     * 
     * @param speed What speed are we placing the gear at?
     * @param distance How far away is the gear?
     * @param reverseDistance How far in reverse do we want to go after placing the gear?
     */
    public VisionCenterPeg(double speed, double distance, double reverseDistance, boolean slow) {
        if(slow) {
            //   this.addSequential(new StraightDriveWithEncoders(0.7, 5, 5, 1));
            //this.addSequential(new StraightDriveWithCamera(0.53, 25, 3), 6);
            this.addSequential(new CloseGearMechanism());
            this.addSequential(new StraightDriveWithPeg(0.5,20,2,1));
            // this.addSequential(new RetryPegUntilComplete());
            this.addSequential(new DriveWhileWaiting(0.5));
             this.addSequential(new StraightDriveWithTime(0.5, 500));
            this.addSequential(new OpenGearMechanism());
            this.addSequential(new StraightDriveWithTime(0.5, 500));
            this.addSequential(new StraightDriveWithTime(-0.5, 750));
            this.addSequential(new CloseGearMechanism());
        }
        else {
            //   this.addSequential(new StraightDriveWithEncoders(0.7, 5, 5, 1));
            //this.addSequential(new StraightDriveWithCamera(0.53, 25, 3), 6);
            this.addSequential(new CloseGearMechanism());
            this.addSequential(new StraightDriveWithPeg(0.55,20,2,1));
            // this.addSequential(new RetryPegUntilComplete());
            this.addSequential(new DriveWhileWaiting(0.55));
             this.addSequential(new StraightDriveWithTime(0.65, 500));
            this.addSequential(new OpenGearMechanism());
            this.addSequential(new StraightDriveWithTime(0.65, 500));
            this.addSequential(new StraightDriveWithEncoders(-speed, -reverseDistance, 5, 1));
            this.addSequential(new CloseGearMechanism());
        }
    }
}
