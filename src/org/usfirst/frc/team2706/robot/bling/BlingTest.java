package org.usfirst.frc.team2706.robot.bling;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2706.robot.Robot;
// TODO get rid of this whole class
public class BlingTest extends Command {
    
    public BlingTest (int id) {
        // Test display distance for peg and peg is in.
        if (id == 1) {
            Robot.blingSystem.testDistance = 10;
            Robot.blingSystem.testGearState = 2;
            Robot.blingSystem.testTimeLeft = 50;
        }
        // Test display distance for peg and peg not in
        if (id == 2) {
            Robot.blingSystem.testDistance = 10;
            Robot.blingSystem.testGearState = 5;
            Robot.blingSystem.testTimeLeft = 50;
        }
        
        // Test display distance for receiving gear
        if (id == 3) {
            Robot.blingSystem.testDistance = 10;
            Robot.blingSystem.testGearState = 0;
            Robot.blingSystem.testTimeLeft = 50;
        }
        
        // Test display climb time
        if (id == 4) {
            Robot.blingSystem.testDistance = 30;
            Robot.blingSystem.testGearState = 1;
            Robot.blingSystem.testTimeLeft = 30;
        }
        
        // Test display fun
        if (id == 5) {
            Robot.blingSystem.testDistance = 20;
            Robot.blingSystem.testGearState = 1;
            Robot.blingSystem.testTimeLeft = 50;
        }
        System.out.println("Switched values to " + id);
    }
    
    public boolean isFinished () {
        return true;
    }
}
