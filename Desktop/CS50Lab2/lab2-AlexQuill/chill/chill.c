/*
 * Alex Quill - 07/08/2019
 * 
 * Chill.c - Calculate the wind chill based on input temperature and wind speed. 
 * usage: Input no parameters (results in large variant temp/windchill table), One parameter (), or two parameters ()
 * 
 * Input: None, Temp, or Temp _ Velocity
 * Output: Table with calculated windchill column based on Temp and Velocity inputs
 * 
 * Assumptions: Temp and Velocity considered to be floats
 * CS50, Summer 2019
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h> 

/*
* Exit 1 - Too many arguments
* Exit 2 - Temp out of bounds
* Exit 3 - Wind Velocity out of bounds
 */

// Print out the header for the table
void Printheader(char headerList[][5]){
    for (int i = 0; i < 3; i++){
        printf("%*s    ", 4, headerList[i]);
    }
    printf("\n");

    for (int i = 0; i < 3; i++){
        if (i == 0) {
            printf("-----   ");
        }
        else if (i == 1) {
            printf ("----    ");
        }
        else if (i == 2) {
                printf("-----\n");
        }
    }
}

// Calculate windchill from temp and velocity
float calculate(float t, float v){
    float wc = (35.74 + 0.6215*(t) - 35.75*powf(v,0.16) + 0.4275*(t)*powf(v,0.16));
    return wc;
}

int main(const int argc, const char *argv[]) {

    char header[][5] = {"Temp", "Wind", "Chill"};
    float velocities[] = {5.0, 10.0, 15.0};

    if (argc > 3) {
        printf("You have too many arguments!");
        return 1;
    }

    // Two arguments passes
    else if (argc == 3) {
        float t = atof(argv[1]);
        float v = atof(argv[2]);

        // Check temp inbounds
        if (t <= 50 && t >= -99){
            if (v >= 0.5){
                Printheader(header);
                printf("%*.1f    %*.1f    %*.1f \n", 4, t, 4, v, 5, calculate(t,v));
            }
            // Handle velocity out of bounds
            else{
                printf("Your wind speed of %.1f is unacceptable!\n", v);
                if (v < 0.5){
                    printf("Wind speed must be greater than or equal to 0.5 mph\n");
                }
            }
        }
        // Handle temp out of bounds
        else{
            printf("Your temperature of %.1f is unacceptable!\n", t);
            if (t > 50) {
                printf("Temperatute must be less than or equal to 50 degrees Fahrenheit\n");
            }
            else if (t < -99){
                printf("Temperatute must be greater than or equal to -99 degrees Fahrenheit\n");
            }
            return 2;
        }
    }

    // One argument
    else if (argc == 2) {

        float t;
        t = atof(argv[1]);

        // Check temp in bounds
        if (t <= 50 && t >= -99){
            Printheader(header);

            for (int i = 0; i < 3; i++) {
                float v = velocities[i];
                printf("%*.1f    %*.1f    %*.1f", 4, t, 4, v, 5, calculate(t,v));
                printf("\n");
            }
        }
        // Handle temp out of bounds
        else {
            printf("Your temperature of %.1f is unacceptable!\n", t);
            if (t > 50) {
                printf("Temperatute must be less than or equal to 50 degrees Fahrenheit\n");
            }
            else if (t < -99){
                printf("Temperatute must be greater than or equal to -99 degrees Fahrenheit\n");
            }
           return 2;
        }
    }

    // No arguments
    else {
        float temps[] = {-10.0, 0.0, 10.0, 20.0, 30.0, 40.0};

        Printheader(header);
        printf("\n");

        // Build full table
        for (int i = 0; i < 6; i++){
            float t = temps[i];
            for (int j = 0; j < 3; j++) {
                float v = velocities[j];
                printf("%*.0f    %*.0f    %*.1f", 4, t, 4, v, 5, calculate(t,v));
                printf("\n");
            }
            printf("\n");
        }
    }
    return 0;
}
