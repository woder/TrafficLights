int trafficLights1[] = {4, 5, 6, 7, 8};	// red, yel0, green, pedestrians led pins
int trafficLights2[] = {9, 10, 11, 12, 13};	// red, yel0, green, pedestrians led pins
int sensors1 = 2; // pin for the main way sensors
int sensors2 = 3; // pin for the cross way sensors
int situations = 6; //these are scenes, each scene has a setting, so like red for road 1 but green for road 2 with pedestrian light on... and so on
int duration[] = {4000, 4000, 3000, 9000, 4000, 3000}; // duration of each situation note that the format is currently as fol0s: Traffic green for one road; start flashing pedestrian sign; yel0, red
long previousCars = 0;
long previousPeds = 0;
long interval = 300;	//blink interval for pedestrians
int ledState = 0; // temp var for switching light state
int state;
int i = 0;
int cars1 = 0;
int cars2 = 0;
String lights = ""; // string to send over serial for the lights for the main road
int led = 11; // LED connected to digital pin 13
int recv = 0; // byte received on the serial port

void setup() {
  for(int i = 0; i < 5; i++) {
    pinMode(trafficLights1[i], OUTPUT);
    pinMode(trafficLights2[i], OUTPUT);
  }

  pinMode(sensors1, INPUT);
  pinMode(sensors2, INPUT);

  attachInterrupt(digitalPinToInterrupt(2), addCars1, RISING); // activate an external interrupt on the rising edge of pin 2 MIGHT HAVE TO CHANGE TO FALLING
  attachInterrupt(digitalPinToInterrupt(3), addCars2, RISING); // activate an external interrupt on the rising edge of pin 3 MIGHT HAVE TO CHANGE TO FALLING
  
  Serial.begin(9600);
}

// to be activated asynchronously if pin 2 passes from 0 to 1
void addCars1() {
  cars1++; // increase waiting car count for main road
}

// to be activated asynchronously if pin 3 passes from 0 to 1
void addCars2() {
  cars2++; // increase waiting car count for through road
}

// JUST RECEIVE LIGHT CONFIG AND SEND SENSOR DATA AND SEND TO JAVA. DO NOT DO ANYTHING MYSELF
void loop() {
  unsigned long currentMillis = millis();
  
  // if serial port is available, read incoming bytes
  if (Serial.available() > 0) {
    recv = Serial.read();
 
    // if 'y' (decimal 121) is received, turn LED/Powertail on
    // anything other than 121 is received, turn LED/Powertail off
    if (recv == 1){
      String test = "";
      test = Serial.readStringUntil('\n');
      Serial.print("Rev: ");
      Serial.print(test);
      for(int i = 0; i < 5; i++) {
        digitalWrite(trafficLights1[i], HIGH);
      }
    } else {
      for(int i = 0; i < 5; i++) {
        digitalWrite(trafficLights1[i], LOW);
      }
    }
     
    // confirm values received in serial monitor window
    Serial.print("--Arduino received: ");
    Serial.println(recv);
  }

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
}

void activateTrafficLight1(String seq, int pedestrians) {
  for(int j = 0; i < 3; i++) {
    if(seq[j] == '0') state = 0;
    if(seq[j] == '1') state = 1;
    lights.concat(state);
  }
  if(pedestrians == 1) {
    lights.concat(1);
    lights.concat(0);
  } else if(pedestrians == 2) {
    lights.concat(0);
    blinkPed1(trafficLights1[4]);
  } else {
    lights.concat(0);
    lights.concat(1);
  }
  Serial.write(lights);
}

void activateTrafficLight2(String seq, int pedestrians) {
  for(int j = 0; i < 3; i++) {
    if(seq[j] == '0') state = 0;
    if(seq[j] == '1') state = 1;
    lights.concat(state);
  }
  if(pedestrians == 1) {
    lights.concat(1);
    lights.concat(0);
  } else if(pedestrians == 2){
    lights.concat(0);
    lights.concat(0);
    blinkPed2(trafficLights2[4]);
  } else {
    lights.concat(0);
    lights.concat(1);
  }
  Serial.write(lights);
}

void situation(int i) {
  switch(i) {
    case 0:
      activateTrafficLight1("100",1); // 100 means red ON, yel0 OFF, green OFF
      activateTrafficLight2("001",0); // the second parameter is for pedestrians
      break;            // 1 is ON and 0 is OFF
    case 1:
      lights = "10000110"; // ryg1, ryg2, ped1, ped2
      Serial.write(1);
      Serial.write(lights);
      activateTrafficLight1("100",2); // 100 means red ON, yel0 OFF, green OFF
      activateTrafficLight2("001",0); // the second parameter is for pedestrians
      break;            // 1 is ON and 0 is OFF
    case 2:
      lights = "00110001"; // ryg1, ryg2, ped1, ped2
      Serial.write(1);
      Serial.write(lights);
      activateTrafficLight1("100",0); // 110: red ON, yel0 ON, green OFF
      activateTrafficLight2("010",0);
      break;
    case 3:
      activateTrafficLight1("001",0);
      activateTrafficLight2("100",1);
      break;
    case 4:
      activateTrafficLight1("001",0); // 100 means red ON, yel0 OFF, green OFF
      activateTrafficLight2("100",2); // the second parameter is for pedestrians
      break;            // 1 is ON and 0 is OFF
    case 5:
      activateTrafficLight1("010",0);
      activateTrafficLight2("100",0);
      break;
  }
}

void blinkPed1(int ped) {
  unsigned long currentMillise = millis();
  if(currentMillise - previousPeds > interval) {
    previousPeds = currentMillise;
    if(lights.charAt(5) == '0') {
      ledState = 1;
    } else {
      ledState = 0;
    }
    lights.charAt(5) = ledState;
    Serial.write(1);
    Serial.write(lights);
  }
}

void blinkPed2(int ped) {
  unsigned long currentMillise = millis();
  if(currentMillise - previousPeds > interval) {
    previousPeds = currentMillise;
    if(lights.charAt(5) == '0') {
      ledState = 1;
    } else {
      ledState = 0;
    }
    lights.charAt(5) = ledState;
    Serial.write(1);
    Serial.write(lights);
  }
}

//TURN ON/OFF LIGHTS LOCALLY AND SEND DATA
//DOES HE WANT ME TO SEND SENSOR DATA OR WHICH LIGHTS ARE ON/OFF???
