package org.usfirst.frc.team2706.robot.bling;

import org.usfirst.frc.team2706.robot.Robot;

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
    
    /** Will tell us which teleop thing we're displaying <p>
     * 0 = Nothing (strip is clear) <p>
     * 1 = Distance<p> 
     * 2 = Climb<p>
     * 3 = gear<p>
     * 4 = fun <p>
     * 5 = teleop init<p>
     * 6 = auto init<p>
     * 7 = disabling flashiness<p>
     */
    private static int displayState = 0;
    
    private static double timePoint = Timer.getFPGATimestamp();
    
    public final double distanceThreshold = Robot.blingSystem.distanceThreshold;
    
    public BlingPeriodic() {
        requires(Robot.blingSystem);
    }

    @Override
    public void initialize() {
        
        // autoInitialized won't ever be true here, but just in case.
        if (DriverStation.getInstance().isAutonomous() && displayState != 6) {
            Robot.blingSystem.auto();
            displayState = 6;
        }
        else if (displayState != 5){
            Robot.blingSystem.teleopInit();
            displayState = 5;
        }
    }

    @Override
    public void execute() {
        
        if (DriverStation.getInstance().isAutonomous() && displayState == 6)
            return;
        else if (DriverStation.getInstance().isAutonomous() && Robot.blingSystem.getSpecialState() == "autoTrue") {
            Robot.blingSystem.auto();
            displayState = 6; 
            return;
        }
        // Don't want to do anything quite yet if we are disabling flashiness.
        if (Robot.blingSystem.getSpecialState() == "flashiness") {
            return;
        }

        double timePassed = Timer.getFPGATimestamp() - timePoint;
        double timeSinceInit = timeSinceInitialized();

         // Only want to run this every so often.       
        if (((timePassed / 0.5) < 1)) {               
            return;
        }
        
        timePoint += timePassed;
        
        
        // Below is the stuff that we need for the following conditions 
        
        int gearState = Robot.gearHandler.gearHandlerState();

        // Get the average distance from whatever obstacle.
        double distance = (Robot.driveTrain.getRightDistanceToObstacle()
                           + Robot.driveTrain.getLeftDistanceToObstacle()) / 2;
        
        // Used to make sure we don't do the fun loop while doing other stuff.
        boolean doingSomethingElse = false;
        
        
        // Basically, if we're in range of the peg and have a gear.
        if (distance < distanceThreshold && ((1 <= gearState && 3 >= gearState) || gearState == 5)) {
            
            doingSomethingElse = true;
            Robot.blingSystem.showDistance(distance, (gearState >= 2 && gearState <= 3));
            displayState = 1;
        }
        
        // Basically, if we're ready to get a gear from loading station  
        else if (!doingSomethingElse && (gearState == 0 || gearState == 4) && distance < distanceThreshold && Robot.blingSystem.getDistanceShower()) {
            doingSomethingElse = true;
            
            if (displayState != 3) {
                boolean armsClosed = gearState == 0; 
                Robot.blingSystem.showReadyToReceiveGear(distance, armsClosed);
                displayState = 3;
            }
        }   
        // Basically, if we must climb  
        else if ((timeSinceInitialized() >= 120) && !doingSomethingElse) {
            
            doingSomethingElse = true;
            if (displayState != 2) {        
                Robot.blingSystem.showReadyToClimb(true);
                displayState = 2;
            
            }
        }
        
        // Just running around the field and we have a gear.
        else if (displayState != 4 && !doingSomethingElse && gearState == 1) {
            doingSomethingElse = true;
            Robot.blingSystem.funDisplay();
            displayState = 4;
        }
        
        else if (displayState != 0 && !doingSomethingElse) {
            Robot.blingSystem.clear();
            displayState = 0; 
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
