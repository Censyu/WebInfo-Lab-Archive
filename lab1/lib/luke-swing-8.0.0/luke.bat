@echo off
set JAVA_OPTIONS=%JAVA_OPTIONS% -Xmx1024m -Xms512m -XX:MaxMetaspaceSize=256m
start javaw %JAVA_OPTIONS% -Dswing.systemlaf=com.jgoodies.looks.plastic.PlasticXPLookAndFeel -jar .\target\luke-swing-with-deps.jar
