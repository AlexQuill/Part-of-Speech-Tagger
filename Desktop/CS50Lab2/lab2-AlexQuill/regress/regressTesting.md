# Regress.sh Testing

### Make new directory
[aquill@flume ~/cs50/labs/lab2-AlexQuill/regress]$ ./regress.sh base test2
making your directory: base
added copy file to directory: base
added output file to directory: base
added stderr file to directory: base
saved results in base

### Compare to already existing directory
##### Different files different names  
[aquill@flume ~/cs50/labs/lab2-AlexQuill/regress]$ ./regress.sh base test3
saved results in 20190708.203347
comparing 20190708.203347 with base
Only in base: test2.output
Only in base: test2.status
Only in base: test2.test
Only in 20190708.203347: test3.output
Only in 20190708.203347: test3.status
Only in 20190708.203347: test3.test
No differences

##### Same files different content
[aquill@flume ~/cs50/labs/lab2-AlexQuill/regress]$ echo echo I have changed the file contents! > test2
[aquill@flume ~/cs50/labs/lab2-AlexQuill/regress]$ ./regress.sh base test2 test3
saved results in 20190708.203922
comparing 20190708.203922 with base
Only in 20190708.203922: test3.output
Only in 20190708.203922: test3.status
Only in 20190708.203922: test3.test
Files base/test2.output and 20190708.203922/test2.output differ
Files base/test2.test and 20190708.203922/test2.test differ


### Erroneous directory name
[aquill@flume ~/cs50/labs/lab2-AlexQuill/regress]$ ./regress.sh test1
first argument (test1) is not a directory
usage: regress.sh dirname testfilename

### Erroneous argument number
[aquill@flume ~/cs50/labs/lab2-AlexQuill/regress]$ ./regress.sh  
usage: regress.sh dirname testfilename

### Erroneous file name
[aquill@flume ~/cs50/labs/lab2-AlexQuill/regress]$ ./regress.sh base base
test case (base) is not a file or is not readable