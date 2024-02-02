package frc.robot.commands.shooter.pivot;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.ShooterPivotSubsystem;

public class ShooterPivotVerticalCommand extends Command{
    ShooterPivotSubsystem pivotSubsystem;

    public ShooterPivotVerticalCommand(ShooterPivotSubsystem pivotSubsystem){
        this.pivotSubsystem = pivotSubsystem;
        addRequirements(pivotSubsystem);
    }

    @Override
    public void initialize() {
        pivotSubsystem.setAutoAimBoolean(false);
        pivotSubsystem.setAngle(90.0);
    }

    @Override
    public boolean isFinished() {
        if(Math.abs(pivotSubsystem.getPosition()) - 90 < pivotSubsystem.ERRORTOLERANCE){
            return true;
        }

        return false;
    }
}