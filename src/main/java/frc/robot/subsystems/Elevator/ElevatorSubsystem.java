package frc.robot.subsystems.Elevator;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.SparkPIDController.ArbFFUnits;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;

public class ElevatorSubsystem extends SubsystemBase{

    private volatile boolean IS_MANUAL = false;
    
    private ElevatorState state = ElevatorState.GROUND;
    private ElevatorState targetState = ElevatorState.START;

    private final CANSparkMax extensionMotor;
    private RelativeEncoder extensionEncoder;
    private SparkPIDController extensionPidController;

    //PID Values
    private static final double extensionP = 2.4;
    private static final double extensionI = 0;
    private static final double extensionD = 0;
    private static final double extensionTolerance = 0.003;

    private final CANSparkMax extensionFollow;

    private final DigitalInput zeroLimitSwitch;



    public ElevatorSubsystem(){
        extensionMotor = new CANSparkMax(Constants.ElevatorConstants.EXTENSION_ID, MotorType.kBrushless);
        extensionMotor.setIdleMode(IdleMode.kBrake);
        
        extensionEncoder = extensionMotor.getEncoder();
        extensionEncoder.setPositionConversionFactor([factor]);
        extensionEncoder.setVelocityConversionFactor([factor]);
        extensionEncoder.setPosition(0)
        
        extensionFollow = new CANSparkMax(Constants.ElevatorConstants.EXTENSION_FOLLOW_ID, MotorType.kBrushless);
        extensionFollow.follow(extensionMotor);
        extensionFollow.setIdleMode(IdleMode.kBrake);

        extensionPidController = extensionMotor.getPIDController();
        extensionPidController.setP(extensionP);
        extensionPidController.setI(extensionI);
        extensionPidController.setD(extensionD);
        extensionPidController.setSmartMotionAllowedClosedLoopError(extensionTolerance, 0);
        
        zeroLimitSwitch = new DigitalInput(Constants.ElevatorConstants.ZERO_LIMIT_ID);
    }
    @Override
    public void periodic(){
        if (zeroLimitSwitch != null && !zeroLimitSwitch.get()){
            extensionEncoder.setPosition(0); 
        }

        extensionMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);

        //Start move to target posision
        if (targetState != state){
            extensionPidController.setReference(targetState.getExtension(), ControlType.kPosition, 0, 0.03, ArbFFUnits.kPercentOut);
        }
        
    }
    
    public void setTarget(ElevatorState targetState){
        this.targetState = targetState;
        return;
    }

    public double getExtensionMeters() { 
        return extensionEncoder.getPosition();
    }

    public ElevatorState getState() {
        return state;
    }

    public void setState(ElevatorState state) {
        this.state = state;
        return;
    }
}