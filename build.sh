#!/bin/bash

revision=$(git rev-parse HEAD)
remoteurl=$(git ls-remote --get-url origin)

if [[ ! -d gh-pages ]]; then
    git clone --branch master ${remoteurl} gh-pages
fi
(
cd gh-pages
git pull
)

boot build
cp -r target/public/* gh-pages

(
cd gh-pages

git add --all
git commit -m "Build from ${revision}."
git push origin master
)

rm -rf gh-pages

