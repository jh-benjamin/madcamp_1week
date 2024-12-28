package com.example.madcamp_week1.model

data class RegionSeats(
    val region: String,
    val parties: List<PartySeats>
)

data class PartySeats(
    val partyName: String,
    val seats: Int
)
