/*
 * Alex Quill - 07/08/2019
 * 
 * Words.c - Take user input and textfile as arguments and prints all whole words within. 
 * 
 * Input: Accepts stdin from user as well as text files in command line
 * Output: Each word in stdin and txt files printed on a new line
 * Assumptions: None
 * 
 * CS50, Summer 2019
 */

 /*
  * exit 1 - File opened is null
  * exit 2 - Stdin can't be read
  */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <ctype.h>
#include <string.h>
#include <stdbool.h>

// Method for parsing standard input
int inParse(){
    FILE *fp;
    fp = stdin;

    if (fp == NULL){
        printf("Your input can't be read!");
        return 2;
    }

    char c;
    bool newLine = false; // Boolean switch for controlling newline printing

    while ((c = fgetc(fp)) != EOF){
        if isalpha(c){
            printf("%c",c);
            newLine = true;
        }
        else {
                if (newLine == true){
                        printf("\n");
                        newLine = false;

                }
        }
    }
    return 0;
}

// Method for parsing text file
int fileParse(FILE* file){

    if (file == NULL){
        printf("Your file is null\n");
        return 1;
    }
    bool newLine = false; // Boolean switch for controlling newline printing
    char c;
    while ((c = fgetc(file)) != EOF){

        if isalpha(c){
            printf("%c",c);
            newLine = true;
        }
        else {
            if (newLine == true){
                printf("\n");//no two newLines in a row. 
                newLine = false;
            }
        }
    }
    fclose(file);
    return 0;
}

int main(const int argc, const char *argv[]) {

    // No args, read from stdin
    if (argc == 1) {
        inParse();
    }

    // Files to read. 
    for (int i = 1; i < argc; i++) {
        // Read from stdin
        if ((strcmp(argv[i], "-")) == 0 ){
            inParse();
        }
        // Read file argument
        else{
            fileParse(fopen(argv[i], "r"));
        }
    }
  return 0;
}