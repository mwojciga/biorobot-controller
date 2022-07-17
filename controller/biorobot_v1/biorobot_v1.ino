// this is where we will put our data
int robot_speed = 1;
int x = 3;
int y = 5;
int z = 7;
int error = 9;
byte space = 0;
byte separator = 0;

void setup(){
  // Start up our serial port, we configured our XBEE devices for 38400 bps. 
  Serial.begin(9200);
}

void loop(){
//  while(1){
//    Serial.print(robot_speed);
//    Serial.print(x);
//    Serial.print(y);
//    Serial.print(z);
//    Serial.print(error);
//    Serial.print("\n");
//    delay(1000);
//  }
  // handle serial data, if any
  if (Serial.available() >= 4){
    robot_speed = Serial.read();
    separator = Serial.read();
    x = Serial.read();
    separator = Serial.read();
    y = Serial.read();
    separator = Serial.read();
    z = Serial.read();
    separator = Serial.read();
    error = Serial.read();
    space = Serial.read();
    Serial.flush();

    Serial.print(robot_speed);
    Serial.write(byte(separator));
    Serial.print(x);
    Serial.write(byte(separator));
    Serial.print(y);
    Serial.write(byte(separator));
    Serial.print(z);
    Serial.write(byte(separator));
    Serial.print(error);
    Serial.write(byte(space));
    Serial.print("\n");
  }
}
