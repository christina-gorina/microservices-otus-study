code for prod = cfp



https://codelabs.solace.dev/codelabs/spring-cloud-stream-beyond/#7
By default when using Spring Cloud Stream with imperative functions (not reactive!) it automatically acknowledges a message when the Function successfully exists. However sometimes you want more control. In this section we'll cover how you can use client/manual acknowledgements to handle this situation.

Using Client/Manual Acknowledgements can be simplified into a two step process:

Disable auto-acknowledgement in the acknowledgment callback header
Acknowledge the message!


https://docs.spring.io/spring-kafka/reference/html/#committing-offsets

Several options are provided for committing offsets. If the enable.auto.commit consumer property is true, Kafka auto-commits the offsets according to its configuration. If it is false, the containers support several AckMode settings (described in the next list). The default AckMode is BATCH. Starting with version 2.3, the framework sets enable.auto.commit to false unless explicitly set in the configuration. Previously, the Kafka default (true) was used if the property was not set.

The consumer poll() method returns one or more ConsumerRecords. The MessageListener is called for each record. The following lists describes the action taken by the container for each AckMode (when transactions are not being used):


По умолчанию AckMode это BATCH, таботает так, под коробкой работает консьюмер, забирает сообщения, пачками или по одному в зависимости от настроек.
Каждое сообщение передпется в лисенер или функцию функционального интерфейса, если не одна функцию не выдаст ошибку, то отправляется ack.
Если хоть одна функция выдала ошибку, то аски не отправятся и сообщение не будет считаться прочитанным.
Но рекомендуется отправлять ack вручную, то еть менять параметр batch на manual_imidiatly. Почему до конца не поняла.
Но может быть такая ситуация. Мы получили сообщение, и упали потому что нехватило памяти. ACK кафка не получила, и прислала его снова,
после того, как мы поднялись и тд по кругу. Вообще можно сообщения в БД например сохранять еще и таким образом следить за их отправкой.

https://www.stackchief.com/blog/%40KafkaListener%20%7C%20Spring%20Boot%20Example
y default, Spring Kafka does not automatically commit offsets for you. The default AckMode is BATCH. This means that your @KafkaListener method won't commit offsets until every single record in the batch has been processed. So when the underlying Kafka consumer runs the poll() method, it's going to bring back (default 500) messages. Depending on the type of listener you implement (MessageListener vs BatchMessageListener), you will receive either individual consumer records or a List of all the records in the batch.

This is important to understand these default configurations because it can lead to unexpected results. There is a limit on how much time Kafka waits between poll() calls. If the processing for each message leads to the total execution time exceeding this interval then you run the risk of reprocessing the same messages over and over again.

You can either change config properties to read fewer messages, change time interval to wait longer to solve this issue.

https://docs.spring.io/spring-kafka/docs/latest-ga/reference/html/
 
The listener container for the replies MUST be configured with AckMode.MANUAL or AckMode.MANUAL_IMMEDIATE; the consumer property enable.auto.commit must be false (the default since version 2.3). To avoid any possibility of losing messages, the template only commits offsets when there are zero requests outstanding, i.e. when the last outstanding request is released by the release strategy. After a rebalance, it is possible for duplicate reply deliveries; these will be ignored for any in-flight requests; you may see error log messages when duplicate replies are received for already released replies.
 -------------------
 пример
 https://www.sitepen.com/blog/architecture-spotlight-event-sourcing-part-two
 ----
 https://www.springcloud.io/post/2021-12/kafka-streams-with-spring-cloud-stream/#gsc.tab=0

 In Spring Cloud Stream there are two binders supporting the Kafka platform.
  We will focus on the second of them – Apache Kafka Streams Binder. You can read more about it in Spring Cloud documentation available
  here. https://docs.spring.io/spring-cloud-stream-binder-kafka/docs/3.1.5/reference/html/spring-cloud-stream-binder-kafka.html#_apache_kafka_binder
  <dependency>
 	<groupId>org.springframework.cloud</groupId>
 	<artifactId>spring-cloud-stream-binder-kafka-streams</artifactId>
 </dependency>

 а я в курсовике использую зависимость
 <dependency>
 	<groupId>org.springframework.cloud</groupId>
 	<artifactId>spring-cloud-stream-binder-kafka</artifactId>
 </dependency>
 -----


