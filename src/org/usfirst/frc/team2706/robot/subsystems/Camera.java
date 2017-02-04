package org.usfirst.frc.team2706.robot.subsystems;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Does everything java related to the rPI camera, servo controllers have been deleted.
 *
 */
public class Camera extends Subsystem {
	
	public static final String CAMERA_IP = "10.27.6.240";
	public boolean PRINT_STUFF = false;
	public  String RPi_addr;
	public final  int visionDataPort = 1182;
	public static TargetObject cachedTarget;
	
public Camera(String ip) {
	super();	
	RPi_addr = ip;
}
public class TargetObject implements Comparable<TargetObject> {
	  public float boundingArea = -1;     // % of cam [0, 1.0]
	  //center of target
	  public float ctrX = -1;             // [-1.0, 1.0]
	  public float ctrY = -1;             // [-1.0, 1.0]
	  // the aspect ratio of the target we found. This can be used directly as a poor-man's measure of skew.
	  public float aspectRatio = -1;
	  
		public String toString() {
			return "position: (" + ctrX + ", " + ctrY + "), boundingArea: " + boundingArea + ", aspectRatio: " + aspectRatio;
		}

		@Override
		public int compareTo(TargetObject other) {
			if (this.ctrY - other.ctrY < 0)
				return -1;
			else if (this.ctrY - other.ctrY > 0)
				return 1;
			else 
				return 0;
			
		}
}
	
//@SuppressWarnings("deprecation")
	public ArrayList<TargetObject> getVisionData() {
		ArrayList<TargetObject> prList = new ArrayList<>();

		if(PRINT_STUFF)
			System.out.println("Setting up Sockets");

		Socket sock = new Socket();
/*		try {
			sock.setSoTimeout(5);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}*/
		try {
			sock.connect(new InetSocketAddress(RPi_addr, visionDataPort), 20);
		} catch (Exception e) {
			try {
				sock.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		try {
			PrintWriter outToServer = new PrintWriter(sock.getOutputStream(), true);

			if(PRINT_STUFF)
				System.out.println("Sending request to TrackerBox2 for vision data");
			outToServer.println(""); // basically send an empty message
			outToServer.flush();

			byte[] rawBytes = new byte[2048];
			try {
				try {
				if( sock.getInputStream().read(rawBytes) < 0 ) {
					System.out.println("Something went wrong reading response from TrackerBox2: ");
					return null;
				}
				} catch(SocketTimeoutException e) {
					
				}
				String rawData = new String(rawBytes);
				if(PRINT_STUFF)
					System.out.println("I got back: " + rawData);
					String[] targets = rawData.split(":");
					for(String target : targets) {
						try {
							String[] targetData = target.split(",");

							TargetObject pr = new TargetObject();
							pr.ctrX = Float.parseFloat(targetData[0]);
							pr.ctrY	= Float.parseFloat(targetData[1]);
							pr.aspectRatio = Float.parseFloat(targetData[2]);
							pr.boundingArea = Float.parseFloat(targetData[3]);

							if(PRINT_STUFF)
								System.out.println("Target found at: " + pr.ctrX + "," + pr.ctrY + ", and aspectRatio and boundingArea is: " + pr.aspectRatio + "," + pr.boundingArea);

							prList.add(pr);
						} catch (NumberFormatException e) {
							continue;
						}
					}
//				}
			} catch (java.io.EOFException e) {
				System.out.println("Camera: Communication Error");
			}

			sock.close();
		} catch ( UnknownHostException e) {
			System.out.println("Host unknown: "+RPi_addr);
			return null;
		} catch (java.net.ConnectException e) {
			System.out.println("Camera initialization failed at: " + RPi_addr);
			return null;
		} catch( IOException e) {
			e.printStackTrace();
			return null;
		}
		if(PRINT_STUFF)
			System.out.println("Network call successful, returning not null data...");

		return prList;
	}

	@Override
	protected void initDefaultCommand() {
		
	}
	
	public static final int LEFT_TARGET = -3;
	public static final int CENTER_TARGET = -1;
	public static final int RIGHT_TARGET = -2;
	
/*	public float savedXaxis = DEFAULT_PAN;
	public float savedYaxis = DEFAULT_TILT;*/
	public  TargetObject getVisionDataByTarget(int target) { // put in -1 for center target, -2 for right and -3 for left
		ArrayList<TargetObject> pr = getVisionData();
		Collections.sort((List<TargetObject>) pr);
		// pr should now be sorted
		
		if(pr.size() == 0) {
			if(PRINT_STUFF)
			System.out.println("Can't find a target");
			return null;
		}
		if (target == CENTER_TARGET) {
			if (pr.size() % 2 == 0)
				return pr.get(pr.size() / 2);
			else
				return pr.get((int) (pr.size() / 2 + 0.5));
		} else if (target == RIGHT_TARGET)
			return pr.get(pr.size() - 1);
		else if (target == LEFT_TARGET)
			return pr.get(0);
		else
			return pr.get(target);
	}
	public float GetAspectRatio() {
		return cachedTarget.aspectRatio;
	}
	public float GetBoundingArea() {
		return cachedTarget.boundingArea;
	}
	public boolean HasTarget() {
		return cachedTarget == null ? false : true;
	}
}











