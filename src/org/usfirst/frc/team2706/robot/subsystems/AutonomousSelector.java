package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * Controls the 12 switch dial on the robot to select an autonomous mode. The autonomous modes on
 * each of the switches are defined in Robot.java
 */
public class AutonomousSelector extends SensorBase implements Sendable {

    public static final int NUM_INDICES = 13;
    
    private static final Range[] voltages = {   new Range(0, 2.5), new Range(2.5, 2.75),
                                                new Range(2.75, 3.1), new Range(3.1, 3.5), new Range(3.5, 3.75),
                                                new Range(3.75, 3.95), new Range(3.95, 4.1), new Range(4.1, 4.2),
                                                new Range(4.2, 4.3), new Range(4.3, 4.4), new Range(4.4, 4.5),
                                                new Range(4.5, 4.6), new Range(4.6, 5)};

    private final Command[] commands;
    private final AnalogInput selector;
    
    private boolean isFree = true;
    private int numCommands = 0;

    /**
     * Creates AutoSelector with a list of commands to bind to each input
     * 
     * @param commands The commands to bind. The zeroth is default, one is the first notch
     */
    public AutonomousSelector(Command...commands) {
        this.commands = new Command[NUM_INDICES];
        this.selector = new AnalogInput(RobotMap.SELECTOR_CHANNEL);

        isFree = false;
        setCommands(commands);
    }

    public void setCommands(Command...commands) {
        for(int i = 0; i < NUM_INDICES; i++) {
            if(i < commands.length) {
                this.commands[i] = commands[i];
            }
            else {
                this.commands[i] = null;
            }
        }
        
        numCommands = Math.min(commands.length, NUM_INDICES);
    }

    /**
     * Gets the currently selected command
     * 
     * @return The selected command
     */
    public Command getSelected() {
        int idx = getVoltageAsIndex();
        if (idx >= numCommands)
            idx = 0;

        return commands[idx];
    }

    public int getVoltageAsIndex() {
        if (isFree) {
            return -1;
        }

        for (int i = 0; i < voltages.length; i++) {
            double voltage = selector.getAverageVoltage();
            if (voltage >= voltages[i].min && voltage < voltages[i].max) {
                return i;
            }
        }

        return 0;
    }

    /**
     * Delete (free) the analog switch used for the autonomous selector.
     */
    @Override
    public void free() {
        if (selector != null && !isFree) {
            selector.free();
        }
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Selector Switch");
        builder.addDoubleProperty("Voltage", selector::getAverageVoltage, null);
        builder.addDoubleProperty("Index", this::getVoltageAsIndex, null);
    }
    
    private static class Range {

        public final double min, max;

        Range(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }
}