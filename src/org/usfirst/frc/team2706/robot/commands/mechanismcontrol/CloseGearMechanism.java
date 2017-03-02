package org.usfirst.frc.team2706.robot.commands.mechanismcontrol;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class CloseGearMechanism extends Command {

    public CloseGearMechanism() {
        requires(Robot.gearHandler);
    }
    
    @Override
    protected void initialize() {
            Robot.gearHandler.closeArm();
    }
    
    @Override
    protected boolean isFinished() {
        return true;
    }
}
