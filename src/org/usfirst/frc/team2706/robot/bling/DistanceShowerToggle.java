package org.usfirst.frc.team2706.robot.bling;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DistanceShowerToggle extends Command {
    
    public void initialize() {
        Robot.blingSystem.toggleDistanceShower();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
