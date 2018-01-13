package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The DriveTrain subsystem incorporates the sensors and actuators attached to the robots chassis.
 * These include four drive motors, a left and right encoder and a gyro.
 */
public class DriveTrain extends Subsystem {
    private CANTalon front_left_motor, back_left_motor, front_right_motor, back_right_motor;
    private RobotDrive drive;
    private Encoder left_encoder, right_encoder;
    private Ultrasonic leftDistanceSensor, rightDistanceSensor;
    private AHRS gyro;

    // TODO: maybe we don't need this
    private GyroPIDSource gyroPIDSource;
    private AverageEncoderPIDSource encoderPIDSource;
    private UltrasonicPIDSource ultrasonicPIDSource;

    /**
     * Used to determine what angle is desired when using straight drive
     */
    public double initGyro;

    private Command defaultCommand;
    
    // The spinny dial on the robot that selects what autonomous mode we are going to do
    private AutonomousSelector selectorSwitch;

    /**
     * Sets up all of the sensors and motors used in driving
     */
    public DriveTrain() {
        super();
        front_left_motor = new CANTalon(RobotMap.MOTOR_FRONT_LEFT);
        back_left_motor = new CANTalon(RobotMap.MOTOR_REAR_LEFT);
        front_right_motor = new CANTalon(RobotMap.MOTOR_FRONT_RIGHT);
        back_right_motor = new CANTalon(RobotMap.MOTOR_REAR_RIGHT);

        front_left_motor.setInverted(RobotMap.MOTOR_FRONT_LEFT_INVERTED);
        back_left_motor.setInverted(RobotMap.MOTOR_REAR_LEFT_INVERTED);
        front_right_motor.setInverted(RobotMap.MOTOR_FRONT_RIGHT_INVERTED);
        back_right_motor.setInverted(RobotMap.MOTOR_REAR_RIGHT_INVERTED);

        drive = new RobotDrive(front_left_motor, back_left_motor, front_right_motor,
                        back_right_motor);

        left_encoder = new Encoder(RobotMap.ENCODER_LEFT_A, RobotMap.ENCODER_LEFT_B);
        right_encoder = new Encoder(RobotMap.ENCODER_RIGHT_A, RobotMap.ENCODER_RIGHT_B);

        // Encoders may measure differently in the real world and in
        // simulation. In this example the robot move at some random value
        // per tick in the real world, but the simulated encoders
        // simulate 360 tick encoders. This if statement allows for the
        // real robot to handle this difference in devices.
        if (Robot.isReal()) {
            left_encoder.setDistancePerPulse(RobotMap.ENCODER_LEFT_DPP);
            right_encoder.setDistancePerPulse(RobotMap.ENCODER_RIGHT_DPP);
        } else {
            // Circumference in ft = 4in/12(in/ft)*PI
            left_encoder.setDistancePerPulse((4.0 / 12.0 * Math.PI) / 360.0);
            right_encoder.setDistancePerPulse((4.0 / 12.0 * Math.PI) / 360.0);
        }

        leftDistanceSensor = new Ultrasonic(RobotMap.LEFT_ULTRASONIC_PING_CHANNEL,
                        RobotMap.LEFT_ULTRASONIC_ECHO_CHANNEL);
        rightDistanceSensor = new Ultrasonic(RobotMap.RIGHT_ULTRASONIC_PING_CHANNEL,
                        RobotMap.RIGHT_ULTRASONIC_ECHO_CHANNEL);

        leftDistanceSensor.setAutomaticMode(true);

        encoderPIDSource = new AverageEncoderPIDSource(left_encoder, right_encoder);
        ultrasonicPIDSource = new UltrasonicPIDSource(leftDistanceSensor, rightDistanceSensor);

        // Set up navX gyro
        gyro = new AHRS(SPI.Port.kMXP);
        while (gyro.isCalibrating()) {
            ;
        }

        gyroPIDSource = new GyroPIDSource(this);

        reset();

        selectorSwitch = new AutonomousSelector();

        // Let's show everything on the LiveWindow
        LiveWindow.addActuator("Drive Train", "Front Left Motor", front_left_motor);
        LiveWindow.addActuator("Drive Train", "Back Left Motor", back_left_motor);
        LiveWindow.addActuator("Drive Train", "Front Right Motor", front_right_motor);
        LiveWindow.addActuator("Drive Train", "Back Right Motor", back_right_motor);
        LiveWindow.addSensor("Drive Train", "Left Encoder", left_encoder);
        LiveWindow.addSensor("Drive Train", "Right Encoder", right_encoder);
        LiveWindow.addSensor("Drive Train", "Left Distance Sensor", leftDistanceSensor);
        LiveWindow.addSensor("Drive Train", "Right Distance Sensor", rightDistanceSensor);
        LiveWindow.addSensor("Drive Train", "Gyro", gyro);
        LiveWindow.addSensor("Drive Train", "Autonomous Selector", selectorSwitch);
    }

