
/usr/lib/jvm/java-21-openjdk-amd64/bin/java \
  -Dcom.sun.xml.ws.transport.http.client.HttpTransportPipe.dump=true \
  -Dcom.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump=true \
  -Dcom.sun.xml.ws.transport.http.HttpAdapter.dump=true \
  -Dcom.sun.xml.internal.ws.transport.http.HttpAdapter.dump=true \
  -Dcom.sun.xml.ws.transport.http.HttpAdapter.dumpTreshold=1024 \
  -jar target/JavaJolieBank-services.jar
