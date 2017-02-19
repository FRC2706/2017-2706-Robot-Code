package org.usfirst.frc.team2706.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class BatteryReader extends Command {


    public static double batteryOutputVoltage;
    // Used to tell us if the battery level is critical
    public static boolean batCritical;

    protected static double timePassed;
    // Used for knowing when this command started.
    protected static double startTime = Timer.getFPGATimestamp();

    public static double fullBatteryCapacity = RobotMap.BatteryCapacity;

    // Get a new battery object.
    public static PowerDistributionPanel pdp; 

    public BatteryReader() {

        pdp = new PowerDistributionPanel();
        batteryOutputVoltage = pdp.getVoltage();
        double batteryPercent = (batteryOutputVoltage - 10) / (fullBatteryCapacity - 10);
        System.out.println("Battery Percent " + batteryPercent);
        Robot.blingSystem.batteryInd(batteryPercent, false);

    }

    /**
     * This will run when the scheduler calls the command.
     */
    public void execute() {

        // Get the battery voltage.
        batteryOutputVoltage = pdp.getVoltage();
        double batteryPercent = (batteryOutputVoltage - 10) / (fullBatteryCapacity - 10);

        /*
         * The battery critical boolean should be true if the battery voltage is below 20%, but
         * don't want to spam the bling system.
         */
        if (batteryPercent <= 0.2 && !batCritical) {
           
            batCritical = true;
            Robot.blingSystem.batteryInd(batteryPercent, batCritical);

        }

        if (batteryPercent > 0.2 && batCritical)
            
            batCritical = false;

        timePassed = Timer.getFPGATimestamp() - startTime;

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
