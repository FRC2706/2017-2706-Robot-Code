package org.usfirst.frc.team2706.robot.bling;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/***
 * 
 * @author eAUE (Kyle Anderson)
 * @see <a href = "https://docs.google.com/spreadsheets/d/1zb40w_BbmzKFPKRT7XTrjqsIKUw0Jn152sTJA3ppQCs/edit?usp=sharing">
 * Bling Subsystem Map </a>. 
 *
 */
public class BlingPeriodic2 extends Command {
    
    
    // Distances for lining up to receive a gear in inches.
    
    // Too close if we're below this.
    protected final int lowerDistanceThreshold = 11;
    
    // Too far if we're above this.
    protected final int upperDistanceThreshold = 20;
    
    // Don't bother displaying the pattern if we're outside of this.
    protected final int outsideDistanceThreshold = 40;
    
    protected static double timePoint = Timer.getFPGATimestamp();
    
    public BlingPeriodic2() {
        requires (Robot.blingSystem);
    }
    
    protected void initialize () {
        if (DriverStation.getInstance().isAutonomous() && Robot.blingSystem.getSpecialState() == "autoTrue") {
            Robot.blingSystem.auto();
        }
    }
    
    protected void execute () {
        
        double timePassed = Timer.getFPGATimestamp() - timePoint;
        
        if (timePassed < 0.4)
            return;
        
        timePoint = Timer.getFPGATimestamp();
        
        // Used to make sure we do not interrupt patterns with other patterns.
        boolean busy = false;
        
        if (DriverStation.getInstance().isAutonomous() && 
            Robot.blingSystem.getSpecialState() == "autoTrue") {
            busy = true;
            Robot.blingSystem.auto();
        }
        
        else if (DriverStation.getInstance().isOperatorControl() && Robot.blingSystem.getSpecialState() == "") {
            
            // Required measurements. 
            double distance = Robot.driveTrain.getDistanceToObstacle();   
            int gearState = Robot.gearHandler.gearHandlerState();
            
            // Displaying peg line up
            if (distance < outsideDistanceThreshold && ((1 <= gearState && gearState <= 3) || gearState == 5)) {
                busy = true;
                Robot.blingSystem.showDistance(100 - (int) Math.round((distance / outsideDistanceThreshold) * 100), 2 <= gearState && gearState <= 3);
            }
            
            // Displaying pickup line up
            else if ((distance < outsideDistanceThreshold) && (gearState == 0 || gearState == 4) && !busy) {
                busy = true;
                
                // Just right (arms closed and in nice distance)
                if ((lowerDistanceThreshold <= distance) && (distance <= upperDistanceThreshold) && gearState == 0)
                    Robot.blingSystem.showReadyToReceiveGear(1);
                
                // Too close (arms closed and too close)
                else if (distance < lowerDistanceThreshold && gearState == 0)
                    Robot.blingSystem.showReadyToReceiveGear(0);
                
                // Too far or arms open
                else
                    Robot.blingSystem.showReadyToReceiveGear(2);
            }
            
            // Time to climb
            else if (timeSinceInitialized() > 120 && !busy) {
                busy = true;
                Robot.blingSystem.showReadyToClimb(true);
            }
            
            // 
            else if (gearState == 1 && !busy) {
                busy = true;
                Robot.blingSystem.funDisplay();                
            }
            
            else if (!busy) {
                Robot.blingSystem.clear();
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
    
    protected void end () {
        Robot.blingSystem.clear();
    }
    
    protected void interrupted () {
        end();
    }
}
