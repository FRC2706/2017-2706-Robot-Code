package org.usfirst.frc.team2706.robot.controls;

import org.usfirst.frc.team2706.robot.EJoystickButton;

import edu.wpi.first.wpilibj.Joystick;

/**
 * JoystickButton for an analog axis
 */
public class TriggerButtonJoystick extends EJoystickButton {

    private Joystick joystick;
    private int axis;

    /**
     * Sets up JoystickButton with joystick and axis
     * 
     * @param joystick The joystick the analog axis is on
     * @param axis The axis number
     */
    public TriggerButtonJoystick(Joystick joystick, int axis) {
        super(joystick, axis);

        this.joystick = joystick;
        this.axis = axis;
    }

    /**
     * Gets whether the axis is greater than 0
     * 
     * @return Pressed or not
     */
    public boolean get() {
        if (joystick.getRawAxis(axis) > 0) {
            return true;
        }
        return false;
    }
}
