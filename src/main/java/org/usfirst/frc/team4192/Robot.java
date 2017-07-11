package org.usfirst.frc.team4192;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.usfirst.frc.team4192.autonRoutines.Default;
import org.usfirst.frc.team4192.autonRoutines.Left;
import org.usfirst.frc.team4192.autonRoutines.Middle;
import org.usfirst.frc.team4192.autonRoutines.Right;
import org.usfirst.frc.team4192.utilities.JaguarJoystick;


@SuppressWarnings("WeakerAccess")
public class Robot extends IterativeRobot {
  public static   CANTalon rightMaster;
  public static   CANTalon leftMaster;
  public static   CANTalon actuator;
  private static  CANTalon leftSlave;
  private static  CANTalon rightSlave;
  private static  CANTalon lift;
  
  public  static  JagDrive      jagDrive;
  public  static  boolean       actuatorInit;
  
  public  static  AHRS           gyro;
  public  static  PIDController  turnController;
  public  static  PIDController  moveController;
  
  private         JaguarJoystick joystick;
  
  private VisionThread visionThread;
  private double centerX;
  
  private final Object imgLock = new Object();
  
  private static double driveSensitivity;
  private boolean scoringPosition = false;
  
  public static void zeroSensors() {
    gyro.reset();
  }
  
  @Override
  public void robotInit() {
    lift          = new CANTalon(JagConstants.liftID);
    rightMaster   = new CANTalon(JagConstants.rightMasterID);
    rightSlave    = new CANTalon(JagConstants.rightSlaveID);
    actuator      = new CANTalon(JagConstants.actuatorID);
    leftSlave     = new CANTalon(JagConstants.leftSlaveID);
    leftMaster    = new CANTalon(JagConstants.leftMasterID);
    
    jagDrive    = new JagDrive(leftMaster, leftSlave, rightMaster, rightSlave);
    jagDrive.setSlewRate(60);
    driveSensitivity = 0.85;
    
    lift.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    
    actuator.changeControlMode(CANTalon.TalonControlMode.Position);
    actuator.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    
    joystick = new JaguarJoystick(JagConstants.joystick);
    
    gyro = new AHRS(SPI.Port.kMXP);
  
    gyro.setPIDSourceType(PIDSourceType.kDisplacement);
    turnController = new PIDController(JagConstants.gyroKp, JagConstants.gyroKi, JagConstants.gyroKd, gyro, arg0 -> {});
    turnController.setInputRange(-180, 180);
    turnController.setContinuous(true);
    turnController.setOutputRange(-.8, 8);
    turnController.setAbsoluteTolerance(1.0f);
    turnController.disable();
  
    rightMaster.setPIDSourceType(PIDSourceType.kDisplacement);
    moveController = new PIDController(JagConstants.driveKp, JagConstants.driveKi, JagConstants.driveKd, rightMaster,arg0 -> {});
    moveController.setOutputRange(-.6, .6);
    moveController.setAbsoluteTolerance(.05f);
    moveController.disable();
  
    /*UsbCamera camera = new UsbCamera("camera", "cam2");
    CameraServer.getInstance().startAutomaticCapture(camera);
    
    camera.setResolution(320, 240);
    
    centerX = 0;
    visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
      if (!pipeline.filterContoursOutput().isEmpty()) {
        Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
        synchronized (imgLock) {
          centerX = r.x + (r.width / 2);
        }
      }
    });
    visionThread.start();*/
    
    Thread dashboardUpdateThread = new Thread(() -> {
      while (!Thread.interrupted()) {
        SmartDashboard.putNumber("Gyro Angle",
            gyro.getAngle());
        
        SmartDashboard.putNumber("Left Encoder Value",
            -jagDrive.getLeftValue()/4096);
        
        SmartDashboard.putNumber("Actuator Encoder Position",
            actuator.getEncPosition());
      }
    });
    dashboardUpdateThread.start();
  }
  
  @Override
  public void disabledInit() {super.disabledInit();}
  
  @Override
  public void disabledPeriodic() {
    super.disabledPeriodic();
  }
  
  @Override
  public void autonomousInit() {
    zeroSensors();
    
    
    jagDrive.prepareForAuton();
    
    
    Left    leftAuton;
    Middle  middleAuton;
    Right   rightAuton;
    Default defaultAuton;
  
    String autonSelection = "Default";
    switch (autonSelection) {
      case "Left":
        leftAuton = new Left();
        leftAuton.start();
        break;
        
      case "Middle":
        middleAuton = new Middle();
        middleAuton.start();
        break;
      
      case "Right":
        rightAuton = new Right();
        rightAuton.start();
        break;
        
      default:
        defaultAuton = new Default();
        defaultAuton.start();
        break;
    }
  }
  
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }
  
  
  private void sensitivityControl() {
    if (joystick.buttonPressed(JagConstants.sensitivityButton)) {
      if (driveSensitivity == 0.65)
        driveSensitivity = 0.90;
      else
        driveSensitivity = 0.65;
    }
  }
  
  private void actuatorControl() {
    if (actuatorInit && joystick.buttonPressed(JagConstants.actuatorToggle)) {
      if (scoringPosition) {
        actuator.setSetpoint(JagConstants.gearLoadPosition);
        scoringPosition = false;
      } else if(actuatorInit){
        actuator.setSetpoint(JagConstants.gearScorePosition);
        scoringPosition = true;
      }
    }
  }
  
  private void driveControl() {
    jagDrive.arcadeDrive(
        -joystick.getYaxis()*driveSensitivity,
        -joystick.getXaxis()*driveSensitivity,
        true);
  }
  
  private void liftControl() {
    if (joystick.isHeldDown(JagConstants.liftButton))
      lift.set(1);
    else
      lift.set(0);
  }
  
  @Override
  public void teleopInit() {
    zeroSensors();
    actuator.enable();
    lift.enable();
    
    jagDrive.enable();
    jagDrive.prepareForTeleop();
    
    jagDrive.setSlewRate(60);
  }
  
  @Override
  public void teleopPeriodic() {
    joystick.update();
    driveControl();
    liftControl();
    sensitivityControl();
    actuatorControl();
  }
}