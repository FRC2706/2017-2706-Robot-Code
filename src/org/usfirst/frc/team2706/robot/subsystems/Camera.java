package org.usfirst.frc.team2706.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team2706.robot.Log;
import org.usfirst.frc.team2706.robot.Robot;
import org.usfirst.frc.team2706.robot.RobotMap;
import org.usfirst.frc.team2706.robot.subsystems.TrackerBox2.TargetObject;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Does everything java related to the rPI camera, servo controllers have been deleted.
 */
public class Camera extends Subsystem {


    TrackerBox2 trackerbox = new TrackerBox2(RobotMap.RPI_IPS);
    public boolean PRINT_STUFF = true;
    public final int visionDataPort = 1182;

    private TargetObject target = null;

    @SuppressWarnings("unused")
    private DigitalOutput ringLightRelay = new DigitalOutput(RobotMap.RING_LIGHT);

    @Override
    protected void initDefaultCommand() {}

    public Camera() {
        super();
    }

    public void GetTargets(boolean auto) {
        ArrayList<TrackerBox2.TargetObject> targets = trackerbox.getVisionData();

        if (targets != null) {
            if (PRINT_STUFF) {
                Log.d("Camera", "I found " + targets.size() + " targets.");
                for (TrackerBox2.TargetObject target : targets)
                    Log.d("Camera", "\tI found: " + target.toString());
                System.out.println();
            }
            if (!targets.isEmpty()) {
                target = targets.get(0);
            } else {
                target = null;
            }
        } else {
            target = null;
        }
        
        if(auto)
            Robot.blingSystem.toggleAutoState(target != null);
    }

    public TargetObject getTarget() {
        return target;
    }
}
