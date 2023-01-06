// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveTrain extends SubsystemBase {
  /** Creates a new DriveTrain. */
  // calling up values
  protected WPI_TalonFX backLeftMotor;
  protected WPI_TalonFX backRightMotor;
  protected WPI_TalonFX frontLeftMotor;
  protected WPI_TalonFX frontRightMotor;
  protected XboxController joystick;
  protected PIDController pid;
  protected MotorControllerGroup backGroup; 
  protected MotorControllerGroup leftGroup;
  protected DifferentialDrive differentialDrive;

  public DriveTrain() {
    // Initializing motor controllers according to CAN ids
    this.backLeftMotor = new WPI_TalonFX(Constants.kBackLeftMotor);
    this.backRightMotor = new WPI_TalonFX(Constants.kBackRightMotor);
    this.frontLeftMotor = new WPI_TalonFX(Constants.kFrontLeftMotor);
    this.frontRightMotor = new WPI_TalonFX(Constants.kFrontRightMotor);
    // Move joystick
    this.joystick = new XboxController(Constants.kJoystickPort);
    // In a couple of days this object will be used. Now it's redundant 
    this.pid = new PIDController(Constants.kP, Constants.kI, Constants.kD);
    // Making each speedController follow each other
    this.backLeftMotor.follow(this.frontLeftMotor);
    this.backRightMotor.follow(this.frontRightMotor);
    // Grouping the motors for comfort
    this.backGroup = new MotorControllerGroup(backLeftMotor, backRightMotor);
    this.leftGroup = new MotorControllerGroup(frontLeftMotor, frontRightMotor);
    // Creating a diffrential drive driving module
    this.differentialDrive = new DifferentialDrive(backGroup, leftGroup);
  }

  public void driveArcade() {
    // Arcade drive
    float move = (float) joystick.getLeftX();
    float rot = (float) joystick.getLeftY();
    if (Math.abs(move) < 0.05)
      move = 0;
    if (Math.abs(rot) < 0.05)
      rot = 0;
      differentialDrive.arcadeDrive(move, rot); // Arcade drive
  }

  public void driveTank(){
      // Tank drive
      float leftSpeed = (float) joystick.getLeftY();
      float rightSpeed = (float) joystick.getRightY();
      differentialDrive.tankDrive(leftSpeed, rightSpeed);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
