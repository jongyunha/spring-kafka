package com.example.springkafka.producer

import com.example.springkafka.domain.LibraryEvent
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class LibraryEventProducer(
    private val kafkaTemplate: KafkaTemplate<Int, String>,
    private val objectMapper: ObjectMapper
) {

    companion object {
        const val TOPIC = "library-events"
    }

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

    fun sendAsync(libraryEvent: LibraryEvent) {
        val key = libraryEvent.id
        val value = objectMapper.writeValueAsString(libraryEvent)
        val record = buildProducerRecord(key, value)
        kafkaTemplate.send(record).addCallback({ recordMetadata ->
            handleSuccess(key, value, recordMetadata)
        }, { throwable ->
            handleFailure(key, value, throwable)
        })
    }

    fun sendSync(libraryEvent: LibraryEvent) {
        val key = libraryEvent.id
        val value = objectMapper.writeValueAsString(libraryEvent)
        try {
            val sendResult = kafkaTemplate.sendDefault(key, value).get(3, TimeUnit.SECONDS)
            handleSuccess(key, value, sendResult)
        } catch (e: Exception) {
            handleFailure(key, value, e)
        }
    }

    fun sendAsyncWithHeader(libraryEvent: LibraryEvent) {
        val key = libraryEvent.id
        val value = objectMapper.writeValueAsString(libraryEvent)
        val record = buildProducerRecord(key, value)
        record.headers().add("event-source", "scanner".toByteArray())
        kafkaTemplate.send(record).addCallback({ recordMetadata ->
            handleSuccess(key, value, recordMetadata)
        }, { throwable ->
            handleFailure(key, value, throwable)
        })
    }

    private fun buildProducerRecord(key: Int, value: String): ProducerRecord<Int, String> {
        return ProducerRecord( TOPIC, null, key, value )
    }

    private fun handleSuccess(key: Int, value: String, recordMetadata: SendResult<Int, String>?) {
        logger.info("key: $key value $value Success: $recordMetadata")
    }

    private fun handleFailure(key: Int, value: String, throwable: Throwable) {
        logger.error("key: $key value $value Failure: $throwable")
        throw throwable
    }
}
