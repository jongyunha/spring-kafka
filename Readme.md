## Start zookeeper
```shell
sh /bin/zookeeper-server-start $CONFLUENT_HOME/etc/kafka/zookeeper.properties
```

## Start kafka
```shell
sh bin/kafka-server-start /etc/kafka/server.properties
```

## How to get Topic list
```shell
sh bin/kafka-topics --list --bootstrap-server localhost:9092
```
