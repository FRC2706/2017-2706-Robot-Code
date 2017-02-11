package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climb extends Command {

    
    private boolean finished = false;

    public Climb() {
    }

    protected void execute(){
            Robot.climber.Climb();
    }

    @Override
    protected boolean isFinished() {
        return finished;
    }
    
    protected void end(){
        Robot.climber.Stop();
    }
}
