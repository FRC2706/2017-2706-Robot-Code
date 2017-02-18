package org.usfirst.frc.team2706.robot;

import java.lang.reflect.Field;

import org.usfirst.frc.team2706.robot.commands.autonomous.movements.CurveDrive;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.RotateDriveWithGyroDistanceSensorHybrid;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands
 * and command groups that allow control of the robot.
 */
public class OI {

    // Joystick for driving the robot around
    private final Joystick driverStick;

    // Joystick for controlling the mechanisms of the robot
    private final Joystick controlStick;

    public Joystick getDriverJoystick() {
        return driverStick;
    }

    public Joystick getOperatorJoystick() {
        return controlStick;
    }

    /**
     * Initializes Oi using the two default real joysticks
     */
    public OI() {
        this(new Joystick(0), new Joystick(1));
    }

    /**
     * Initializes Oi with non-default joysticks
     * 
     * @param driverStick The driver joystick to use
     * @param controlStick The operator joystick to use
     */
    public OI(Joystick driverStick, Joystick controlStick) {
        // Joystick for driving the robot around
        this.driverStick = driverStick;

        EJoystickButton backLeftButton = new EJoystickButton(driverStick, 5);
        backLeftButton.runWhileHeld(new CurveDrive(2,3,45,0.45));
       // backLeftButton.whenReleased();
        // Joystick for controlling the mechanisms of the robot
        this.controlStick = controlStick;
    }

    /**
     * Removes ButtonSchedulers that run commands that were added in Oi
     */
    public void destroy() {
        try {
            Field f = Scheduler.getInstance().getClass().getDeclaredField("m_buttons");
            f.setAccessible(true);
            f.set(Scheduler.getInstance(), null);
            f.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }
}

