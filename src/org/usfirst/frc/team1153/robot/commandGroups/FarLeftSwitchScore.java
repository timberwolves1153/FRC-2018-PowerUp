package org.usfirst.frc.team1153.robot.commandGroups;

import org.usfirst.frc.team1153.robot.Robot;
import org.usfirst.frc.team1153.robot.commands.CarriageUpCommand;
import org.usfirst.frc.team1153.robot.commands.DriveDistanceCommand;
import org.usfirst.frc.team1153.robot.commands.FireShooterCommand;
import org.usfirst.frc.team1153.robot.commands.GyroTurnAbsoluteCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class FarLeftSwitchScore extends CommandGroup {

	double baselineToSideSwitchDistance = 140;
	double turnToSwitchDistance = 18;

	public FarLeftSwitchScore() {
		super();
		
		if (Robot.initialWait > 0.0) {
			addSequential(new WaitCommand(Robot.initialWait));
		}
		
		addSequential (new CarriageUpCommand());
		addSequential(new DriveDistanceCommand(baselineToSideSwitchDistance, -1 * baselineToSideSwitchDistance));
		addSequential(new WaitCommand(1));
		addSequential(new GyroTurnAbsoluteCommand(90));
		addSequential(new DriveDistanceCommand(turnToSwitchDistance, -1 * turnToSwitchDistance));
		addSequential(new FireShooterCommand());
	}

}
