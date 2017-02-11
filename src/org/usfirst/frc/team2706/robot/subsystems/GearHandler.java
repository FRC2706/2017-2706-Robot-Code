package org.usfirst.frc.team2706.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/** 
 * This is a subsystem for the gear handler arm thing
 */
public class GearHandler extends Subsystem {

    private DoubleSolenoid solenoid = new DoubleSolenoid(4,5);
    
    // Let's use this to keep track of whether the arm is closed :)
    private boolean closed = true;

    public void initDefaultCommand() {
    }
    
    public void OpenArm(){
        solenoid.set(DoubleSolenoid.Value.kForward);
        closed = false;
    }
    
    public void CloseArm(){
        solenoid.set(DoubleSolenoid.Value.kReverse); 
        closed = true;
    }
    
    public void ToggleArm(){
        if (closed){
            OpenArm();
        } else {
            CloseArm();
        }
    }
}

