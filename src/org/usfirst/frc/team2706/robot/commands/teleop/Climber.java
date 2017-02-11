package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Command {

    private SpeedController motor = new Talon(6);
    private Joystick stick ;
    private boolean finished = false;

    public Climber() {
//      stick = Robot.oi.getDriverJoystick();
    }

    protected void execute(){
//      if (stick.getRawButton(1)){
            motor.set(0.5);
//      } else {
//          motor.set(0.0);
//      }
    //  finished = true;
    }

    @Override
    protected boolean isFinished() {
        return finished;
    }
    
    protected void end(){
        motor.set(0.0);
    }
}
