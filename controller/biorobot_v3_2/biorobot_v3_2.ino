/*
Klaudia Trembecka & Maciej Wojciga
Last update: 20130310
A software for uC, used to aquire commands from USB (from java app),
and use them to move the robot motors (x, y, z).
*/

/* 
CHANGELOG
3.2: Changed some read/write functions to bitmath style.
     Also, int to long in x, y, z. Added timing after all motors finish work.
3.1: Changed millis to micros to fasten up the motors.
*/

/*
TODO
1. Put the motors into LOW after work;
2. Check if it accepts smal numbers.
3. X and Y in one loop! The frequency between X, Y and Z are different! Why?
*/

int place = 0;
int i = 0;
int j = 0;
int started = 0;
int timeout = 1000; 

char a;

/*
This arrays are 1 char larger, because they should
have also place for the terminating \0 for atoi method.
*/
char serialReadString[28]; //20
char xarray[4]; 
char yarray[4];
char zarray[4];
char tarray[4];

/* Used only for creating x, y, z arrays */
byte b = 0;
byte c = 0;
byte d = 0;
byte f = 0;

/* Initializing variables */
byte v = 0;
long x = 0;
long y = 0;
long z = 0;
int t = 0;
byte e = 0;
byte xdir = 0;
byte ydir = 0;
byte zdir = 0;
long waited = 0;
byte waiting = 0;

/* Variables connected with motors */
long xmicros = 0;
long xmov = 0;
byte xstate = 0;
byte xstarted = 0;
long ymicros = 0;
long ymov = 0;
byte ystate = 0;
byte ystarted = 0;
long zmicros = 0;
long zmov = 0;
byte zstate = 0;
byte zstarted = 0;
byte allfinished = 0;
long tmillis = 0;

/* Limit switches */
byte xlimit = 0;
byte ylimit = 0;
byte zlimit = 0;
byte x2limit = 0;
byte y2limit = 0;
byte z2limit = 0;

void setup() {
  Serial.begin(9600); // it must be the same as in our java app.
  /* Specifing pins connected with our motor controller.*/
  pinMode(12, OUTPUT); // enable.
  pinMode(11, OUTPUT); // stop.
  pinMode(A5, OUTPUT); // x clock.
  pinMode(A3, OUTPUT); // y clock.
  pinMode(A1, OUTPUT); // z clock.
  pinMode(A4, OUTPUT); // x direction.
  pinMode(A2, OUTPUT); // y direction.
  pinMode(13, OUTPUT); // z direction.
  pinMode(6, INPUT); // x limit.
  pinMode(7, INPUT); // x limit.
  pinMode(8, INPUT); // y limit.
  pinMode(9, INPUT); // y limit.
  pinMode(10, INPUT); // z limit.
  pinMode(5, INPUT); // z limit.
  /* By default all pins should be LOW */
  digitalWrite(12, HIGH); // block motors by default
  digitalWrite(11, LOW); // stop.
  digitalWrite(A5, LOW);
  digitalWrite(A3, LOW);
  digitalWrite(A1, LOW);
  digitalWrite(A4, LOW);
  digitalWrite(A2, LOW);
  digitalWrite(13, LOW);
}

