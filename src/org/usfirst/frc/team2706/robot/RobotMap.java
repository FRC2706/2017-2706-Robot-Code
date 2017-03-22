package org.usfirst.frc.team2706.robot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.Scanner;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into to a variable name.
 * This provides flexibility changing wiring, makes checking the wiring easier and significantly
 * reduces the number of magic numbers floating around.
 */
@SuppressWarnings("unused")
public class RobotMap {

    private static final int ROBOT_ID = getRobotID();

    // CONSTANT_VALS [ Competition, Practice, Simulation]

    // Left gearbox
    private static final int[] MOTOR_FRONT_LEFT_VALS = {1, 1, 1};
    public static final int MOTOR_FRONT_LEFT = getConstant("MOTOR_FRONT_LEFT");

    private static final boolean[] MOTOR_FRONT_LEFT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_FRONT_LEFT_INVERTED =
                    getConstant("MOTOR_FRONT_LEFT_INVERTED");


    // Right gearbox
    private static final int[] MOTOR_FRONT_RIGHT_VALS = {3, 3, 3};
    public static final int MOTOR_FRONT_RIGHT = getConstant("MOTOR_FRONT_RIGHT");

    private static final boolean[] MOTOR_FRONT_RIGHT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_FRONT_RIGHT_INVERTED =
                    getConstant("MOTOR_FRONT_RIGHT_INVERTED");


    // Left gearbox
    private static final int[] MOTOR_REAR_LEFT_VALS = {2, 2, 2};
    public static final int MOTOR_REAR_LEFT = getConstant("MOTOR_REAR_LEFT");

    private static final boolean[] MOTOR_REAR_LEFT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_REAR_LEFT_INVERTED = getConstant("MOTOR_REAR_LEFT_INVERTED");


    // Right gearbox
    private static final int[] MOTOR_REAR_RIGHT_VALS = {4, 4, 4};
    public static final int MOTOR_REAR_RIGHT = getConstant("MOTOR_REAR_RIGHT");

    private static final boolean[] MOTOR_REAR_RIGHT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_REAR_RIGHT_INVERTED =
                    getConstant("MOTOR_REAR_RIGHT_INVERTED");


    // Motor for panning camera
    private static final int[] MOTOR_CAMERA_PAN_VALS = {8, 8, 8};
    public static final int MOTOR_CAMERA_PAN = getConstant("MOTOR_CAMERA_PAN");


    // Motor for tilting camera
    private static final int[] MOTOR_CAMERA_TILT_VALS = {9, 9, 9};
    public static final int MOTOR_CAMERA_TILT = getConstant("MOTOR_CAMERA_TILT");

    // XXX: Encoders got flipped
    private static final int[] ENCODER_LEFT_A_VALS = {0, 1, 1};
    public static final int ENCODER_LEFT_A = getConstant("ENCODER_LEFT_A");

    private static final int[] ENCODER_LEFT_B_VALS = {1, 0, 0};
    public static final int ENCODER_LEFT_B = getConstant("ENCODER_LEFT_B");

    private static final double[] ENCODER_LEFT_DPP_VALS = {1.0 / 328, 1.0 / 264, 1.0 / 264};
    public static final double ENCODER_LEFT_DPP = getConstant("ENCODER_LEFT_DPP");


    private static final int[] ENCODER_RIGHT_A_VALS = {3, 2, 2};
    public static final int ENCODER_RIGHT_A = getConstant("ENCODER_RIGHT_A");

    private static final int[] ENCODER_RIGHT_B_VALS = {2, 3, 3};
    public static final int ENCODER_RIGHT_B = getConstant("ENCODER_RIGHT_B");

    private static final double[] ENCODER_RIGHT_DPP_VALS = {1.0 / 328, 1.0 / 264, 1.0 / 264};
    public static final double ENCODER_RIGHT_DPP = getConstant("ENCODER_RIGHT_DPP");


    private static final boolean[] INVERT_JOYSTICK_X_VALS = {false, false, false};
    public static final boolean INVERT_JOYSTICK_X = getConstant("INVERT_JOYSTICK_X");

    private static final boolean[] INVERT_JOYSTICK_Y_VALS = {false, false, false};
    public static final boolean INVERT_JOYSTICK_Y = getConstant("INVERT_JOYSTICK_Y");

    private static final int[] SELECTOR_CHANNEL_VALS = {0, 0, 0};
    public static final int SELECTOR_CHANNEL = getConstant("SELECTOR_CHANNEL");

    private static final int[] RING_LIGHT_VALS = {12, 12, 12};
    public static final int RING_LIGHT = getConstant("RING_LIGHT");

    private static final int[] LEFT_ULTRASONIC_PING_CHANNEL_VALS = {4, 4, 4};
    public static final int LEFT_ULTRASONIC_PING_CHANNEL =
                    getConstant("LEFT_ULTRASONIC_PING_CHANNEL");

