package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

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
        
        boolean gearCaptured = Robot.gearHandler.gearCaptured();
        
        // Need this to determine if we're ready to climb
        double timeLeft = 150 - Timer.getMatchTime(); 
        
        // We use the teleopDisplayState to make sure we only call each of these once.
        if (distance < 3 && gearCaptured && teleopDisplayState != "distance") {
            Robot.blingSystem.showDistance(distance);
            teleopDisplayState = "distance";
        }
        else if (!gearCaptured && teleopDisplayState != "gear") {
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
