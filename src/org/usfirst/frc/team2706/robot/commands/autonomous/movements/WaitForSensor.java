package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Waits for the IR sensor and the distance sensors to both be good for the gear handler.
 * @author cnnr2
 *
 */
public class WaitForSensor extends Command {
    public final double distance;
    public WaitForSensor(double distance) {
        this.distance = distance;
    }
    protected void execute() {
      
        if(Robot.gearHandler.pegDetected()) {
            //System.out.println("Blinging");
        //    Robot.blingSystem.showReadyToReceiveGear(true);
        }
    }
    @Override
    protected boolean isFinished() {
      //  System.out.println("waiting");
        if(Robot.gearHandler.pegDetected()) {
            System.out.println("Detected");
        }
        // TODO Auto-generated method stub
        return Robot.gearHandler.pegDetected();
    }

}
