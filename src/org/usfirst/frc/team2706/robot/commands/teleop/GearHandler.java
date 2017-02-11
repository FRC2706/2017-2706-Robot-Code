package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;

public class GearHandler extends Command {

    private DoubleSolenoid solenoid = new DoubleSolenoid(4,5);
    private Joystick stick ;
    private boolean closed;
    
    public GearHandler(boolean closed) {
        stick = Robot.oi.getDriverJoystick();
        this.closed = closed;
    }
    
    protected void execute(){
        if (stick.getRawButton(2)){
            if (closed == true){
                solenoid.set(DoubleSolenoid.Value.kReverse);
            }else {
                solenoid.set(DoubleSolenoid.Value.kForward);
            }
        }
    }
    @Override
    protected boolean isFinished() {
        return true;
    }   
}
