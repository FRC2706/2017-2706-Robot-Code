package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Waits for the IR sensor and the distance sensors to both be good for the gear handler.
 * 
 * @author Connor A.
 *
 */
public class RetryPegUntilComplete extends Command {

    public final double distance;

    public RetryPegUntilComplete(double distance) {
        requires(Robot.driveTrain);
        this.distance = distance;
    }

    protected void initialize() {}

    Realign r = new Realign(0.5, 1.5);

    protected void execute() {
        if (!r.isRunning() && (!Robot.gearHandler.pegDetected()
                        || Robot.driveTrain.getDistanceToObstacle() > distance)) {
            r.start();
        } else {
        }
    }

    protected void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return Robot.gearHandler.pegDetected()
                        && Robot.driveTrain.getDistanceToObstacle() < distance && !r.isRunning();
    }

}
