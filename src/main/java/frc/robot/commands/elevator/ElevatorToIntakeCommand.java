package frc.robot.commands.elevator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorState;
import frc.robot.subsystems.elevator.ElevatorSubsystem;

/** Sends the elevator to its ground intake position. */
public class ElevatorToIntakeCommand extends Command {
    private ElevatorSubsystem elevatorSubsystem;

    /** Constructs a {@link ElevatorToIntakeCommand} for the specified elevator. */
    public ElevatorToIntakeCommand(ElevatorSubsystem elevatorSubsystem) {
        this.addRequirements(elevatorSubsystem);
        this.elevatorSubsystem = elevatorSubsystem;
    }

    @Override
    public void end(boolean interrupted) {
        return;
    }

    @Override
    public void execute() {
        System.out.println("ELEVATOR RUNNING");
        this.elevatorSubsystem.setTargetState(ElevatorState.INTAKE);
    }

    @Override
    public boolean isFinished() {
        return elevatorSubsystem.atState(ElevatorState.INTAKE);
    }
}