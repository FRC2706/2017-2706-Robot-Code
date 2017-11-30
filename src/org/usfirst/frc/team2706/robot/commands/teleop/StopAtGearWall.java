package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

// TODO: Don't use EncodersNoReturn
/**
 * Lets the driver control the robot, until the robot is close enough to the wall, and stops it with
 * PIDs
 */
public class StopAtGearWall extends Command {

    private final double stopRange;

    private final double encoderTakeOverRange;

    private double distance;

    private boolean part = true;

    /**
     * Stops at the wall to pick up a gear
     * 
     * @param stopRange the range at which you should stop
     * @param encoderTakeOverRange the range at which the encoders take over the distance sensors.
     */
    public StopAtGearWall(double stopRange, double encoderTakeOverRange) {
        this.stopRange = stopRange;
        this.encoderTakeOverRange = encoderTakeOverRange;
    }

    protected void initialize() {
        t = new EncodersNoReturn();
        distance = Robot.driveTrain.getDistanceToObstacle();
        Robot.driveTrain.resetEncoders();
    }

    private EncodersNoReturn t;

    protected void execute() {
        if (part) {
            part = PartOne();
        }
    }

    private boolean doneOne = false;

    /**
     * Gets the distance sensor distance and sees if the encoders should take over
     */
    public boolean PartOne() {
        distance = Robot.driveTrain.getDistanceToObstacle();
        Log.d("StopAtGearWall", "Distance from the wall is " + distance);
        if (distance <= encoderTakeOverRange) {
            doneOne = true;
            distance = Robot.driveTrain.getDistanceToObstacle() - stopRange;
            Robot.driveTrain.resetEncoders();
            return false;
        }
        return true;
    }

    protected void end() {
        part = true;
        Log.d("StopAtGearWall", "Ended command");
        q = false;
        doneOne = false;
        t.cancel();
    }

    protected void interrupted() {
        end();
    }

    private boolean q = false;

    @Override
    protected boolean isFinished() {
        Log.d("StopAtGearWall", "CurrentDistance:" + Robot.driveTrain.getDistance() * 12.0 + ", "
                        + "DesiredDistance:" + distance);
        if (Robot.driveTrain.getDistance() * 12.0 >= distance && !q && doneOne) {
            q = true;
            t.start();
        }
        // if(q) {
        // i++;
        //// if(i > 5) {
        // return true;
        // }
        // }
        return false;
    }

}
