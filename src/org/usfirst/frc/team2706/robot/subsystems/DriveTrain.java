package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The DriveTrain subsystem incorporates the sensors and actuators attached to
 * the robots chassis. These include four drive motors, a left and right encoder
 * and a gyro.
 */
public class DriveTrain extends Subsystem {
	private Victor front_left_motor, back_left_motor,
							front_right_motor, back_right_motor;
	private RobotDrive drive;
	private Encoder left_encoder, right_encoder;
	private AnalogInput rangefinder;
	private AHRS gyro;
	
	// TODO: maybe we don't need this
	private GyroPIDSource gyroPIDSource;
	
	public double initGyro;

	public DriveTrain() {
		super();
		front_left_motor = new Victor(RobotMap.MOTOR_FRONT_LEFT);
		back_left_motor = new Victor(RobotMap.MOTOR_REAR_LEFT);
		front_right_motor = new Victor(RobotMap.MOTOR_FRONT_RIGHT);
		back_right_motor = new Victor(RobotMap.MOTOR_REAR_RIGHT);
		
		front_left_motor.setInverted(RobotMap.MOTOR_FRONT_LEFT_INVERTED);
		back_left_motor.setInverted(RobotMap.MOTOR_REAR_LEFT_INVERTED);
		front_right_motor.setInverted(RobotMap.MOTOR_FRONT_RIGHT_INVERTED);
		back_right_motor.setInverted(RobotMap.MOTOR_REAR_RIGHT_INVERTED);
		
		drive = new RobotDrive(front_left_motor, back_left_motor,
							   front_right_motor, back_right_motor);
		
		left_encoder = new Encoder(8, 7);
		right_encoder = new Encoder(0,1);

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
			left_encoder.setDistancePerPulse((4.0/12.0*Math.PI) / 360.0);
			right_encoder.setDistancePerPulse((4.0/12.0*Math.PI) / 360.0);
		}

		// @TODO: Use RobotMap values
		rangefinder = new AnalogInput(6);
		
		// Set up navX gyro
		gyro = new AHRS(SPI.Port.kMXP);
		while(gyro.isCalibrating()) {
			;
		}
		
		gyroPIDSource = new GyroPIDSource(this);
		
