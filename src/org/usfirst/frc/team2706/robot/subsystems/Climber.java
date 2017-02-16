package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.RobotMap;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Coordinates commands for the climber mechanism.
 * 
 * @author wakandacat, FilledWithDetermination, Crazycat200
 */
public class Climber extends Subsystem {
  
    private SpeedController motor = new Talon(RobotMap.CLIMBER_MOTOR);

    public void initDefaultCommand() {}
    
    public void climb() {
        motor.set(0.5);
    }
    
    public void stop() {
        motor.set(0.0);
    }   
}