    /**
     * When no other command is running let the operator drive around using the Xbox joystick.
     */
    public void initDefaultCommand() {
        if (defaultCommand == null) {
            getDefaultCommand();
        }
        setDefaultCommand(defaultCommand);
    }

    /**
     * Gets the default command that should run when no other commands are scheduled
     */
    public Command getDefaultCommand() {
        if (defaultCommand == null) {
            defaultCommand = new ArcadeDriveWithJoystick();
        }
        return defaultCommand;
    }

    /**
     * Get the NavX AHRS
     * 
     * @return the NavX AHRS
     */
    public AHRS getGyro() {
        return gyro;
    }

    /**
     * The log method puts interesting information to the SmartDashboard.
     */
    public void log() {
        SmartDashboard.putNumber("Left Distance", left_encoder.getDistance());
        SmartDashboard.putNumber("Right Distance", right_encoder.getDistance());
        SmartDashboard.putNumber("Left Speed (RPM)", left_encoder.getRate());
        SmartDashboard.putNumber("Right Speed (RPM)", right_encoder.getRate());
        SmartDashboard.putNumber("Left Distance Sensor", leftDistanceSensor.getRangeInches());
        SmartDashboard.putNumber("Right Distance Sensor", rightDistanceSensor.getRangeInches());
        SmartDashboard.putNumber("Gyro", gyro.getAngle());
        SmartDashboard.putNumber("Autonomous Selector", selectorSwitch.getVoltageAsIndex());
    }

    /**
     * Tank style driving for the DriveTrain.
     * 
     * @param left Speed in range [-1,1]
     * @param right Speed in range [-1,1]
     */
    public void drive(double left, double right) {
        drive.tankDrive(left, right);
    }

    /**
     * @param joy The Xbox style joystick to use to drive arcade style.
     */
    public void arcadeDrive(double forward, double turn) {
        drive.arcadeDrive(forward, turn, true);
    }

    /**
     * @param joy The Xbox style joystick to use to drive arcade style.
     */
    public void drive(GenericHID joy) {
        double YAxis = RobotMap.INVERT_JOYSTICK_Y ? -joy.getRawAxis(5) : joy.getRawAxis(5);
        double XAxis = RobotMap.INVERT_JOYSTICK_X ? -joy.getRawAxis(4) : joy.getRawAxis(4);
        if (joy.getRawButton(9)) {
            XAxis *= 0.7;
            YAxis *= 0.7;
        }
        drive.arcadeDrive(YAxis, XAxis, true);

    }

    /**
     * Untested code that should allow you to drive relative to the field instead of the robot TODO
     * add a rotation joystick function for robot relative control when needed to align
     * 
     * @param joy The main drive joystick
     * @param rotate Joystick to rotate the robot with
     */
    public void headlessDrive(GenericHID joy) {
        Log.d("HeadlessDrive", joy.getRawAxis(5) + "," + joy.getRawAxis(4));
        double raw5 = joy.getRawAxis(5);
        double raw4 = joy.getRawAxis(4);
        double angle = normalize(Math.toDegrees(Math.atan(raw5 / raw4)));

        double speed = (raw5 + raw4) / 2; // hyp
        if (Math.abs(speed) < 0.1) {
            speed = 0;
            angle = Robot.driveTrain.getHeading();
        }
        Log.d("HeadlessDrive", "Angle: " + angle + ", Speed: " + speed);
        double gyroAngle;
        gyroAngle = normalize(Robot.driveTrain.getHeading());
        Log.d("HeadlessDrive", (angle - gyroAngle * 0.1) * speed);
        drive.arcadeDrive(-speed, -(angle - gyroAngle * 0.1) * speed, true);
    }

