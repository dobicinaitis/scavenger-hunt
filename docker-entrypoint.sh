#!/bin/sh

# set time zone
export TZ=Europe/Riga

if [ "$1" != "" ]; then
    echo "Executing '$@'"
    exec "$@"
else
    # start application
    java -jar /app/app.jar
fi