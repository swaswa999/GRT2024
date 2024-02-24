package frc.robot.commands.sequences;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.elevator.ElevatorToZeroCommand;
import frc.robot.commands.intake.roller.IntakeRollerFeedCommand;
import frc.robot.commands.shooter.flywheel.ShooterFlywheelReadyCommand;
import frc.robot.commands.shooter.pivot.ShooterPivotSetAngleCommand;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.intake.IntakeRollersSubsystem;
import frc.robot.subsystems.leds.LEDSubsystem;
import frc.robot.subsystems.shooter.ShooterFlywheelSubsystem;
import frc.robot.subsystems.shooter.ShooterPivotSubsystem;
import frc.robot.subsystems.superstructure.NotePosition;

/**
 *  The command sequence to move the note within the robot into a shooting position and prepare the shooter to shoot.
 */
public class ShootModeSequence extends SequentialCommandGroup {

    /*
     * START
     * / | `---------.
     * Angle shooter lower Start Flywheels
     * for intaking elevator |
     * \ / |
     * | |
     * / \ |
     * Intake feed Shooter recieve |
     * to shooter Note |
     * \ / /
     * Aim shooter /
     * \ /
     * FINISH
     */

    public ShootModeSequence(
            IntakeRollersSubsystem intakeRollerSubsystem,
            ElevatorSubsystem elevatorSubsystem,
            ShooterFlywheelSubsystem shooterFlywheelSubsystem,
            ShooterPivotSubsystem shooterPivotSubsystem,
            LEDSubsystem ledSubsystem) {
        addCommands(new ShooterFlywheelReadyCommand(shooterFlywheelSubsystem).alongWith(
                // new ElevatorToGroundCommand(elevatorSubsystem),
                new ShooterPivotSetAngleCommand(shooterPivotSubsystem, Units.degreesToRadians(20)) // STUB FOR AUTOAIM
        ));
    }
}
