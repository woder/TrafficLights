int trafficLightPins[] = {4, 5, 6, 9, 10, 11, 7, 8, 12, 13}; //green, yellow, red; green yellow red; ped blue ped orange; ped blue ped orange
int sensors1 = 2; // pin for the main way sensors
int sensors2 = 3; // pin for the cross way sensors
long previousPeds = 0;
long interval = 300;	//blink interval for pedestrians
int ledState = 0; // temp var for switching light state
int i = 0;
int recv = 0; // byte received on the serial port
boolean pedtoggle = false; //are we toggling the ped1
boolean ped2toggle = false; //are we toggling the ped2
int pedstate = 0;
int ped2state = 0;

void setup() {
  for(int i = 0; i < 10; i++) {
    pinMode(trafficLightPins[i], OUTPUT);
  }

  pinMode(sensors1, INPUT);
  pinMode(sensors2, INPUT);
  
  Serial.begin(9600);
}

void loop() {
  // while there is no serial connection, flash red lights to treat as 4-way stop
  while (!Serial) {
    for(int i = 0; i < 10; i++) {
      digitalWrite(trafficLightPins[i], LOW);
    }
    delay(2500);
    digitalWrite(trafficLightPins[2], HIGH);
    digitalWrite(trafficLightPins[5], HIGH);
    delay(2500);
  }
  
  unsigned long currentMillis = millis();
  
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
