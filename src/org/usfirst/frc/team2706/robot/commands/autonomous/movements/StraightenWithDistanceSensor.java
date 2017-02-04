package org.usfirst.frc.team2706.robot.commands.autonomous.movements;

import org.usfirst.frc.team2706.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class StraightenWithDistanceSensor extends Command {

    private final double minSpeed;

    private final double maxSpeed;

    private final double baseSpeed;

    private final double error;

    private final double stopCycles;

    private final boolean inverted;

    private double currentStopCycles;

    private final double speedIncrease;

    private final double speedDecrease;

    /**
     * Straightens out against a wall in close proximity(~5ft)
     * 
     * @param minSpeed Slowest possible speed that keeps the robot moving
     * @param maxSpeed Top speed while turning
     * @param baseSpeed Normal speed while turning
     * @param error How much leeway there is for the sensors
     * @param stopCycles How many cycles does it wait in the final position before ending?
     * @param inverted Change turning direction
     * @param speedIncrease How much the offset affects the turning speed
     * @param speedDecrease How much being in the zone slows down turning speed
     * 
     */
    public StraightenWithDistanceSensor(double minSpeed, double maxSpeed, double baseSpeed,
                    double error, double stopCycles, boolean inverted, double speedIncrease,
                    double speedDecrease) {
        requires(Robot.driveTrain);

        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.baseSpeed = baseSpeed;
        this.error = error;
        this.stopCycles = stopCycles;
        this.inverted = inverted;
        this.speedIncrease = speedIncrease;
        this.speedDecrease = speedDecrease;
    }

    protected void initialize() {
        Robot.driveTrain.reset();

        currentStopCycles = 0;
    }

    protected void execute() {
        inPosition();

        double offset = Math.abs(Robot.driveTrain.getLeftDistanceToObstacle()
                        - Robot.driveTrain.getRightDistanceToObstacle());
        double turnSpeed =
                        baseSpeed + (offset / speedIncrease) - (speedDecrease * currentStopCycles);

        if (turnSpeed > maxSpeed) {
            turnSpeed = maxSpeed;
        }

        if (turnSpeed < minSpeed) {
            turnSpeed = minSpeed;
        }

        if (Robot.driveTrain.getRightDistanceToObstacle() > Robot.driveTrain
                        .getLeftDistanceToObstacle()) {
            turnSpeed *= -1;
        }

        if (inverted) {
            turnSpeed *= -1;
        }

        Robot.driveTrain.drive(turnSpeed, -turnSpeed);
    }

    @Override
    protected boolean isFinished() {
        if (currentStopCycles > stopCycles) {
            return true;
        }
        return false;
    }

    protected void inPosition() {
        if (Math.abs(Robot.driveTrain.getLeftDistanceToObstacle()
                        - Robot.driveTrain.getDistanceToObstacle()) < error
                        && Math.abs(Robot.driveTrain.getRightDistanceToObstacle()
                                        - Robot.driveTrain.getDistanceToObstacle()) < error) {
            currentStopCycles++;
        } else {
            currentStopCycles--;
            if (currentStopCycles < 0) {
                currentStopCycles = 0;
            }
        }
    }

    protected void interrupted() {
        end();
    }

    protected void end() {
        Robot.driveTrain.drive(0, 0);
    }
}
