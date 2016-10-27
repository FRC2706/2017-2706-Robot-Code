package org.usfirst.frc.team2706.robot.subsystems;

import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class AutonomousSelector extends Subsystem {

	// Can't be static?
	private final Range[] voltages = { 	new Range(0,2.5), 
										new Range(2.5,2.75), new Range(2.75,3.1), new Range(3.1,3.5),
										new Range(3.5,3.75), new Range(3.75,3.95), new Range(3.95,4.1),
										new Range(4.1,4.2), new Range(4.2,4.3), new Range(4.3,4.4),
										new Range(4.4,4.5), new Range(4.5,4.6), new Range(4.6,5 )};
	
	private final List<Command> commands;
	private final AnalogInput selector;
	
	public AutonomousSelector(Command ...commands) {	
		List<Command> commandList = Arrays.asList(commands);
		
		this.commands = commandList;
		this.selector = new AnalogInput(0);
	}
	
	@Override
	protected void initDefaultCommand() {}
	
	public Command getSelected() {
		int idx = getVoltageAsIndex();
		if(idx >= commands.size())
			idx = 0;
		
		return commands.get(idx);
	}

	private int getVoltageAsIndex() {
		for(int i = 0; i < voltages.length; i++) {
			double voltage = selector.getAverageVoltage();
			if(voltage >= voltages[i].min && voltage < voltages[i].max) {
				return i;
			}
		}
		
		return 0;
	}
	
	private class Range {
		
		public final double min, max;
		
		Range(double min, double max) {
			this.min = min;
			this.max = max;
		}
	}
}