    /**
     * Reset the robots sensors to the zero states.
     */
    public void reset() {
        resetEncoders();
        resetGyro();
        resetDisplacement();
    }

    /**
     * Reset the robot gyro to the zero state.
     */
    public void resetGyro() {
        gyro.reset();
    }

    /**
     * Reset the robot encoders to zero states
     */
    public void resetEncoders() {
        left_encoder.reset();
        right_encoder.reset();
    }

    /**
     * @return The robots heading in degrees.
     */
    public double getHeading() {
        return gyro.getAngle();
    }

    public void setAutonomousCommandList(Command...commands) {
        selectorSwitch.free();
        selectorSwitch = new AutonomousSelector(commands);
    }
    
    public Command getAutonomousCommand() {
        return selectorSwitch.getSelected();
    }
    
    /**
     * Resets the displacement of the robot
     */
    public void resetDisplacement() {
        gyro.resetDisplacement();
    }

    /**
     * Gets the x distance of the robot with a direction
     */
    public double getDisplacementX() {
        return gyro.getDisplacementX();
    }

    /**
     * Gets the y distance of the robot with a direction
     */

    public double getDisplacementY() {
        return gyro.getDisplacementY();
    }

    /**
     * Gets the z distance of the robot with a direction
     */
    public double getDisplacementZ() {
        return gyro.getDisplacementZ();
    }

    /**
     * Sets the CANTalon motors to go into brake mode or coast mode
     * 
     * @param on Set to brake when true and coast when false
     */
    public void brakeMode(boolean on) {
        front_left_motor.enableBrakeMode(on);
        back_left_motor.enableBrakeMode(on);
        front_right_motor.enableBrakeMode(on);
        back_right_motor.enableBrakeMode(on);
    }

    /**
     * @param useGyroStraightening True to invert second motor direction for rotating
     * 
     * @return The robot's drive PIDOutput
     */
    public PIDOutput getDrivePIDOutput(boolean useGyroStraightening, boolean useCamera,
                    boolean invert) {
        return new DrivePIDOutput(drive, useGyroStraightening, useCamera, invert);
    }

    /**
     * @return The robot's gyro PIDSource
     */
    public PIDSource getGyroPIDSource(boolean invert) {
        gyroPIDSource.invert(invert);
        return gyroPIDSource;
    }

    public void invertGyroPIDSource(boolean invert) {
        gyroPIDSource.invert(invert);
    }

    /**
     * Takes values of the two distance sensors and finds the angle the robot is on with the wall
     * 
     * @return -90 to 90 degrees
     */
    public double GetAngleWithDistanceSensors() {
        double opposite = getRightDistanceToObstacle() - getLeftDistanceToObstacle();
        // Converts centimeters to inches so the two measurements match up
        double adjacent = RobotMap.DISTANCE_SENSOR_SEPARATION_CM / 2.54;
        // Inverse tangent to take two sides of the triangle and get the angle
        double theta = Math.toDegrees(Math.atan2(opposite, adjacent));
        Log.d("Degree Sensor Angle", theta);

        return theta;
    }

    /**
     * @return The distance driven (average of left and right encoders).
     */
    public double getDistance() {
        return (left_encoder.getDistance() + right_encoder.getDistance()) / 2;
    }

    /**
     * Gets the distance on the left distance sensor in inches
     * 
     * @return Distance in inches
     */
    public double getLeftDistanceToObstacle() {
        return leftDistanceSensor.getRangeInches();
    }

    /**
     * Gets the distance on the left distance sensor in inches
     * 
     * @return Distance in inches
     */
    public double getRightDistanceToObstacle() {
        return rightDistanceSensor.getRangeInches();
    }

    /**
     * @return The robot's encoder PIDSource
     */
    public PIDSource getEncoderPIDSource(boolean left) {
        if (left) {
            return left_encoder;
        } else {
            return right_encoder;
        }

    }

    /**
     * Gets a PIDSource that combines both encoders as one input
     * 
     * @return The PIDSource
     */
    public PIDSource getAverageEncoderPIDSource() {
        return encoderPIDSource;
    }

    /**
     * @return The distance to the obstacle detected by the distance sensor.
     */
    public double getDistanceToObstacle() {
        return (leftDistanceSensor.getRangeInches() + rightDistanceSensor.getRangeInches()) / 2;
    }

