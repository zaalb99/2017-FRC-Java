package org.usfirst.frc.team4192.autonRoutines;

import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team4192.Robot;

public class Auton{
  
  public static void PIDmove(double inches, int target){
    if(Robot.autonStep == target){
      Robot.moveController.setSetpoint(inchesToTicks(inches));
      Robot.turnController.setSetpoint(0);
      
      if(!Robot.moveController.onTarget())
        Robot.jagDrive.drive(Robot.moveController.get(),Robot.turnController.get());
      else{
        Robot.jagDrive.drive(0,0);
        Robot.moveController.disable();
        Robot.turnController.disable();
        Robot.gyro.reset();
        Robot.jagDrive.zeroEncoders();
        Robot.autonStep ++;
        Timer.delay(.5);
      }
    }
  }
  
  public static void PIDturn(double targetAngle, int target){
    if(Robot.autonStep == target){
      Robot.turnController.setSetpoint(targetAngle);
      if(!Robot.turnController.onTarget()){
        Robot.jagDrive.drive(0,Robot.turnController.get());
      }
      else{
        Robot.jagDrive.drive(0,0);
        Robot.turnController.disable();
        Robot.gyro.reset();
        Robot.jagDrive.zeroEncoders();
        Robot.autonStep ++;
        Timer.delay(.5);
      }
    }
  }
  
  private static double inchesToTicks(double inches){
    return (inches * (Math.PI * 6) * 4096);
  }
  
}

/*public void moveForward(int inches, int target) {
    if(Robot.autonStep == target) {
      Robot.targetRotations = (inches / (6 * Math.PI));
      double angle = Robot.gyro.getAngle() * .023;
      Robot.timerRunning = Robot.timer.get();
      
      if (Robot.jagDrive.getRightValue() / 4096 < Robot.targetRotations - 1.47) {
        Robot.jagDrive.drive(speed, -angle);
      }
      
      if (Robot.jagDrive.getRightValue() / 4096 >= Robot.targetRotations - 1.47) {
        Robot.jagDrive.drive(0, -angle);
        Robot.jagDrive.zeroEncoders();
        Robot.gyro.reset();
        Robot.timer.delay(1.5);
        Robot.leftFinish = Robot.jagDrive.getLeftValue() / 4096;
        Robot.rightFinish = Robot.jagDrive.getRightValue() / 4096;
        Robot.autonStep += 1;
      }
    }
  }*/

/* public void actuateOut(int target){
    if(Robot.autonStep == target) {
      Robot.actuator.setSetpoint(JagConstants.gearScorePosition);
      Robot.timer.delay(2);
    }
  }
  
  public void actuateIn(int target) {
    if(Robot.autonStep == target)
      Robot.actuator.setSetpoint(JagConstants.gearLoadPosition);
  }*/