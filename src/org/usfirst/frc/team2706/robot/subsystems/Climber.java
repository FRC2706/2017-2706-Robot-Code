package org.usfirst.frc.team2706.robot.subsystems;

import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotMap;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Coordinates commands for the climber mechanism.
 * 
 * @author wakandacat, FilledWithDetermination, Crazycat200
 */
public class Climber extends Subsystem {
    // Touchpad is 4ft 10in (1.4732 m) above the ground

    // Determines if the robot is climbing (to be verified)
    private static final float I_KNOW_I_AM_CLIMBING_PITCH = 45.0f;

    // Determines if the robot is not climbing (to be verified)
    private static final float I_KNOW_I_AM_FINISHED_CLIMBING_PITCH = 30.0f;

    // This is a wild guess (to be verified)
    private static final double NORMAL_CURRENT_DELTA = 0.5;

    // Flag to determine if we are verifying the climb.
    private boolean verifyingClimb = false;

    // This is the value of the normal current
    private double pastCurrent = 0.0;

    // This is the value of the normal acceleration
    private float pastLinearAccelZ = 0.0f;

    private CANTalon motor = new CANTalon(RobotMap.CLIMBER_MOTOR);
    
    private Encoder encoder = new Encoder(RobotMap.CLIMBER_ENCODER_A, RobotMap.CLIMBER_ENCODER_B);

    public Climber() {
        encoder.setDistancePerPulse(RobotMap.CLIMBER_ENCODER_DPP);
        encoder.reset();
    }
    
    /**
     * Checking conditions to see if the robot is climbing.
     * 
     * @return whether or not the robot is climbing.
     */
    public boolean isClimbing() {
        if ((motor.get() > 0)
                        && (Robot.driveTrain.getGyro().getPitch() > I_KNOW_I_AM_CLIMBING_PITCH)) {
            return true;
        }
        return false;
    }

    /**
     * Verifying if the robot is ready to climb.
     */
    public void startVerifyingClimb() {
        verifyingClimb = false;
    }

    /**
     * Checking conditions to see if the robot is hitting the touchpad.
     * 
     * @return whether or not the robot is hitting the touchpad.
     */
    public boolean isHittingTouchpad() {
        if (verifyingClimb == false) {
            pastCurrent = motor.getOutputCurrent();
            pastLinearAccelZ = Robot.driveTrain.getGyro().getWorldLinearAccelZ();
            verifyingClimb = true;
            return false;
        }
        // The robot starts as not hitting the touchpad
        boolean hittingTouchpad = false;

        // Determines what the present current is
        double presentCurrent = motor.getOutputCurrent();

        // Determines what the present acceleration is
        float presentLinearAccelZ = Robot.driveTrain.getGyro().getWorldLinearAccelZ();

        // Determines where the robot is relative to the ground
        float presentPitch = Robot.driveTrain.getGyro().getPitch();

        if (((presentCurrent - pastCurrent) > NORMAL_CURRENT_DELTA)
                        || ((presentLinearAccelZ - pastLinearAccelZ) < 0)
                        || (presentPitch > I_KNOW_I_AM_FINISHED_CLIMBING_PITCH)) {
            hittingTouchpad = true;
        }
        pastCurrent = presentCurrent;
        pastLinearAccelZ = presentLinearAccelZ;
        return hittingTouchpad;
    }
    
    public double climberDistance() {
        return encoder.getDistance();
        
    }
    public void resetClimberDistance() {
        encoder.reset();
    }
    
    public void initDefaultCommand() {}

    public void climb() {
        motor.set(0.5);
        debugOutput();
    }

    public void stop() {
        motor.set(0.0);
    }
    
    public void setClimberSpeed(double speed) {
        if (speed <= 0) {
            speed = 0;
        } else if (speed >= 1) {
            speed = 1;
        }
        motor.set(speed);
        debugOutput();
    }
    
    public double getSpeed() {
        return (motor.get());
    }
    
    public void debugOutput() {
        System.out.println("Climber: vc=" + verifyingClimb + " speed=" + motor.get() + " pCurr=" + 
                        pastCurrent + " cCurr=" + motor.getOutputCurrent() + " pLAZ=" + pastLinearAccelZ +
                        " cLAZ=" + Robot.driveTrain.getGyro().getWorldLinearAccelZ() + " pitch=" +
                        Robot.driveTrain.getGyro().getPitch() + " enc=" + encoder.getDistance());
    }
}
