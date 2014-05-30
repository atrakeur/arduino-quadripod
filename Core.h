#ifndef CORE_H_INCLUDED
#define CORE_H_INCLUDED

#include "Comm.h"

class Core {
public:

private:
    Comm comm;
public:
    void init();
    void loop();
};

#endif // CORE_H_INCLUDED
