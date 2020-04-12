FROM flaviostutz/spark-submit-scala:2.4.5.1

WORKDIR /app

#warmup
ADD /app/project/assembly.sbt /app/project/
ADD /app/build.sbt /app/
RUN sbt assembly

RUN sed -i -e 's|/bash|/bash -e|g' /submit.sh

#compile
ADD /app/src /app/src
RUN sbt assembly

RUN mv /app/target/scala-2.11/app.jar /app/app.jar
