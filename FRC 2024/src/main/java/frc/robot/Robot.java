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
import com.revrobotics.RelativeEncoder;


public class Robot extends TimedRobot {

  double maxSpeed = 0.5;

  private final XboxController controller = new XboxController(0);
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

  private RelativeEncoder encoder;

  @Override
  public void robotInit() {
    leftFollowing.follow(leftMotor);
    rightFollowing.follow(rightMotor);
    rightMotor.setInverted(true);

    shooterFollowing.follow(shooter);

    encoder = leftMotor.getAlternateEncoder(8192);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("encoder", encoder.getPosition());
  }

  @Override
  public void autonomousInit() {
    timer.reset();
    timer.start();
  }

  @Override
  public void autonomousPeriodic() {
    if (timer.get()<=2) {
      shooter.set(-1);
      robot.arcadeDrive(0, 0);
    } if (timer.get()>2 && timer.get()<=4) {
      shooter.set(0);
      robot.arcadeDrive(-0.5, 0);
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
