package org.usfirst.frc.team4192;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;

/**
 * Created by Al on 2/25/17.
 */
public class JagDrive extends RobotDrive{
  private CANTalon leftMaster, leftSlave, rightMaster, rightSlave;
  
  public JagDrive(CANTalon leftMaster, CANTalon leftSlave, CANTalon rightMaster, CANTalon rightSlave) {
    super(leftMaster, rightMaster);
    
    this.leftMaster  = leftMaster;
    this.rightMaster = rightMaster;
    this.leftSlave = leftSlave;
    this.rightSlave = rightSlave;
    
    leftMaster.setInverted(false);
    rightMaster.setInverted(false);
    
    leftMaster.enableBrakeMode(true);
    leftSlave.enableBrakeMode(true);
    rightMaster.enableBrakeMode(true);
    rightSlave.enableBrakeMode(true);
  
    leftMaster.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    rightMaster.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
  
    leftMaster.reverseOutput(false);
    leftMaster.reverseSensor(true);
    
    rightMaster.reverseOutput(true);
    
    leftSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
    leftSlave.set(leftMaster.getDeviceID());
    rightSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
    rightSlave.set(rightMaster.getDeviceID());
    
    leftMaster.configNominalOutputVoltage(+0.0f, -0.0f);
    leftSlave.configPeakOutputVoltage(+12.0f, -12.0f);
    rightMaster.configNominalOutputVoltage(+0.0f, -0.0f);
    rightSlave.configPeakOutputVoltage(+12.0f, -12.0f);
    
    setExpiration(0.1);
  
    
  }
  
  public void setSlewRate(double rampRate) {
    leftMaster.setVoltageRampRate(rampRate);
    rightMaster.setVoltageRampRate(rampRate);
  }
  
  public void prepareForAuton() {
    leftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    rightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    setSlewRate(60);
    setSafetyEnabled(false);
    zeroEncoders();
  }
  
  public void prepareForTeleop() {
    leftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    rightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    setSafetyEnabled(true);
    setSlewRate(60);
    zeroEncoders();
  }
  
  public void zeroEncoders() {
    leftMaster.setEncPosition(0);
    rightMaster.setEncPosition(0);
  }
  
  public double getLeftValue() {
    return leftMaster.getEncPosition();
  }
  
  public double getRightValue() {
    return rightMaster.getEncPosition();
  }
  
  
  public void disable() {
    leftMaster.disable();
    rightMaster.disable();
  }
  
  public void enable() {
    leftMaster.enable();
    rightMaster.enable();
  }
  
}
