package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/** 
 * Coordinates commands for the Gear Handler Arm mechanisms. 
 * 
 * @author wakandacat, FilledWithDetermination, Crazycat200
 */
public class GearHandler extends Subsystem {

    private DoubleSolenoid solenoid = new DoubleSolenoid(RobotMap.SOLENOID_FORWARD_CHANNEL, RobotMap.SOLENOID_REVERSE_CHANNEL);
    
    // Let's use this to keep track of whether the arm is closed :)
    private boolean closed = true;

    public void initDefaultCommand() {
    }
    
    public void openArm() {
        solenoid.set(DoubleSolenoid.Value.kForward);
        closed = false;
    }
    
    public void closeArm() {
        solenoid.set(DoubleSolenoid.Value.kReverse); 
        closed = true;
    }
    
    public void toggleArm() {
        if (closed) {
            openArm();
        } else {
            closeArm();
        }
    }
}

