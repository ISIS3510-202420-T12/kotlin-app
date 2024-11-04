package com.wearabouts.models

data class ClothingItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val description: String = "A beautiful piece of clothing",
    val category: String
)