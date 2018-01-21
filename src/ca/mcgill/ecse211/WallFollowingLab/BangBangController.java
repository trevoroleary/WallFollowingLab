package ca.mcgill.ecse211.WallFollowingLab;

import lejos.hardware.motor.*;

public class BangBangController implements UltrasonicController {

  private final int bandCenter;
  private final int bandwidth;
  private final int motorLow;
  private final int motorHigh;
  private int distance;
  
  private int sensorAngle = -90;
  private int counter = 0;
  //private boolean gapSkip;
  //private int count = 30;
  
  
  public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
    // Default Constructor
    this.bandCenter = bandCenter;
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
    counter++;
    int x = 3;
    // TODO: process a movement based on the us distance passed in (BANG-BANG style)
	   
    if(distance <= bandCenter - bandwidth) {
    	WallFollowingLab.rightMotor.setSpeed(x*motorLow);
    	WallFollowingLab.leftMotor.setSpeed(x*(motorHigh + 50));
    }
    else if(distance >= bandCenter + bandwidth) {
    	WallFollowingLab.rightMotor.setSpeed(x*motorHigh);
    	WallFollowingLab.leftMotor.setSpeed(x*(motorLow));
    }
    else {
    	WallFollowingLab.rightMotor.setSpeed(x*motorHigh);
    	WallFollowingLab.leftMotor.setSpeed(x*motorHigh);
    }
    
    /*
    if(distance <= bandCenter - bandwidth) {
	    	if(sensorAngle == -45) {
	    		WallFollowingLab.sensorMotor.rotate(-45);
	    		sensorAngle = -90;
	    	}
	    	WallFollowingLab.leftMotor.setSpeed(motorHigh + 50);
	    	WallFollowingLab.rightMotor.setSpeed(motorLow - 10);
	    }
    
    
	    else if(distance >= 50 && sensorAngle == -90) {
	    	if(sensorAngle == -90) {
		    	WallFollowingLab.sensorMotor.rotate(45);
		    	sensorAngle = -45;
	    	}
	    }
	    else if(distance >= bandCenter + bandwidth) {
	    	WallFollowingLab.rightMotor.setSpeed(motorHigh);
	    	WallFollowingLab.leftMotor.setSpeed(motorLow);
	    	if(sensorAngle == -45) {
	    		WallFollowingLab.sensorMotor.rotate(-45);
	    		sensorAngle = -90;
	    	}
	    }
	    else {
	    	if(sensorAngle == -45) {
	    		WallFollowingLab.sensorMotor.rotate(-45);
	    		sensorAngle = -90;
	    	}
	    	WallFollowingLab.leftMotor.setSpeed(motorHigh);
	    	WallFollowingLab.rightMotor.setSpeed(motorHigh);
	    }
	    */
    }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}

