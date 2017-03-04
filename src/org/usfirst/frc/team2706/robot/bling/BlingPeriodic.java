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
    
    private static boolean pegIn = false;

    @Override
    public void initialize() {
        if (DriverStation.getInstance().isAutonomous()) {
            Robot.blingSystem.auto();
        } else {
            Robot.blingSystem.teleopInit();
        }
    }

    @Override
    public void execute() {

        // TODO int gearState = Robot.gearHandler.gearHandlerState();
        int gearState = 1;
        if (DriverStation.getInstance().isAutonomous())
            return;

        double timePassed = timeSinceInitialized();

        // Wait some seconds from initialization to tell drivers entering teleop.
        if (timePassed < 3)
            return;

        // Get the average distance from whatever obstacle.
       //TODO double distance = (Robot.driveTrain.getRightDistanceToObstacle()
                        //+ Robot.driveTrain.getLeftDistanceToObstacle()) / 2;
        
        double distance = 5;

        // Need this to determine if we're ready to climb
        // TODO double timeLeft = 150 - Timer.getMatchTime();
        double timeLeft = 29;

        // We use the teleopDisplayState to make sure we only call each of these once.
        
        // Basically, if we're in range and have a gear.
        if (distance < 3 && ((1 <= gearState && 3>= gearState) | gearState == 5)) {
            
            System.out.println("Distance");
            
            // Basically, if we have the gear, either arm open or closed.
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
            
          // Basically, if we're ready to get a gear   
        } else if (gearState == 0 && teleopDisplayState != "gear") {
            System.out.println("Ready to get gear");
            

            Robot.blingSystem.showReadyToReceiveGear(true);
            teleopDisplayState = "gear";
           
          // Basically, if we must climb  
        } else if (timeLeft <= 30 && teleopDisplayState != "climb") {
            System.out.println("Climb");
            
            Robot.blingSystem.showReadyToClimb(true);
            teleopDisplayState = "climb";
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        Robot.blingSystem.clear();
    }
    
    @Override
    public void interrupted() {
        end();
    }
}
