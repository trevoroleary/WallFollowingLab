package ca.mcgill.ecse211.WallFollowingLab;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class PController implements UltrasonicController {

  /* Constants */
  private static final int MOTOR_SPEED = 200;
  private final int bandCenter;
  private final int bandWidth;
  private int distance;
  private int dClose;
  private int dFar;
  public PController(int bandCenter, int bandwidth) {
    this.bandCenter = bandCenter;
    bandCenter = 33;
    this.bandWidth = bandwidth;

    WallFollowingLab.leftMotor.setSpeed(MOTOR_SPEED); // Initalize motor rolling forward
    WallFollowingLab.rightMotor.setSpeed(MOTOR_SPEED);
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
  }

  @Override
  public void processUSData(int distance) {
	  int x = 1;
	  double P = 1.5;
  
	this.distance = distance;
	
	if(distance > 70) {
		distance = 70;
	}
	
	//Variables used for changing the motor speeds relative to the error
    dClose = (int) ((bandCenter - distance)*P);
    dFar = (int) ((distance - bandCenter)*P);
    
    //If the motor speed change variables are too great it will result in a negative number passed to the motor speed
    //We did not want the motor to go reverse or completely stop because we found that it would not start again
    //This was all we needed for a filter
    if(dClose > 200){
    	dClose = 199;
    }
    if(dClose < 0) {
    	dClose = 1;
    }
    if(dFar > 200) {
    	dFar = 199;
    }
    if(dFar < 0) {
    	dFar = 1;
    }
    
    //Restating the motor direction is important to ensure the fail safe does not effect normal operation
    WallFollowingLab.rightMotor.forward();
	WallFollowingLab.leftMotor.forward();
	
	//This if chain ensures that the robot does not approach the wall too closely
	if(distance < 15) {									//Fail safe to prevent a collision if the robot is too close it will rotate clockwise
		WallFollowingLab.leftMotor.setSpeed(x*MOTOR_SPEED);
		WallFollowingLab.rightMotor.setSpeed(x*MOTOR_SPEED);
		WallFollowingLab.rightMotor.backward();
		
	}
	else if(distance < bandCenter - bandWidth) {		//If the robot is too close to the wall it will adjust the motor speeds relative to the error
		WallFollowingLab.rightMotor.forward();
    	WallFollowingLab.rightMotor.setSpeed(x*(MOTOR_SPEED - dClose));
    	WallFollowingLab.leftMotor.setSpeed(x*(MOTOR_SPEED + dClose));
    }
    else if(distance > bandCenter + bandWidth) {		//If the robot is too far from the wall it will adjust the motor speeds relative to the error
    	WallFollowingLab.rightMotor.forward();
    	WallFollowingLab.rightMotor.setSpeed(x*(MOTOR_SPEED + dFar));
    	WallFollowingLab.leftMotor.setSpeed(x*(MOTOR_SPEED - dFar));
    }
    else {
    	WallFollowingLab.rightMotor.forward();			//If the robot is within the bandcenter it will proceed in a normal forward fassion
    	WallFollowingLab.rightMotor.setSpeed(x*MOTOR_SPEED);
    	WallFollowingLab.leftMotor.setSpeed(x*MOTOR_SPEED);
    }
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }

}

