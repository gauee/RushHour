#! /bin/bash
LOG_FILE=~/app-root/logs/cron_minutely.log
function log {
    curDate=$(date +"%x %T")
    echo "$curDate $1" >> $LOG_FILE
}
log "Check app health"
cd ~/app-root/data/RushHour/
log "$(pwd)"
. custom_env
log $JAVA_HOME
cd startApp
log "$(pwd)"
sh ./scripts/restart.sh
log "App checked"
exit 0
