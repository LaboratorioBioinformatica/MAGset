#!/bin/bash 
set -e

if [ $# -eq 0 ]; then
  echo "Usage: `basename $0` <docker-image-name:version>"
  exit 1
fi

image_name=$1

mvn clean install

cd others/docker

cp ../../target/magset-export-1.0.0-SNAPSHOT.jar magset-export.jar

docker build -t $image_name .

rm magset-export.jar

echo "done!"
