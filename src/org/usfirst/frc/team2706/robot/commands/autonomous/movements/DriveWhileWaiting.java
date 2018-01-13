package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Waits for the IR sensor and the distance sensors to both be good for the gear handler.
 * 
 * @author cnnr2
 *
 */
public class DriveWhileWaiting extends Command {
    
    /**
     * The speed to drive at
     */
    public final double speed;

    /**
     * Drives while waiting to have peg
     * 
     * @param speed The speed to drive at
     */
    public DriveWhileWaiting(double speed) {
        this.speed = speed;
    }

    protected void initialize() {
        Robot.driveTrain.reset();
    }

    protected void execute() {
        if (!Robot.gearHandler.pegDetected()) {
            Robot.driveTrain.arcadeDrive(-speed,
                            Robot.driveTrain.normalize(-Robot.driveTrain.getHeading()) / 10);
        }
    }

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return Robot.gearHandler.pegDetected();
    }

}
