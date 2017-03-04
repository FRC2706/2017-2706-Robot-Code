package org.usfirst.frc.team2706.robot.commands.mechanismcontrol;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Opens the gear holder mechanism so a gear can be placed on the peg
 */
public class OpenGearMechanism extends Command {

    public OpenGearMechanism() {
        requires(Robot.gearHandler);
    }

    @Override
    protected void initialize() {
        Robot.gearHandler.openArm();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
