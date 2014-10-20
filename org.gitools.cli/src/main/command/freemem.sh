#!/bin/bash

###
# #%L
# gitools-cli
# %%
# Copyright (C) 2013 Biomedical Genomics Lab
# %%
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as
# published by the Free Software Foundation, either version 3 of the 
# License, or (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public 
# License along with this program.  If not, see
# <http://www.gnu.org/licenses/gpl-3.0.html>.
# #L%
###
#
# ---------------------------------------------------------------------
# Gitools startup script.
# ---------------------------------------------------------------------
#


MACHINE_MEM=0
if [ "$(uname)" == "Darwin" ]; then
    # Do something under Mac OS X platform
    MACHINE_MEM=$(top -l 1 | head | grep PhysMem | sed -e 's/ /\'$'\n/g' | head -2 | tail -1)
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    # Do something under Linux platform
    MACHINE_MEM=$(free -m | awk '/^Mem:/{print $2}')M
fi

#MACHINE_MEM=${MACHINE_MEM::(-1)}
MACHINE_MEM=$(echo ${MACHINE_MEM%?})

echo "MACHINE MEM: "$MACHINE_MEM"M"

let "TWENTY_PER= $MACHINE_MEM / 100 * 20"

SPARE_MEM=$(($TWENTY_PER<1024?$TWENTY_PER:1024))

if [ $BITS -eq 32 ]; then
  OCCUPY_MEM=1500
  echo "32 bit Java VM"
else
  let "OCCUPY_MEM= $MACHINE_MEM - $SPARE_MEM"
  echo "64 bit Java VM"
fi

VMOPTIONS=$(echo "-Xmx${OCCUPY_MEM}m -Xms256m -XX:MaxPermSize=250m -XX:+UseSerialGC -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20")