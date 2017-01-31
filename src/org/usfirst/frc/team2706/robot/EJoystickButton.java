package org.usfirst.frc.team2706.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class EJoystickButton extends JoystickButton {
	
	public EJoystickButton(GenericHID joystick, int buttonNumber) {
		super(joystick, buttonNumber);
	}
	
	/**
	 * Cancels a command when a button is released, used in OI
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
