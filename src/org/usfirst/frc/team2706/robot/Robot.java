
package org.usfirst.frc.team2706.robot;

import org.usfirst.frc.team2706.robot.commands.autonomous.automodes.ExampleAutoMode;
import org.usfirst.frc.team2706.robot.commands.camera.AutomaticCameraControl;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;
import org.usfirst.frc.team2706.robot.commands.teleop.TeleopPneumaticControl;
import org.usfirst.frc.team2706.robot.subsystems.AutonomousSelector;
import org.usfirst.frc.team2706.robot.subsystems.Camera;
import org.usfirst.frc.team2706.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * This class controls all of the robot initialization, every tick of the robot,
 * and should be very bare bones to preserve readability and simplicity.
 * Do not change the class name without updating the manifest file, and
 * references to different subsystems should be static.
 * Refer to your local gatekeeper if you have no idea what you are doing :)
 */
public class Robot extends IterativeRobot {
	// Reference for the main vision camera on the robot
	public static Camera camera;
	
	// The robot's main drive train
	public static DriveTrain driveTrain;
	
	// The spinny dial on the robot that selects what autonomous mode we are going to do
	public static AutonomousSelector hardwareChooser;
	
	// Stores all of the joysticks, and returns them as read only.
	public static OI oi;

	// Which command is going to be ran based on the hardwareChooser
	Command autonomousCommand;
	
	// The camera has 3 different modes, controls which mode the camera is in
	AutomaticCameraControl cameraCommand;
	
	// Uses the joysticks to control the robot in teleop mode
    TeleopPneumaticControl teleopControl;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	oi = new OI();
		
		// Instantiate the robot subsystems
        driveTrain = new DriveTrain();      
        camera = new Camera(Camera.CAMERA_IP);
        
        // Set up our autonomous modes with the hardware selector switch
        hardwareChooser = new AutonomousSelector(
        	/*  no switch: do nothing      */	 new ArcadeDriveWithJoystick(), 
        	/* position 1: do nothing      */	 new ArcadeDriveWithJoystick(),
      /* position 2: Run example automode  */	 new ExampleAutoMode()
     										    );
        
        teleopControl = new TeleopPneumaticControl();

		// Set up the Microsoft LifeCam and start streaming it to the Driver Station
		CameraServer server = CameraServer.getInstance();
		server.startAutomaticCapture(0);	
    
		cameraCommand = new AutomaticCameraControl();
		
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){
    	teleopControl.cancel();
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
    	driveTrain.reset();
        cameraCommand.start();
    	
        // Great for safety just incase you set the wrong one in practice ;)
    	System.out.println("Running " + hardwareChooser.getSelected() + "...");
    	
        autonomousCommand = hardwareChooser.getSelected();
        
    	// Schedule the autonomous command that was selected
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        log();
    }

    public void teleopInit() {
		/* This makes sure that the autonomous stops running when
         teleop starts running. If you want the autonomous to 
         continue until interrupted by another command, remove
         this line or comment it out. */
        if (autonomousCommand != null) autonomousCommand.cancel();
         cameraCommand.start();
         cameraCommand.cancel(); // Uncomment/comment to disable/enable camera movement
        Robot.camera.ResetCamera();
        teleopControl.start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	Robot.camera.RobotTurnDegrees();
        Scheduler.getInstance().run();
        log();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    private void log() {
        driveTrain.log();
    }
}
