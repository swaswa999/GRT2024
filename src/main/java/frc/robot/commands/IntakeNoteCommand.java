package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakePivotSubsystem;
import frc.robot.subsystems.IntakeRollerSubsystem;

/** Procedure for Intake note and getting into AMP or Shooter position. */
public class IntakeNoteCommand extends Command {

    private final IntakeRollerSubsystem intakeRollerSubsystem;
    private final IntakePivotSubsystem intakePivotSubsystem;

    // STATE VARIABLES
    private boolean isPivotExtended; // True = Extended, False = Stowed
    private int ampORshooter; //var to hold which current procedure 1 = AMP, 2 = Shooter

    private boolean isFinished; // track if command done completion

    /**
     * Sets the intake pivot to a position.
     *
     * @param intakeRollerSubsystem object for IntakeRollerSubsystem class
     * @param intakePivotSubsystem  object for IntakePivotSubsystem class
     * @param isPivotExtended       boolean Pivot's current position (true = extended, false = stowed)
     * @param ampORshooter          int (1 or 2) for desired intake procedure (1 = amp, 2 = shooter)
     */
    public IntakeNoteCommand(IntakeRollerSubsystem intakeRollerSubsystem, IntakePivotSubsystem intakePivotSubsystem, boolean isPivotExtended, int ampORshooter) {
        this.intakeRollerSubsystem = intakeRollerSubsystem;
        this.intakePivotSubsystem = intakePivotSubsystem;
        this.isPivotExtended = isPivotExtended;
        this.ampORshooter = ampORshooter;
        this.isFinished = false;

        // Declare subsystem dependencies
        addRequirements(intakeRollerSubsystem, intakePivotSubsystem);
    }

    @Override
    public void initialize() {
        // Extend the pivot if not already extended
        if (!this.isPivotExtended) {
            intakePivotSubsystem.setPosition(1); // Extend the pivot
            this.isPivotExtended = true;  
        }
    }

    @Override
    public void execute() {

        // Decide whether to run AMP or Shooter procedure
        if (ampORshooter == 1) {
            //if note is not at AMP
            if (!intakeRollerSubsystem.isAtAMP()) {  
                intakeRollerSubsystem.moveFrontRoller(0.2);
            } else {
                intakeRollerSubsystem.noSpeedRoller();
                isFinished = true; 
            }
        } else if (ampORshooter == 2) {
            //if note is not at Shooter
            if (!intakeRollerSubsystem.isAtShooter()) {
                intakeRollerSubsystem.moveFrontRoller(0.2);
            } else {
                intakeRollerSubsystem.noSpeedRoller();
                isFinished = true; 
            }
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void end(boolean interrupted) { //stops and resets everything
        intakeRollerSubsystem.noSpeedRoller();
        intakePivotSubsystem.setPosition(0);
        isPivotExtended = false; 
    }
}