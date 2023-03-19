package com.example.springkafka.consumer

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class LibraryEventConsumer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = ["library-events"])
    fun onMessage(record: ConsumerRecord<Int, String>) {
        logger.info("ConsumerRecord: $record")
    }
}
