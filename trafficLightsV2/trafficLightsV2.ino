int trafficLightPins[] = {4, 5, 6, 7, 8}; // Vert, jaune, rouge; bleu, orange
int trafficLightPins2[] = {9, 10, 11, 12, 13}; //vert, jaune, rouge; bleu, orange; Deuxieme set
int one = 0; //si ceci est zero la lumiere va utiliser le premier array, sinon le deuxieme
int sensor1 = 2;                                             // Pin pour les senseurs sur la voie principale
int sensor2 = 3;                                             // Pin pour les senseurs sur la voie transversale
long previousPeds = 0;
long interval = 600;                                         // Durée de l'intervalle de clignotement de la lumière pour piétons
int ledState = 0;                                            // Variable temporaire pour changer l'état des DELs
int i = 0;
int recv = 0;                                                // Octet reçu sur la connexion en série
boolean pedtoggle = false;                                   // Faire clignoter ou non la lumière pour piétons sur la voie principale
boolean ped2toggle = false;                                  // Faire clignoter ou non la lumière pour piétons sur la voie transversale
int pedstate = 0;                                            // État de la lumière pour piétons sur la voie principale
int ped2state = 0;                                           // État de la lumière pour piétons sur la voie transversale
long sensorData1 = 0;                                        // État analogue du senseur piézo sur la voie principale
long sensorData2 = 0;                                        // État analogue du senseur piézo sur la voie transversale
byte mask = 1;
int data = 0;

void setup() {
  if(one == 0){
   for(int i = 0; i < 5; i++) {
    pinMode(trafficLightPins[i], OUTPUT); // Initialiser tous les pins des DELs en tant que sortie
   }
  }else{
   for(int i = 0; i < 5; i++) {
    pinMode(trafficLightPins2[i], OUTPUT); // Initialiser tous les pins des DELs en tant que sortie
   }
  }

  pinMode(sensor1, INPUT); // Initialiser le pin du senseur sur la voie principale en tant qu'entrée
  pinMode(sensor2, INPUT); // Initialiser le pin du senseur sur la voie transversale en tant qu'entrée
  
  Serial.begin(115200); // Commencer la connexion en série à 9600Bd
  delay(300);
  Serial.println(one);
}

void loop() {
  unsigned long sensorCheck = millis(); // Une minuterie pour quand vérifier les données des senseurs
  
  //blinkPed1(trafficLightPins[8]);
  blinkPed2(trafficLightPins[13]);
  
  // if serial port is available, read incoming bytes
  if (Serial.available() > 0) {
    // if 'y' (decimal 121) is received, turn LED/Powertail on
    // anything other than 121 is received, turn LED/Powertail off
         //String test = "";
         //test = Serial.readStringUntil('\n');
         data = Serial.read();
         // say what you got:

         //000 000 00 L'ordre est: blank blank vert; jaune rouge bleu; orange, flash on/off;
         int c = 5;
         for (mask = 00000001; mask>0; mask <<= 1) { //iterate through bit mask
           if(c < 0){
            break;
           }
           if (data & mask){ // if bitwise AND resolves to true
             if(c == 5){
               pedtoggle = true;
               ped2toggle = true;
             }else{
              if(one == 0){
               digitalWrite(trafficLightPins[c],HIGH); // send 1
              }else{
               digitalWrite(trafficLightPins2[c],HIGH); // send 1
              }
             }
           }else{ //if bitwise and resolves to false
             if(c == 5){
               pedtoggle = false;
               ped2toggle = false;
             }else{
              if(one == 0){
               digitalWrite(trafficLightPins[c],LOW); // send 0
              }else{
               digitalWrite(trafficLightPins2[c],LOW); // send 0
              }
             }
           }
           c--;
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
