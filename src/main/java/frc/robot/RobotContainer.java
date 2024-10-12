// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.IntakePivotSetPositionCommand;
import frc.robot.subsystems.IntakePivotSubsystem;

//subsystem imports:
import frc.robot.subsystems.IntakeRollerSubsystem;
import frc.robot.commands.IntakeNoteCommand;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  //Subsystems defined:
  private final IntakePivotSubsystem intakePivotSubsystem;
  private final IntakeRollerSubsystem intakeRollerSubsystem;

  //Controls defined
  private final XboxController mechController;
  private final JoystickButton xButton; //Toggles intake being stowed 
  private final JoystickButton bButton; //Stop all rollers no matter what
  private final JoystickButton aButton; //Intake note, and get intake and note  to AMP position (pictured below)
  private final JoystickButton yButton; //Moves note from AMP position to SHOOTER position
  private final JoystickButton leftBumper; //Run all rollers inwards 
  private final JoystickButton rightBumper; //Run all rollers outwards

  // State variables
  private double pivotCurrentPosition; //position of the intake var
  private boolean isPivotExtended; //boolean for if intake extended or not

  
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    intakePivotSubsystem = new IntakePivotSubsystem();
    intakeRollerSubsystem = new IntakeRollerSubsystem();

    //Configure Bindings for Controller
    mechController = new XboxController(0);
    xButton = new JoystickButton(mechController, XboxController.Button.kX.value);
    bButton = new JoystickButton(mechController, XboxController.Button.kB.value);
    aButton = new JoystickButton(mechController, XboxController.Button.kA.value);
    yButton = new JoystickButton(mechController, XboxController.Button.kY.value);
    leftBumper = new JoystickButton(mechController,XboxController.Button.kLeftBumper.value); 
    rightBumper = new JoystickButton(mechController,XboxController.Button.kRightBumper.value);
    configureBindings();
    
    //Assign var to Intake position (0 = retracted, 1 = extended )
    pivotCurrentPosition = intakePivotSubsystem.getEncoderPosition();
    pivotCurrentPosition = (pivotCurrentPosition < 0) ? 0 : 1;

    //Assign var to if pivot is extended
    isPivotExtended = (pivotCurrentPosition == 1) ? true : false;


  }

  private void configureBindings() {

    //INTAKE Position Changes

    // Get the current position of intake and set it to a 0(closed) or 1(open) value
    pivotCurrentPosition = intakePivotSubsystem.getEncoderPosition(); // Get position from the subsystem
    pivotCurrentPosition = (pivotCurrentPosition < 0) ? 0 : 1;  // Turn current position into a 1 or 0 value
    isPivotExtended = (pivotCurrentPosition == 1) ? true : false; //Update Pivot Position
    // set X button to Toggles intake being stowed or enabled
    xButton.onTrue(new InstantCommand(() -> {
        pivotCurrentPosition = (pivotCurrentPosition == 1) ? 0 : 1;  // Change position from 1 to 0 or 0 to 1
        new IntakePivotSetPositionCommand(intakePivotSubsystem, pivotCurrentPosition).schedule(); // Execute pos change
    }));


    //Turning Roller off
    bButton.onTrue(new InstantCommand(() -> intakeRollerSubsystem.noSpeedRoller())); //just sets speed of motors to 0

    
    //ROLLERS MOVEMENT DIRECTION

    //Run Rollers inwards with left trigger
    leftBumper.onTrue(new InstantCommand(() -> intakeRollerSubsystem.moveRoller(0.2, 1))); //turns on all roller motor inwards
    //Run Rollers outwards with right trigger
    rightBumper.onTrue(new InstantCommand(() -> intakeRollerSubsystem.moveRoller(0.2, -1))); //turns on all roller motor out

    
    //AMP and SHOOTER Positions
    aButton.onTrue(new IntakeNoteCommand(intakeRollerSubsystem,intakePivotSubsystem, isPivotExtended, 1)); //executes code to get note into AMP position
    yButton.onTrue(new IntakeNoteCommand(intakeRollerSubsystem,intakePivotSubsystem, isPivotExtended, 2)); //executes code to get note into Shooter position

    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}
