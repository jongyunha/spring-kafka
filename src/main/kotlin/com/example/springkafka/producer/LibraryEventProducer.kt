package com.example.springkafka.producer

import com.example.springkafka.domain.LibraryEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component

@Component
class LibraryEventProducer(
    private val kafkaTemplate: KafkaTemplate<Int, String>,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun sendLibraryEvent(libraryEvent: LibraryEvent) {
        val key = libraryEvent.id
        val value = objectMapper.writeValueAsString(libraryEvent)
        val listenableFuture = kafkaTemplate.sendDefault(key, value)
        listenableFuture.addCallback({ recordMetadata ->
            handleSuccess(key, value, recordMetadata)
        }, { throwable ->
            handleFailure(key, value, throwable)
        })
    }

    private fun handleSuccess(key: Int, value: String, recordMetadata: SendResult<Int, String>?) {
        logger.info("key: $key value $value Success: $recordMetadata")
    }

    private fun handleFailure(key: Int, value: String, throwable: Throwable) {
        logger.error("key: $key value $value Failure: $throwable")
        throw throwable
    }
}
