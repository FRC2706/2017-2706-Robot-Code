package org.usfirst.frc.team2706.robot.commands.autonomous.plays;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.mechanismcontrol.GearMechanism;

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
        // Adds a movement one after another instead of at the same time
        this.addSequential(new StraightDriveWithEncoders(speed, distance, 25, 5));
        this.addSequential(new GearMechanism());
        this.addSequential(new StraightDriveWithEncoders(-speed, -reverseDistance, 25, 5));
    }
}
