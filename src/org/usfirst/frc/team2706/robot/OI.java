package org.usfirst.frc.team2706.robot;

import java.lang.reflect.Field;

import org.usfirst.frc.team2706.robot.commands.GetTargets;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithCamera;
import org.usfirst.frc.team2706.robot.commands.mechanismcontrol.CloseGearMechanism;
import org.usfirst.frc.team2706.robot.commands.mechanismcontrol.OpenGearMechanism;
import org.usfirst.frc.team2706.robot.commands.teleop.ClimbManually;
import org.usfirst.frc.team2706.robot.commands.teleop.ClimbVariableManually;
import org.usfirst.frc.team2706.robot.commands.teleop.HandBrake;
import org.usfirst.frc.team2706.robot.commands.teleop.StopAtGearWall;
import org.usfirst.frc.team2706.robot.controls.TriggerButtonJoystick;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands
 * and command groups that allow control of the robot.
 */
// Operator Interface
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

        // Stop driving and go into breakmode, stopping the robot
        EJoystickButton backLeftButton = new EJoystickButton(driverStick, 5);
        backLeftButton.runWhileHeld(new HandBrake(true));

        // Stop the robot by going into break mode
        EJoystickButton backRightButton = new EJoystickButton(driverStick, 6);
        backRightButton.runWhileHeld(new HandBrake(false));
        
        EJoystickButton xButton = new EJoystickButton(driverStick,3);
        xButton.runWhileHeld(new StopAtGearWall(14,40));

        EJoystickButton y = new EJoystickButton(driverStick,4);
        y.runWhileHeld(new StraightDriveWithCamera(0.5,0,12));
        // test the camera integration
        EJoystickButton cameraButton = new EJoystickButton(driverStick, 7);
        cameraButton.whenPressed(new GetTargets());
        
        
        
        // Joystick for controlling the mechanisms of the robot
        this.controlStick = controlStick;
        
        TriggerButtonJoystick climberTrigger = new TriggerButtonJoystick(controlStick, 3);
        climberTrigger.whenPressed(new ClimbVariableManually());

        // Runs a motor at a set speed to make the robot climb the rope
        EJoystickButton aButton = new EJoystickButton(controlStick, 1);
        aButton.runWhileHeld(new ClimbManually());

        // Closes gear holder mechanism so holder can hold gears
        EJoystickButton bButton = new EJoystickButton(controlStick, 2);
        bButton.whenPressed(new CloseGearMechanism());

        // Opens gear holder mechanism for when peg is in
        EJoystickButton yButton = new EJoystickButton(controlStick, 4);
        yButton.whenPressed(new OpenGearMechanism());
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

