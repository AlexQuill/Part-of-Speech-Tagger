# Words.c testing

### Stdin

[aquill@flume ~/cs50/labs/lab2-AlexQuill/word]$ ./word  
hello my name is Alex  
hello  
my  
name  
is  
Alex  
CS50 Lab 2  
CS  
Lab  
56789a  
a  

### Text File Argument + "-" Argument
[aquill@flume ~/cs50/labs/lab2-AlexQuill/word]$ cat testFile.txt  
hello my name is 3Alex 44 Quill  
[aquill@flume ~/cs50/labs/lab2-AlexQuill/word]$ ./word testFile.txt -  
hello  
my  
name  
is  
Alex  
Quill  
This is now my own input55  
This  
is  
now  
my  
own  
input  
I am 44 going to }{}Press ^D           
I  
am  
going  
to  
Press   
D    
  