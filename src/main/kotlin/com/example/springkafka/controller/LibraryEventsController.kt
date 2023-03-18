package com.example.springkafka.controller

import com.example.springkafka.domain.LibraryEvent
import com.example.springkafka.producer.LibraryEventProducer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LibraryEventsController(
    private val libraryEventProducer: LibraryEventProducer
) {

    @PostMapping("/v1/library-event")
    fun libraryEvent(@RequestBody libraryEvent: LibraryEvent): ResponseEntity<LibraryEvent> {
        libraryEventProducer.sendLibraryEvent(libraryEvent)
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent)
    }
}
