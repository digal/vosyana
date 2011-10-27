@echo off
echo ------- BOT STARTED -------- >> log.txt
echo ------- BOT STARTED -------- >> errs.txt
javaw -classpath libs -jar skypebot.jar >> log.txt 2>>errs.txt