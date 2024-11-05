package com.wearabouts.models

import com.google.firebase.firestore.PropertyName

data class ClothingItem(

    @get:PropertyName("Id") @set:PropertyName("Id")
    var id: String = "",

    @get:PropertyName("Title") @set:PropertyName("Title")
    var name: String = "",

    @get:PropertyName("Price") @set:PropertyName("Price")
    var price: Double = 0.0,

    @get:PropertyName("Description") @set:PropertyName("Description")
    var description: String = "A beautiful piece of clothing",

    @get:PropertyName("Size") @set:PropertyName("Size")
    var size: String = "M",

    @get:PropertyName("Rating") @set:PropertyName("Rating")
    var rating: Double = 0.0,

    @get:PropertyName("ImagesURLs") @set:PropertyName("ImagesURLs")
    var imageUrls: List<String> = emptyList(),
    @get:PropertyName("Labels") @set:PropertyName("Labels")
    var labels: List<String> = emptyList()

    // var seller: Seller,
    
)

