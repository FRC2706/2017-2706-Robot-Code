package org.usfirst.frc.team2706.robot;

import java.lang.reflect.Field;

import org.usfirst.frc.team2706.robot.bling.ToggleFlashiness;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.RotateDriveWithGyro;
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

        // Stop driving and go into brake mode, stopping the robot
        TriggerButtonJoystick driverBackLeftTrigger = new TriggerButtonJoystick(driverStick, 2);
        driverBackLeftTrigger.runWhileHeld(new HandBrake(true));

        // Stop the robot by going into brake mode
        TriggerButtonJoystick driverBackRightTrigger = new TriggerButtonJoystick(driverStick, 3);
        driverBackRightTrigger.runWhileHeld(new HandBrake(false));

        // Will stop the robot as it approaches the gear wall
        EJoystickButton driverBackRightButton = new EJoystickButton(driverStick, 6);
        driverBackRightButton.runWhileHeld(new StopAtGearWall(14, 40));

        // Hold to align with the peg when within a couple feet of the peg
        EJoystickButton driverYButton = new EJoystickButton(driverStick, 4);
               
        driverYButton.runWhileHeld(new StraightDriveWithCamera(0.5, 0, 12));

        // Joystick for controlling the mechanisms of the robot
        this.controlStick = controlStick;

        // Climb at the speed the analog trigger is pressed
        TriggerButtonJoystick operatorBackRightTrigger = new TriggerButtonJoystick(controlStick, 3);
        operatorBackRightTrigger.whenPressed(new ClimbVariableManually());

        // Runs a motor at a set speed to make the robot climb the rope
        EJoystickButton operatorAButton = new EJoystickButton(controlStick, 1);
        operatorAButton.runWhileHeld(new ClimbManually());

        // Closes gear holder mechanism so holder can hold gears
        EJoystickButton operatorBButton = new EJoystickButton(controlStick, 2);
        operatorBButton.whenPressed(new CloseGearMechanism());

        // Opens gear holder mechanism for when peg is in
        EJoystickButton operatorYButton = new EJoystickButton(controlStick, 4);
        operatorYButton.whenPressed(new OpenGearMechanism());
        
        // This will toggle whether or not we have flashy patterns on the LED strips
        EJoystickButton displayButton = new EJoystickButton(controlStick, 7);
        displayButton.whenPressed(new ToggleFlashiness());
        
        EJoystickButton rotateButton = new EJoystickButton(controlStick, 3);
        rotateButton.runWhileHeld(new RotateDriveWithGyro(0.5, 90));
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
            Log.e("Oi", "Error occured destroying m_buttons", e);
        }
    }
}

