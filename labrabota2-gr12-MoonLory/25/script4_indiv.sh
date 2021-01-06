#! /bin/bash
read name
read mod
touch $name
sudo chmod $mod $name
read x
sudo chmod u+rw $name
