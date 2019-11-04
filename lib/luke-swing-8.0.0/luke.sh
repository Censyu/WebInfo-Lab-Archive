#!/bin/bash

JAVA_OPTIONS="${JAVA_OPTIONS} -Xmx1024m -Xms512m -XX:MaxMetaspaceSize=256m"
JAR_FILE="target/luke-swing-with-deps.jar"

LOG_DIR=${HOME}/.luke.d/
if [[ ! -d ${LOG_DIR} ]]; then
  mkdir ${LOG_DIR}
fi

LUKE_PATH=$(cd $(dirname $0) && pwd)
cd ${LUKE_PATH}
nohup java ${JAVA_OPTIONS} -jar ${JAR_FILE} > ${LOG_DIR}/luke_out.log 2>&1 &
