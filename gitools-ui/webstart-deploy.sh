#!/bin/bash

mvn webstart:jnlp && rsync -av --exclude="gitools.jks" target/jnlp/ bgadmin@ankara:/usr/local/gitools/webstart/
