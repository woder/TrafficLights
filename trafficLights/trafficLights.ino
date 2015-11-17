int trafficLightPins[] = {4, 5, 6, 9, 10, 11, 7, 8, 12, 13}; // Vert, jaune, rouge; vert, jaune, rouge; bleu, orange; bleu, orange
int sensor1 = 2;                                             // Pin pour les senseurs sur la voie principale
int sensor2 = 3;                                             // Pin pour les senseurs sur la voie transversale
long previousPeds = 0;
long interval = 300;	                                       // Durée de l'intervalle de clignotement de la lumière pour piétons
int ledState = 0;                                            // Variable temporaire pour changer l'état des DELs
int i = 0;
int recv = 0;                                                // Octet reçu sur la connexion en série
boolean pedtoggle = false;                                   // Faire clignoter ou non la lumière pour piétons sur la voie principale
boolean ped2toggle = false;                                  // Faire clignoter ou non la lumière pour piétons sur la voie transversale
int pedstate = 0;                                            // État de la lumière pour piétons sur la voie principale
int ped2state = 0;                                           // État de la lumière pour piétons sur la voie transversale
long sensorData1 = 0;                                        // État analogue du senseur piézo sur la voie principale
long sensorData2 = 0;                                        // État analogue du senseur piézo sur la voie transversale

void setup() {
  for(int i = 0; i < 10; i++) {
    pinMode(trafficLightPins[i], OUTPUT); // Initialiser tous les pins des DELs en tant que sortie
  }

  pinMode(sensor1, INPUT); // Initialiser le pin du senseur sur la voie principale en tant qu'entrée
  pinMode(sensor2, INPUT); // Initialiser le pin du senseur sur la voie transversale en tant qu'entrée
  
  Serial.begin(9600); // Commencer la connexion en série à 9600Bd
}

void loop() {
  unsigned long sensorCheck = millis();
  
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
    }else{
      unsigned long timeoutTimer = millis();
  
      while(timeoutTimer >= 5000) {
        for(int i = 0; i < 10; i++) {
          digitalWrite(trafficLightPins[i], LOW);
        }
        delay(2500);
        digitalWrite(trafficLightPins[2], HIGH);
        digitalWrite(trafficLightPins[5], HIGH);
        delay(2500);
      }
    }
  }

  if(sensorCheck%1000 == 0) {
    sensorData1 = analogRead(sensor1);
    sensorData1 = map(sensorData1, 0, 1023, 0, 255);
    sensorData1 = constrain(sensorData1, 0, 255);
    sensorData2 = analogRead(sensor2);
    sensorData2 = map(sensorData2, 0, 1023, 0, 255);
    sensorData2 = constrain(sensorData2, 0, 255);
    //Serial.println(sensorData1);
    if(sensorData1 > 127) {
      Serial.write(2);
      Serial.write(sensorData1);
    }
    if(sensorData2 > 127) {
      Serial.write(3);
      Serial.write(sensorData2);
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
