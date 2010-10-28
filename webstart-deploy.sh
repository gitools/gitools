#!/bin/bash

# run it from the gitools root folder

set +e

if [ -z "$1" ]; then
	echo "Usage: $(basename $0) <version>"
	exit -1
fi

VERSION=$1

echo "Compiling ..."
echo

cd gitools-ui-app
mvn clean package
mvn webstart:jnlp
mkdir -p target/jnlp/examples

echo "Creating examples zip's ..."
echo

cd ../examples
EXAMPLES=$(find . -maxdepth 1 -type d -printf "%f\n" | grep -v "\.svn" | grep -v "\.")
for example in $EXAMPLES; do
	dest_prefix="../gitools-ui-app/target/jnlp/examples/$example"
	echo "Example $example into $dest_prefix.zip ..."
	echo $(date +%Y%m%d%H%M%S) > $dest_prefix.timestamp
	zip -r $dest_prefix.zip $example -x "*/*.svn/*"
done

echo "Deploying files ..."
echo

cd ../gitools-ui-app
cat > target/jnlp/index.html <<EOF
<html>
<head>
</head>
<body>
        <script src="http://www.java.com/js/deployJava.js"></script>
        <script>
                // using JavaScript to get location of JNLP file relative to HTML page
                var dir = location.href.substring(0, location.href.lastIndexOf('/')+1);
                var url = dir + "gitools.jnlp";
                deployJava.createWebStartLaunchButton(url, '1.6.0');
        </script>
</body>
</html>
EOF

REMOTE_WEBSTART_DIR="/usr/local/gitools/webstart"

DST_DIR="$REMOTE_WEBSTART_DIR/$VERSION"

echo "These files will be synchronized to $DST_DIR ..."
echo
rsync -nav --delete --exclude="gitools.jks" target/jnlp/ bgadmin@ankara:$DST_DIR
echo
echo "Press any key to continue or Ctrl-C to abort ..."
read
rsync -av --delete --exclude="gitools.jks" target/jnlp/ bgadmin@ankara:$DST_DIR

echo
echo "Linking default webstart to $VERSION ..."
echo "Press any key to continue or Ctrl-C to abort ..."
read

ssh bgadmin@ankara "cd ${REMOTE_WEBSTART_DIR}; rm -f default; ln -s $VERSION default"

set -e
