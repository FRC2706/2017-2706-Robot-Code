package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import java.util.function.Supplier;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.OI;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.commands.teleop.ArcadeDriveWithJoystick;
import org.usfirst.frc.team2706.robot.controls.RecordableJoystick;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Replays a {@link RecordableJoystick} for the driver and operator Joystick
 */
public class ReplayRecordedJoystick extends Command {

    private final boolean deserializeInConstructor;

    private final Supplier<String> nameSupplier;

    private Joystick driverStick;
    private Joystick operatorStick;

    /**
     * Uses a String as the Supplier for the name
     * 
     * @see #ReplayRecordableJoystick(Joystick, Joystick, Supplier) The main constructor
     */
    public ReplayRecordedJoystick(Joystick driverStick, Joystick operatorStick, String name,
                    boolean deserializeInConstructor) {
        this(driverStick, operatorStick, () -> name, deserializeInConstructor);
    }

    /**
     * Replays the driver and operator Joysticks from files based on the name from the Supplier
     * given
     * 
     * @param driverStick The driver Joystick to replay
     * @param operatorStick The operator Joystick to replay
     * @param nameSupplier The Supplier that is used to find file locations when the command is
     *        enabled
     * @param deserializeInConstructor When true, does not wait until the command is enabled to find
     *        the location of the files, this is important for competitions where delays in the
     *        start of autonomous are a bigger issue
     */
    public ReplayRecordedJoystick(Joystick driverStick, Joystick operatorStick,
                    Supplier<String> nameSupplier, boolean deserializeInConstructor) {
        this.nameSupplier = nameSupplier;

        this.driverStick = driverStick;
        this.operatorStick = operatorStick;

        this.deserializeInConstructor = deserializeInConstructor;

        if (deserializeInConstructor) {
            String name = nameSupplier.get();
            String folder = "/home/lvuser/joystick-recordings/" + name + "/";

            String driverLoc = folder + name + "-driver";
            String operatorLoc = folder + name + "-operator";

            this.driverStick = new RecordableJoystick(driverStick, driverLoc, true);
            this.operatorStick = new RecordableJoystick(operatorStick, operatorLoc, true);

            ((RecordableJoystick) this.driverStick).init(this::timeSinceInitialized);
            ((RecordableJoystick) this.operatorStick).init(this::timeSinceInitialized);
        }
    }

    @Override
    public void initialize() {
        String name = nameSupplier.get();
        String folder = "/home/lvuser/joystick-recordings/" + name + "/";

        if (!deserializeInConstructor) {
            String driverLoc = folder + name + "-driver";
            String operatorLoc = folder + name + "-operator";

            driverStick = new RecordableJoystick(driverStick, driverLoc, true);
            operatorStick = new RecordableJoystick(operatorStick, operatorLoc, true);

            ((RecordableJoystick) driverStick).init(this::timeSinceInitialized);
            ((RecordableJoystick) operatorStick).init(this::timeSinceInitialized);
        }

        if (Robot.driveTrain.getDefaultCommand() instanceof ArcadeDriveWithJoystick) {
            ((ArcadeDriveWithJoystick) Robot.driveTrain.getDefaultCommand())
                            .setJoystick(driverStick);
        }

        Robot.oi.destroy();
        Robot.oi = new OI(driverStick, operatorStick);

        Log.i("Record and Replay", "Replaying joystick from folder " + folder);
    }

    @Override
    public void execute() {
        ((RecordableJoystick) driverStick).update();
        ((RecordableJoystick) operatorStick).update();
    }


    @Override
    public boolean isFinished() {
        return ((RecordableJoystick) driverStick).done()
                        && ((RecordableJoystick) operatorStick).done();
    }

    @Override
    public void end() {
        super.end();
        
        ((RecordableJoystick) driverStick).end();
        ((RecordableJoystick) operatorStick).end();

        if (Robot.driveTrain.getDefaultCommand() instanceof ArcadeDriveWithJoystick) {
            ((ArcadeDriveWithJoystick) Robot.driveTrain.getDefaultCommand())
                            .setJoystick(((RecordableJoystick) driverStick).getRealJoystick());
        }

        Robot.oi.destroy();
        
        Joystick driverStick = this.driverStick, operatorStick = this.operatorStick;
        
        // Make sure that Oi receives a real joystick, not a RecordableJoystick
        while(driverStick instanceof RecordableJoystick) {
            driverStick = ((RecordableJoystick) driverStick).getRealJoystick();
        }
        
        while(operatorStick instanceof RecordableJoystick) {
            operatorStick = ((RecordableJoystick) operatorStick).getRealJoystick();
        }
        
        Robot.oi = new OI(driverStick, operatorStick);
    }

    @Override
    public void interrupted() {
        end();
    }
}
