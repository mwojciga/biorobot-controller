/*
Klaudia Trembecka & Maciej Wojciga
Last update: 20130425
A software for uC, used to aquire commands from USB (from java app),
and use them to move the robot motors (x, y, z).
*/

/* 
CHANGELOG
3.2.6: Fixed a calibration bug; Changed directions; 
3.2.5: Changed the limit switches working states and other minor things.
3.2.4: Changed directions. Since now, when dir = 1, then we're gaining distance from 0,0,0.
3.2.3: Changed limiting features, removed some conditions from the loop;
3.2.2: Added new features, more info about current position;
       Integration with JAVA app when it comes to positioning.
3.2.1: Fixed bugs with y, z axis. Changed PC to PINC.
       Added printing after work done. Added positioning.
3.2:   Changed some read/write functions to bitmath style.
       Also, int to long in x, y, z. Added timing after all motors finish work.
3.1:   Changed millis to micros to fasten up the motors.
*/

int place = 0;
int i = 0;
int j = 0;
int started = 0;
int timeout = 1000; 

char a;

String message = "";

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
long xpos = 0;
byte xstate = 0;
byte xstarted = 0;
long ymicros = 0;
long ymov = 0;
long ypos = 0;
byte ystate = 0;
byte ystarted = 0;
long zmicros = 0;
long zmov = 0;
long zpos = 0;
byte zstate = 0;
byte zstarted = 0;
long tmillis = 0;

/* Limit switches */
byte xlimit = 0;
byte ylimit = 0;
byte zlimit = 0;
byte x2limit = 0;
byte y2limit = 0;
byte z2limit = 0;

