package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Coordinates commands for the Gear Handler Arm mechanisms.
 * 
 * @author wakandacat, FilledWithDetermination, Crazycat200
 */
public class GearHandler extends Subsystem {


    private DoubleSolenoid solenoid = new DoubleSolenoid(RobotMap.SOLENOID_FORWARD_CHANNEL,
                    RobotMap.SOLENOID_REVERSE_CHANNEL);
    protected Compressor compressor = new Compressor();

    public static final int ARMS_CLOSED_NO_GEAR = 0;
    public static final int ARMS_CLOSED_WITH_GEAR = 1;
    public static final int ARMS_CLOSED_PEG_IN_WITH_GEAR = 2;
    public static final int ARMS_OPEN_PEG_IN_WITH_GEAR = 3;
    public static final int ARMS_OPEN_NO_GEAR = 4;
    public static final int ARMS_OPEN_WITH_GEAR = 5;
    public static final int ARMS_OPEN_PEG_IN_NO_GEAR = 6;
    public static final int ARMS_CLOSED_PEG_IN_NO_GEAR = 7;

    public int gearHandlerState() {
        int state = ARMS_CLOSED_NO_GEAR;
        if (checkArmOpen()) {
            if (gearCaptured()) {
                if (pegDetected()) {
                    state = ARMS_OPEN_PEG_IN_WITH_GEAR;
                } else {
                    state = ARMS_OPEN_WITH_GEAR;
                }
            } else {
                if (pegDetected()) {
                    state = ARMS_OPEN_PEG_IN_NO_GEAR;
                } else {
                    state = ARMS_OPEN_NO_GEAR;
                }
            }
        } else {
            // Arm closed
            if (gearCaptured()) {
                if (pegDetected()) {
                    state = ARMS_CLOSED_PEG_IN_WITH_GEAR;
                } else {
                    state = ARMS_CLOSED_WITH_GEAR;
                }
            } else {
                if (pegDetected()) {
                    state = ARMS_CLOSED_PEG_IN_NO_GEAR;
                } else {
                    state = ARMS_CLOSED_NO_GEAR;
                }
            }
        }
        return state;
    }

    /*
     * some interesting things about the sensor... (measured in flash card lines)
     *  (right on top of sensor = line 1)
     * line 01 = 1.2V ++++++++++++
     * line 02 = 1.4V ++++++++++++++
     * line 03 = 1.7V +++++++++++++++++
     * line 04 = 1.8V ++++++++++++++++++
     * line 05 = 2.4V ++++++++++++++++++++++++
     * line 06 = 2.9V +++++++++++++++++++++++++++++
     * line 07 = 2.9V +++++++++++++++++++++++++++++
     * line 08 = 2.8V ++++++++++++++++++++++++++++
     * line 09 = 2.2V ++++++++++++++++++++++
     * line 10 = 2.2V ++++++++++++++++++++++
     * line 11 = 2.1V +++++++++++++++++++++
     * line 12 = 1.8V ++++++++++++++++++
     * line 13 = 1.7V +++++++++++++++++
     * line 14 = 1.6V ++++++++++++++++
     * line 15 = 1.3V +++++++++++++
     * line 16 = 1.2V ++++++++++++
     * line 17 = 1.1V +++++++++++
     * line 18 = 1.1V +++++++++++
     * line 19 = 1.0V ++++++++++
     * line 20 = 0.9V +++++++++
     * line 21 = 0.9V +++++++++
     */
    private AnalogInput irGearSensor = new AnalogInput(RobotMap.INFRARED_SENSOR_GEAR_ANALOG);
    private static final double GEAR_CAPTURED = 0.5;


    private AnalogInput irPegSensor = new AnalogInput(RobotMap.INFRARED_SENSOR_PEG_ANALOG);
    private static final double PEG_DETECTED = 0.25;

    // Calls limit switches from robot map
    private DigitalInput limitSwitchLeft = new DigitalInput(RobotMap.LIMIT_SWITCH_LEFT_CHANNEL);
    private DigitalInput limitSwitchRight = new DigitalInput(RobotMap.LIMIT_SWITCH_RIGHT_CHANNEL);


    // Let's use this to keep track of whether the arm is closed :)
    private boolean closed = true;

    public void initDefaultCommand() {}

    /**
     * Opens the gear handler arm
     */
    public void openArm() {
        Log.d("Gear Handler", "Opening Gear Handler");

        solenoid.set(DoubleSolenoid.Value.kForward);

        // Check to see if arm is open (see bottom of code)
        closed = checkArmOpen();
    }

    /**
     * Closes the gear handler arm
     */
    public void closeArm() {
        Log.d("Gear Handler", "Opening Gear Handler");

        solenoid.set(DoubleSolenoid.Value.kReverse);

        // Check to see if arm is open (see bottom of code)
        closed = checkArmOpen();
    }

    /**
     * Toggles whether the gear handler arm is open or not
     */
    public void toggleArm() {
        if (closed) {
            openArm();
        } else {
            closeArm();
        }
    }

    /**
     * Uses the IR sensor to detect whether the robot has a gear
     * 
     * @return Whether the robot has a gear or not
     */
    public boolean gearCaptured() {
        if (irGearSensor.getVoltage() >= GEAR_CAPTURED) {
            return true;
        }
        return false;
    }


    /**
     * Uses the IR sensor to detect whether the peg is in the robot
     * 
     * @return Whether the peg is in the robot or not
     */
    public boolean pegDetected() {
        if (irPegSensor.getVoltage() >= PEG_DETECTED) {
            return true;
        }
        return false;
    }

    /**
     * Uses limit switch to help see if arm is open (Currently a TODO)
     * 
     * @return Whether the arm is working or not
     */
    public boolean checkArmOpen() {
        if (!limitSwitchRight.get()) {
            return true;
        }
        return false;
    }

    /**
     * Turn compressor on and off
     * 
     * @param compressorState Whether compressor is on or not
     */
    public void setCompressor(boolean compressorState) {
        compressor.setClosedLoopControl(compressorState);
    }

    /**
     * Logs gear handler information to SmartDashboard
     */
    public void log() {
        SmartDashboard.putNumber("Peg Sensor", irPegSensor.getVoltage());
        SmartDashboard.putNumber("Gear Sensor", irGearSensor.getVoltage());
        SmartDashboard.putBoolean("Limit Switch left", limitSwitchLeft.get());
        SmartDashboard.putBoolean("Limit Switch Right", limitSwitchRight.get());
    }
}
