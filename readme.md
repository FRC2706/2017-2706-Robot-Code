# 2017-2706-Robot-Code
The main robot code for the Merge Robotics (2706) robot.

## Want to help write robot code?

We have a lot of programmers on the team this year, so we've split the code out into chunks so each group can be in charge of a piece. Talk to you group's mentor or project leader to see which chunk you can work on.

## Code Structure

### Subsystems

Subsystems classes are for the lowest end things like gyro, direct communication with motors, camera etc. These classes will typically be owned by the group working with the hardware, such as the Controls group for motors and sensors, Vision group for cameras, etc.


### Commands

The Commands folder / package is split into two types of commands:

**Autonomous Commands**:

The autonomous code is split into 3 parts, based on how low down the code is:

- **Movements**: Moving forward, turning with or without camera, using different sensors to perform certain movements. This layer is good for advanced / veteran students.

- **Plays**: Combine these movements to make small plays that have multiple purposes. For example, last year's "shoot" was actually a play that involved lowering the arm, spinning up the motors, and firing the kicker. This layer is good for rookie students wanting to do auto mode.

- **AutoModes**: String together multiple plays and movements to make the actual autonomous runs that we use in competition. Make it as simple as possible by putting more code into the lower levels so its easy to add and edit these combos. This layer is good for rookies wanting to do auto mode.

**Teleop commands**:

The code for teleop mode will build off the same subsystem classes as the auto mode and may borrow some of the simple plays as "macros".

The main goal of writing teleop code is handling joystick / button input and making the robot as easy to drive as possible. Things that may include could be:

- Talking with the drivers and playing with the robot to get a comfortable button layout.

- Lower driver mistakes by automating repetitive sequences of button presses, or things where timing is important.

- Smooth out the motors using sensor loops so it's less jerky / easier to control.

- Controlling teleop with the gamepads will now be done in OI, with commands in the mechanismcontrol package.
- etc

We didn't do very much of this last year, so lots of room for creativity!
