FROM jolielang/jolie
EXPOSE 9004
COPY ../locations.ol /
COPY content /ws/content
COPY leonardo-master /ws/leonardo-master
WORKDIR /ws
CMD jolie ./leonardo-master/launcher.ol ./content/