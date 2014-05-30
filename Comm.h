#ifndef COMM_H_INCLUDED
#define COMM_H_INCLUDED

#define COMM_COMMAND_KEY 32
#define COMM_COMMAND_VAL 32

extern const char COMM_ERROR_STR[];
extern const char COMM_ERROR_STR_NOERROR[];
extern const char COMM_ERROR_STR_NODELIM[];
extern const char COMM_ERROR_STR_BUFFLEN[];

typedef struct Command {
	char key[COMM_COMMAND_KEY];
	char val[COMM_COMMAND_VAL];
} Command;

class Comm {
public:

private:
	Command input;
	int inputUsed;
	char inputBuff[COMM_COMMAND_KEY + COMM_COMMAND_VAL];
public:
	void init();

	Command* recvCommand();
	void sendCommand(const char* key, const char* value);
	void resetInputCommand();
};

#endif // COMM_H_INCLUDED
