package frc.robot;

//FRC
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;

//REV
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
//NAVX
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class Robot extends TimedRobot {

  int konum = 2;

  double maxSpeed = 0.5;
  double heading;
  double error;
  double newAngle;

  private final XboxController controller = new XboxController(0);
  private final AHRS gyro = new AHRS(SPI.Port.kMXP);
  private final Timer timer = new Timer();

  //ROBOTS
  private final CANSparkMax leftMotor = new CANSparkMax(9, MotorType.kBrushed);
  private final CANSparkMax leftFollowing = new CANSparkMax(8, MotorType.kBrushed);
  private final CANSparkMax rightMotor = new CANSparkMax(7, MotorType.kBrushed);
  private final CANSparkMax rightFollowing = new CANSparkMax(6, MotorType.kBrushed);
  
  private final DifferentialDrive robot = new DifferentialDrive(leftMotor, rightMotor);

  //SHOOTER
  private final CANSparkMax shooter = new CANSparkMax(5, MotorType.kBrushed);
  private final CANSparkMax shooterFollowing = new CANSparkMax(4, MotorType.kBrushed);

  @Override
  public void robotInit() {
    leftFollowing.follow(leftMotor);
    rightFollowing.follow(rightMotor);
    rightMotor.setInverted(true);

    shooterFollowing.follow(shooter);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("gyro", gyro.getAngle());
    SmartDashboard.putNumber("heading", heading);
    SmartDashboard.putNumber("error", error);
    SmartDashboard.putNumber("newAngle", newAngle);
    SmartDashboard.putNumber("shooter", shooter.get());
    SmartDashboard.putNumber("leftSpeed", leftMotor.get());
    SmartDashboard.putNumber("rightSpeed", rightMotor.get());
  }

  @Override
  public void autonomousInit() {
    timer.reset();
    timer.start();

    heading = gyro.getAngle();
  }

  @Override
  public void autonomousPeriodic() {
    if (konum == 1) {
      if (timer.get()<=1) {
        shooter.set(-1);
        robot.arcadeDrive(0, 0);
      } if (timer.get()>1 && timer.get()<=2) {
        shooter.set(0);
        robot.arcadeDrive(-0.5, 0);
      } if (timer.get()>2 && timer.get()<=7) {
        newAngle = heading+50;
        error = newAngle-gyro.getAngle();
        if (error<=25) {
          shooter.set(0);
          robot.arcadeDrive(0, 0);
        } if (error>25) {
          shooter.set(0);
          robot.arcadeDrive(0, -0.4);
        }
      } if (timer.get() > 7 && timer.get()<=8) {
        shooter.set(0);
        robot.arcadeDrive(-0.5, 0);
      }
    } 
    
    if (konum == 2) {
      if (timer.get()<=2) {
        shooter.set(-1);
        robot.arcadeDrive(0, 0);
      } if (timer.get()>2 && timer.get()<=4) {
        shooter.set(0);
        robot.arcadeDrive(-0.5, 0);
      }
    } 
    
    if (konum == 3) {
      //2'yi Kullan
    } 
    
    if (konum == 4) {
      if (timer.get()<2) {
        robot.arcadeDrive(-0.5, 0);
      } 
    }
  }

  @Override
  public void teleopInit() {}                                                   

  @Override
  public void teleopPeriodic() {
    if (controller.getLeftBumper()) {
      maxSpeed=0.6;
    } if (controller.getRightBumper()) {
      maxSpeed=0.8;
    } if (controller.getRawButtonPressed(1)) {
      shooter.set(-1);
    } if (controller.getRawButtonReleased(1)) {
      shooter.set(0);
    } if (controller.getRawButtonPressed(4)) {
      shooter.set(0.5);
    } if (controller.getRawButtonReleased(4)) {
      shooter.set(0);
    }

    robot.arcadeDrive(-controller.getLeftY()*maxSpeed, controller.getRightX()*maxSpeed);
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
