package org.usfirst.frc.team2706.robot.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;

public class TriggerButtonJoystick extends Button {

    private Joystick joystick;
    private int axis;
    
    public TriggerButtonJoystick(Joystick joystick, int axis) {
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
