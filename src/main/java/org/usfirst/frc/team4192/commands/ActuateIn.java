package org.usfirst.frc.team4192.commands;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4192.JagConstants;
import org.usfirst.frc.team4192.Robot;

public class ActuateIn extends Command{
  
  public ActuateIn(){
  }
  
  @Override
  protected void initialize() {
    Robot.actuator.changeControlMode(CANTalon.TalonControlMode.Position);
    Robot.actuator.enable();
    Robot.actuator.setSetpoint(JagConstants.gearLoadPosition);
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
    return(Robot.actuator.getEncPosition() <= JagConstants.gearLoadPosition+5);
  }
}
