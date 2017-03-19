package org.usfirst.frc.team2706.robot.commands.autonomous.plays;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.WaitForSensor;
import org.usfirst.frc.team2706.robot.commands.mechanismcontrol.CloseGearMechanism;
import org.usfirst.frc.team2706.robot.commands.mechanismcontrol.OpenGearMechanism;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DrivePlaceGear extends CommandGroup {

    /**
     * Place the gear and then back up after
     * 
     * @param speed What speed are we placing the gear at?
     * @param distance How far away is the gear?
     * @param reverseDistance How far in reverse do we want to go after placing the gear?
     */
    public DrivePlaceGear(double speed, double distance, double reverseDistance) {
        requires(Robot.driveTrain);
        if (distance != 0) {
            // Adds a movement one after another instead of at the same time
            this.addSequential(new StraightDriveWithEncoders(speed, distance, 0.1),6);
        }
        
        this.addSequential(new WaitForSensor(10));
        this.addSequential(new OpenGearMechanism());
        this.addSequential(new StraightDriveWithTime(0.5, 1000));
        this.addSequential(new StraightDriveWithEncoders(-speed, -reverseDistance, 5));
        this.addSequential(new CloseGearMechanism());
    }
}
