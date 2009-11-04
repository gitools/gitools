#!/bin/bash

javacmd=$JAVACMD
[ -z "$javacmd" ] && [ -n "$JAVA_HOME" ] && javacmd="$JAVA_HOME/bin/java"
[ -z "$javacmd" ] && javacmd="java"

[ -n "$GITOOLS_JAVA_OPTS" ] && javacmd="$javacmd $GITOOLS_JAVA_OPTS"

APPDIR="$BINDIR/.."
LIBDIR=$APPDIR/lib

