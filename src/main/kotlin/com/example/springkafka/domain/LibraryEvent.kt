package com.example.springkafka.domain

data class LibraryEvent(
    private val id: Int,
    private val book: Book,
)
