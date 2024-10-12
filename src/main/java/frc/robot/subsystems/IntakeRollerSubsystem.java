package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;

/** The subsystem for the rollers on the intake. */
public class IntakeRollerSubsystem extends SubsystemBase{

    // Declaring motors
    private final CANSparkMax frontRoller; // Entry motor
    private final TalonSRX integrationRoller;// Integration motor

    // Declaring Sensors
    private final DigitalInput ampPosSensor;
    private final DigitalInput shooterPosSensor;

    /**
     * Subsystem for controlling the rollers on the intake.
     */
    public IntakeRollerSubsystem(){

        //Setting motor info
        frontRoller = new CANSparkMax(17, MotorType.kBrushless); // NEO: ID: #17
        integrationRoller = new TalonSRX(19); // Talon: ID: #19

        //Turn motors to run free
        frontRoller.setIdleMode(CANSparkMax.IdleMode.kCoast); //turning braking to Coast
        integrationRoller.setNeutralMode(NeutralMode.Coast); //turning braking to Coast

        //Setting sensors 
        ampPosSensor = new DigitalInput(5);
        shooterPosSensor = new DigitalInput(4);

    }

    /**
     * Sets the rollers' speed and direction.
     *
     * @param speed     The raw motor speed to set the rollers to (typically between -1.0 and 1.0).
     * @param direction A negative or positive integer (-1 or 1) to determine the direction of rotation.
     * @throws IllegalArgumentException if direction is not 1 or -1.
     */
    public void moveRoller(double speed, int direction) {
        // Check if Direction is a 1
        if (direction != 1 && direction != -1) { //PS: added this cuz i could not stop messing it up (don't need)
            throw new IllegalArgumentException("Direction must be 1 or -1");
        }

        // Set speed and direction for both Rollers
        double speedWithDirection = (speed * direction); //sets direction and mesh with speed

        frontRoller.set(speedWithDirection);
        integrationRoller.set(ControlMode.PercentOutput, speedWithDirection);
    }

    /**
     * Turns active braking ON.
     * Will NOT turn off until restarted
     */
    public void brakeRoller() { 
        frontRoller.set(0);
        integrationRoller.set(ControlMode.PercentOutput, 0);

        frontRoller.setIdleMode(CANSparkMax.IdleMode.kBrake); 
        integrationRoller.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Turns speed to 0.
     */
    public void noSpeedRoller() { 
        frontRoller.set(0);
        integrationRoller.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Check if ampSensor triggered
     * @return boolean for if note is in amp pos or not
     */
    public boolean isAtAMP() {
        return ampPosSensor.get();
    }

    /**
     * Check if Shooter Sensor triggered
     * @return boolean for if note is in amp pos or not
     */  
    public boolean isAtShooter() {
        return shooterPosSensor.get();
    }

    /**
     * Runs Front Rollers.
     * @param speed  The motor speed to run rollers 
     */
    public void moveFrontRoller(double speed) {  
        frontRoller.set(speed);
    }

    /**
     * Runs Integrated Rollers.
     * @param speed  The motor speed to run rollers 
     */
    public void moveIntegratedRoller(double speed) {  
        integrationRoller.set(ControlMode.PercentOutput, speed);
    }
}