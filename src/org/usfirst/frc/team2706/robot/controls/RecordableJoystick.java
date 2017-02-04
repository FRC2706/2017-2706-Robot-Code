package org.usfirst.frc.team2706.robot.controls;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.google.gson.Gson;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Can be used as a Joystick object, it saves Joystick values to a file while recording, that can
 * later be replayed in autonomous mode.
 */
public class RecordableJoystick extends Joystick {

    /**
     * The location of the joystick if the given file cannot be found
     */
    public static final String EMPTY_LOC = "/home/lvuser/joystick-recordings/empty/empty";

    private final boolean replay;

    private final Joystick joy;

    private JoystickConfig config;
    private List<JoystickState> states;

    private final IndexFinder indexFinder;

    private int index;
    private double time;

    private Supplier<Double> timeSupplier;

    private final String loc;

    private boolean looping;

    /**
     * Sets up recording or replaying from or to a real Joystick
     * 
     * @param joy The real joystick that provides values if there are no values saved, or if
     *        recording
     * @param loc The location to load or save the data from the Joystick
     * @param replay Is true if replaying false if recording
     */
    public RecordableJoystick(Joystick joy, String loc, boolean replay) {
        super(joy.getPort());

        this.replay = replay;
        this.joy = joy;
        this.loc = loc;
        this.indexFinder = new IndexFinder();
    }

    /**
     * Gets the real Joystick that provides values if there are no values saved, or if recording
     * 
     * @return The real Joystick
     */
    public Joystick getRealJoystick() {
        return joy;
    }

    /**
     * Stops recording or replaying and if recording, saves the values to files
     */
    public void end() {
        looping = false;

        reset();

        if (!replay) {
            saveFile(new Gson().toJson(config), new File(loc + "-config.json"));
            saveFile(new Gson().toJson(states), new File(loc + "-states.json"));
        }
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

    private void saveFile(String line, File file) {
        // Create new file if the file doesn't exist already
        file.getParentFile().mkdirs();

        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            br.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JoystickState grabJoystickValues() {
        final double[] axes = new double[getAxisCount()];
        final boolean[] buttons = new boolean[getButtonCount()];
        final int[] povs = new int[getPOVCount()];

        for (int i = 0; i < axes.length; i++) {
            axes[i] = getRawAxis(i);
        }

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = getRawButton(i + 1);
        }

        for (int i = 0; i < povs.length; i++) {
            povs[i] = getPOV(i);
        }

        return new JoystickState(axes, buttons, povs, time);
    }

    /**
     * Sets up for recording/replaying. If replaying, load the files to replay
     * 
     * @param timeSupplier The supplier to get the time since the command is initialized. Use
     *        {@code Command::timeSinceInitialized} for this.
     */
    public void init(Supplier<Double> timeSupplier) {
        if (replay) {
            if (new File(loc + "-config.json").isFile()
                            && new File(loc + "-config.json").isFile()) {
                config = new Gson().fromJson(loadFile(new File(loc + "-config.json")),
                                JoystickConfig.class);
                states = Arrays.asList(new Gson().fromJson(loadFile(new File(loc + "-states.json")),
                                JoystickState[].class));
            } else {
                System.out.println(loc + "-config.json and/or " + loc
                                + "-states.json do not exist...");
                config = new Gson().fromJson(loadFile(new File(EMPTY_LOC + "-config.json")),
                                JoystickConfig.class);
                states = Arrays.asList(
                                new Gson().fromJson(loadFile(new File(EMPTY_LOC + "-states.json")),
                                                JoystickState[].class));
            }
        } else {
            config = new JoystickConfig(joy.getAxisCount(), joy.getButtonCount(), joy.getPOVCount(),
                            joy.getIsXbox(), joy.getType().value, joy.getName());
            states = new ArrayList<JoystickState>();
        }

        this.timeSupplier = timeSupplier;
        this.looping = true;

        Thread indexFinder = new Thread(this.indexFinder);
        indexFinder.setDaemon(true);
        indexFinder.start();
    }

    /**
     * If replaying, updates the values to be replayed
     */
    public void update() {
        if (!replay) {
            states.add(grabJoystickValues());
            index++;
        }
    }

    /**
     * Whether there are no more states to be replayed or false if recording
     * 
     * @return Whether finished or not
     */
    public boolean done() {
        return replay && index >= states.size();
    }

    /**
     * Resets states played to the start
     */
    public void reset() {
        index = 0;
    }

    @Override
    public int getAxisCount() {
        if (replay && config != null && states != null)
            return config.axisCount;
        else
            return joy.getAxisCount();
    }

    @Override
    public int getButtonCount() {
        if (replay && config != null && states != null)
            return config.buttonCount;
        else
            return joy.getButtonCount();
    }

    @Override
    public boolean getIsXbox() {
        if (replay && config != null && states != null)
            return config.isXbox;
        else
            return joy.getIsXbox();
    }

    @Override
    public HIDType getType() {
        if (replay && config != null && states != null)
            return HIDType.values()[config.type];
        else
            return joy.getType();
    }

    @Override
    public String getName() {
        if (replay && config != null && states != null)
            return config.name;
        else
            return joy.getName();
    }

    @Override
    public int getPOVCount() {
        if (replay && config != null && states != null)
            return config.povCount;
        else
            return joy.getPOVCount();
    }

    @Override
    public double getRawAxis(final int axis) {
        if (replay && config != null && states != null)
            return states.get(index).axes[axis];
        else
            return joy.getRawAxis(axis);
    }

    @Override
    public boolean getRawButton(final int button) {
        if (replay && config != null && states != null)
            return states.get(index).buttons[button - 1];
        else {
            return joy.getRawButton(button);
        }
    }

    @Override
    public int getPOV(int pov) {
        if (replay && config != null && states != null)
            return states.get(index).povs[pov];
        else
            return joy.getPOV(pov);
    }

    private class JoystickConfig {

        private final int axisCount;
        private final int buttonCount;
        private final int povCount;

        private final boolean isXbox;

        private final int type;

        private final String name;

        private JoystickConfig(int axisCount, int buttonCount, int povCount, boolean isXbox,
                        int type, String name) {
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
        private final double time;

        private JoystickState(double axes[], boolean buttons[], int povs[], double time) {
            this.axes = axes;
            this.buttons = buttons;
            this.povs = povs;
            this.time = time;
        }
    }

    private class IndexFinder implements Runnable {

        @Override
        public void run() {
            while (looping) {
                time = timeSupplier.get();

                if (replay) {
                    double closest = Double.MAX_VALUE;

                    for (int i = index; i < states.size(); i++) {
                        double timeToIndex = Math.abs(time - states.get(i).time);

                        if (timeToIndex <= closest) {
                            closest = timeToIndex;
                        } else {
                            index = Math.max(i - 1, 0);
                            break;
                        }
                    }
                }
            }
        }
    }
}
