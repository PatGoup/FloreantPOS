cwd=$(pwd)
cd target/floreantpos-bin/floreantpos
java -jar floreantpos.jar  > $cwd/log.txt &2>1
