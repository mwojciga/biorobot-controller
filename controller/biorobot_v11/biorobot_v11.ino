// this is where we will put our data
int left = 0;
int right = 0;
byte space = 0;
byte separator = 0;

void setup(){
  // Start up our serial port, we configured our XBEE devices for 38400 bps. 
  Serial.begin(9200);
}

void loop(){
  // handle serial data, if any
  if (Serial.available() >= 4){
    left = Serial.read();
    separator = Serial.read();
    right = Serial.read();
    space = Serial.read();
    Serial.flush();

    Serial.print(left);
    Serial.print(byte(separator));
    Serial.print(right);
    Serial.print(byte(space));
    Serial.print("\n");
  }
}
