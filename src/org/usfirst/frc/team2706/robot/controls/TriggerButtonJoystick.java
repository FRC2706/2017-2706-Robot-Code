package org.usfirst.frc.team2706.robot.controls;

import org.usfirst.frc.team2706.robot.EJoystickButton;

import edu.wpi.first.wpilibj.Joystick;

public class TriggerButtonJoystick extends EJoystickButton {

    private Joystick joystick;
    private int axis;
    
    public TriggerButtonJoystick(Joystick joystick, int axis) {
        super(joystick, axis);
        
        this.joystick = joystick;
        this.axis = axis;
    }
    
    public boolean get() {
        if (joystick.getRawAxis(axis) > 0) {
            return true;
        }
        return false;
    }
}
