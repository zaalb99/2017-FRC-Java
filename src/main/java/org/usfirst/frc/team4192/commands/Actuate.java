package org.usfirst.frc.team4192.commands;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4192.JagConstants;
import org.usfirst.frc.team4192.Robot;

public class Actuate extends Command{
  private double driveTarget;
  private boolean scored;
  
  public Actuate() {
    scored = false;
  }
  
  @Override
  protected void initialize() {
    Robot.actuator.changeControlMode(CANTalon.TalonControlMode.Position);
    Robot.actuator.enable();
    Robot.actuator.setSetpoint(JagConstants.gearScorePosition);
  }
  
  @Override
  protected void execute() {
    if(Robot.actuator.getEncPosition() >= JagConstants.gearScorePosition - 3 && Robot.actuator.getEncPosition() <= JagConstants.gearScorePosition + 3 && !scored){
      Robot.actuator.setSetpoint(JagConstants.gearLoadPosition);
      scored = true;
    }
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
    return (Robot.actuator.getEncPosition() >= JagConstants.gearLoadPosition - 3 && Robot.actuator.getEncPosition() <= JagConstants.gearLoadPosition + 3 && scored);
  }
}
