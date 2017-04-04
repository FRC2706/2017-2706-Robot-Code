package org.usfirst.frc.team2706.robot.bling;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.StickRumble;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    
    protected static StickRumble rumbler = null; 
    
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
        
        // Teleop display modes
        else if (DriverStation.getInstance().isOperatorControl() && Robot.blingSystem.getSpecialState() == "") {
            
            
            // Required measurements. 
            double distance = Robot.driveTrain.getDistanceToObstacle(); 
            
            /**
             * Description of gearHandler states (from GearHandler subsystem)<p>
             * 0 = Arms closed with no gear.<p>
             * 1 = arms closed with a gear.<p>
             * 2 = arms closed with gear and peg in.<p>
             * 3 = Arms open with a gear and peg in. <p>
             * 4 = Arms open with no gear.<p>
             * 5 = Arms open with a gear.<p>
             * 6 = Arms open with no gear and peg in.<p>
             * 7 = Arms closed with no gear and peg in.
             */
            int gearState = Robot.gearHandler.gearHandlerState();
            
            // TODO remove debug print
            System.out.println("Distance : " + distance + " GearState : " + gearState);
            
            /* Turn off the rumbler at an appropriate time 
             * (when the peg is no longer in or arms are open).
             */
            if (rumbler != null && gearState < 1 && gearState > 2) {
                rumbler.end();
                rumbler = null;
            }
            
            // Displaying peg line up
            if (distance < outsideDistanceThreshold && ((1 <= gearState && gearState <= 3) || gearState == 5)) {
                busy = true;
                
                // Get some vibration going, but only if there is already none.
                if (1 <= gearState && gearState <= 2 && rumbler == null) {
                    rumbler = new StickRumble(0.7, 0.3, 2, 0.2, -1, 1.0, 0);
                    Scheduler.getInstance().add(rumbler); 
                }
                
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
