#!/usr/bin/env bash

# e - script stops on error (return !=0)
# u - error if undefined variable
# o pipefail - script fails if one of piped command fails
# x - output each line (debug)
set -euo pipefail

LAYOUT=../springboot-layout

cp ${LAYOUT}/.env.example ./
cp ${LAYOUT}/.gitattributes ./
cp ${LAYOUT}/.gitignore ./
cp ${LAYOUT}/Dockerfile ./
cp ${LAYOUT}/compose.yml ./
cp ${LAYOUT}/src/main/resources/* src/main/resources/
cp -r ${LAYOUT}/src/main/java/cl/duoc/foo/exception src/main/java/cl/duoc/*/
