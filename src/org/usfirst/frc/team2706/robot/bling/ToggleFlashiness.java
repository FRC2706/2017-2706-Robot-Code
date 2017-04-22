package org.usfirst.frc.team2706.robot.bling;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command to toggle bling from flashy to not flashy mode
 */
public class ToggleFlashiness extends Command {

    protected void initialize() {

        Robot.blingSystem.toggleFlashiness();

    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
