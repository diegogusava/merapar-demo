FROM openjdk:13.0.2-jdk
VOLUME /tmp
COPY target/*.jar app.jar

EXPOSE 9010

ENTRYPOINT java -Dcom.sun.management.jmxremote=true \
           -Dcom.sun.management.jmxremote.local.only=false \
           -Dcom.sun.management.jmxremote.authenticate=false \
           -Dcom.sun.management.jmxremote.ssl=false \
           -Djava.rmi.server.hostname=localhost \
           -Dcom.sun.management.jmxremote.port=9010 \
           -Dcom.sun.management.jmxremote.rmi.port=9010 \
           -Xmx512m \
           -jar /app.jar

