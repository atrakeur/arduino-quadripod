#ifndef CONTROLLER_H_INCLUDED
#define CONTROLLER_H_INCLUDED

#include "Comm.h"

#include <Servo.h>

#define CONTROLLER_ARM_COUNT 4
//defined in Core.cpp
extern const int armConfig[CONTROLLER_ARM_COUNT][6];

#define CONTROLLER_ARM_FL 0
#define CONTROLLER_ARM_FR 1
#define CONTROLLER_ARM_BL 2
#define CONTROLLER_ARM_BR 3

typedef struct Arm {
	int defined;

	Servo servoX;  //Le bras (haut/bas)
	Servo servoY;  //La position (droite gauche)
	
	int limits[4];
} Arm;

class Controller {
public:

private:
	Comm* comm;
	Arm arms[CONTROLLER_ARM_COUNT];
public:
	void init(Comm* comm);
    void defineArm(unsigned int armPos, const int armData[6]);
};

#endif // CONTROLLER_H_INCLUDED
