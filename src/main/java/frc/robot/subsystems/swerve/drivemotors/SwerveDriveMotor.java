package frc.robot.subsystems.swerve.drivemotors;

import com.ctre.phoenix6.hardware.TalonFX;

public interface  SwerveDriveMotor {
    
    public void setVelocity(double velocity);

    public void setPower(double power);

    public void configPID(double P, double I, double D, double FF);

    public double getDistance();

    public double getVelocity();

    public void setVelocityConversionFactor(double factor);
    
    public void setPositionConversionFactor(double factor);

    public double getError();

    public double getSetpoint();

    public double getAmpDraw();

    public TalonFX getTalonMotor();
}
