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
  protected WPI_TalonFX m_BackLeftMotor;
  protected WPI_TalonFX m_BackRightMotor;
  protected WPI_TalonFX m_FrontLeftMotor;
  protected WPI_TalonFX m_FrontRightMotor;
  protected XboxController joystick;
  protected PIDController pid;

  public DriveTrain() {
    // setting up values
    this.m_BackLeftMotor = new WPI_TalonFX(Constants.k_back_left_motor);
    this.m_BackRightMotor = new WPI_TalonFX(Constants.k_back_right_motor);
    this.m_FrontLeftMotor = new WPI_TalonFX(Constants.k_front_left_motor);
    this.m_FrontRightMotor = new WPI_TalonFX(Constants.k_front_right_motor);

    this.joystick = new XboxController(Constants.k_joystick_port);
    // for later..
    this.pid = new PIDController(Constants.k_p, Constants.k_i, Constants.k_d);
    // making each speedController follow each other
    this.m_BackLeftMotor.follow(this.m_FrontLeftMotor);
    this.m_BackRightMotor.follow(this.m_FrontRightMotor);
  }

  // grouping the motors for comfort
  MotorControllerGroup MBackGroup = new MotorControllerGroup(m_BackLeftMotor, m_BackRightMotor);
  MotorControllerGroup MFrontGroup = new MotorControllerGroup(m_FrontLeftMotor, m_FrontRightMotor);

  DifferentialDrive differentialDrive = new DifferentialDrive(MBackGroup, MFrontGroup);

  public void MoveRobot() {
    // arcade drive
    float move = (float) joystick.getLeftX();
    float rot = (float) joystick.getLeftY() * -1;
    if (move < 0.05)
      move = 0;
    if (rot < 0.05)
      rot = 0;
    // switching between arcade and tank drive modules
    if (joystick.getAButton()) {
      String current = "";
      Constants.count++;
      if (Constants.count % 2 == 0) {current = "arcade";}
      else {current = "tank";}  
      System.out.println("swiched! \ncurrent driving module: " + current);
    }
    if (Constants.count % 2 == 0)
      differentialDrive.arcadeDrive(move, rot); // arcade drive
    else {
      // tank drive
      float leftSpeed = (float) joystick.getLeftY();
      float rightSpeed = (float) joystick.getRightY();
      differentialDrive.tankDrive(leftSpeed, rightSpeed);
    }

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
