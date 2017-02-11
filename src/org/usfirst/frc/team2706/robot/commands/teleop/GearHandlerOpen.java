package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class GearHandlerOpen extends Command {

    public GearHandlerOpen() {
        //  stick = Robot.oi.getDriverJoystick();
        // this.closed = closed;
    }

    protected void execute(){
        Robot.gearHandler.OpenArm();
    }
    @Override
    protected boolean isFinished() {
        return true;
    }   
}

