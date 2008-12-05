#!/bin/bash

javacmd=$JAVACMD
[ -z "$javacmd" ] && [ -n "$JAVA_HOME" ] && javacmd="$JAVA_HOME/bin/java"
[ -z "$javacmd" ] && javacmd="java"

APPDIR="$BINDIR/.."
LIBDIR=$APPDIR/lib

