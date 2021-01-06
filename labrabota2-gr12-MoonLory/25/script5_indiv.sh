#! /bin/bash
find $1 -type f -size +$2c -size -$3c -printf "%h. %f. %s\n"
