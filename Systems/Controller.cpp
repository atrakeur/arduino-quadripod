#include "Arduino.h"
#include <Servo.h>

#include "Controller.h"

extern const char COMM_ERROR_ARM_COUNT[] = "Arm %d gt max %d";
extern const char COMM_ERROR_ARM_NOTDEFINED[] = "Arm %d allready defined";
extern const char COMM_ERROR_ARM_WRONGPOS[] = "Arm %d can't move in position %d";

void Controller::init(Comm* comm)
{
	this->comm = comm;

	for (int i = 0; i < CONTROLLER_ARM_COUNT; i++) 
	{
		this->arms[i].defined = 0;
	}
}

void Controller::defineArm(int armPos, const int armData[6]) 
{
	if (armPos >= CONTROLLER_ARM_COUNT) 
	{
		this->comm->sendError(COMM_ERROR_ARM_COUNT, armPos, CONTROLLER_ARM_COUNT);
		return;
	}

	if (this->arms[armPos].defined == 1)
	{
		this->comm->sendError(COMM_ERROR_ARM_NOTDEFINED, armPos);
		return;	
	}

	this->arms[armPos].defined = 1;
	memcpy(this->arms[armPos].limits, &armData[2], sizeof(int) * 4);
	this->arms[armPos].servoX.attach(armData[0]);
	this->arms[armPos].servoY.attach(armData[1]);

	this->comm->sendCommand("Controller@defineArm", "%d [%d, %d]", armData[0], armData[2], armData[3]);
	this->comm->sendCommand("Controller@defineArm", "%d [%d, %d]", armData[1], armData[4], armData[5]);
}

void Controller::setXPosition(int armPos, int position)
{
	if (armPos >= CONTROLLER_ARM_COUNT) 
	{
		this->comm->sendError(COMM_ERROR_ARM_COUNT, armPos, CONTROLLER_ARM_COUNT);
		return;
	}

	if (this->arms[armPos].defined != 1)
	{
		this->comm->sendError(COMM_ERROR_ARM_NOTDEFINED, armPos);
		return;	
	}

	if (position == CONTROLLER_ARMX_UP) 
	{
		this->arms[armPos].servoX.writeMicroseconds(this->arms[armPos].limits[0]);
		this->comm->sendCommand("Controller@setXPosition", "%d [%d, %d]", armPos, position, this->arms[armPos].limits[0]);
	}
	else if (position == CONTROLLER_ARMX_DOWN) 
	{
		this->arms[armPos].servoX.writeMicroseconds(this->arms[armPos].limits[1]);
		this->comm->sendCommand("Controller@setXPosition", "%d [%d, %d]", armPos, position, this->arms[armPos].limits[1]);
	}
	else
	{
		this->comm->sendError(COMM_ERROR_ARM_WRONGPOS, armPos, position);
	}
}