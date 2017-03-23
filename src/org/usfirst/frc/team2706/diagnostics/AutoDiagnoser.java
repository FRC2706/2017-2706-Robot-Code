package org.usfirst.frc.team2706.diagnostics;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2706.robot.Robot;
/**
 * Goal of this class is to test the hardware of the robot automatically
 * to make sure that everything is working right.
 * @author eAUE (Kyle Anderson)
 *
 */
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithTime;
public class AutoDiagnoser extends Command{
    
    protected static int caseNo = 0;
    protected static boolean error = false;
    protected static double timeSpot = 0;
    protected static double motorSpeed;
    protected static StraightDriveWithTime drive;
    
    public AutoDiagnoser() {
        System.out.println("\nDiagnostics beginning...");
    }
    
    
    public void execute() {
        
        switch (caseNo) {
            // Testing gearHandler mechanisms
            case 1:
                int originalGearState = Robot.gearHandler.gearHandlerState();
                Robot.gearHandler.toggleArm();
                int gearState = Robot.gearHandler.gearHandlerState();
                if ((0 <= gearState && gearState <= 2 || gearState == 7) && originalGearState >=3
                                && originalGearState <= 6)
                    System.out.println("Arms can be moved");
                else if (!(0 <= gearState && gearState <= 2 || gearState == 7) && !(originalGearState >=3
                                && originalGearState <= 6)) {
                    System.out.println("Arms can be moved");
                }
                else {
                    DriverStation.reportWarning("Arms cannot be moved!", false);
                    error = true;
                }
                caseNo += 1;

            // Testing climber functionality
            case 2:
                if (timeSpot == 0) {
                    Robot.climber.climb();
                    timeSpot = Timer.getFPGATimestamp();
                }
                
                else if ((timeSpot - Timer.getFPGATimestamp()) > 2) {
                    caseNo += 1;
                    motorSpeed = Robot.climber.getSpeed();
                    timeSpot = 0;
                    if (motorSpeed < 0.1) {
                        System.out.println("Climber functioning properly");
                    }
                    else {
                        DriverStation.reportWarning("Climber malfunctioning!", false);
                        error = true;
                    }
                }
                caseNo += 1;
            // Testing movement
            case 3:
                if (timeSpot == 0) {
                    timeSpot = Timer.getFPGATimestamp();
                    drive = new StraightDriveWithTime(0.1, 600);
                    drive.start();
                }
                
                else if ((Timer.getFPGATimestamp() - timeSpot) > 0.5) {
                    double distance = Robot.driveTrain.getDistance();
                    if (distance < 3) {
                        DriverStation.reportWarning("Cannot drive!", false);
                        error = true;
                    }
                    else
                        System.out.println("Driving appears to be working unless encoders " +
                                        "also don't work");
                }
                
            // Testing 
            case 4:
            case 5:
        }
    }

    @Override
    protected boolean isFinished() {
        return (caseNo > 5);
    }
    
    protected void end() {
        Robot.blingSystem.showError(error);
    }
    
    protected void interrupted() {
        end();
    }
    
}
