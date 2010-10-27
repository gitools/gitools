#!/bin/bash

# run it from the gitools root folder

set +e

echo "Compiling ..."
echo 

mvn clean
mvn package install assembly:assembly -DskipTests=true

echo "Creating zip ..."
cd target
DIR1=$(ls -d gitools-*)
[ -z $DIR1 ] && echo "gitools directory not found !!!" && exit -1

echo "  Entering $DIR1"
cd $DIR1
DIR2=$(ls -d gitools-*)
[ -z $DIR2 ] && echo "gitools directory not found !!!" && exit -1

ZIP_FILE="../${DIR2}-bin.zip"

[ -f $ZIP_FILE ] && rm $ZIP_FILE
echo "  $ZIP_FILE"
zip -r $ZIP_FILE $DIR2

RELEASE_DIR="/shared/gitools/releases/$DIR2"

echo
echo "Synchronizing release to $RELEASE_DIR..."
echo
rsync -nrv --delete ./$DIR2/ $RELEASE_DIR/
echo "Press any key to continue or Ctrl-C to abort..."
read
rsync -rv --delete ./$DIR2/ $RELEASE_DIR/

set -e
