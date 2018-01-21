package ca.mcgill.ecse211.WallFollowingLab;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class PController implements UltrasonicController {

  /* Constants */
  private static final int MOTOR_SPEED = 200;
  private static final int FILTER_OUT = 20;

  private final int bandCenter;
  private final int bandWidth;
  private int distance;
  private int filterControl;
  private int dClose;
  private int dFar;
  public PController(int bandCenter, int bandwidth) {
    this.bandCenter = bandCenter;
    this.bandWidth = bandwidth;
    this.filterControl = 0;

    WallFollowingLab.leftMotor.setSpeed(MOTOR_SPEED); // Initalize motor rolling forward
    WallFollowingLab.rightMotor.setSpeed(MOTOR_SPEED);
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
  }

  @Override
  public void processUSData(int distance) {
	  int x = 2;
	  double P = 2.0;
    // rudimentary filter - toss out invalid samples corresponding to null
    // signal.
    // (n.b. this was not included in the Bang-bang controller, but easily
    // could have).
    //
	
    if (distance >= 255 && filterControl < FILTER_OUT) {
      // bad value, do not set the distance var, however do increment the
      // filter value
      filterControl++;
    } else if (distance >= 255) {
      // We have repeated large values, so there must actually be nothing
      // there: leave the distance alone
      this.distance = distance;
    } else {
      // distance went below 255: reset filter and leave
      // distance alone.
      filterControl = 0;
      this.distance = distance;
    }
    
	  
	//this.distance = distance;
	
    dClose = (int) ((bandCenter - distance)*P);
    dFar = (int) ((distance - bandCenter)*P);
    if(dClose > 30){
    	dClose = 30;
    }
    if(dFar > 30) {
    	dFar = 30;
    }
    // TODO: process a movement based on the us distance passed in (P style)
    if(distance < bandCenter - bandWidth) {
    	WallFollowingLab.rightMotor.setSpeed(x*(MOTOR_SPEED - dClose));
    	WallFollowingLab.leftMotor.setSpeed(x*(MOTOR_SPEED + dClose + 25));
    }
    else if(distance > bandCenter + bandWidth) {
    	WallFollowingLab.rightMotor.setSpeed(x*(MOTOR_SPEED + dFar));
    	WallFollowingLab.leftMotor.setSpeed(x*(MOTOR_SPEED - dFar));
    }
    else {
    	WallFollowingLab.rightMotor.setSpeed(x*MOTOR_SPEED);
    	WallFollowingLab.leftMotor.setSpeed(x*MOTOR_SPEED);
    }
  }


  @Override
  public int readUSDistance() {
    return this.distance;
  }

}

