package org.usfirst.frc.team2706.robot.commands.mechanismcontrol;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Closes the gear holder mechanism so a gear can be placed in the gear holder
 */
public class CloseGearMechanism extends Command {

    /**
     * Constructs Command
     */
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
