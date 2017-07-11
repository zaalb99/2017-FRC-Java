package org.usfirst.frc.team4192.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4192.Robot;

public class Turn extends Command{
  private double gyroTarget;
  
  public Turn(double gyroTarget) {
    this.gyroTarget = gyroTarget;
    SmartDashboard.putNumber("Gyro Target", this.gyroTarget);
  }
  
  @Override
  protected void initialize() {
    Robot.jagDrive.prepareForAuton();
    Robot.jagDrive.enable();
    Robot.turnController.enable();
    Robot.turnController.setSetpoint(gyroTarget);
  }
  
  @Override
  protected void execute() {
    Robot.jagDrive.drive(0,Robot.turnController.get());
  }
  
  @Override
  public void end() {
    Robot.jagDrive.drive(0,0);
    Robot.jagDrive.disable();
    Robot.turnController.disable();
  }
  
  @Override
  public void interrupted() {
    end();
  }
  
  @Override
  protected boolean isFinished() {
    return Robot.moveController.onTarget();
  }
}
