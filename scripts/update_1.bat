echo ------- DATABASE UPDATE -------- >> log.txt
java -cp libs/derby.jar;libs/derbytools.jar org.apache.derby.tools.ij sql/update_1.sql >> log.txt