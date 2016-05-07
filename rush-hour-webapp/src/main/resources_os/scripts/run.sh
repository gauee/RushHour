#!/bin/bash

JAVA_CMD=$OPENSHIFT_JBOSSEWS_JDK8/bin/java

$JAVA_CMD -cp rush-hour-webapp-1.0-SNAPSHOT.jar:config pl.edu.uj.ii.webapp.StartApp &