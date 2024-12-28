package com.example.madcamp_week1.model

data class Person (
    val imageResId: Int,
    val name: String,
    val party: String,
    val tel: String,
    val office: String,
    val email: String,
    val attendance: Double,
    var isFavorite: Boolean = false
)