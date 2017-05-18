package org.usfirst.frc.team2706.robot.commands;

import org.usfirst.frc.team2706.robot.Log;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Reads battery and sends alerts if battery gets too low
 */
public class BatteryReader extends Command {

    public static double batteryOutputVoltage;

    /**
     * Used to tell us if the battery level is critical
     */
    public static boolean batCritical;

    protected static double timePassed;

    /**
     * Volts that the battery is considered full at
     */
    public static final double FULL_BATTERY_CAPACITY = 12.0;

    /**
     * Get a new battery object.
     */
    public static PowerDistributionPanel pdp;

    /**
     * Reads battery and sends alerts if battery gets too low
     */
    public BatteryReader() {
        pdp = new PowerDistributionPanel();
        batteryOutputVoltage = pdp.getVoltage();

        double batteryPercent = (batteryOutputVoltage - 10) / (FULL_BATTERY_CAPACITY - 10);

        Log.i("Battery Percentage", batteryPercent * 100);
    }

    /**
     * This will run when the scheduler calls the command.
     */
    public void execute() {
        // Get the battery voltage.
        batteryOutputVoltage = pdp.getVoltage();
        double batteryPercent = (batteryOutputVoltage - 10) / (FULL_BATTERY_CAPACITY - 10);

        /*
         * The battery critical boolean should be true if the battery voltage is below 20%, but
         * don't want to spam the bling system.
         */
        if (batteryPercent <= 0.2 && !batCritical) {
            DriverStation.reportWarning("Battery low. " + (batteryPercent * 100) + "% remaining.",
                            false);
            batCritical = true;
        }
        if (batteryPercent > 0.2 && batCritical)
            batCritical = false;

        timePassed = timeSinceInitialized();
    }

    @Override
    public void end() {}

    /**
     * Used to tell the scheduler when to terminate running this command. Will only return true (in
     * which case the command is terminated) when the robot is turned off, because we always want to
     * have warnings about battery level.
     */
    public boolean isFinished() {
        return false;
    }
}
