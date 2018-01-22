package ca.mcgill.ecse211.WallFollowingLab;

import lejos.hardware.motor.*;

public class BangBangController implements UltrasonicController {

  private final int bandCenter;
  private final int bandwidth;
  private final int motorLow;
  private final int motorHigh;
  private int distance;
  
  public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
    // Default Constructor
    this.bandCenter = bandCenter;
    bandCenter = 25;				//restating the bandcenter allowed us to have sperate bandcenters for each controller
    this.bandwidth = bandwidth;
    this.motorLow = motorLow;
    this.motorHigh = motorHigh;
    WallFollowingLab.leftMotor.setSpeed(motorHigh); // Start robot moving forward
    WallFollowingLab.rightMotor.setSpeed(motorHigh);
    WallFollowingLab.rightMotor.forward();
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.sensorMotor.setSpeed(200);
    WallFollowingLab.sensorMotor.rotate(-45);
  }
  
  @Override
  public void processUSData(int distance) {
    this.distance = distance;
    //This variable x is a multiplier to increase the speed if desired
    int x = 1;
    // TODO: process a movement based on the us distance passed in (BANG-BANG style)
	
    //This if chain ensures that the robot does not approach the wall too closely
    if(distance < 15) {										//This case is to prevent any collisions
    	WallFollowingLab.rightMotor.setSpeed(motorLow);
    	WallFollowingLab.leftMotor.setSpeed(motorLow);
    	WallFollowingLab.rightMotor.backward();
    }
    else if(distance <= bandCenter - bandwidth) {			//This case occurs duing normal operation - if the robot is slightly too close its makes a correction outward
    	WallFollowingLab.rightMotor.forward();
    	WallFollowingLab.rightMotor.setSpeed(x*motorLow);
    	WallFollowingLab.leftMotor.setSpeed(x*(motorHigh + 50));
    }
    else if(distance >= bandCenter + bandwidth) {			//If the robot is too far it will make a correction inwards to the wall
    	WallFollowingLab.rightMotor.forward();
    	WallFollowingLab.rightMotor.setSpeed(x*motorHigh);
    	WallFollowingLab.leftMotor.setSpeed(x*(motorLow));
    }
    else {													//If the robot is within the bandwidth it proceeds in a stright manner
    	WallFollowingLab.rightMotor.forward();
    	WallFollowingLab.rightMotor.setSpeed(x*motorHigh);
    	WallFollowingLab.leftMotor.setSpeed(x*motorHigh);
    }
    
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}

