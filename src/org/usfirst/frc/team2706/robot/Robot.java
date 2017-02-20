
package org.usfirst.frc.team2706.robot;

import org.usfirst.frc.team2706.robot.commands.autonomous.automodes.CenterToLaunch;
import org.usfirst.frc.team2706.robot.commands.autonomous.automodes.SideStartSideGear;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.ReplayRecordedJoystick;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.QuickRotate;
import org.usfirst.frc.team2706.robot.commands.autonomous.movements.StraightDriveWithEncoders;
import org.usfirst.frc.team2706.robot.commands.autonomous.plays.DrivePlaceGear;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;
import org.usfirst.frc.team2706.robot.commands.teleop.RecordJoystick;
import org.usfirst.frc.team2706.robot.subsystems.AutonomousSelector;
import org.usfirst.frc.team2706.robot.subsystems.Camera;
import org.usfirst.frc.team2706.robot.subsystems.Climber;
import org.usfirst.frc.team2706.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2706.robot.subsystems.GearHandler;
import org.usfirst.frc.team2706.robot.subsystems.Bling;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class controls all of the robot initialization, every tick of the robot, and should be very
 * bare bones to preserve readability and simplicity. Do not change the class name without updating
 * the manifest file, and references to different subsystems should be static. Refer to your local
 * gatekeeper if you have no idea what you are doing :)
 */
public class Robot extends IterativeRobot {

    // Reference for the main vision camera on the robot
    public static Camera camera;

    // The robot's main drive train
    public static DriveTrain driveTrain;

    // The spinny dial on the robot that selects what autonomous mode we are going to do
    public static AutonomousSelector hardwareChooser;
    
    // The gear handler arm
    public static GearHandler gearHandler;
    
    // The climber
    public static Climber climber;

    // Stores all of the joysticks, and returns them as read only.
    public static OI oi;

    // This will be the bling subsystem controller
    public static Bling blingSystem;

    // Which command is going to be ran based on the hardwareChooser
    Command autonomousCommand;

    // Records joystick states to file for later replaying
    RecordJoystick recordAJoystick;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    public void robotInit() {

        // Instantiate the robot subsystems
        driveTrain = new DriveTrain();
        // camera = new Camera(Camera.CAMERA_IP);
        
        gearHandler = new GearHandler();
        
        climber = new Climber();

        // New bling system class.
        blingSystem = new Bling();
        blingSystem.batteryInd(1.0); // Display battery voltage.

        oi = new OI();

        // Set up our autonomous modes with the hardware selector switch
        hardwareChooser = new AutonomousSelector(
                    /*  no switch: do nothing      */    new ArcadeDriveWithJoystick(), 
                    /* position 1: do nothing      */    new ArcadeDriveWithJoystick(),
                 /* position 2: Drive to baseline  */    new StraightDriveWithEncoders(0.65,5,25),
     /* position 3: Drive to opposing launch line  */    new StraightDriveWithEncoders(0.65,20,0),
        /* position 4: Center Position place gear  */    new DrivePlaceGear(0.5,4,2),
/* position 5: Right position place gear > launch  */    new SideStartSideGear(true,0.5,7,45,5,2,20),
 /* position 6: Left position place gear > launch  */    new SideStartSideGear(true,0.5,7,45,5,2,20),
         /* position 7: Center and left to launch  */    new CenterToLaunch(false,0.5,9,2,90,7,20),
        /* position 8: Center and right to launch  */    new CenterToLaunch(true,0.5,9,2,90,7,20),
 /* position 9: Left/ gear double side hopper pop  */    new QuickRotate(90),
/* position 10: Right gear double side hopper pop  */    new ReplayRecordedJoystick(oi.getDriverJoystick(), oi.getOperatorJoystick(),
                                                             () -> SmartDashboard.getString("record-joystick-name", "default"), false)
      /* position 11: Left gear middle hopper pop  */
     /* position 12: Right gear middle hopper pop  */
                                                );


        // Set up the Microsoft LifeCam and start streaming it to the Driver Station
        // CameraServer.getInstance().startAutomaticCapture();

        recordAJoystick = new RecordJoystick(oi.getDriverJoystick(), oi.getOperatorJoystick(),
                        () -> SmartDashboard.getString("record-joystick-name", "default"));
    }

    /**
     * This function is called once each time the robot enters Disabled mode. You can use it to
     * reset any subsystem information you want to clear when the robot is disabled.
     */
    public void disabledInit() {}

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select between different
     * autonomous modes using the dashboard. The sendable chooser code works with the Java
     * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
     * uncomment the getString code to get the auto name from the text box below the Gyro
     *
     * You can add additional auto modes by adding additional commands to the chooser code above
     * (like the commented example) or additional comparisons to the switch structure below with
     * additional strings & commands.
     */
    public void autonomousInit() {
        driveTrain.reset();

        // Get the bling doing autonomous patterns.
        blingSystem.auto(); 

        // Great for safety just in case you set the wrong one in practice ;)
        System.out.println("Running " + hardwareChooser.getSelected() + "...");

        autonomousCommand = hardwareChooser.getSelected();

        // Schedule the autonomous command that was selected
        if (autonomousCommand != null)
            autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        log();
    }

    public void teleopInit() {
        /*
         * This makes sure that the autonomous stops running when teleop starts running. If you want
         * the autonomous to continue until interrupted by another command, remove this line or
         * comment it out.
         */
        if (autonomousCommand != null)
            autonomousCommand.cancel();

        if (SmartDashboard.getBoolean("record-joystick", false))
            recordAJoystick.start();

        blingSystem.startTeleOp();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
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
        hardwareChooser.log();
    }
}
