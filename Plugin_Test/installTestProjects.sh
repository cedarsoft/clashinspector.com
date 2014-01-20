#!/bin/bash
#
# Copyright (C) cedarsoft GmbH.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


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

