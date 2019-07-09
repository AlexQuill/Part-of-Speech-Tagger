 # Alex Quill - 07/08/2019
 #
 # Regress.sh - Regression testing. 
 # usage: Input greater than or equal to two parameters. 
 # The first should be the name of a current or non-existing directory in your current directory. 
 # The second -> nth should be files you would like to regression test
 # If the directory already exists, all subsequent test files will be run and compared by output, exit status, and content
 # Differences will be output to the console and comparison files will be stored in a directory labeled as the current date
 # If the directory does not exists, a new directory will be created and the output/exit/content of the files will be stored therein
 
 # Input: Dirname Testfile1 Testfile2 ... TestfileN
 # Output: Confirmation of created directory or explanation of differences between files, as well as error/progress messages
 # CS50, Summer 2019

 # Assumptions

 # Error labels:
 # Exit 1: Incorrect input format. "usage: regress.sh dirname testfilename"
 # Exit 2: Test file not readable. "test case ("$i") is not a file or is not readable"

 #!/bin/bash


if [ $# -eq 0 ]; then
    echo 1>&2 "usage: regress.sh dirname testfilename"
    exit 1
elif [ $# -eq 1 ]; then # exactly one input
    if [ ! -d "$1" ]; then
        echo 1>&2 "first argument ($1) is not a directory"
        echo 1>&2 "usage: regress.sh dirname testfilename"
        exit 1
    else
        echo 1>&2 "usage: regress.sh dirname testfilename"
        exit 1
    fi
fi

# Find directory on machine
direc=$(find $HOME -name "$1")

# Two or more inputs 
if [ -z "$direc" ]; then # if the directory does not exist:
    counter=0
    for i in "$@" # start with second argument, first is directory name 
    do
        if [ $counter -ge 1 ]; then
                if [ ! -f "$i" ] || [ ! -r $i ]; then  # if the test file does not exist or is unreadable
                echo 1>&2 "test case ($i) is not a file or is not readable"
                exit 2
            fi
        fi
        let counter+=1
    done

    #The files do exist and are readable but the directory is not real (yet)
    echo "making your directory: $1"
    mkdir "$1"

    countertwo=0
    for i in "$@"
    do
        if [ $countertwo -ge 1 ]; then
            cat "$i" > "$i".test && mv "$i".test "$1" # "$1" is not correct name for directory. 
            echo "added copy file to directory: $1"

            bash "$i" > "$i".output 2>&1 && mv "$i".output "$1"
            echo "added output file to directory: $1"

            echo "$?" > "$i".status && mv "$i".status "$1"
            echo "added stderr file to directory: $1"
        fi
        let countertwo+=1
    done
    echo "saved results in" "$1"

else # The directory does exist 

    counterthree=0
    for i in "$@"
    do
        if [ $counterthree -ge 1 ]; then
            if [ ! -f "$i" ] || [ ! -r "$i" ]; then
                echo 1>&2 "test case ($i) is not a file or is not readable"
                exit 2

            fi
        fi
        let counterthree+=1
    done

    # directory and testfiles are good to go, create timedir and compare contents to base. 
    time=$(date '+%Y%m%d.%H%M%S')
    mkdir "$time"

    counter=0
    for i in "$@"
    do
        if [ $counter -ge 1 ]; then
            cat "$i" > "$i".test && mv "$i".test "$time"
            bash "$i" > "$i".output 2>&1 && mv "$i".output "$time"
            echo "$?" > "$i".status && mv "$i".status "$time"
        fi
        let counter+=1
    done

        # files created and stored
    echo "saved results in" "$time"
    echo "comparing $time with $1"

        # compare existence of files
    diff "$1" "$time" | grep "Only"

    file1=$(diff "$1" "$time" | grep "diff" | cut -d " " -f 2,3 | sed 's/\ / and /g' | sed 's/^/Files /g' | sed 's/$/ differ/g')

    if [ ! -z "$file1" ]; then
            echo "$file1"
    else
            echo "No differences in mutually held files"
    fi

fi
              