    /**
     * Gets a PIDSource for the distance sensor using distance in inches for input
     * 
     * @return The PIDSource
     */
    public PIDSource getDistanceSensorPIDSource() {
        return ultrasonicPIDSource;
    }

    class UltrasonicPIDSource implements PIDSource {

        private final Ultrasonic left, right;

        public UltrasonicPIDSource(Ultrasonic left, Ultrasonic right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            left.setPIDSourceType(pidSource);
            right.setPIDSourceType(pidSource);
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return left.getPIDSourceType();
        }

        @Override
        public double pidGet() {
            return (left.getRangeInches() + right.getRangeInches()) / 2;
        }

    }

    class AverageEncoderPIDSource implements PIDSource {

        private final Encoder left, right;

        public AverageEncoderPIDSource(Encoder left, Encoder right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            left.setPIDSourceType(pidSource);
            right.setPIDSourceType(pidSource);
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return left.getPIDSourceType();
        }

        @Override
        public double pidGet() {
            return (left.getDistance() + right.getDistance()) / 2;
        }

    }


    class GyroPIDSource implements PIDSource {

        private final DriveTrain driveTrain;
        private boolean invert;

        public GyroPIDSource(DriveTrain driveTrain) {
            this.driveTrain = driveTrain;
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            driveTrain.gyro.setPIDSourceType(pidSource);
        };

        @Override
        public PIDSourceType getPIDSourceType() {
            return driveTrain.gyro.getPIDSourceType();
        }

        @Override
        public double pidGet() {
            double heading = driveTrain.getHeading();
            if (heading > 358.0)
                heading = 0;

            return invert ? -heading : heading;
        }


        public void invert(boolean invert) {
            this.invert = invert;
        }
    }

    /**
     * PIDOutput that drives the robot using its four motors
     */
    public class DrivePIDOutput implements PIDOutput {

        private final RobotDrive drive;

        private boolean invert;

        private final boolean useGyroStraightening;

        private final boolean useCamera;

        /**
         * Sets up the PIDOutput
         * 
         * @param drive The RobotDrive to pass the outputs to
         * @param useGyroStraightening When true makes sure robot stays at same rotation throughout
         *        driving
         * @param useCamera Whether to align using vision data
         * @param invert Invert driving
         */
        public DrivePIDOutput(RobotDrive drive, boolean useGyroStraightening, boolean useCamera,
                        boolean invert) {
            this.drive = drive;
            this.useGyroStraightening = useGyroStraightening;
            this.useCamera = useCamera;
            this.invert = invert;
        }

        @Override
        public void pidWrite(double output) {

            double rotateVal;
            if (useCamera) {
                // Checks if target is found, cuts off the edges, and then creates a rotation value
                if (Robot.camera.getTarget() != null) {
                    if (Robot.camera.getTarget().ctrX > -0.8 && Robot.camera.getTarget().ctrX < 0.8
                                    && Robot.camera.getTarget().ctrY > -0.8
                                    && Robot.camera.getTarget().ctrY < 0.8) {
                        rotateVal = Robot.camera.getTarget() != null
                                        ? (Robot.camera.getTarget().ctrY + 0.05) * 1.7 : 0;
                        if (rotateVal > 0.6) {
                            rotateVal = 0.6;
                        }
                        if (rotateVal < -0.6) {
                            rotateVal = -0.6;
                        }
                    } else {
                        rotateVal = 0;
                    }
                } else {
                    rotateVal = 0;
                }
            } else {
                rotateVal = normalize(getHeading() - initGyro) * 0.15;
            }


            if (useGyroStraightening)
                if (invert) {
                    drive.arcadeDrive(output, rotateVal);
                } else {
                    drive.arcadeDrive(-output, -rotateVal);
                }
            else if (invert) {
                drive.setLeftRightMotorOutputs(-output, output);
            } else {
                drive.setLeftRightMotorOutputs(output, output);
            }
        }

        /**
         * When true inverts driving
         * 
         * @param invert Whether to invert or not
         */
        public void setInvert(boolean invert) {
            this.invert = invert;
        }
    }

    /**
     * Normalizes an angle to between -180 and 180
     * 
     * @param input The angle to normalize
     * @return The normalized angle
     */
    public double normalize(double input) {
        double normalizedValue = input;
        while (normalizedValue > 180)
            normalizedValue -= 360;
        while (normalizedValue < -180)
            normalizedValue += 360;

        return normalizedValue;
    }
}
