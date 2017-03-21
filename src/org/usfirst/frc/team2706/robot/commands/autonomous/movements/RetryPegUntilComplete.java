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

    public RetryPegUntilComplete() {
        requires(Robot.driveTrain);
    }

    protected void initialize() {
        r = new Realign(0.6, 1.5);
    }

    Realign r = new Realign(0.6, 1.5);

    protected void execute() {
        if (!r.isRunning() && !Robot.gearHandler.pegDetected()) {
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
        return Robot.gearHandler.pegDetected() && !r.isRunning();
    }

}
