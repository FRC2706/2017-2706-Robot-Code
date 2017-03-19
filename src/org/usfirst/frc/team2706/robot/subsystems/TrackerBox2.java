package org.usfirst.frc.team2706.robot.subsystems;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TrackerBox2 {
    public String RPi_addr;
    public final int visionDataPort = 1182;
    public static final String DATA_VERSION_CODE = "2017";

    public boolean PRINT_STUFF = false;

    public class TargetObject {
        public float boundingArea = -1; // % of cam [0, 1.0]
        // center of target
        public float ctrX = -1; // [-1.0, 1.0]
        public float ctrY = -1; // [-1.0, 1.0]
        // the aspect ratio of the target we found. This can be used directly as a poor-man's
        // measure of skew.


        public String toString() {
            return "position: (" + ctrX + ", " + ctrY + "), boundingArea: " + boundingArea;
        }
    }

    public TrackerBox2(String raspberryPiAddress) {
        RPi_addr = raspberryPiAddress;
    }

    public ArrayList<TargetObject> getVisionData() {
        ArrayList<TargetObject> prList = new ArrayList<>();

        if (PRINT_STUFF)
            System.out.println("Setting up Sockets");

        Socket sock = new Socket();
        try {
            if (PRINT_STUFF)
                System.out.println(visionDataPort);
            sock.connect(new InetSocketAddress(RPi_addr, visionDataPort), 100);
        } catch (Exception e) {
            try {
                sock.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }

        try {
            PrintWriter outToServer = new PrintWriter(sock.getOutputStream(), true);

            if (PRINT_STUFF)
                System.out.println("Sending request to TrackerBox2 for vision data");
            outToServer.println(""); // basically send an empty message
            outToServer.flush();
            // we stopped here

            byte[] rawBytes = new byte[2048];
            try {
                if (sock.getInputStream().read(rawBytes) < 0) {
                    System.out.println("Something went wrong reading response from TrackerBox2: ");
                    return null;
                }

                String rawData = new String(rawBytes);
                if (PRINT_STUFF)
                    System.out.println("I got back: " + rawData);

                // check protocol version
                String verCode = rawData.split("\\|")[0];
                rawData = rawData.split("\\|")[1];

                if (!verCode.equals(DATA_VERSION_CODE)) {
                    System.out.println("Wrong version code! I am version " + DATA_VERSION_CODE
                                    + " but I got " + verCode + "from the Pi");
                    return null;
                }


                if (rawData.length() == 0) {
                    prList.add(new TargetObject());
                    System.out.println("No Targets Found");
                } else {
                    String[] targets = rawData.split(":");
                    for (String target : targets) {
                        try {
                            String[] targetData = target.split(",");

                            TargetObject pr = new TargetObject();
                            pr.ctrX = Float.parseFloat(targetData[0]);
                            pr.ctrY = Float.parseFloat(targetData[1]);
                            pr.boundingArea = Float.parseFloat(targetData[2]);

                            if (PRINT_STUFF)
                                System.out.println("Target found at: " + pr.ctrX + "," + pr.ctrY
                                                + ", and boundingArea is: " + ","
                                                + pr.boundingArea);

                            prList.add(pr);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                }

                // up to here
            } catch (java.io.EOFException e) {
                System.out.println("Camera: Communication Error");
            }

            sock.close();
        } catch (UnknownHostException e) {
            System.out.println("Host unknown: " + RPi_addr);
            return null;
        } catch (java.net.ConnectException e) {
            System.out.println("Camera initialization failed at: " + RPi_addr);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (PRINT_STUFF)
            System.out.println("Network call successful, returning not null data...");

        return prList;

    }


}
