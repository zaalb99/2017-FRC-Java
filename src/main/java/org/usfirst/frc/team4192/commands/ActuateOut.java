package org.usfirst.frc.team4192.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4192.JagConstants;
import org.usfirst.frc.team4192.Robot;

public class ActuateOut extends Command{
  
  public ActuateOut(){
  }
  
  @Override
  protected void initialize() {
    Robot.actuator.enable();
    Robot.actuator.setSetpoint(JagConstants.gearScorePosition);
  }
  
  @Override
  protected void execute() {
  }
  
  @Override
  public void end() {
    Robot.actuator.disable();
  }
  
  @Override
  public void interrupted() {
    end();
  }
  
  @Override
  protected boolean isFinished() {
    return(Robot.actuator.getEncPosition() >= JagConstants.gearScorePosition-5);
  }
}
