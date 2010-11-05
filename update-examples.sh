#!/bin/bash

echo "These are the changes:"
echo

rsync -nav --delete --exclude ".svn" /shared/gitools/examples/ examples/

echo "Press any key to continue of Ctrl-C to abort..."

read

rsync -av --delete --exclude ".svn" /shared/gitools/examples/ examples/
