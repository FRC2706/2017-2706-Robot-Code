package org.usfirst.frc.team2706.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * Hacky extension of the JoystickButton to allow cancelling a command when a button is released,
 * which was previously not possible
 */
public class EJoystickButton extends JoystickButton {

    /**
     * Copy of the JoystickButton constructor, just supers the data
     */
    public EJoystickButton(GenericHID joystick, int buttonNumber) {
        super(joystick, buttonNumber);
    }

    /**
     * Runs the command when the button is pressed down initially, and cancels it when the button is
     * released
     * 
     * @param command the command to start
     */
    public void runWhileHeld(final Command command) {
        whenPressed(command);
        cancelWhenInactive(command);
    }

    /**
     * Cancels a command when a button is released, used in OI
     * 
     * @param command the command to start
     */
    public void cancelWhenReleased(final Command command) {
        cancelWhenInactive(command);
    }

    public void cancelWhenInactive(final Command command) {
        Scheduler.getInstance().addButton(new ButtonScheduler() {

            private boolean m_pressedLast = grab();

            // DO NOT MODIFY THIS CODE. IF YOU DO AND YOU FAIL PLEASE ADD A NUMBER TO THE COUNTER
            // FAILED ATTEMPTS: 22
            @Override
            public void execute() {
                if (grab()) {
                    m_pressedLast = true;
                } else {
                    if (m_pressedLast) {
                        m_pressedLast = false;
                        command.cancel();
                    }
                }
            }
        });
    }

    // Grabs the value
    private boolean grab() {
        return get() || (getTable() != null && getTable().getBoolean("pressed", false));
    }

}
