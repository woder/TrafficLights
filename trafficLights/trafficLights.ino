int trafficLights1[] = {4, 5, 6, 7, 8};	// red, yellow, green, pedestrians led pins
int trafficLights2[] = {9, 10, 11, 12, 13};	// red, yellow, green, pedestrians led pins
int sensors1 = 2; // pin for the main way sensors
int sensors2 = 3; // pin for the cross way sensors
int situations = 6; //these are scenes, each scene has a setting, so like red for road 1 but green for road 2 with pedestrian light on... and so on
int duration[] = {4000, 4000, 3000, 9000, 4000, 3000}; // duration of each situation note that the format is currently as follows: Traffic green for one road; start flashing pedestrian sign; yellow, red
long previousCars = 0;
long previousPeds = 0;
long interval = 300;	//blink interval for pedestrians
int ledState = LOW;
int state;
int i = 0;
int cars1 = 0;
int cars2 = 0;

void setup() {
  for(int i = 0; i < 5; i++) {
	  pinMode(trafficLights1[i], OUTPUT);
	  pinMode(trafficLights2[i], OUTPUT);
  }

  attachInterrupt(digitalPinToInterrupt(2), addCars1, RISING); // activate an external interrupt on the rising edge of pin 2 MIGHT HAVE TO CHANGE TO FALLING
  attachInterrupt(digitalPinToInterrupt(3), addCars2, RISING); // activate an external interrupt on the rising edge of pin 3 MIGHT HAVE TO CHANGE TO FALLING

  pinMode(sensors1, INPUT);
  pinMode(sensors2, INPUT);
  
	Serial.begin(9600);
}

// to be activated asynchronously if pin 2 passes from low to high
void addCars1() {
  cars1++; // increase waiting car count for main road
}

// to be activated asynchronously if pin 3 passes from low to high
void addCars2() {
  cars2++; // increase waiting car count for through road
}

void loop() {
  unsigned long currentMillis = millis();

  // if there are more than 5 cars waiting on the main road, change scenes
  if(cars1 > 5) {
    i++;
    delay(5000);
    situation(i);
    previousCars = currentMillis; //reset the time to the current time, since we have just changed the scene
    cars1 = 0; // reset number of waiting cars for main road
  }

  // if there are more than 5 cars waiting on the through road, change scenes
  if(cars2 > 5) {
    i++;
    delay(5000);
    situation(i);
    previousCars = currentMillis; //reset the time to the current time, since we have just changed the scene
    cars2 = 0; // reset number of waiting cars for through road
  }
  
  // main loop, every tick we increase our counter and test to see if we should enter the next phase
	if(currentMillis - previousCars < duration[i]) { //if the current time minus the last time we changed scene is less than the required duration to change scene, then just tick the scene in question
		situation(i); //tick the scene
	} else {
		previousCars = currentMillis; //reset the time to the current time, since we have just changed the scene
		if(i >= situations) { //if we passed the maximum amount of scenes, then reset to 0
			i = 0;
		} else {
			i++; //increase the scene count
		}
	}
}

void activateTrafficLight1(String lights, int pedestrians) {
	for(int x = 0; x < 3; x++) {
		if(lights[x] == '0') state = LOW;
		if(lights[x] == '1') state = HIGH;
		digitalWrite(trafficLights1[x], state);
	}
	if(pedestrians == 1) {
                digitalWrite(trafficLights1[4], LOW);
		            digitalWrite(trafficLights1[3], HIGH);
  } else if(pedestrians == 2){
                digitalWrite(trafficLights1[3], LOW);
                blinkPed(trafficLights1[4]);
	} else {
		            digitalWrite(trafficLights1[3], LOW);
                digitalWrite(trafficLights1[4], HIGH);
	}
}

void activateTrafficLight2(String lights, int pedestrians) {
	for(int x = 0; x < 3; x++) {
		if(lights[x] == '0') state = LOW;
		if(lights[x] == '1') state = HIGH;
		digitalWrite(trafficLights2[x], state);
	}
	if(pedestrians == 1) {
                digitalWrite(trafficLights2[4], LOW);
		            digitalWrite(trafficLights2[3], HIGH);
  } else if(pedestrians == 2){
                digitalWrite(trafficLights2[3], LOW);
                blinkPed(trafficLights2[4]);
	} else {
                digitalWrite(trafficLights2[4], HIGH);
		            digitalWrite(trafficLights2[3], LOW);
	}
}

void situation(int i) {
	switch(i) {
		case 0:
			activateTrafficLight1("100",1); // 100 means red ON, yellow OFF, green OFF
			activateTrafficLight2("001",0); // the second parameter is for pedestrians
			break;						// 1 is ON and 0 is OFF
    case 1:
      activateTrafficLight1("100",2); // 100 means red ON, yellow OFF, green OFF
			activateTrafficLight2("001",0); // the second parameter is for pedestrians
			break;						// 1 is ON and 0 is OFF
		case 2:
			activateTrafficLight1("100",0); // 110: red ON, yellow ON, green OFF
			activateTrafficLight2("010",0);
			break;
		case 3:
			activateTrafficLight1("001",0);
			activateTrafficLight2("100",1);
			break;
    case 4:
      activateTrafficLight1("001",0); // 100 means red ON, yellow OFF, green OFF
			activateTrafficLight2("100",2); // the second parameter is for pedestrians
			break;						// 1 is ON and 0 is OFF
		case 5:
			activateTrafficLight1("010",0);
			activateTrafficLight2("100",0);
			break;
	}
}

void blinkPed(int ped) {
	unsigned long currentMillise = millis();
	if(currentMillise - previousPeds > interval) {
		previousPeds = currentMillise;
		if (ledState == LOW) {
		  ledState = HIGH;
		} else {
		  ledState = LOW;
	  }
    digitalWrite(ped, ledState);
	}
}
