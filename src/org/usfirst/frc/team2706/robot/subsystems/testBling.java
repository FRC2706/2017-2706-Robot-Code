package org.usfirst.frc.team2706.robot.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2706.robot.Robot;

public class testBling extends Command {
    int id;
    public testBling(int id){
        this.id = id;
    }
    public void execute(){
        //End is run whenever the command is terminated
        System.out.println("Starting up a bling system with id " + id);
        if (id == 1) Robot.blingSystem.auto();
        else if (id == 2) Robot.blingSystem.batteryInd(0.2);
        else if (id == 3) Robot.blingSystem.showDistance(2.7);
        else if (id == 4) Robot.blingSystem.showReadyToReceiveGear(true);
    }
    
    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return true;
    }

}
