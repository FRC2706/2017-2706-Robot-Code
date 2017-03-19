package org.usfirst.frc.team2706.robot.subsystems;

import java.util.ArrayList;

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
    
    private DigitalOutput ringLightRelay = new DigitalOutput(RobotMap.RING_LIGHT);

    @Override
    protected void initDefaultCommand() {}

    public Camera() {
        super();
    }

    /**
     * Enable the camera ring light.
     * 
     * @param enable the desired ring light state
     */
    public void enableRingLight(boolean enable) {
        ringLightRelay.set(enable);
    }
    
    public void GetTargets() {
        ArrayList<TrackerBox2.TargetObject> targets = trackerbox.getVisionData();

        if (targets != null) {
            if (PRINT_STUFF) {
                System.out.println("I found " + targets.size() + " targets.");
                for (TrackerBox2.TargetObject target : targets)
                    System.out.println("\tI found: " + target.toString());
                System.out.println();
            }
            if (!targets.isEmpty()) {
                target = targets.get(0);
            }
            else {
                target = null;
            }
        }
        else {
            target = null;
            }
    }

    public TargetObject getTarget() {
        return target;
    }
}
