
if [ ! -z "${JAVA_HOME}" ]; then
  JAVA="${JAVA_HOME}/bin/java"
else
  JAVA=java
fi

CLASSPATH="${ZTREAMYBINDIR}/../target/ztreamy.jar:${CLASSPATH}"
