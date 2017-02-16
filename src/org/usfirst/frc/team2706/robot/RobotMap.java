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
    private static final int[] MOTOR_FRONT_LEFT_VALS = {0, 2, 3};
    public static final int MOTOR_FRONT_LEFT = getConstant("MOTOR_FRONT_LEFT");

    private static final boolean[] MOTOR_FRONT_LEFT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_FRONT_LEFT_INVERTED =
                    getConstant("MOTOR_FRONT_LEFT_INVERTED");


    // Right gearbox
    private static final int[] MOTOR_FRONT_RIGHT_VALS = {2, 5, 5};
    public static final int MOTOR_FRONT_RIGHT = getConstant("MOTOR_FRONT_RIGHT");

    private static final boolean[] MOTOR_FRONT_RIGHT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_FRONT_RIGHT_INVERTED =
                    getConstant("MOTOR_FRONT_RIGHT_INVERTED");


    // Left gearbox
    private static final int[] MOTOR_REAR_LEFT_VALS = {1, 3, 3};
    public static final int MOTOR_REAR_LEFT = getConstant("MOTOR_REAR_LEFT");

    private static final boolean[] MOTOR_REAR_LEFT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_REAR_LEFT_INVERTED = getConstant("MOTOR_REAR_LEFT_INVERTED");


    // Right gearbox
    private static final int[] MOTOR_REAR_RIGHT_VALS = {3, 6, 6};
    public static final int MOTOR_REAR_RIGHT = getConstant("MOTOR_REAR_RIGHT");

    private static final boolean[] MOTOR_REAR_RIGHT_INVERTED_VALS = {true, true, false};
    public static final boolean MOTOR_REAR_RIGHT_INVERTED =
                    getConstant("MOTOR_REAR_RIGHT_INVERTED");


    private static final int[] CAN_ADDR_PNEUMATIC_VALS = {0, 0, 0};
    public static final int CAN_ADDR_PNEUMATIC = getConstant("CAN_ADDR_PNEUMATIC");


    private static final int[] CAN_INTAKE_LEFT_VALS = {2, 2, 2};
    public static final int CAN_INTAKE_LEFT = getConstant("CAN_INTAKE_LEFT");

    private static final int[] CAN_INTAKE_RIGHT_VALS = {1, 1, 1};
    public static final int CAN_INTAKE_RIGHT = getConstant("CAN_INTAKE_RIGHT");


    // Motor for panning camera
    private static final int[] MOTOR_CAMERA_PAN_VALS = {8, 8, 8};
    public static final int MOTOR_CAMERA_PAN = getConstant("MOTOR_CAMERA_PAN");


    // Motor for tilting camera
    private static final int[] MOTOR_CAMERA_TILT_VALS = {9, 9, 9};
    public static final int MOTOR_CAMERA_TILT = getConstant("MOTOR_CAMERA_TILT");

    // XXX: Encoders got flipped
    private static final int[] ENCODER_LEFT_A_VALS = {8, 3, 2};
    public static final int ENCODER_LEFT_A = getConstant("ENCODER_LEFT_A");

    private static final int[] ENCODER_LEFT_B_VALS = {7, 2, 3};
    public static final int ENCODER_LEFT_B = getConstant("ENCODER_LEFT_B");

    private static final double[] ENCODER_LEFT_DPP_VALS = {1.0 / 1075, 1.0 / 1075, 1.0 / 1960};
    public static final double ENCODER_LEFT_DPP = getConstant("ENCODER_LEFT_DPP");


    private static final int[] ENCODER_RIGHT_A_VALS = {0, 5, 1};
    public static final int ENCODER_RIGHT_A = getConstant("ENCODER_RIGHT_A");

    private static final int[] ENCODER_RIGHT_B_VALS = {1, 6, 0};
    public static final int ENCODER_RIGHT_B = getConstant("ENCODER_RIGHT_B");

    private static final double[] ENCODER_RIGHT_DPP_VALS = {1.0 / 1075, 1.0 / 1075, 1.0 / 1960};
    public static final double ENCODER_RIGHT_DPP = getConstant("ENCODER_RIGHT_DPP");


    private static final boolean[] INVERT_JOYSTICK_X_VALS = {false, false, false};
    public static final boolean INVERT_JOYSTICK_X = getConstant("INVERT_JOYSTICK_X");

    private static final boolean[] INVERT_JOYSTICK_Y_VALS = {false, false, false};
    public static final boolean INVERT_JOYSTICK_Y = getConstant("INVERT_JOYSTICK_Y");


    private static final int[] SELECTOR_CHANNEL_VALS = {0, 0, 0};
    public static final int SELECTOR_CHANNEL = getConstant("SELECTOR_CHANNEL");


    private static final int[] RING_LIGHT_VALS = {6, 6, 6};
    public static final int RING_LIGHT = getConstant("RING_LIGHT");


    // Compressors
    // FIXME! There were no values before, so I set them all to 0
    private static final int[] BALL_KICKER_A_VALS = {0, 0, 0};
    public static final int BALL_KICKER_A = getConstant("BALL_KICKER_A");

    private static final int[] BALL_KICKER_B_VALS = {0, 0, 0};
    public static final int BALL_KICKER_B = getConstant("BALL_KICKER_B");

    private static final int[] ARM_A_VALS = {0, 0, 0};
    public static final int ARM_A = getConstant("ARM_A");

    private static final int[] ARM_B_VALS = {0, 0, 0};
    public static final int ARM_B = getConstant("ARM_B");

    private static final int[] FLOAT_A_VALS = {0, 0, 0};
    public static final int FLOAT_A = getConstant("FLOAT_A");

    private static final int[] FLOAT_B_VALS = {0, 0, 0};
    public static final int FLOAT_B = getConstant("FLOAT_B");

    private static final int[] LEFT_ULTRASONIC_PING_CHANNEL_VALS = {4, 4, 4};
    public static final int LEFT_ULTRASONIC_PING_CHANNEL =
                    getConstant("LEFT_ULTRASONIC_PING_CHANNEL");

    private static final int[] LEFT_ULTRASONIC_ECHO_CHANNEL_VALS = {3, 3, 3};
    public static final int LEFT_ULTRASONIC_ECHO_CHANNEL =
                    getConstant("LEFT_ULTRASONIC_ECHO_CHANNEL");

    private static final int[] RIGHT_ULTRASONIC_PING_CHANNEL_VALS = {5, 5, 5};
    public static final int RIGHT_ULTRASONIC_PING_CHANNEL =
                    getConstant("RIGHT_ULTRASONIC_PING_CHANNEL");

    private static final int[] RIGHT_ULTRASONIC_ECHO_CHANNEL_VALS = {6, 6, 6};
    public static final int RIGHT_ULTRASONIC_ECHO_CHANNEL =
                    getConstant("RIGHT_ULTRASONIC_ECHO_CHANNEL");


    private static final String ROBOT_ID_LOC = "/home/lvuser/robot-type.conf";

    // Bling Various
    // Number of pixels in LED Strip
    public static final int LED_Strip_PixelCount = 120;
    
    // Battery Variable which equals the voltage of the battery
    public static final double BatteryCapacity = 12.0;
                    
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
