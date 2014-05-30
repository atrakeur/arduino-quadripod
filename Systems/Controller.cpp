#include "Arduino.h"
#include <Servo.h>

#include "Controller.h"

extern const char COMM_ERROR_ARM_COUNT[] = "Arm count %d gt max %d";
extern const char COMM_ERROR_ARM_DEFIN[] = "Arm %d allready defined";

void Controller::init(Comm* comm)
{
	this->comm = comm;
}

void Controller::defineArm(unsigned int armId, const int armData[6]) 
{
	if (armId >= CONTROLLER_ARM_COUNT) 
	{
		this->comm->sendError(COMM_ERROR_ARM_COUNT, armId, CONTROLLER_ARM_COUNT);
		return;
	}

	if (this->arms[armId].defined != 0)
	{
		this->comm->sendError(COMM_ERROR_ARM_DEFIN, armId);
		return;	
	}

	this->arms[armId].defined = 1;
	memcpy(this->arms[armId].limits, &armData[2], sizeof(int) * 4);
	this->arms[armId].servoX.attach(armData[0]);
	this->arms[armId].servoY.attach(armData[1]);

	this->comm->sendCommand("Controller@defineArm", "%d [%d, %d]", armData[0], armData[2], armData[3]);
	this->comm->sendCommand("Controller@defineArm", "%d [%d, %d]", armData[1], armData[4], armData[5]);
}