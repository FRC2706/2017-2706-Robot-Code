package org.usfirst.frc.team2706.robot.controls;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
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

    public static boolean on = false;

    public static double vibrationIntensity;

    public static double intervalTime;

    public static int intervalCount;

    public static int repeatCountCopy;

    public static boolean intervalOn = false; // Used to determine if we are in the interval time
                                              // period

    /**
     * Simple function for setting vibration on the controller.
     * 
     * @param timeOn : The time for which the controller will vibrate in seconds
     * @param timeOff : The delay time between when the controller is vibrating
     * @param repeatCount : The number of times to repeat the rumble on - rumble off pattern.
     * @param intervalTime : The time in seconds between the patterns of vibrations.
     * @param intervalCount : The number of times to repeat the pattern sets.
     * @param intensity : How much the controllers will vibrate.
     */
    public StickRumble(double timeOn, double timeOff, int repeatCount, double intervalTime,
                    int intervalCount, double intensity) {

        joystick = Robot.oi.getDriverJoystick();
        operatorJoy = Robot.oi.getOperatorJoystick();
        finished = false;

        // Need to know this to get time passed.
        this.timeOn = timeOn;
        this.timeOff = timeOff;
        this.repeatCount = repeatCount;
        this.repeatCountCopy = repeatCount;
        this.intervalTime = intervalTime;
        this.intervalCount = intervalCount;
        vibrationIntensity = intensity;
        finished = false;
        startTime = Timer.getFPGATimestamp();

    }

    /**
     * Simply to make it easier to rumble certain sticks.
     * 
     * @param on : Determines whether we're turning the rumble on or off.
     */
    public void rumbleAll(boolean on) {

        if (on) {
            joystick.setRumble(RumbleType.kRightRumble, vibrationIntensity);
            joystick.setRumble(RumbleType.kLeftRumble, vibrationIntensity);
            operatorJoy.setRumble(RumbleType.kLeftRumble, vibrationIntensity);
            operatorJoy.setRumble(RumbleType.kRightRumble, vibrationIntensity);
        } else {
            joystick.setRumble(RumbleType.kRightRumble, 0.0);
            joystick.setRumble(RumbleType.kLeftRumble, 0.0);
            operatorJoy.setRumble(RumbleType.kLeftRumble, 0.0);
            operatorJoy.setRumble(RumbleType.kRightRumble, 0.0);
        }
    }

    @Override
    public void execute() {


        timePassed = Timer.getFPGATimestamp() - startTime; // Get the time passes since the start.


        if (repeatCount < 0 && !intervalOn) {

            intervalOn = true;
            rumbleAll(false);
        }

        if (intervalOn && timePassed >= intervalTime) {

            intervalCount -= 1;
            repeatCount = repeatCountCopy; // Reset the repeat count.
            intervalOn = false;
            on = false;

        }

        // Turn on the rumble when it needs to be turned on.
        if (((timePassed / (timeOn + timeOff)) >= 1) | (timePassed < timeOn) && !on
                        && !intervalOn) {

            on = true;
            repeatCount -= 1; // Subtract from this to eventually get to 0.
            if (repeatCount >= 0)
                rumbleAll(true);
            startTime += timePassed; // Need to add to startTime so timePassed is 0 again.

        }

        /*
         * In other words, if we have surpassed rumble On time, turn off the rumble
         */
        else if (((timePassed / timeOn) >= 1) && on && !intervalOn) {

            rumbleAll(false);
            on = false;

        }
    }

    /**
     * Determines if the command is finished yet. Used by the scheduler to delete the command from
     * the schedule.
     */
    public boolean isFinished() {

        if (intervalCount <= 0) { // If on is set to off anywhere (false) then we
                                  // should quit.

            finished = true;
            rumbleAll(false);
        }
        return finished;
    }

    @Override
    public void interrupted() {

        end();
    }

    @Override
    public void end() {

        System.out.println("Ended the stick rumble forcefully");
    }

}
