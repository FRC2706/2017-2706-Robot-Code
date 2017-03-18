package org.usfirst.frc.team2706.robot.bling;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.StickRumble;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * @author eAUE (Kyle Anderson)
 * This command should be run with the scheduler during teleop to decide what to display, either
 * distance to gear delivery, if we're ready to get another gear or if it is time to climb/ we're
 * ready to climb.
 */
public class BlingPeriodic extends Command {
    
    /* Will tell us which teleop thing we're displaying
     * 0 = Nothing
     * 1 = Distance (1.5 = distance init)
     * 2 = Climb (2.5 = climb init)
     * 3 = gear (3.5 = gear init)
     * 4 = fun (4.5 = fun init)
     * 5 = teleop init (5.5 = teleop init init)
     * 6 = auto init (6.5 = auto init init)
     * 7 = disabling flashiness (7.5 = start disabling flashiness)
     */
    private static double displayState = 0;
    
    private static StickRumble rumbler;
    
    private static boolean rumbleOn = false;
    
    private static double timePoint = Timer.getFPGATimestamp();
        
    private final double distanceThreshold = 18;
    
    public BlingPeriodic() {
        requires(Robot.blingSystem);
    }

    @Override
    public void initialize() {
        
        // autoInitialized won't ever be true here, but just in case.
        if (DriverStation.getInstance().isAutonomous() && displayState != 6) {
            Robot.blingSystem.auto();
            displayState = 6.5;
        }
        else {
            Robot.blingSystem.teleopInit();
            displayState = 7.5;
        }
    }

    @Override
    public void execute() {
        
        if (DriverStation.getInstance().isAutonomous() && displayState == 6)
            return;
        else if (DriverStation.getInstance().isAutonomous() && Robot.blingSystem.getSpecialState() == "autoTrue") {
            Robot.blingSystem.auto();
            displayState = 6.5; 
            return;
        }
        
        if (displayState % 1 != 0) {
            Robot.blingSystem.send();
            displayState -= 0.5;
        }

        double timePassed = Timer.getFPGATimestamp() - timePoint;
        double timeSinceInit = timeSinceInitialized();

        /* Wait some seconds from initialization to tell drivers entering teleop.
         * Also don't want to spam the arduino so only run around every 0.5 seconds.
         */
        if (timeSinceInit < 3 || ((timePassed / 0.5) < 1)) {
            if ((timeSinceInit - 15) < 3) {
                Robot.blingSystem.teleopInit();
                displayState = 5.5;
            }
            return;
        }
        
        timePoint += timePassed;
        
        
        // Below is the stuff that we need for the following conditions 
        
        int gearState = Robot.gearHandler.gearHandlerState();

        // Get the average distance from whatever obstacle.
        double distance = (Robot.driveTrain.getRightDistanceToObstacle()
                           + Robot.driveTrain.getLeftDistanceToObstacle()) / 2;
        
        // Need this to determine if we're ready to climb
        double timeLeft = 150 - Timer.getMatchTime();

        // Used to make sure we don't do the fun loop while doing other stuff.
        boolean doingSomethingElse = false;
        
        String specialOperations = Robot.blingSystem.getSpecialState();
        
        // We use the teleopDisplayState to make sure we only call each of these once.
        if (rumbleOn && displayState != 3 && displayState != 1) {
            try {
                rumbler.end();
                rumbleOn = false;
            }
            catch (Exception e) {}
        }
        
        if (specialOperations == "flashiness") {
            displayState = 7.5;
        }
        
        // Basically, if we're in range and have a gear.
        if (distance < distanceThreshold && ((1 <= gearState && 3 >= gearState) || gearState == 5)) {
            doingSomethingElse = true;
            
            // Basically, if we have the gear, either arm open or closed and the peg is in.
            if (gearState >= 2 && gearState <= 3) {
                
                // Only want to run this the first time.
                if (!rumbleOn) {
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
            
            Robot.blingSystem.showDistance(distance, gearState >= 2 && gearState <= 3);
            displayState = 1.5;
        }
        
        // Basically, if we're ready to get a gear from loading station  
        else if (gearState == 0 && distance < distanceThreshold) {
            doingSomethingElse = true;
            
            if (!rumbleOn) {              
            }
            
            if (displayState != 3) {
                Robot.blingSystem.showReadyToReceiveGear(true);
                displayState = 3.5;
            }
        }   
        // Basically, if we must climb  
        else if ((timeLeft <= 30 || timeSinceInitialized() >= 120) && !doingSomethingElse) {
            
            doingSomethingElse = true;
            if (displayState != 2) {        
                Robot.blingSystem.showReadyToClimb(true);
                displayState = 2.5;
            
            }
        }
        
        // Just running around the field having fun.
        else if (displayState != 4 && !doingSomethingElse) {
            Robot.blingSystem.funDisplay();
            displayState = 4.5;
        }
        
        // TODO Remove this later.
        if (Math.round((timeSinceInitialized() % 2)) == 0)
            System.out.println("displayState = " + displayState + " distance = " + distance 
                                + " gearState = " + gearState + " timeSinceInit = " + 
                                timeSinceInit);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        Robot.blingSystem.clear();
        displayState = 0;
    }
    
    @Override
    public void interrupted() {
        end();
    }
}
