package org.usfirst.frc.team2706.robot.subsystems;

import java.util.Arrays;
import java.util.List;

import org.usfirst.frc.team2706.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * Controls the 12 switch dial on the robot to select an autonomous mode. The autonomous modes on
 * each of the switches are defined in Robot.java
 */
public class AutonomousSelector extends SensorBase implements LiveWindowSendable {

    private static final Range[] voltages = {   new Range(0, 2.5), new Range(2.5, 2.75),
                                                new Range(2.75, 3.1), new Range(3.1, 3.5), new Range(3.5, 3.75),
                                                new Range(3.75, 3.95), new Range(3.95, 4.1), new Range(4.1, 4.2),
                                                new Range(4.2, 4.3), new Range(4.3, 4.4), new Range(4.4, 4.5),
                                                new Range(4.5, 4.6), new Range(4.6, 5)};

    private final List<Command> commands;
    private final AnalogInput selector;

    private boolean isFree = true;

    /**
     * Creates AutoSelector with a list of commands to bind to each input
     * 
     * @param commands The commands to bind. The zeroth is default, one is the first notch
     */
    public AutonomousSelector(Command... commands) {
        List<Command> commandList = Arrays.asList(commands);

        this.commands = commandList;
        this.selector = new AnalogInput(RobotMap.SELECTOR_CHANNEL);

        isFree = false;
    }


    /**
     * Gets the currently selected command
     * 
     * @return The selected command
     */
    public Command getSelected() {
        int idx = getVoltageAsIndex();
        if (idx >= commands.size())
            idx = 0;

        return commands.get(idx);
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

    /**
     * Live Window code, only does anything if live window is activated.
     */
    @Override
    public String getSmartDashboardType() {
        return "Analog Input";
    }

    private ITable m_table;

    @Override
    public void initTable(ITable subtable) {
        m_table = subtable;
        updateTable();
    }

    @Override
    public void updateTable() {
        if (isFree) {
            return;
        }

        if (m_table != null) {
            m_table.putNumber("Voltage", selector.getAverageVoltage());
            m_table.putNumber("Index", getVoltageAsIndex());
        }
    }

    @Override
    public ITable getTable() {
        return m_table;
    }

    /**
     * Analog Channels don't have to do anything special when entering the LiveWindow. {@inheritDoc}
     */
    @Override
    public void startLiveWindowMode() {}

    /**
     * Analog Channels don't have to do anything special when exiting the LiveWindow. {@inheritDoc}
     */
    @Override
    public void stopLiveWindowMode() {}
    
    private static class Range {

        public final double min, max;

        Range(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }
}