https://www.springcloud.io/post/2021-12/kafka-streams-with-spring-cloud-stream/#gsc.tab=0
public BiConsumer<KStream<Long, Order>, KStream<Long, Order>> orders()
можем мержить разные стримы в один, можем фильтровать и вообще работать как с Flux

https://www.springcloud.io/post/2021-12/kafka-streams-with-spring-cloud-stream/#gsc.tab=0
@Lock(LockModeType.PESSIMISTIC_WRITE)
It initiates a transaction and locks both Order entities - использование транзакций

https://piotrminkowski.com/2022/01/24/distributed-transactions-in-microservices-with-kafka-streams-and-spring-boot/
есть сущность ktable, можем работать как с таблицей
spring.kafka.streams.state-dir: /tmp/kafka-streams/1


сделать запись по теории spring cloud stream и указать, что еще kafka stream есть


https://medium.com/sfu-cspmp/sailing-through-kafka-streams-ec045d78c667
Wait, we’ve heard of Kafka. What is Kafka Streams?



//https://stackoverflow.com/questions/11881479/how-do-i-update-an-entity-using-spring-data-jpa
//Use @Transaction above method for several db request. An in this case no need in userRepository.save(inbound);, changes flushed automatically.

-------------------------------------------------- ГЕОРАССТОЯНИЕ
https://stackoverflow.com/questions/10410502/how-do-you-calculate-the-distance-between-two-points-in-gps-java
Take a look at this: http://www.zipcodeworld.com/samples/distance.java.html

Because the above link is broken look at this: Calculate distance between two latitude-longitude points? (Haversine formula).

A summary of this post:


https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula/12600225#12600225

This link might be helpful to you, as it details the use of the Haversine formula to calculate the distance.

----------------------------------------------

https://docs.confluent.io/platform/current/platform-quickstart.html#step-2-create-ak-topics-for-storing-your-data
для запуска кафки в докере использую вот это, так как просто

curl --silent --output docker-compose.yml https://raw.githubusercontent.com/confluentinc/cp-all-in-one/7.2.2-post/cp-all-in-one/docker-compose.yml

docker-compose up -d

docker-compose ps

---------------------
https://hub.docker.com/r/wurstmeister/kafka/
в кубе запущен вот этот релиз, он из образца, но не понятно какв докере запускать
есть файлы для куба в примере
--------------------------------
https://github.com/sylleryum/kafka-microservices-with-saga/blob/6f87a7e4618ea03b2b9267b4b51c96f2646b7b4a/common/src/main/resources/shared.properties#L29


https://translated.turbopages.org/proxy_u/en-ru.ru.569e4405-635eb956-3bc5c06f-74722d776562/https/stackoverflow.com/questions/64060416/spring-kafkalistener-auto-commit-offset-or-manual-which-is-recommended

"the Spring team does not recommend using auto commit; the listener container Ackmode (BATCH or RECORD) will commit the offsets in a deterministic manner; recent versions of the framework disable auto commit (unless specifically enabled)"


https://habr.com/ru/post/466585/
До Kafka 0.10 клиент, использовавший этот параметр, отправлял смещение последнего прочитанного сообщения при следующем вызове poll () после обработки. Это означало, что любые сообщения, которые уже были извлечены (fetched), могли быть повторно обработаны, если клиент их уже обработал, но был неожиданно уничтожен перед вызовом poll ().
Управлять процессом коммита смещения вручную можно в API консюмера Kafka, установив
 В Kafka отсутствуют транзакции. У клиента нет возможности сделать следующее:
 образом, одна из стратегий заключается в перемотке (rewind) смещения на предыдущую позицию

https://github.com/sylleryum/kafka-microservices-with-saga
Kafka may be tricky to handle proficiently duplications/idempotency. In this project the approach of enabling idempotent producer was used. Consumer and its commit offset strategy should be considered also. E.g.: Idempotent Kafka Consumer


------------------------------------





