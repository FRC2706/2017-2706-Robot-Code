package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climb extends Command {

    protected void execute() {
        Robot.climber.Climb();
    }

    @Override
    protected boolean isFinished() {
        // sfinished is always false because we are using EJoystickButton.whileHeld 
        // which will call end() when the button is released
        return false;
    }

    protected void end() {
        Robot.climber.Stop();
    }
}
