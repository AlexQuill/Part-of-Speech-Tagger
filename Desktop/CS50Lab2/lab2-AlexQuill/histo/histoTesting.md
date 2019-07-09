# Histo.c testing
### Normal case
##### Note - Proper spacing for buckets is maintained in program but did not convert to markdown. 
[aquill@flume ~/cs50/labs/lab2-AlexQuill/histo]$ ./histo
16 bins of size 1 for range [0,16)   
-5 3 4 5 6 17 30   
16 bins of size 2 for range [0,32)    
45 100     
16 bins of size 4 for range [0,64)      
16 bins of size 8 for range [0,128)    
128   
16 bins of size 16 for range [0,256)    
[  0: 15] 4   
[ 16: 31] 2   
[ 32: 47] 1   
[ 48: 63]    
[ 64: 79]    
[ 80: 95]    
[ 96:111] 1   
[112:127]    
[128:143] 1   
[144:159]    
[160:175]    
[176:191]    
[192:207]    
[208:223]    
[224:239]    
[240:255]    
$  

### No Input

[aquill@flume ~/cs50/labs/lab2-AlexQuill/histo]$ ./histo
16 bins of size 1 for range [0,16) 
[ 0: 0]   
[ 1: 1]    
[ 2: 2]    
[ 3: 3]    
[ 4: 4]    
[ 5: 5]    
[ 6: 6]    
[ 7: 7]    
[ 8: 8]    
[ 9: 9]    
[10:10]    
[11:11]    
[12:12]    
[13:13]    
[14:14]    
[15:15]   
$  

### Piped input
[aquill@flume ~/cs50/labs/lab2-AlexQuill/histo]$ echo {1..100} 160 | ./histo   
16 bins of size 1 for range [0,16)   
16 bins of size 2 for range [0,32)   
16 bins of size 4 for range [0,64)  
16 bins of size 8 for range [0,128)  
16 bins of size 16 for range [0,256)  
[  0: 15] 15  
[ 16: 31] 16   
[ 32: 47] 16  
[ 48: 63] 16  
[ 64: 79] 16  
[ 80: 95] 16  
[ 96:111] 5
[112:127]   
[128:143]   
[144:159]   
[160:175] 1  
[176:191]   
[192:207]   
[208:223]   
[224:239]   
[240:255]   
$  

### Erroneous Input
[aquill@flume ~/cs50/labs/lab2-AlexQuill/histo]$ ./histo  
16 bins of size 1 for range [0,16)  
1 3 18 a  
16 bins of size 2 for range [0,32)  
Your input was not valid  
[ 0: 1] 1  
[ 2: 3] 1  
[ 4: 5]   
[ 6: 7]  
[ 8: 9]  
[10:11]  
[12:13]  
[14:15]  
[16:17]  
[18:19] 1  
[20:21]  
[22:23]  
[24:25]  
[26:27]  
[28:29]  
[30:31]  
$  