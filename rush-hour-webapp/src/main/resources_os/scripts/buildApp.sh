#!/bin/bash

APP=./rush-hour-webapp/
MVN_CMD=$M2_HOME/bin/mvn

$MVN_CMD clean package

mkdir startApp
rm startApp/*.jar
rm -rf startApp/config
cp $APP/target/rush-hour-webapp-1.0-SNAPSHOT.jar startApp/
cp -r $APP/src/main/resources_os startApp/config
mv startApp/config/scripts startApp/scripts
