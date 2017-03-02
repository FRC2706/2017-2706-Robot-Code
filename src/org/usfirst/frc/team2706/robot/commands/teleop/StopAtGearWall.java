package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

@SuppressWarnings("unused")
public class StopAtGearWall extends Command {

    private final double stopRange;

    private double distance;
    public StopAtGearWall(double stopRange) {
        this.stopRange = stopRange;
    }

    protected void initialize() {
        t = new TakeOverCommandBreak();
        distance = Robot.driveTrain.getDistanceToObstacle();
        Robot.driveTrain.resetEncoders();
    }
    TakeOverCommandBreak t;
    protected void execute() {
       
        
    }
    protected void end() {
        t.cancel();
    }

    protected void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

}
