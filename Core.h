#ifndef CORE_H_INCLUDED
#define CORE_H_INCLUDED

#include "Comm.h"
#include "Controller.h"

class Core {
public:

private:
    Comm comm;
    Controller control;
public:
    void init();
    void loop();
};

#endif // CORE_H_INCLUDED
