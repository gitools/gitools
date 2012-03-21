#!/bin/bash

mvn -q package install assembly:assembly

# Follow this steps to do a release

# First commit
# svn commit

# To prepare the release
# mvn release:prepare

# To perform the release
# mvn release:perform -DaltDeploymentRepository=none::default::file:target/deploy


