package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class RotateWithCamera extends Command {

    
    protected void execute() {
        Robot.camera.GetTargets(false);
        System.out.println(Robot.camera.getTarget().ctrY + 0.05);
    }
    @Override
    protected boolean isFinished() {
        return false;
    }

}
