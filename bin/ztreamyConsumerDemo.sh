#!/usr/bin/env bash

# use POSTIX interface, symlink is followed automatically
ZTREAMYBIN="${BASH_SOURCE-$0}"
ZTREAMYBIN="$(dirname "${ZTREAMYBIN}")"
ZTREAMYBINDIR="$(cd "${ZTREAMYBIN}"; pwd)"

"${ZTREAMYBINDIR}/ztreamyConsumer.sh" http://localhost:9000/events/stream
