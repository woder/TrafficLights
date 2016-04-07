int trafficLightPins[] = {4, 5, 6, 9, 10, 11, 7, 8, 12, 13}; //green, yellow, red; green yellow red; ped blue ped orange; ped blue ped orange
int sensor1 = 0;
int sensor2 = 1;
int sensor3 = 2; // pin for the main way sensors
int sensor4 = 3; // pin for the cross way sensors
long previousPeds = 0;
long interval = 300;	//blink interval for pedestrians
int ledState = 0; // temp var for switching light state
int i = 0;
int recv = 0; // byte received on the serial port
boolean pedtoggle = false; //are we toggling the ped1
boolean ped2toggle = false; //are we toggling the ped2
int pedstate = 0;
int ped2state = 0;
int sensorData1 = 0;
int sensorData2 = 0;
int sensorData3 = 0;
int sensorData4 = 0;
long lastSensor1 = 0;
long lastSensor2 = 0;
long lastSensor3 = 0;
long lastSensor4 = 0;

long sensorInterval = 1000;

void setup() {
  for(int i = 0; i < 10; i++) {
    pinMode(trafficLightPins[i], OUTPUT);
  }

  pinMode(sensor1, INPUT);
  pinMode(sensor2, INPUT);
  
  Serial.begin(9600);
}

void loop() {
  /*
  // while there is no serial connection, flash red lights to treat as 4-way stop
  while (!Serial) {
    unsigned long timeoutTimer = millis();

    if(timeoutTimer >= 5000) {
      for(int i = 0; i < 10; i++) {
        digitalWrite(trafficLightPins[i], LOW);
      }
      delay(2500);
      digitalWrite(trafficLightPins[2], HIGH);
      digitalWrite(trafficLightPins[5], HIGH);
      delay(2500);
    }
  }
  */
  
  blinkPed1(trafficLightPins[7]);
  blinkPed2(trafficLightPins[9]);
  
  // if serial port is available, read incoming bytes
  if (Serial.available() > 0) {
    recv = Serial.read() - 0;
    Serial.print("Recv: ");
    Serial.println(recv);
    // if 'y' (decimal 121) is received, turn LED/Powertail on
    // anything other than 121 is received, turn LED/Powertail off
    if (recv == 1){
      String test = "";
      test = Serial.readStringUntil('\n');
      Serial.print("Rev: ");
      Serial.println(test);
      
      for(int i = 0; i < 8; i++){
         char number = test[i];
         
         Serial.print("I: ");
         Serial.println(number);
         if(i == 6){
           if(number == '0'){
              pedtoggle = false;
              digitalWrite(trafficLightPins[6], LOW);
              digitalWrite(trafficLightPins[7], HIGH);
           }else if(number == '1'){
              digitalWrite(trafficLightPins[6], HIGH);
              digitalWrite(trafficLightPins[7], LOW);
           }else if(number == '2'){
              digitalWrite(trafficLightPins[6], LOW);
              pedtoggle = true;
           }
         }else if(i == 7){
           if(number == '0'){
              ped2toggle = false;
              digitalWrite(trafficLightPins[8], LOW);
              digitalWrite(trafficLightPins[9], HIGH);
           }else if(number == '1'){
              digitalWrite(trafficLightPins[8], HIGH);
              digitalWrite(trafficLightPins[9], LOW);
           }else if(number == '2'){
              digitalWrite(trafficLightPins[8], LOW);
              ped2toggle = true;
           }
         }else{
           if(test[i] == '1'){
              digitalWrite(trafficLightPins[i], HIGH);
           }else{
              digitalWrite(trafficLightPins[i], LOW);
           }
         }
      }
    }
    /*  unsigned long timeoutTimer = millis();
  
      while(timeoutTimer >= 5000) {
        for(int i = 0; i < 10; i++) {
          digitalWrite(trafficLightPins[i], LOW);
        }
        delay(2500);
        digitalWrite(trafficLightPins[2], HIGH);
        digitalWrite(trafficLightPins[5], HIGH);
        delay(2500);
      }*/
  }

  sensorData1 = analogRead(sensor1);
  sensorData1 = map(sensorData1, 0, 1023, 0, 255);
  sensorData1 = constrain(sensorData1, 0, 255);
  sensorData2 = analogRead(sensor2);
  sensorData2 = map(sensorData2, 0, 1023, 0, 255);
  sensorData2 = constrain(sensorData2, 0, 255);
  sensorData3 = analogRead(sensor3);
  sensorData3 = map(sensorData3, 0, 1023, 0, 255);
  sensorData3 = constrain(sensorData3, 0, 255);
  sensorData4 = analogRead(sensor4);
  sensorData4 = map(sensorData4, 0, 1023, 0, 255);
  sensorData4 = constrain(sensorData4, 0, 255);
  unsigned long sensorTime = millis();
  if(sensorData1 > 110 && sensorTime - lastSensor1 > sensorInterval) {
    Serial.write(2);
    Serial.write(sensorData1);
    lastSensor1 = sensorTime;
  }
  if(sensorData2 > 110 && sensorTime - lastSensor2 > sensorInterval) {
    Serial.write(3);
    Serial.write(sensorData2);
    lastSensor2 = sensorTime;
  }
  if(sensorData3 > 110 && sensorTime - lastSensor3 > sensorInterval) {
    Serial.write(4);
    Serial.write(sensorData3);
    lastSensor3 = sensorTime;
  }
  if(sensorData4 > 110 && sensorTime - lastSensor4 > sensorInterval) {
    Serial.write(5);
    Serial.write(sensorData4);
    lastSensor4 = sensorTime;
  }
}

void blinkPed1(int ped) {
 if(pedtoggle == true){
  unsigned long currentMillise = millis();
  if(currentMillise - previousPeds > interval) {
    previousPeds = currentMillise;
    if(pedstate == 0) {
      ledState = 1;
    } else {
      ledState = 0;
    }
    pedstate = ledState;
    digitalWrite(ped, ledState);
  }
 }
}

void blinkPed2(int ped) {
 if(ped2toggle == true){
  unsigned long currentMillise = millis();
  if(currentMillise - previousPeds > interval) {
    previousPeds = currentMillise;
    if(ped2state == 0) {
      ledState = 1;
    } else {
      ledState = 0;
    }
    ped2state = ledState;
    digitalWrite(ped, ledState);
  }
 }
}
