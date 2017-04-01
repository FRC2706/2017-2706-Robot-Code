package org.usfirst.frc.team2706.robot.commands.autonomous.automodes;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithCamera;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.WaitForSensor;
import org.usfirst.frc.team2706.robot.commands.mechanismcontrol.CloseGearMechanism;
import org.usfirst.frc.team2706.robot.commands.mechanismcontrol.OpenGearMechanism;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class VisionCenterPeg extends CommandGroup {

    /**
     * Place the gear and then back up after
     * 
     * @param speed What speed are we placing the gear at?
     * @param distance How far away is the gear?
     * @param reverseDistance How far in reverse do we want to go after placing the gear?
     */
    public VisionCenterPeg(double speed, double distance, double reverseDistance) {
        this.addSequential(new StraightDriveWithEncoders(0.7, 2, 3, 1));
        this.addSequential(new StraightDriveWithCamera(0.53, 25, 3), 6);
        this.addSequential(new StraightDriveWithTime(0.6, 1200));
        // this.addSequential(new RetryPegUntilComplete());
        // this.addSequential(new StraightDriveWithTime(0.65, 500));
        this.addSequential(new WaitForSensor(10));
        this.addSequential(new OpenGearMechanism());
        this.addSequential(new StraightDriveWithTime(0.65, 500));
        this.addSequential(new StraightDriveWithEncoders(-speed, -reverseDistance, 5, 1));
        this.addSequential(new CloseGearMechanism());
    }
}
