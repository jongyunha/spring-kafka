package com.example.springkafka.domain

data class LibraryEvent(
    val id: Int,
    val book: Book,
    val type: LibraryEventType
)

enum class LibraryEventType {
    NEW,
    UPDATE,
}
