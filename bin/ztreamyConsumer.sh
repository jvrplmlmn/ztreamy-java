#!/usr/bin/env bash

# use POSTIX interface, symlink is followed automatically
ZTREAMYBIN="${BASH_SOURCE-$0}"
ZTREAMYBIN="$(dirname "${ZTREAMYBIN}")"
ZTREAMYBINDIR="$(cd "${ZTREAMYBIN}"; pwd)"

. "$ZTREAMYBINDIR"/ztreamyEnv.sh

"${JAVA}" -cp "${CLASSPATH}" \
    org.ztreamy.Consumer "$@"

