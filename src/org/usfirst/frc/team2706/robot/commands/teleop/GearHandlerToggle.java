package org.usfirst.frc.team2706.robot.commands.teleop;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This command is activated by the press of a button, and opens or closes the Gear Handler arms depending on their position.
 * 
 * @author wakandacat, FilledWithDetermination, Crazycat200
 */
public class GearHandlerToggle extends Command {

    protected void execute(){
        Robot.gearHandler.toggleArm();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