void loop() {
  /* LIMIT SWITCHES */
  xlimit = PIND & ( 1 << 6 ); //digitalRead(6);
  x2limit = PIND & ( 1 << 7 ); //digitalRead(7);
  ylimit = PIND & ( 1 << 8 ); //digitalRead(8);
  y2limit = PIND & ( 1 << 9 ); //digitalRead(9);
  zlimit = PIND & ( 1 << 10 ); //digitalRead(10);
  z2limit = PIND & ( 1 << 5 ); //digitalRead(5);
  
  if(xlimit == LOW || x2limit == LOW ) {
    x = 0;
    xmov = 0;
    xmicros = 0;
    xstarted = 0;
  }
  if(ylimit == LOW || y2limit == LOW ) {
    y =0;
    ymov = 0;
    ymicros = 0;
    ystarted = 0;
  }
  if(zlimit == LOW || z2limit == LOW ) {
    z =0;
    zmov = 0;
    zmicros = 0;
    zstarted = 0;
  }
  
  /* X AXIS */
  if (xmov < x && micros() - xmicros >= timeout && e != 1 && xlimit != LOW && x2limit != LOW) {
    xmov++;
    xmicros = micros();
    xstate = !xstate;
    xstarted = 1;
    if (xstate == 0){
      PORTC &= ~_BV(PC5);
    } else {
      PORTC |= _BV(PC5);
    }
  }
  if (xmov == x && xstarted == 1) {
    xmov = 0;
    xstarted = 0;
    x = 0;
    Serial.write("XFin;");
  }
  /* Y AXIS */
  if (ymov < y && micros() - ymicros >= timeout && e != 1 && ylimit != LOW && y2limit != LOW) {
    ymov++;
    ymicros = micros();
    ystate = !ystate;
    ystarted = 1;
    if (ystate == 0){
      PORTC &= ~_BV(PC3);
    } else {
      PORTC |= _BV(PC3);
    }
  }
  if (ymov == y && ystarted == 1) {
    ymov = 0;
    ystarted = 0;
    y = 0;
    Serial.write("YFin;");
  }
  /* Z AXIS */
  if (zmov < z && micros() - zmicros >= timeout && e != 1 && xstarted == 0 && ystarted == 0 && zlimit != LOW && z2limit != LOW) {
    zmov++;
    zmicros = micros();
    zstate = !zstate;
    zstarted = 1;
    if (zstate == 0){
      PORTC &= ~_BV(PC1);
    } else {
      PORTC |= _BV(PC1);
    }
  }
  if (zmov == z && zstarted == 1) {
    zmov = 0;
    zstarted = 0;
    z = 0;
    allfinished = 1;
    Serial.write("ZFin;");
  }
  /* WAIT AFTER ALL MOTORS */
  if (allfinished == 1 && millis() - tmillis >= 10 && waited <= t*100 && t != 0) {
    tmillis = millis();
    waiting = 1;
    waited++;
  }
  if (allfinished == 1 && waited == t*100 && waiting == 1) {
    waiting = 0;
    waited = 0;
    t =0;
    allfinished = 0;
    Serial.write("WFin;");
  }
  
  /*
  The message looks like this:
  Sv9d000x100y200z200t602e0E
  */
  /* Check the incomming message */
  while(Serial.available()) {
    a = Serial.read();
    if(a == 'S') { // beginning of the command.
      started = 1;
    }
    if(a == 'E' && started == 1){ // end of the command.
      place = 0; // reset the place in the array.
      for(i=0; i<=2; i++){
        b = i+8;
        c = i+12;
        d = i+16;
        f = i+20;
        xarray[i]=serialReadString[b];
        yarray[i]=serialReadString[c];
        zarray[i]=serialReadString[d];
        tarray[i]=serialReadString[f];
      }
      v = serialReadString[2]-48; // int = char-48;
      e = serialReadString[24]-48;
      x = atol(xarray)*100;
      y = atol(yarray)*100;
      z = atol(zarray)*100;
      t = atoi(tarray);
      xdir = serialReadString[4]-48;
      ydir = serialReadString[5]-48;
      zdir = serialReadString[6]-48;
      /* Now we have x, y, z, v, e, xdir, ydir, zdir 
      We can end the loop */
      if(e == 0) {
        /* Set ENABLE to LOW to release the motors */
        digitalWrite(12, LOW);
        /* Set STOP to HIGH */
        digitalWrite(11, HIGH);
      } else {
        Serial.print("ERROR!");
        digitalWrite(12, HIGH);
        digitalWrite(11, LOW);
        x = 0;
        xmov = 0;
        xmicros = 0;
        xstarted = 0;
        y =0;
        ymov = 0;
        ymicros = 0;
        ystarted = 0;
        z =0;
        zmov = 0;
        zmicros = 0;
        zstarted = 0;
        digitalWrite(A5, LOW);
        digitalWrite(A3, LOW);
        digitalWrite(A1, LOW);
      }
      if(xdir == 0){
        digitalWrite(A4, LOW);
      }
      if(ydir == 0){
        digitalWrite(A2, LOW);
      }
      if(zdir == 0){
        digitalWrite(13, LOW);
      }
      if(xdir == 1){
        digitalWrite(A4, HIGH);
      }
      if(ydir == 1){
        digitalWrite(A2, HIGH);
      }
      if(zdir == 1){
        digitalWrite(13, HIGH);
      }      
      timeout = 910-v*100;
      started = 0;
    } else {
      if (started == 1){ // the middle of the command.
        serialReadString[place] = a;
        place++; 
      }
    }
  }
 delayMicroseconds(3);
}
