#!/usr/bin/env bash

set -euo pipefail

BASE_DIR="$(cd $(dirname "$0"); pwd)"

if [ ".toolchain" = "$(basename "${BASE_DIR}")" ]; then
  TOOLCHAIN_DIR="${BASE_DIR}"
  BASE_DIR="$(dirname "${TOOLCHAIN_DIR}")"
else
  TOOLCHAIN_DIR="${BASE_DIR}/.toolchain"
fi

DEPENDENCIES_DIR="${TOOLCHAIN_DIR}/dependencies"
FUNCTIONS_DIR="${TOOLCHAIN_DIR}/functions"

REPOSITORY_URL="http://central.maven.org/maven2"

source "${FUNCTIONS_DIR}/dependency"
