#include "Arduino.h"
#include <Servo.h>
#include <stdio.h>
#include <string.h>

#include "Systems/Comm.h"
#include "Systems/Controller.h"

#include "Core.h"

extern const int armConfig[CONTROLLER_ARM_COUNT][6] = {
    { 3,  2, 770 , 2000, 950 , 2400},
    { 4,  5, 800 , 2000, 2000,  850},
    { 7,  6, 1110, 2000, 2000,  850},
    {19, 20, 750 , 2000, 950 , 2400}
};

void Core::init() 
{
	comm.init();
	control.init(&comm);

	for (int i = 0; i < CONTROLLER_ARM_COUNT; i++) 
	{
		control.defineArm(i, armConfig[i]);
		control.setVPosition(i, CONTROLLER_ARMX_DOWN);
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
	if (strcmp(cmd->key, "Controller@setVPosition") == 0)
	{
		unsigned int armdId;
		unsigned int position;
		if (sscanf(cmd->val, "%u %u", &armdId, &position))
		{
			control.setVPosition(armdId, position);
		}
	}
	else if (strcmp(cmd->key, "Controller@setHPosition") == 0)
	{
		unsigned int armdId;
		unsigned int position;
		if (sscanf(cmd->val, "%u %u", &armdId, &position))
		{
			control.setHPosition(armdId, position);
		}
	}
}
