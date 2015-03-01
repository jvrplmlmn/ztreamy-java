#!/usr/bin/env bash

# use POSTIX interface, symlink is followed automatically
ZTREAMYBIN="${BASH_SOURCE-$0}"
ZTREAMYBIN="$(dirname "${ZTREAMYBIN}")"
ZTREAMYBINDIR="$(cd "${ZTREAMYBIN}"; pwd)"

"${ZTREAMYBINDIR}/ztreamyPublisher.sh" http://localhost:9000/events/publish 10
