package org.usfirst.frc.team1153.robot.commandGroups;

import org.usfirst.frc.team1153.robot.commands.CarriageUpCommand;
import org.usfirst.frc.team1153.robot.commands.FireShooterCommand;
import org.usfirst.frc.team1153.robot.commands.GyroTurnRelativeCommand;
import org.usfirst.frc.team1153.robot.commands.TimedDriveCommand;
import org.usfirst.frc.team1153.robot.commands.VisionDriveSwitch;
import org.usfirst.frc.team1153.robot.commands.WaitCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CenterSwitch extends CommandGroup {

	/**
	 * @param degreesToTurn
	 */
	public CenterSwitch(int degreesToTurn, double timeToDrive, double turnDiviser ) {
		
		/**
		 * Working code for 
		 */
		// addSequential (new WaitCommand(Robot.initialWait));
		addSequential(new GyroTurnRelativeCommand(degreesToTurn));
		addSequential( new TimedDriveCommand(0.4, timeToDrive));
		addSequential(new GyroTurnRelativeCommand(-(degreesToTurn/turnDiviser)));
		addSequential(new VisionDriveSwitch());
		addSequential(new CarriageUpCommand());
		addSequential(new FireShooterCommand());

	}
}