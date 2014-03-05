#!/bin/bash
#
# Script to install an external library into the local /lib maven repository
#
# Usage: ./install-library.sh [file-path] [groupId] [artifactId] [version]
#

mvn install:install-file -Dfile=$1 -DgroupId=$2 -DartifactId=$3 -Dversion=$4 -Dpackaging=jar -DlocalRepositoryPath="./lib" -DcreateChecksum=true -DupdateReleaseInfo=true
