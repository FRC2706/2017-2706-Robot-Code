package org.usfirst.frc.team2706.robot.controls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.first.wpilibj.Joystick;

public class RecordableJoystick extends Joystick {
	
	private final Joystick joy;

	private final JoystickConfig config;
	private final List<JoystickState> states;

	private int index;

	public RecordableJoystick(Joystick joy, String loc) {
		super(getPort(joy));

		this.joy = joy;
		
		if(new File(loc + "-config.json").isFile() && new File(loc + "-config.json").isFile()) {
			config = new Gson().fromJson(loadFile(new File(loc + "-config.json")), JoystickConfig.class);
			states = Arrays.asList(new Gson().fromJson(loadFile(new File(loc + "-states.json")), JoystickState[].class));
		}
		else {
			config = null;
			states = null;
		}
	}
	
	public Joystick getRealJoystick() {
		return joy;
	}

	private String loadFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = "";
			String tmp = "";
			while ((tmp = br.readLine()) != null) {
				line += tmp + "\n";
			}
			return line;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean update() {
		return ++index >= states.size();
	}

	@Override
	public int getAxisCount() {
		return config.axisCount;
	}

	@Override
	public int getButtonCount() {
		return config.buttonCount;
	}

	@Override
	public boolean getIsXbox() {
		return config.isXbox;
	}

	@Override
	public int getType() {
		return config.type;
	}
	
	@Override
	public String getName() {
		return config.name;
	}

	@Override
	public int getPOVCount() {
		return config.povCount;
	}
	
	@Override
	public double getRawAxis(final int axis) {
		return states.get(index).axes[axis];
	}

	@Override
	public boolean getRawButton(final int button) {
		return states.get(index).buttons[button - 1];
	}

	@Override
	public int getPOV(int pov) {
		return states.get(index).povs[pov];
	}

	private class JoystickConfig {

		private final int axisCount;
		private final int buttonCount;
		private final int povCount;

		private final boolean isXbox;

		private final int type;
		
		private final String name;
		
		private JoystickConfig(int axisCount, int buttonCount, int povCount, boolean isXbox, int type, String name) {
			this.axisCount = axisCount;
			this.buttonCount = buttonCount;
			this.povCount = povCount;
			this.isXbox = isXbox;
			this.type = type;
			this.name = name;
		}

	}

	private class JoystickState {

		private final double axes[];
		private final boolean buttons[];
		private final int povs[];

		private JoystickState(double axes[], boolean buttons[], int povs[]) {
			this.axes = axes;
			this.buttons = buttons;
			this.povs = povs;
		}
	}

	public static int getPort(Joystick joy) {
		try {
			Field f = joy.getClass().getDeclaredField("m_port");
			f.setAccessible(true);
			return f.getInt(joy);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
}
