package org.usfirst.frc.team2706.robot.controls;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2706.robot.Robot;

import org.usfirst.frc.team2706.robot.Robot;

public class StickRumble extends Command {

    protected Joystick joystick;

    protected Joystick operatorJoy;

    protected double startTime;

    protected double timePassed;

    public double timeOn;

    public double timeOff;

    public int repeatCount;

    public boolean finished;

    /**
     * Simple function for setting vibration on the controller.
     * 
     * @param timeOn : The time for which the controller will vibrate in seconds
     * @param timeOff : The delay time between when the controller is vibrating
     * @param repeatCount : The number of times to repeat the rumble on - rumble off pattern.
     */
    public StickRumble(double timeOn, double timeOff, int repeatCount) {

        joystick = Robot.oi.getDriverJoystick();
        operatorJoy = Robot.oi.getOperatorJoystick();
        finished = false;

        // Need to know this to get time passed.
        startTime = Timer.getMatchTime();
        this.timeOn = timeOn;
        this.timeOff = timeOff;
        this.repeatCount = repeatCount;
        finished = false;
        this.repeatCount -= 1;
        rumbleAll(true);
    }

    /**
     * Simply to make it easier to rumble certain sticks.
     * 
     * @param on : Determines whether we're turning the rumble on or off.
     */
    public void rumbleAll(boolean on) {

        if (on) {
            joystick.setRumble(RumbleType.kRightRumble, 1.0);
            joystick.setRumble(RumbleType.kLeftRumble, 1.0);
            operatorJoy.setRumble(RumbleType.kLeftRumble, 1.0);
            operatorJoy.setRumble(RumbleType.kRightRumble, 1.0);
        } else {
            joystick.setRumble(RumbleType.kRightRumble, 0.0);
            joystick.setRumble(RumbleType.kLeftRumble, 0.0);
            operatorJoy.setRumble(RumbleType.kLeftRumble, 0.0);
            operatorJoy.setRumble(RumbleType.kRightRumble, 0.0);
        }
    }

    public void execute() {

        timePassed = Timer.getMatchTime() - startTime; // Get the time passes since the start.

        // Turn on the rumble if the no rumble time plus the rumble time has been passed.
        if (timePassed / (timeOn + timeOff) > (timeOn + timeOff)) {
            rumbleAll(true);
            repeatCount -= 1; // Subtract from this to eventually get to 0.
            startTime += timePassed; // Add on to the start time to get
        } else if (timePassed / timeOn > timeOn) { // In other words, if we have surpassed rumble On
                                                   // time.
            rumbleAll(false);
        }
    }

    /**
     * Determines if the command is finished yet. Used by the scheduler to delete the command from
     * the schedule.
     */
    public boolean isFinished() {

        if (repeatCount <= 0 | finished == true) { // If on is set to off anywhere (false) then we
                                                   // should quit.
            finished = true;
        }
        return finished;
    }

}
