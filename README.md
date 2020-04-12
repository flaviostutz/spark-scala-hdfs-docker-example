# spark-scala-hdfs-docker-example
Spark with Scala reading/writing files to HDFS with automatic additions of new Spark workers using Docker "scale"

## Usage

* Create docker-compose.yml

```yml
version: '3.3'
services:

  #SPARK SERVICES
  spark-master:
    image: bde2020/spark-master:2.4.5-hadoop2.7
    ports:
      - "8080:8080"
      - "7077:7077"
    environment:
      - INIT_DAEMON_STEP=setup_spark
    networks:
      - spark

  spark-worker:
    image: bde2020/spark-worker:2.4.5-hadoop2.7
    environment:
      - "SPARK_MASTER=spark://spark-master:7077"
    networks:
      - spark

  #HDFS SERVICES
  namenode1:
    image: bde2020/hadoop-namenode:2.0.0-hadoop3.2.1-java8
    ports:
      - "50070:50070"
      - "9870:9870"
    environment:
      - CLUSTER_NAME=example1
      - INIT_DAEMON_STEP=setup_hdfs
      - HDFS_CONF_dfs_webhdfs_enabled=true
      - HDFS_CONF_dfs_permissions_enabled=false
    #   - CORE_CONF_hadoop_http_staticuser_user=root
      - CORE_CONF_hadoop_proxyuser_hue_hosts=*
      - CORE_CONF_hadoop_proxyuser_hue_groups=*
    volumes:
      - ./volumes/namenode1:/hadoop/dfs/name
    networks:
      - hdfs

  datanode1:
    image: bde2020/hadoop-datanode:2.0.0-hadoop3.2.1-java8
    environment:
      - CORE_CONF_fs_defaultFS=hdfs://namenode1:8020
      - HDFS_CONF_dfs_webhdfs_enabled=true
    ports:
      - "50075:50075"
      - "9864:9864"
    volumes:
      - ./volumes/datanode1:/hadoop/dfs/data
    networks:
      - hdfs

  #TOOLS
  filebrowser:
    image: bde2020/hdfs-filebrowser
    ports:
      - "8088:8088"
    environment:
      - CORE_CONF_fs_defaultFS=hdfs://namenode1:8020
    networks:
      - hdfs
```

* Access namenode1 admin at http://localhost:9870/
  * Access filebrowser from menu Utilities->Browser the filesystem

* Access datanode1 admin at http://localhost:9864/

* For scaling datanodes/namenodes, copy datanode/namenode service and map a new volume to them

* For scaling Spark workers, use docker-compose scale spark-worker=5, for example

