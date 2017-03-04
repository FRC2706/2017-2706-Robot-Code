package org.usfirst.frc.team2706.robot.bling;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ToggleFlashiness extends Command {
    
    protected void initialize() {
        
        Robot.blingSystem.toggleFlashiness();
        
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
