package org.usfirst.frc.team2706.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {

    private SpeedController motor = new Talon(6);
    private boolean finished = false;


    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void Climb() {
        motor.set(0.5);
    }
    
    public void Stop() {
        motor.set(0.0);
    }
    
}