		reset();
		

		
		// Let's show everything on the LiveWindow
		LiveWindow.addActuator("Drive Train", "Front Left Motor", front_left_motor);
		LiveWindow.addActuator("Drive Train", "Back Left Motor",  back_left_motor);
		LiveWindow.addActuator("Drive Train", "Front Right Motor",  front_right_motor);
		LiveWindow.addActuator("Drive Train", "Back Right Motor", back_right_motor);
		LiveWindow.addSensor("Drive Train", "Left Encoder", left_encoder);
		LiveWindow.addSensor("Drive Train", "Right Encoder", right_encoder);
		LiveWindow.addSensor("Drive Train", "Rangefinder", rangefinder);
		LiveWindow.addSensor("Drive Train", "Gyro", gyro);
	}

	/**
	 * When no other command is running let the operator drive around
	 * using the Xbox joystick.
	 */
	public void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveWithJoystick());
	}

	/**
	 * The log method puts interesting information to the SmartDashboard.
	 */
	public void log() {
		SmartDashboard.putNumber("Left Distance", left_encoder.getDistance());
		SmartDashboard.putNumber("Right Distance", right_encoder.getDistance());
		SmartDashboard.putNumber("Left Speed (RPM)", left_encoder.getRate());
		SmartDashboard.putNumber("Right Speed (RPM)", right_encoder.getRate());
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
	}

	/**
	 * Tank style driving for the DriveTrain.
	 * @param left Speed in range [-1,1]
	 * @param right Speed in range [-1,1]
	 */
	public void drive(double left, double right) {
		drive.tankDrive(left, right);
	}

	/**
	 * @param joy The Xbox style joystick to use to drive arcade style.
	 */
	public void drive(GenericHID joy) {
		drive.arcadeDrive(RobotMap.INVERT_JOYSTICK_Y ? -joy.getRawAxis(5) : joy.getRawAxis(5), 
				RobotMap.INVERT_JOYSTICK_X ? -joy.getRawAxis(4) : joy.getRawAxis(4), true);
	}

	/**
	 * Reset the robots sensors to the zero states.
	 */
	public void reset() {
		left_encoder.reset();
		right_encoder.reset();
		resetGyro();
	}
	
	/**
	 * Reset the robot gyro to the zero state.
	 */
	public void resetGyro() {
		gyro.reset();
	}
	
	/**
	 * @return The robots heading in degrees.
	 */
	public double getHeading() {
		return gyro.getAngle();
	}
	
	/**
	 * @param invert True to invert second motor direction for rotating
	 * 
	 * @return The robot's drive PIDOutput
	 */
	public PIDOutput getDrivePIDOutput(boolean invert, boolean left) {
		if(left) {
			DrivePIDOutput out = new DrivePIDOutput(front_left_motor, back_left_motor, left, invert);
			return out;
		}
		else {
			DrivePIDOutput out = new DrivePIDOutput(front_right_motor, back_right_motor, left, invert);
			return out;
		}
	}
	
	/**
	 * @return The robot's gyro PIDSource
	 */
	public PIDSource getGyroPIDSource(boolean invert) {
		gyroPIDSource.invert(invert);
		return gyroPIDSource;
	}

	public void inverGyroPIDSource(boolean invert) {
		gyroPIDSource.invert(invert);
	}

	/**
	 * @return The distance driven (average of left and right encoders).
	 */
	public double getDistance() {
		return (left_encoder.getDistance() + right_encoder.getDistance())/2;
	}

	/**
	 * @return The robot's encoder PIDSource
	 */
	public PIDSource getEncoderPIDSource(boolean left) {
		if(left) {
			return left_encoder;
		}
		else {
			return right_encoder;
		}
	}
	
	/**
	 * @return The distance to the obstacle detected by the rangefinder.
	 */
	public double getDistanceToObstacle() {
		// Really meters in simulation since it's a rangefinder...
		return rangefinder.getAverageVoltage();
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
			if(heading > 358.0)
				heading = 0;
			
			return invert ? -heading : heading;
		}
		
		
		public void invert(boolean invert) {
			this.invert = invert;
		}
	}
	
	public class DrivePIDOutput implements PIDOutput {

		private final Victor front;
		private final Victor rear;
		
		private boolean invert;
		
		private final boolean left;
		
		public DrivePIDOutput(Victor front, Victor rear, boolean left, boolean invert) {
			this.front = front;
			this.rear = rear;
			this.left = left;
			this.invert = invert;
		}
		
		@Override
		public void pidWrite(double output) {
			double rotateVal = (normalize(getHeading() - initGyro) * 0.1);
			
			System.out.println("Rotate:\t"+rotateVal);
			
			// XXX: Motors must be opposite to avoid fighting
			if(left)
				if(invert) {
					drive.arcadeDrive(output, rotateVal);
//					front.set(asSpeed(output, rotateVal, true));
//					rear.set(asSpeed(output, rotateVal, true));
				}
				else {
					drive.arcadeDrive(-output, -rotateVal);
//					front.set(asSpeed(-output, -rotateVal, true));
//					rear.set(asSpeed(-output, -rotateVal, true));
				}
			else
				if(invert) {
					front.set(asSpeed(output, rotateVal, false));
					rear.set(asSpeed(output, rotateVal, false));
				}
				else {
					front.set(asSpeed(-output, -rotateVal, false));
					rear.set(asSpeed(-output, -rotateVal, false));
				}
		}		
		
		public void setInvert(boolean invert) {
			this.invert = invert;
		}
	}
	
    
    private double asSpeed(double moveValue, double rotateValue, boolean left) {

    	double leftMotorSpeed = 0;
    	double rightMotorSpeed = 0;
    	
    	if (moveValue > 0.0) {
    		if (rotateValue > 0.0) {
    			leftMotorSpeed = moveValue - rotateValue;
    			rightMotorSpeed = Math.max(moveValue, rotateValue);
    		}
    		else {
    			leftMotorSpeed = Math.max(moveValue, -rotateValue);
    			rightMotorSpeed = moveValue + rotateValue;
    		}
    	}
    	else {
    		if (rotateValue > 0.0) {
    			leftMotorSpeed = -Math.max(-moveValue, rotateValue);
    			rightMotorSpeed = moveValue + rotateValue;
    		}
    		else {
    			leftMotorSpeed = moveValue - rotateValue;
    			rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
    		}
    	}
		
    	System.out.println("Drive:\t" + (left ? leftMotorSpeed : rightMotorSpeed));
    	
		return left ? leftMotorSpeed : rightMotorSpeed;
    }
    
    private double normalize(double input) {
    	double normalizedValue = input;
    	while (normalizedValue > 180)
    		normalizedValue -= 360;
    	while (normalizedValue < -180)
    		normalizedValue +=360;
    	
		return normalizedValue;
	}	
}
