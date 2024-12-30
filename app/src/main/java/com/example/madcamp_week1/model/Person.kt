package com.example.madcamp_week1.model

data class Person (
    val name: String,
    val birth: String,
    val party: String,
    val tel: String,
    val office: String,
    val email: String,
    val img: String,
    var isFavorite: Boolean = false
)