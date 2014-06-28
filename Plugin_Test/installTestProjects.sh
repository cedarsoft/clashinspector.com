#!/bin/bash

function failed {
 echo "###################################"
 echo "###################################"
 echo "Failure!"
 echo "###################################"
 echo "###################################"

 exit 0
# TODO change to exit 1
}

function inner {
  cd $1

  files=(testproject*)
  for ((i=${#files[@]}-1; i>=0; i--));
  do
    dir=${files[$i]}
    echo "    -- ${dir} --"

    cd $dir
#    mvn install -Dmaven.repo.local=${localRepo} || failed $1
    mvn install -Dmaven.repo.local="${localRepo}" > /dev/null || failed $1
    cd ..

  done

  cd ..
}


localRepo=$1
echo "Using local repository: <$localRepo>"


for f in ./testproject*
do
  echo "Installing <$f>"
  inner $f
done

