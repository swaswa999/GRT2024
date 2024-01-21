// INTAKE one talon for feeding one neo for  pivot
// SHOOTER one neo for shooting, one neo for pivot, one talon for conveyer belt

//create enums, expecting, holding, firing, and no note

package frc.robot.subsystems.shooter;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.I2C;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {

    //change later
    int IDNUMBER = 10; //so I remember to change them later
    double speed = 0.1; //placeholder
    final int ERRORTOLERANCE = 10; //placeholder
    final int LIMIT_SWITCH_ID = 1; //placeholder
    final double GEARBOX_RATIO = 1/20; //placeholder
    final int TOLERANCE = 10; //placeholder

    //angle PID (CHANGE LATER)
    private static final double ANGLE_P = 2.4;
    private static final double ANGLE_I = 0;
    private static final double ANGLE_D = 0;

    //motors
    private final TalonFX feederMotor; 
    private final CANSparkMax pivotMotor;
    private final CANSparkMax shooterMotor;
    private final CANSparkMax shooterMotorTwo;

    //devices
    private RelativeEncoder rotationEncoder;
    private SparkMaxPIDController rotationPIDController;
    private final DigitalInput limitSwitch;
    private ShooterState currentState;

    //sensors
    private final ColorSensorV3 shooterSensor; //distance sensor

    public ShooterSubsystem(){
        //motors
        feederMotor = new TalonFX(10);
        pivotMotor = new CANSparkMax(10, MotorType.kBrushless); 
        shooterMotor = new CANSparkMax(10, MotorType.kBrushless);
        shooterMotorTwo = new CANSparkMax(10, MotorType.kBrushless);

        //shooter follow
        shooterMotorTwo.follow(shooterMotor, true);

        //devices
        rotationEncoder = pivotMotor.getEncoder();
        rotationPIDController = pivotMotor.getPIDController();
        limitSwitch = new DigitalInput(LIMIT_SWITCH_ID);
        
        //setting PID vars
        rotationPIDController.setP(ANGLE_P);
        rotationPIDController.setI(ANGLE_I);
        rotationPIDController.setD(ANGLE_D);

        //encoder stuff
        rotationEncoder.setPositionConversionFactor(GEARBOX_RATIO);
        rotationPIDController.setSmartMotionAllowedClosedLoopError(ERRORTOLERANCE, 0); //what does 0 do (slotID is from 0-3)

        //sensors
        shooterSensor = new ColorSensorV3(I2C.Port.kMXP);

        //enums
        setShooterState(ShooterState.VERTICAL);
    }

    //enum functions
    public ShooterState getShooterState(ShooterState state){
        return currentState;
    }

    public void setShooterState(ShooterState newState){
        currentState = newState;
    }

    //motor speed setting functions
    public void setFlywheelSpeed(double speed){
        shooterMotor.setVoltage(speed * 12);
    }

    public void setFeedingMotorSpeed(double speed){
        feederMotor.setVoltage(speed * 12);
    }

    public void setShooterSpeed(double speed){
        shooterMotor.setVoltage(speed * 12);
    }

    //gets note from intake to shooter
    public void loadNote(){
        if(shooterSensor.getRed() < TOLERANCE){
            setFeedingMotorSpeed(speed);
        } else {
            setFeedingMotorSpeed(0);
        }
    }

    private void shooterState(){
        setFlywheelSpeed(0.75);
        loadNote();
    }

    private void shootNote(){
        if(shooterSensor.getRed() > TOLERANCE){
            setFeedingMotorSpeed(speed);
        } else {
            setFeedingMotor
        }
    }

    public void setAngle(double angle){
        rotationPIDController.setReference(angle, CANSparkMax.ControlType.kPosition);
    }

    public void periodic(){
        //resets relative encoder every time robot starts again
        if(limitSwitch != null && limitSwitch.get()){ //false = limit switch is pressed
            rotationEncoder.setPosition(0); 
        }

        if(currentState == ShooterState.FIRING && (shooterSensor.getRed() < TOLERANCE)){  //when there is no note
            setShooterState(ShooterState.NO_NOTE);
        }

        // switch(currentState) {
        //     case HOLDING_NOTE:
        //       shooterMotor.set(speed); //warming up the motor
        //       break;
        //     case LOADING_NOTE:
        //        shooterMotor.set(speed);
        //        shootNote();
        //       break;
        //     case NO_NOTE:
        //       System.out.println("High level");
        //       break;
        // }

    }
}