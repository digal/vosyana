echo ------- DATABASE CREATION -------- >> log.txt
java -cp libs/derby.jar:libs/derbytools.jar org.apache.derby.tools.ij sql/create_db.sql > log.txt
java -cp libs/derby.jar:libs/derbytools.jar org.apache.derby.tools.ij sql/update_1.sql > log.txt
