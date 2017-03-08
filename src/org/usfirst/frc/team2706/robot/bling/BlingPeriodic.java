package org.usfirst.frc.team2706.robot.bling;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.StickRumble;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This command should be run with the scheduler during teleop to decide what to display, either
 * distance to gear delivery, if we're ready to get another gear or if it is time to climb/ we're
 * ready to climb.
 */
public class BlingPeriodic extends Command {
    
    // Will tell us which teleop thing we're displaying
    private static String teleopDisplayState = "";
    
    private static StickRumble rumbler;
    
    public BlingPeriodic() {
        requires(Robot.blingSystem);
    }
    
    private static boolean rumbleOn = false;
    
    private static double timePoint = Timer.getFPGATimestamp();
    
    // Used to know if specific pattern has been started. 
    private static boolean autoInitialized = false;
    private static boolean teleopInitialized = false;

    @Override
    public void initialize() {
               
        if (DriverStation.getInstance().isAutonomous()) {
            Robot.blingSystem.auto();
            autoInitialized = true;
        }
        else {
            Robot.blingSystem.teleopInit();
            teleopInitialized = true;
        }
    }

    @Override
    public void execute() {
        if (DriverStation.getInstance().isAutonomous() && autoInitialized)
            return;
        else if (DriverStation.getInstance().isAutonomous() && !autoInitialized) {
            Robot.blingSystem.auto();
            autoInitialized = true;
            return;
        }
            

        double timePassed = Timer.getFPGATimestamp() - timePoint;
        double timeSinceInit = timeSinceInitialized();

        /* Wait some seconds from initialization to tell drivers entering teleop.
         * Also don't want to spam the arduino so only run around every 0.5 seconds.
         */
        if (timeSinceInit < 3 || ((timePassed / 1) < 1)) {
            if (!teleopInitialized) {
                teleopInitialized = true;
                Robot.blingSystem.teleopInit();
            }
            return;
        }
        
        timePoint += timePassed;
       
        // Do nothing else if we are displaying low battery.
        if (Robot.blingSystem.getBatteryCriticality()) 
            return;
        
        // Below is the stuff that we need for the following conditions 
        
        int gearState = Robot.gearHandler.gearHandlerState();

        // Get the average distance from whatever obstacle.
        double distance = (Robot.driveTrain.getRightDistanceToObstacle()
                           + Robot.driveTrain.getLeftDistanceToObstacle()) / 2;
        
        // Need this to determine if we're ready to climb
        double timeLeft = 150 - Timer.getMatchTime();

        // Used to make sure we don't do the fun loop while doing other stuff.
        boolean doingSomethingElse = false;
        
        // We use the teleopDisplayState to make sure we only call each of these once.
        if (rumbleOn && teleopDisplayState != "gear" && teleopDisplayState != "distance") {
            try {
                rumbler.end();
                rumbleOn = false;
            }
            catch (Exception e) {}
        }
        
        // Basically, if we're in range and have a gear.
        if (distance < 3 && ((1 <= gearState && 3 >= gearState) || gearState == 5)) {
            doingSomethingElse = true;
            
            // Basically, if we have the gear, either arm open or closed.
            if (gearState >= 2 && gearState <= 3) {
                
                // Only want to run this the first time.
                if (!rumbleOn) {
                    rumbler = new StickRumble(0.5, 0.5, 1, 0, -1, 1.0);
                    rumbler.start();
                }
                rumbleOn = true;
                
            }
            else {
                // If peg was in, the rumble was on.
                if (rumbleOn) {
                    try {
                        rumbler.end();
                    }
                    catch (Exception e) {}
                }
                rumbleOn = false;
            }
            
            Robot.blingSystem.showDistance(distance, rumbleOn);
            teleopDisplayState = "distance";
        }
          // Basically, if we're ready to get a gear   
        else if (gearState == 0 && distance < 1.5) {
            doingSomethingElse = true;
            
            if (!rumbleOn) {
                rumbler = new StickRumble(0.8, 0.7, 2, 0.5, -1, 1.0);
                rumbler.start();                
            }
            
            if (teleopDisplayState != "gear") {
                Robot.blingSystem.showReadyToReceiveGear(true);
                teleopDisplayState = "gear";
            }
        }   
          // Basically, if we must climb  
        else if ((timeLeft <= 30 || timeSinceInitialized() >= 105) && !doingSomethingElse) {
            
            doingSomethingElse = true;
            if (teleopDisplayState != "climb") {        
                Robot.blingSystem.showReadyToClimb(true);
                teleopDisplayState = "climb";
            
            }
        }
        
        // Basically, just running around the field having fun.
        else if (teleopDisplayState != "fun" && !doingSomethingElse) {
            Robot.blingSystem.teleopInit();
            teleopDisplayState = "fun";
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        Robot.blingSystem.clear();
        teleopDisplayState = "";
    }
    
    @Override
    public void interrupted() {
        end();
    }
}
