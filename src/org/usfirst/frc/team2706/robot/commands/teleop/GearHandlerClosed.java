package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;

public class GearHandlerClosed extends Command {

    //    private DoubleSolenoid solenoid = new DoubleSolenoid(4,5);
    //    private Joystick stick ;
    //    private boolean closed;

    public GearHandlerClosed() {
        //  stick = Robot.oi.getDriverJoystick();
        //        this.closed = closed;
    }

    protected void execute(){
        Robot.gearHandler.CloseArm();
    }
    @Override
    protected boolean isFinished() {
        return true;
    }   
}
