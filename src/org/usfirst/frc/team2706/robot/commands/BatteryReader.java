package org.usfirst.frc.team2706.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.hal.PDPJNI;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;

public class BatteryReader extends Command{
       
    
    public static double batteryOutputVoltage; 
    // Used to tell us if the battery level is critical
    public static boolean batCritical;

    protected static double timePassed;
    // Used for knowing when this command started.
    protected static double startTime = Timer.getFPGATimestamp();

    public static double fullBatteryCapacity = RobotMap.BatteryCapacity; 
    
    public BatteryReader(){
        System.out.println("Initialised");   
    }
    
    /**
     * This will run when the scheduler calls the command.
     */
    public void execute(){
        
        // Get the battery voltage.
        batteryOutputVoltage = PDPJNI.getPDPVoltage(0);
        System.out.println("Voltage" + batteryOutputVoltage); // Remove Later
        
        // The battery critical boolean should be true if the battery voltage is below 20%. 
        if (batteryOutputVoltage / fullBatteryCapacity <= 0.2) {
            
            batCritical = true;
            
        }
        
        else batCritical = false;
        
        timePassed = Timer.getFPGATimestamp() - startTime;
        
        /*Essentially, I only want to update the lights every 2 seconds, approximately and 
        only during the first 10 seconds, or no matter what if the battery level is critical..*/
        if (((timePassed % 2.0 < 0.1 | timePassed % 2.0 > 1.9) && Timer.getFPGATimestamp() < 10.0) | batCritical) {
            
            System.out.println("Passed through the if statement");
           // TODO Robot.blingSystem.batteryInd(batteryOutputVoltage / fullBatteryCapacity, batCritical);
        }
    }
    @Override
    public void end(){
        
    }
    
    /**
     * Used to tell the scheduler when to terminate running this command.
     * Will only return true (in which case the command is terminated)
     * when the robot is turned off, because we always want to have
     * warnings about battery level.
     */
    public boolean isFinished(){
        return false;
    }
}