    private static final int[] LEFT_ULTRASONIC_ECHO_CHANNEL_VALS = {5, 5, 5};
    public static final int LEFT_ULTRASONIC_ECHO_CHANNEL =
                    getConstant("LEFT_ULTRASONIC_ECHO_CHANNEL");

    private static final int[] RIGHT_ULTRASONIC_PING_CHANNEL_VALS = {6, 6, 6};
    public static final int RIGHT_ULTRASONIC_PING_CHANNEL =
                    getConstant("RIGHT_ULTRASONIC_PING_CHANNEL");

    private static final int[] RIGHT_ULTRASONIC_ECHO_CHANNEL_VALS = {7, 7, 7};
    public static final int RIGHT_ULTRASONIC_ECHO_CHANNEL =
                    getConstant("RIGHT_ULTRASONIC_ECHO_CHANNEL");

    private static final double[] DISTANCE_SENSOR_SEPARATION_CM_VALS = {59.69, 59.69, 59.69};
    public static final double DISTANCE_SENSOR_SEPARATION_CM =
                    getConstant("DISTANCE_SENSOR_SEPARATION_CM");

    // Gear handler Arms
    private static final int[] SOLENOID_FORWARD_CHANNEL_VALS = {0, 0, 0};
    public static final int SOLENOID_FORWARD_CHANNEL = getConstant("SOLENOID_FORWARD_CHANNEL");

    private static final int[] SOLENOID_REVERSE_CHANNEL_VALS = {1, 1, 1};
    public static final int SOLENOID_REVERSE_CHANNEL = getConstant("SOLENOID_REVERSE_CHANNEL");

    // Gear sensor
    private static final int[] INFRARED_SENSOR_GEAR_ANALOG_VALS = {1, 1, 1};
    public static final int INFRARED_SENSOR_GEAR_ANALOG =
                    getConstant("INFRARED_SENSOR_GEAR_ANALOG");

    // IR Sensor to detect peg
    private static final int[] INFRARED_SENSOR_PEG_ANALOG_VALS = {2, 2, 2};
    public static final int INFRARED_SENSOR_PEG_ANALOG = getConstant("INFRARED_SENSOR_PEG_ANALOG");

    // Limit switch left
    private static final int[] LIMIT_SWITCH_LEFT_CHANNEL_VALS = {8, 8, 8};
    public static final int LIMIT_SWITCH_LEFT_CHANNEL = getConstant("LIMIT_SWITCH_LEFT_CHANNEL");

    // Limit switch right
    private static final int[] LIMIT_SWITCH_RIGHT_CHANNEL_VALS = {9, 9, 9};
    public static final int LIMIT_SWITCH_RIGHT_CHANNEL = getConstant("LIMIT_SWITCH_RIGHT_CHANNEL");

    // Climber motor
    private static final int[] CLIMBER_MOTOR_VALS = {5, 5, 5};
    public static final int CLIMBER_MOTOR = getConstant("CLIMBER_MOTOR");

    // Climber encoder
    private static final double[] CLIMBER_ENCODER_DPP_VALS = {1.0 / 1075, 1.0 / 1075, 1.0 / 1960};
    public static final double CLIMBER_ENCODER_DPP = getConstant("CLIMBER_ENCODER_DPP");

    // Climber encoder
    private static final int[] CLIMBER_ENCODER_A_VALS = {10, 10, 10};
    public static final int CLIMBER_ENCODER_A = getConstant("CLIMBER_ENCODER_A");

    // Climber encoder
    private static final int[] CLIMBER_ENCODER_B_VALS = {11, 11, 11};
    public static final int CLIMBER_ENCODER_B = getConstant("CLIMBER_ENCODER_B");

    private static final String ROBOT_ID_LOC = "/home/lvuser/robot-type.conf";

    // Raspberry Pi IP for vision *NOTE: Mikes laptop is 10.27.6.10, rPI is 10.27.6.240
    private static final String[] RPI_IPS_VALS = {"10.27.6.10", "10.27.6.10", "10.27.6.10"};
    public static final String RPI_IPS = getConstant("RPI_IPS");

    private static int getRobotID() {
        int temp = 0;

        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(ROBOT_ID_LOC)))) {
            temp = sc.nextInt();
        } catch (FileNotFoundException e) {
            temp = 0;
        }

        return temp;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getConstant(String constant) {
        try {
            return (T) getArray(RobotMap.class.getDeclaredField(constant + "_VALS")
                            .get(null))[ROBOT_ID];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    float boat; // must be a float or else it sinks

    private static Object[] getArray(Object val) {
        int arrlength = Array.getLength(val);
        Object[] outputArray = new Object[arrlength];

        for (int i = 0; i < arrlength; ++i) {
            outputArray[i] = Array.get(val, i);
        }

        return outputArray;
    }
}
