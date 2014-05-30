#ifndef CORE_H_INCLUDED
#define CORE_H_INCLUDED

#include <Servo.h>

#include "Systems/Comm.h"
#include "Systems/Controller.h"

class Core {
public:

private:
    Comm comm;
    Controller control;
public:
    void init();
    void loop();

    void parseControlCommand(Command* cmd);
};

#endif // CORE_H_INCLUDED
