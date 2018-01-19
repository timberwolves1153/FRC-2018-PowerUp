/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1153.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	private static final int DRIVER_JOYSTICK = 0;
	private static final int OPERATOR_STICK = 1;
	
	public static final int JOYSTICK_LEFT_Y = 1;
	public static final int JOYSTICK_RIGHT_X = 4;
	
	private Joystick opStick = new Joystick(OPERATOR_STICK);
    private Joystick driverStick = new Joystick(DRIVER_JOYSTICK);
    
    public Joystick getOpStick() {
    	return opStick;
    }
    
    public Joystick getDriverStick() {
    	return driverStick;
    }
}
