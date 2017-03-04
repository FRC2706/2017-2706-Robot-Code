package org.usfirst.frc.team2706.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Does everything java related to the rPI camera, servo controllers have been deleted.
 */
public class Camera extends Subsystem {
    // TODO we should probably consolidate these two IP addresses
    public static final String RPI_IP = "10.27.6.240";
    TrackerBox2 trackerbox = new TrackerBox2(RPI_IP);
    public static final String CAMERA_IP = "10.27.6.240";
    public boolean PRINT_STUFF = false;
    public String RPi_addr;
    public final int visionDataPort = 1182;

    @Override
    protected void initDefaultCommand() {}

    public Camera(String ip) {
        super();
        RPi_addr = ip;
    }

    public void GetTargets() {
        ArrayList<TrackerBox2.TargetObject> targets = trackerbox.getVisionData();

        if (targets != null) {
            System.out.println("I found " + targets.size() + " targets.");
            for (TrackerBox2.TargetObject target : targets)
                System.out.println("\tI found: " + target.toString());
            System.out.println();
        }
    }
}
