package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class StopAtGearWall extends Command {

    private final double stopRange;

    private final double encoderTakeOverRange;
    
    private double distance;

    boolean part = true;
    public StopAtGearWall(double stopRange, double encoderTakeOverRange) {
        this.stopRange = stopRange;
        this.encoderTakeOverRange = encoderTakeOverRange;
    }

    protected void initialize() {
        t =  new EncodersNoReturn();
        distance = Robot.driveTrain.getDistanceToObstacle();
       Robot.driveTrain.resetEncoders();
    }

    EncodersNoReturn t;

    protected void execute() {
        if(part) {
            part = PartOne();
        }
    }
    boolean doneOne = false;
    public boolean PartOne() {
        distance = Robot.driveTrain.getDistanceToObstacle();
        System.out.println(distance);
        if(distance <= encoderTakeOverRange) {
            doneOne = true;
            distance = Robot.driveTrain.getDistanceToObstacle() - stopRange;
            Robot.driveTrain.resetEncoders();
            return false;
        }
        return true;
    }
    protected void end() {
        part = true;
        System.out.println("m");
        q = false;
        doneOne = false;
       t.cancel();
    }

    protected void interrupted() {
        end();
    }
int i = 0;
boolean q = false;
    @Override
    protected boolean isFinished() {
        System.out.println(Robot.driveTrain.getDistance() * 12.0 + "," + distance);
 if(Robot.driveTrain.getDistance() * 12.0 >= distance && !q && doneOne) { 
     q = true;
     t.start();
        }
 //if(q) {
  //   i++;
   ////  if(i > 5) {
    //     return true;
   //  }
// }
 return false;
    }

}
