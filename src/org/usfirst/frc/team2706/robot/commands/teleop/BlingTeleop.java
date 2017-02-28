package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.controls.StickRumble;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This command should be run with the scheduler during teleop
 * to decide what to display, either distance to gear delivery,
 * if we're ready to get another gear or if it is time to climb/
 * we're ready to climb.
 */
public class BlingTeleop extends Command {
    
    private final double startTime = Timer.getFPGATimestamp();
    
    // Will tell us which teleop thing we're displaying
    private static String teleopDisplayState = "";
    
    private static StickRumble rumbler;
    
    public BlingTeleop() {
        
        Robot.blingSystem.teleopInit();
    }
    
    public void execute() {
        
        double timePassed = Timer.getFPGATimestamp() - startTime;
        
        // Wait some seconds from initialization to tell drivers entering teleop.
        if (timePassed < 3)
            return;
        
        //Get the average distance from whatever obstacle.
        double distance = (Robot.driveTrain.getRightDistanceToObstacle() + 
                           Robot.driveTrain.getLeftDistanceToObstacle()) / 2;
        
        int gearState = Robot.gearHandler.gearHandlerState();
        
        // Need this to determine if we're ready to climb
        double timeLeft = 150 - Timer.getMatchTime(); 
        
        // We use the teleopDisplayState to make sure we only call each of these once.
        if (distance < 3 && ((1 <= gearState && 3>= gearState) | gearState == 5)) {
            
          
            boolean pegIn = false;
            if (gearState >= 2 && gearState <= 3) {
                
                // Only want to run this the first time.
                if (!pegIn) {
                    StickRumble rumbler = new StickRumble(0.5, 0.5, 1, 0, -1, 1.0);
                    rumbler.start();
                }
                pegIn = true;
                
            }
            else {
                // If peg was in, the rumble was on.
                if (pegIn) {
                    try {
                        rumbler.end();
                    }
                    catch (Exception e) {}
                }
                pegIn = false;
            }
            Robot.blingSystem.showDistance(distance, pegIn);
            teleopDisplayState = "distance";
        }
        else if (gearState == 0 && teleopDisplayState != "gear") {
            Robot.blingSystem.showReadyToReceiveGear(true);
            teleopDisplayState = "gear";
        }
        else if (timeLeft <= 30 && teleopDisplayState != "climb") {
            Robot.blingSystem.showReadyToClimb(true);
            teleopDisplayState = "climb";
        }
    }
    
    @Override
    protected boolean isFinished(){        
        return false;
        }
    
    public void end() {
        
        Robot.blingSystem.clear();
        
    }
    

}
