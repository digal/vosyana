echo ------- BOT STARTED -------- >> log.txt
echo ------- BOT STARTED -------- >> errs.txt
java -classpath libs -jar out/release/skypebot.jar shaman >> log.txt 2>>errs.txt
