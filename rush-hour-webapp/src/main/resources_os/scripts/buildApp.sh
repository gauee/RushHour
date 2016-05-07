#!/bin/bash

APP=./rush-hour-webapp/
MVN_CMD=$M2_HOME/bin/mvn

$MVN_CMD clean package

rm -rf startApp
mkdir startApp
cp $APP/target/rush-hour-webapp-1.0-SNAPSHOT.jar startApp/
cp -r $APP/src/main/resources_os startApp/config
mv startApp/config/scripts startApp/scripts
