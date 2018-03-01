/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1153.robot;

import org.usfirst.frc.team1153.robot.commandGroups.CenterSwitch;
import org.usfirst.frc.team1153.robot.commandGroups.DriveForwardAndScore;
import org.usfirst.frc.team1153.robot.commandGroups.DriveForwardNoScore;
import org.usfirst.frc.team1153.robot.commandGroups.FarLeftOppositeSideSwitchScore;
import org.usfirst.frc.team1153.robot.commandGroups.FarLeftSwitchScore;
import org.usfirst.frc.team1153.robot.commandGroups.FarRightOppositeSideSwitchScore;
import org.usfirst.frc.team1153.robot.commandGroups.FarRightSwitchScore;
import org.usfirst.frc.team1153.robot.lib.StateScheduler;
import org.usfirst.frc.team1153.robot.subsystems.ArmsHorizontal;
import org.usfirst.frc.team1153.robot.subsystems.ArmsVertical;
import org.usfirst.frc.team1153.robot.subsystems.AutoDrive;
import org.usfirst.frc.team1153.robot.subsystems.Carriage;
import org.usfirst.frc.team1153.robot.subsystems.Climber;
import org.usfirst.frc.team1153.robot.subsystems.Collector;
import org.usfirst.frc.team1153.robot.subsystems.LimelightVision;
import org.usfirst.frc.team1153.robot.subsystems.PTO;
import org.usfirst.frc.team1153.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {

	private Command autoCommand;

	static int loops = 0;

	public static OI oi;
	public static AutoDrive autoDrive;
	public static Shooter shooter;
	public static Carriage carriage;
	public static Collector collector;
	public static ArmsHorizontal collectorArmsHorizontal;
	public static ArmsVertical collectorArmsVertical;
	public static LimelightVision vision;
	public static Climber climber;
	public static PTO pto;

	public static double initialWait = 0;
	public static double middleWait = 0;

	private SendableChooser<String> routineChooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		autoDrive = new AutoDrive();
		shooter = new Shooter();
		carriage = new Carriage();
		collector = new Collector();
		collectorArmsHorizontal = new ArmsHorizontal();
		collectorArmsVertical = new ArmsVertical();
		climber = new Climber();
		pto = new PTO();
		vision = new LimelightVision();
		oi = new OI();

		vision.turnOffLight();
		autoDrive.calibrateGyro();

		StateScheduler.getInstance().addStateSubsystem(shooter);
		StateScheduler.getInstance().addStateSubsystem(collector);
		StateScheduler.getInstance().addStateSubsystem(collectorArmsHorizontal);
		StateScheduler.getInstance().addStateSubsystem(collectorArmsVertical);
		StateScheduler.getInstance().addStateSubsystem(carriage);
		StateScheduler.getInstance().addStateSubsystem(climber);
		StateScheduler.getInstance().addStateSubsystem(pto);

		routineChooser.addDefault("Center", "Center");
		routineChooser.addDefault("Left", "Left");
		routineChooser.addDefault("Right", "Right");
		routineChooser.addDefault("Far Right", "Far Right");
		routineChooser.addDefault("Far Left", "Far Left");
		SmartDashboard.putData("Position", routineChooser);

		/**
		 * text boxes for delays
		 */

		SmartDashboard.putNumber("Initial Delay", 0);
		SmartDashboard.putNumber("Middle Delay", 0);

	}

	public static void updateDashboard() {
		// if (++loops >= 10) {
		// loops = 0;

		SmartDashboard.putNumber("Right Motor Motion Magic Error", autoDrive.getRightMotorClosedLoopError());

		SmartDashboard.putNumber("Right Motor Sensor Position", autoDrive.getRightMotorSensorPosition());
		SmartDashboard.putNumber("Right Motor Sensor Velocity", autoDrive.getRightMotorSensorVelocity());

		SmartDashboard.putNumber("Left Motor Motion Magic Error", autoDrive.getLeftMotorClosedLoopError());

		SmartDashboard.putNumber("Left Motor Sensor Position", autoDrive.getLeftMotorSensorPosition());
		SmartDashboard.putNumber("Left Motor Sensor Velocity", autoDrive.getLeftMotorSensorVelocity());

		SmartDashboard.putNumber("PID ERROR", autoDrive.gyroError());
		SmartDashboard.putNumber("PID Output", autoDrive.getGyroOutput());

		// }

		SmartDashboard.putNumber("AD Gyro Reading", autoDrive.getGyroAngle());
		// SmartDashboard.putNumber("Collector Motor Value",
		// collector.getMotorOutput());

		SmartDashboard.putBoolean("Right Limit Switch", collectorArmsVertical.getRightLimitSwitchState());
		SmartDashboard.putBoolean("Left Limit Switch", collectorArmsVertical.getLeftLimitSwitchState());

	}

	/**
	 * This function is called once each time the robot enters Disabled mode. You
	 * can use it to reset any subsystem information you want to clear when the
	 * robot is disabled.
	 */
	@Override
	public void disabledInit() {
		StateScheduler.getInstance().notifyDisabled();
	}

	@Override
	public void disabledPeriodic() {
		updateDashboard();
		Scheduler.getInstance().run();
		StateScheduler.getInstance().runAll();
	}

	private boolean robotPosEqual(String position) {
		return routineChooser.getSelected().equalsIgnoreCase(position);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString code to get the
	 * auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons to
	 * the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		StateScheduler.getInstance().notifyAuto();

		autoDrive.setServoValue(180);

		vision.turnOnLight();
		autoDrive.resetGyro();
		autoDrive.setEncoderAsFeedback();
		autoDrive.configTalonOutput();
		autoDrive.setFollowers();
		autoDrive.resetEncoders();

		// Carriage and Collector Stuff
		collectorArmsVertical.upInit();
		collectorArmsHorizontal.outInit();
		carriage.upInit();

		initialWait = SmartDashboard.getNumber("Initial Delay", 0);
		middleWait = SmartDashboard.getNumber("Middle Delay", 0);

		String autoPattern = DriverStation.getInstance().getGameSpecificMessage();
		char switchPos = autoPattern.charAt(0);

		System.out.println("switchPos\t" + switchPos);
		System.out.println("chooser:\t" + routineChooser.getSelected());

		if ((robotPosEqual("Right") && switchPos == 'R') || (robotPosEqual("Left") && switchPos == 'L')) {

			autoCommand = new DriveForwardAndScore();

		} else if (robotPosEqual("Center") && switchPos == 'R') {

			autoCommand = new CenterSwitch(50, 50, 5, 'R');

		} else if (robotPosEqual("Center") && switchPos == 'L') {

			autoCommand = new CenterSwitch(-50, 60, -5, 'L');

		} else if (robotPosEqual("Far Right") && switchPos == 'R') {

			autoCommand = new FarRightSwitchScore();

		} else if (robotPosEqual("Far Right") && switchPos == 'L') {

			autoCommand = new FarRightOppositeSideSwitchScore();
			
		} else if (robotPosEqual("Far Left") && switchPos == 'R') {

			autoCommand = new FarLeftOppositeSideSwitchScore();

		} else if (robotPosEqual("Far Left") && switchPos == 'L') {

			autoCommand = new FarLeftSwitchScore();

		} else {

			autoCommand = new DriveForwardNoScore();

		}

		// autoCommand = new DriveDistanceCommand(120, -120);

		// autoCommand = new CenterSwitch(30, 50, 1.5, 'R');
		// autoCommand = new CenterSwitch(30);
		// autoCommand = new CenterSwitch(30);
		autoCommand.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		StateScheduler.getInstance().runAll();
		updateDashboard();

		vision.updateLimelightData();
	}

	@Override
	public void teleopInit() {
		StateScheduler.getInstance().notifyTeleop();

		autoDrive.setServoValue(180);

		autoDrive.resetEncoders();
		carriage.downInit();
		autoDrive.resetEncoders();
		vision.turnOffLight();

	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		StateScheduler.getInstance().runAll();
		updateDashboard();

		// if (oi.getDriverStick().getRawButtonPressed(3)) {
		// pto.engagedInit();
		// } else if (oi.getDriverStick().getRawButtonReleased(3)) {
		// pto.disengagedInit();
		// }

		if (oi.getDriverStick().getRawButtonPressed(6)) {
			climber.moveNewClimber(1);
		} else if (oi.getDriverStick().getRawButtonReleased(6)) {
			climber.moveNewClimber(0);
		}

		if (oi.getDriverStick().getRawButtonPressed(5)) {
			climber.moveNewClimber(-1);
		} else if (oi.getDriverStick().getRawButtonReleased(5)) {
			climber.moveNewClimber(0);
		}

		// climber.moveNewClimber(oi.getOpStick().getRawAxis(1));

		// if (oi.getDriverStick().getRawButtonPressed(4)) {
		// autoDrive.arcadeDriveNoJoystick(0.8);
		// } else if (oi.getDriverStick().getRawButtonReleased(4)) {
		// autoDrive.arcadeDriveNoJoystick(0);
		// }

		autoDrive.createDriveSignal(true);

	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
