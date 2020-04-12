FROM flaviostutz/spark-submit-scala:2.4.5.3 AS BUILD

#warmup
ADD /app/project/assembly.sbt /app/project/
ADD /app/build.sbt /app/
RUN sbt assembly

#compile
ADD /app/src /app/src
RUN sbt assembly


FROM flaviostutz/spark-submit-scala:2.4.5.3

COPY --from=BUILD /app/target/scala-2.11/app.jar /app/

