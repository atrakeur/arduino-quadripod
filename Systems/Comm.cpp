#include <stdarg.h>

#include "Arduino.h"

#include "Comm.h"

extern const char COMM_ERROR_STR[] = "ERROR";
extern const char COMM_ERROR_STR_NOERROR[]  = "No error";
extern const char COMM_ERROR_STR_NODELIM[] = "No delimiter";
extern const char COMM_ERROR_STR_BUFFLEN[] = "Buf overflow";

void Comm::init()
{
	inputUsed = 0;

	Serial.begin(9600);
	while (!Serial);	//wait for serial ready
}

Command* Comm::recvCommand() 
{
	while(Serial.available() > 0) 
	{
		int remSpace = COMM_COMMAND_BUFF - inputUsed;
		if (remSpace <= 0)
		{
			//invalidate command
			Comm::sendCommand(COMM_ERROR_STR, COMM_ERROR_STR_BUFFLEN);
			Comm::resetInputCommand();
			//TODO discard overflow data?
			return NULL;
		}

		char data = (char)Serial.read();

		if (data == '\n' || data == '\r') 
		{
			//locate the delimiter
			int delimiter = -1;
			for (int i = 0; i < inputUsed; i++) 
			{
				if (inputBuff[i] == ':') 
				{
					delimiter = i;
					break;
				}
			}

			if (delimiter == -1) 
			{
				//Invalid command
				Comm::sendCommand(COMM_ERROR_STR, COMM_ERROR_STR_NODELIM);
				Comm::resetInputCommand();
				return NULL;
			}

			strncpy(input.key, inputBuff, delimiter);
			input.key[delimiter] = '\0';
			strncpy(input.val, &inputBuff[delimiter + 1], inputUsed - delimiter - 1);
			input.val[inputUsed - delimiter - 1] = '\0';

			Comm::resetInputCommand();
			
			return &input;
		}
		else
		{
			inputBuff[inputUsed++] = data;
		}
	}

	return NULL;
}

void Comm::sendCommand(const char* key, const char* val, ...) 
{
	char buff[COMM_COMMAND_VAL];

	va_list args;
	va_start(args, val);
	vsnprintf(buff, COMM_COMMAND_VAL, val, args);
	va_end(args);


	Serial.print("");
	Serial.print(key);
	Serial.print(":");
	Serial.print(buff);
	Serial.println();
}

void Comm::sendError(const char* value, ...)
{
	va_list args;
	va_start(args, value);
	
	Comm::sendCommand(COMM_ERROR_STR, value, args);

	va_end(args);
}

void Comm::resetInputCommand()
{
	memset(inputBuff, '\0', COMM_COMMAND_KEY + COMM_COMMAND_VAL);
	inputUsed = 0;
}
