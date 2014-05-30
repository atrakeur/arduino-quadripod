#include <Servo.h>
#include <stdio.h>
#include <string.h>

#include "Systems/Comm.h"
#include "Systems/Controller.h"

#include "Core.h"

extern const int armConfig[CONTROLLER_ARM_COUNT][6] = {
    { 9,  8, 2000,  770,  950, 2400},
    {10, 11, 2000,  800, 2000,  850},
    {13, 12, 2000, 1110, 2000,  850},
    {19, 20, 2000,  750,  950, 2400}
};

void Core::init() 
{
	comm.init();
	control.init(&comm);

	for (int i = 0; i < CONTROLLER_ARM_COUNT; i++) 
	{
		control.defineArm(i, armConfig[i]);
		control.setXPosition(i, CONTROLLER_ARMX_DOWN);
	}
}

void Core::loop()
{
	Command* command = comm.recvCommand();
	if (command != NULL) 
	{
		parseControlCommand(command);	
	}
}

void Core::parseControlCommand(Command* cmd) 
{
	if (strcmp(cmd->key, "Controller@setXPosition") == 0)
	{
		unsigned int armdId;
		unsigned int position;
		if (sscanf(cmd->val, "%u %u", &armdId, &position))
		{
			control.setXPosition(armdId, position);
		}
	}
}
