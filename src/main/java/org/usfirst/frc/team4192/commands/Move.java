package org.usfirst.frc.team4192.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4192.Robot;

public class Move extends Command{
  private double driveTarget;
  
  public Move(double inches) {
    driveTarget = inches * Math.PI * 4096;
    SmartDashboard.putNumber("Drive Target Inches", inches);
    SmartDashboard.putNumber("Drive Target Ticks", driveTarget);
  }

  @Override
  protected void initialize() {
    Robot.jagDrive.prepareForAuton();
    Robot.jagDrive.enable();
    Robot.moveController.enable();
    Robot.turnController.enable();
    Robot.moveController.setSetpoint(driveTarget);
    Robot.moveController.setSetpoint(0);
  }
  
  @Override
  protected void execute() {
    Robot.jagDrive.drive(Robot.moveController.get(),Robot.turnController.get());
  }
  
  @Override
  public void end() {
    Robot.jagDrive.drive(0,0);
    Robot.jagDrive.disable();
    Robot.moveController.disable();
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
