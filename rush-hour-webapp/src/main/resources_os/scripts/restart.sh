#!/bin/bash

FILE_WITH_PID=./scripts/app.pid
LOG_FILE=~/app-root/logs/cron_minutely.log
PID=$(<"$FILE_WITH_PID")

processExist=$(ps aux | awk '{print $2}' | grep $PID | wc -l)

function log {
    curDate=$(date +"%x %T")
    echo "$curDate $1" >> $LOG_FILE
}

if [ "$processExist" -eq "1" ]
then
  log "Application is running"
else
  log "Application is down, need to be run"
  JAVA_CMD=$JAVA_HOME/bin/java
  nohup $JAVA_CMD -cp rush-hour-webapp-1.0-SNAPSHOT.jar:config pl.edu.uj.ii.webapp.StartApp &
  log "jvm started"
  echo $! > scripts/app.pid
  log "pid saved"
  log "Application rescheduled"
fi
sleep 3s
log "Return success"
exit 0
