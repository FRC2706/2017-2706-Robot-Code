package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class GearHandlerToggle extends Command {

    protected void execute(){
        Robot.gearHandler.ToggleArm();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

}
