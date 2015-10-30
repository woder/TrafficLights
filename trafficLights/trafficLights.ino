int trafficLights1[] = {4, 5, 6, 7, 8};  // green, yellow, red, blue + orange pedestrians led pins
int trafficLights2[] = {9, 10, 11, 12, 13}; // green, yellow, red, blue + orange pedestrians led pins
int sensors1 = 2; // pin for the main way sensors
int sensors2 = 3; // pin for the cross way sensors
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
  Serial.write(2); // send signal to server; a car passed on mainway
}

// to be activated asynchronously if pin 3 passes from 0 to 1
void addCars2() {
  Serial.write(3); // send signal to server; a car passed on throughway
}

void loop() {
  // while there is no serial connection, flash red lights to treat as 4-way stop
  while (!Serial) {
    digitalWrite(trafficLights1[2], HIGH);
    digitalWrite(trafficLights2[2], HIGH);
    delay(2500);
    for(int i = 0; i < 5; i++) {
      digitalWrite(trafficLights1[i], LOW);
      digitalWrite(trafficLights2[i], LOW);
    }
    delay(2500);
  }
  
  // if serial port is available, read incoming bytes
  if (Serial.available() > 0) {
    recv = Serial.read();
 
    // if 'y' (decimal 121) is received, turn LED/Powertail on
    // anything other than 121 is received, turn LED/Powertail off
    if (recv == 1){
      String lights = "";
      lights = Serial.readStringUntil('\n');
      Serial.print("Rev: ");
      Serial.print(lights);
      for(int i = 0; i < 5; i++) {
        digitalWrite(trafficLights1[i], LOW);
        digitalWrite(trafficLights2[i], LOW);
      }
      for(int i = 0; i < 8; i++) {
        if(i < 3) {
          digitalWrite(trafficLights1[i], int(lights.charAt(i) - '0'));
          Serial.println("OUAIS 1");
        }
        if(i >= 3 && i < 6) {
          digitalWrite(trafficLights2[i - 3], int(lights.charAt(i) - '0'));
          Serial.println("OUAIS 2");
        }
        if(i == 6) {
          if(lights.charAt(i) == '0') {
            digitalWrite(trafficLights1[3], LOW);
            digitalWrite(trafficLights1[4], HIGH);
            Serial.println("OUAIS 3");
          }
          if(lights.charAt(i) == '1') {
            digitalWrite(trafficLights1[3], HIGH);
            digitalWrite(trafficLights1[4], LOW);
            Serial.println("OUAIS 4");
          } else {
            digitalWrite(trafficLights1[3], LOW);
            for(int j = 0; j < 10; j++) {
              digitalWrite(trafficLights1[4], HIGH);
              delay(1000);
              digitalWrite(trafficLights1[4], LOW);
              delay(1000);
            }
            Serial.println("OUAIS 5");
          }
        }
        if(i == 7) {
          if(lights.charAt(i) == '0') {
            digitalWrite(trafficLights2[3], LOW);
            digitalWrite(trafficLights2[4], HIGH);
            Serial.println("OUAIS 6");
          }
          if(lights.charAt(i) == '1') {
            digitalWrite(trafficLights2[3], HIGH);
            digitalWrite(trafficLights2[4], LOW);
            Serial.println("OUAIS 7");
          } else {
            digitalWrite(trafficLights2[3], LOW);
            for(int j = 0; j < 10; j++) {
              digitalWrite(trafficLights2[4], HIGH);
              delay(1000);
              digitalWrite(trafficLights2[4], LOW);
              delay(1000);
            }
            Serial.println("OUAIS 8");
          }
        }
      }
    } else {
      for(int i = 0; i < 5; i++) {
        digitalWrite(trafficLights1[i], LOW);
        digitalWrite(trafficLights2[i], LOW);
        Serial.println("OUAIS 9");
      }
    }

    // confirm values received in serial monitor window
    Serial.print("--Arduino received: ");
    Serial.println(recv);
  }
}
