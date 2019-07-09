/*
 * Histo.c - Split user input into 16 equal-size buckets and display a histogram of the data. 
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int numDigits(int toCount){
        int digits = 0;
        while(toCount != 0){
                digits++;
                toCount /= 10;
        }
        return digits;
}

void printSizes(int numBins, int binSize){
    int range = numBins*binSize;
    printf("%d bins of size %d for range [0,%d) \n", numBins, binSize, range);
}
  
int main(const int argc, const char *argv[]) {

    int numInputs = 0;
    int inList[100];
    int numBins = 16;
    int binSize = 1;
    int integer;
    int in;

    printSizes(numBins, binSize);
    // runs until ^D entered. 
    while((in = scanf("%d", &integer)) != EOF){
        if (in == 0){
          printf("Your input was not valid \n");
          break;
        }
        inList[numInputs] = integer;
        numInputs++;
        while(integer >= numBins * binSize){
            binSize *= 2;
            printSizes(numBins, binSize);
        }
    }
    // final list is acquired
    int histoList[numBins];

    for(int i = 0; i < numBins; i++){
        histoList[i] = 0;
    }

    for(int i = 0; i < numInputs; i++) {
        if (inList[i] >= 0){
            histoList[inList[i]/binSize]++;
        }
    }

    int j = 0;
    int upperBound = binSize-1;
    int lowerBound = 0;
    int maxVal = numBins*binSize;
    int minField = numDigits(maxVal);
    
    while (upperBound < binSize*numBins) {
        printf("[%*d:%*d] %d\n", minField, lowerBound, minField, upperBound, histoList[j]);
        lowerBound = (upperBound + 1);
        upperBound += (binSize);
        j++;
    }
    return 0;
}