/* Limit bool */
byte xlim = 0;
byte ylim = 0;
byte zlim = 0;

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
  xlimit = digitalRead(6);
  x2limit = digitalRead(7);
  ylimit = digitalRead(8);
  y2limit = digitalRead(9);
  zlimit = digitalRead(10);
  z2limit = digitalRead(5);
  
  /* CHECK LIMIT SWITCHES */
  if(xlimit == HIGH && x2limit == HIGH ) {
    xlim = 0;
  }
  if(ylimit == HIGH && y2limit == HIGH ) {
    ylim = 0;
  }
  if(zlimit == HIGH && z2limit == HIGH ) {
    zlim = 0;
  }
  
  /* IF ANY LIMIT IS PRESSED */
  if(xlimit == LOW || x2limit == LOW ) {
    x = 10;
    xmov = 0;
    if (xlim == 0) {
      printCoordinates(xlimit, x2limit, ylimit, y2limit, zlimit, z2limit, xpos, ypos, zpos, waited, e);
    }
    xlim = 1;
    if(xlimit == LOW) {
      xpos = 0;
      xdir = 1;
    }
    if(x2limit == LOW) {
      xpos = 110000;
      xdir = 0;
    }
  }
  if(ylimit == LOW || y2limit == LOW ) {
    y = 10;
    ymov = 0;
    if (ylim == 0) {
      printCoordinates(xlimit, x2limit, ylimit, y2limit, zlimit, z2limit, xpos, ypos, zpos, waited, e);
    }
    ylim = 1;
    if(ylimit == LOW) {
      ypos = 0;
      ydir = 1;
    }
    if(y2limit == LOW) {
      ypos = 80000;
      ydir = 0; 
    }
  }
  if(zlimit == LOW || z2limit == LOW ) {
    z = 10;
    zmov = 0;
    if (zlim == 0) {
      printCoordinates(xlimit, x2limit, ylimit, y2limit, zlimit, z2limit, xpos, ypos, zpos, waited, e);
    }
    zlim = 1;
    if(zlimit == LOW) {
      zpos = 0;
      zdir = 1;
    }
    if(z2limit == LOW) {
      zpos = 25000;
      zdir = 0;
    }
  }
  
  /* DIRECTIONS */
  if(xdir == 0){
    digitalWrite(A4, LOW);
  }
  if(ydir == 0){
    digitalWrite(A2, HIGH);
  }
  if(zdir == 0){
    digitalWrite(13, HIGH);
  }
  if(xdir == 1){
    digitalWrite(A4, HIGH);
  }
  if(ydir == 1){
    digitalWrite(A2, LOW);
  }
  if(zdir == 1){
    digitalWrite(13, LOW);
  }
  
  /* X AXIS */
  if (xmov < x && micros() - xmicros >= timeout && e != 1) {
    xmov++;
    if (xdir == 1) {
      xpos++;
    } else {
      xpos--;
    }
    xmicros = micros();
    xstate = !xstate;
    xstarted = 1;
    if (xstate == 0){
      PORTC &= ~_BV(PINC5);
    } else {
      PORTC |= _BV(PINC5);
    }
  }
  if (xmov == x && xstarted == 1) {
    xmov = 0;
    xstarted = 0;
    x = 0;
    if (xstate != 0){
      PORTC |= _BV(PINC5);
    }
    digitalWrite(A5, LOW);
    printCoordinates(xlimit, x2limit, ylimit, y2limit, zlimit, z2limit, xpos, ypos, zpos, waited, e);
  }
  /* Y AXIS */
  if (ymov < y && micros() - ymicros >= timeout && e != 1) {
    ymov++;
    if (ydir == 1) {
      ypos++;
    } else {
      ypos--;
    }
    ymicros = micros();
    ystate = !ystate;
    ystarted = 1;
    if (ystate == 0){
      PORTC &= ~_BV(PINC3);
    } else {
      PORTC |= _BV(PINC3);
    }
  }
  if (ymov == y && ystarted == 1) {
    ymov = 0;
    ystarted = 0;
    y = 0;
    if (ystate != 0){
      PORTC |= _BV(PINC3);
    }
    digitalWrite(A3, LOW);
    printCoordinates(xlimit, x2limit, ylimit, y2limit, zlimit, z2limit, xpos, ypos, zpos, waited, e);
  }
  /* Z AXIS */
  if (zmov < z && micros() - zmicros >= timeout && e != 1 && xstarted == 0 && ystarted == 0) {
    zmov++;
    if (zdir == 1) {
      zpos++;
    } else {
      zpos--;
    }
    zmicros = micros();
    zstate = !zstate;
    zstarted = 1;
    if (zstate == 0){
      PORTC &= ~_BV(PINC1);
    } else {
      PORTC |= _BV(PINC1);
    }
  }
  if (zmov == z && zstarted == 1) {
    zmov = 0;
    zstarted = 0;
    z = 0;
    if (zstate != 0){
      PORTC |= _BV(PINC1);
    }
    digitalWrite(A1, LOW);
    printCoordinates(xlimit, x2limit, ylimit, y2limit, zlimit, z2limit, xpos, ypos, zpos, waited, e);
  }
  /* WAIT AFTER ALL MOTORS */
  if (x == 0 && y == 0 && z == 0 && millis() - tmillis >= 10 && waited <= t*100 && t != 0) {
    tmillis = millis();
    waiting = 1;
    waited++;
  }
  if (x == 0 && y == 0 && z == 0 && waited > t*100 && waiting == 1) {
    printCoordinates(xlimit, x2limit, ylimit, y2limit, zlimit, z2limit, xpos, ypos, zpos, waited, e);
    waiting = 0;
    waited = 0;
    t = 0;
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
      } 
      /* If e equals 2, then just send back the actual coordinates. */
      if(e == 2) {
        printCoordinates(xlimit, x2limit, ylimit, y2limit, zlimit, z2limit, xpos, ypos, zpos, waited, e);
        e = 0;
      }
      /* If e equals 3, we want to calibrate. */
      if(e == 3) {
        /* Set ENABLE to LOW to release the motors */
        digitalWrite(12, LOW);
        /* Set STOP to HIGH */
        digitalWrite(11, HIGH);
      }
      if(e == 1) {
        printCoordinates(xlimit, x2limit, ylimit, y2limit, zlimit, z2limit, xpos, ypos, zpos, waited, e);
        digitalWrite(12, HIGH);
        digitalWrite(11, LOW);
        x = 0;
        xmov = 0;
        xmicros = 0;
        xstarted = 0;
        y = 0;
        ymov = 0;
        ymicros = 0;
        ystarted = 0;
        z = 0;
        zmov = 0;
        zmicros = 0;
        zstarted = 0;
        waiting = 0;
        waited = 0;
        t = 0;
        digitalWrite(A5, LOW);
        digitalWrite(A3, LOW);
        digitalWrite(A1, LOW);
      }
      if(xdir == 0){
        digitalWrite(A4, LOW);
      }
      if(ydir == 0){
        digitalWrite(A2, HIGH);
      }
      if(zdir == 0){
        digitalWrite(13, HIGH);
      }
      if(xdir == 1){
        digitalWrite(A4, HIGH);
      }
      if(ydir == 1){
        digitalWrite(A2, LOW);
      }
      if(zdir == 1){
        digitalWrite(13, LOW);
      }      
      timeout = 2500-v*200;
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

//Sl000000x00000y00000z00000t000e0E
void printCoordinates(byte xlimit, byte x2limit, byte ylimit, byte y2limit, byte zlimit, byte z2limit, long xpos, long ypos, long zpos, long waited, byte e) {
    message += "Sl";
    message += xlimit;
    message += x2limit;
    message += ylimit;
    message += y2limit;
    message += zlimit;
    message += z2limit;
    message += "x";
    message += xpos;
    message += "y";
    message += ypos;
    message += "z";
    message += zpos;
    message += "t";
    message += waited/100;
    message += "e";
    message += e;
    message += "E";
    Serial.println(message);
    message = "";
}
