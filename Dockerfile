FROM flaviostutz/spark-submit-scala:2.4.5.1

WORKDIR /app

#warmup
ADD /app/project/assembly.sbt /app/project/
ADD /app/build.sbt /app/
RUN sbt assembly

#compile
ADD /app/src /app/
RUN sbt assembly

RUN ls /app/target -al

