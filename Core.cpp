#include "Arduino.h"

#include "Comm.h"

#include "Core.h"

void Core::init() 
{
	comm.init();
}

void Core::loop()
{
	comm.recvCommand();
}