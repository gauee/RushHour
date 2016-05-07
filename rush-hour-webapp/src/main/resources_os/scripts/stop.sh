#!/bin/bash

FILE_WITH_PID=./scripts/app.pid

kill $(<"$FILE_WITH_PID")
