package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team2706.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Have the robot drive certain amount of time
 */
public class StraightDriveWithTime extends Command {

    private final double speed;

    private final long time;

    /**
     * Drive at a specific speed for a certain amount of time
     * 
     * @param speed Speed in range [-1,1]
     * @param time Time in milliseconds to drive
     */
    public StraightDriveWithTime(double speed, long time) {
        super("StraightDriveWithTime");
        requires(Robot.driveTrain);

        this.speed = -speed;

        this.time = time;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        // Creates task to stop robot after time
        CommandTimerTask interrupt = new CommandTimerTask();
        new Timer().schedule(interrupt, time);

        done = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        // Drive while command is running
        Robot.driveTrain.drive(speed, speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }

    public boolean done;

    class CommandTimerTask extends TimerTask {

        public void run() {
            done = true;
        }
    }
}
