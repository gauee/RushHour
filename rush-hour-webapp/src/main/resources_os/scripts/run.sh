#!/bin/bash

JAVA_CMD=$JAVA_HOME/bin/java

$JAVA_CMD -Xmx256m -cp rush-hour-webapp-1.0-SNAPSHOT.jar:config pl.edu.uj.ii.webapp.StartApp &
echo $! > scripts/app.pid
