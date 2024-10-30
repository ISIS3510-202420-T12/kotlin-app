package com.wearabouts.models

data class DonationPlace(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double
)