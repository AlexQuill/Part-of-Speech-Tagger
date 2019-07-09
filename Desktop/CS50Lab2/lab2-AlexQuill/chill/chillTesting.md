# Chill.c testing

### No Arguments
[aquill@flume ~/cs50/labs/lab2-AlexQuill/chill]$ ./chill  
Temp    Wind    Chill    
-----   ----    -----  

 -10       5    -22.3   
 -10      10    -28.3   
 -10      15    -32.2  
  
   0       5    -10.5  
   0      10    -15.9  
   0      15    -19.4  
  
  10       5      1.2  
  10      10     -3.5  
  10      15     -6.6  

  20       5     13.0  
  20      10      8.9  
  20      15      6.2  
  
  30       5     24.7  
  30      10     21.2  
  30      15     19.0  
  
  40       5     36.5  
  40      10     33.6  
  40      15     31.8  
  
### One Argument    
[aquill@flume ~/cs50/labs/lab2-AlexQuill/chill]$ ./chill 10  
Temp    Wind    Chill      
-----   ----    -----    
10.0     5.0      1.2   
10.0    10.0     -3.5  
10.0    15.0     -6.6  

### Two Arguments   
[aquill@flume ~/cs50/labs/lab2-AlexQuill/chill]$ ./chill -5 12   
Temp    Wind    Chill      
-----   ----    -----  
-5.0    12.0    -23.8   
### Decimal Argument  
[aquill@flume ~/cs50/labs/lab2-AlexQuill/chill]$ ./chill -10.5 4  
Temp    Wind    Chill      
-----   ----    -----   
-10.5     4.0    -21.0    
    
### Temp out of bounds  

[aquill@flume ~/cs50/labs/lab2-AlexQuill/chill]$ ./chill 55  
Your temperature of 55.0 is unacceptable!   
Temperature must be less than or equal to 50 degrees Fahrenheit  
  
### Velocity out of bounds   
[aquill@flume ~/cs50/labs/lab2-AlexQuill/chill]$ ./chill 30 0  
Your wind speed of 0.0 is unacceptable!   
Wind speed must be greater than or equal to 0.5 mph   
   