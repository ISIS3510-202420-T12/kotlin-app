package com.wearabouts.models

import com.google.firebase.firestore.PropertyName

data class DonationPlace(
    @get:PropertyName("Name") @set:PropertyName("Name")
    var name: String = "",
    
    @get:PropertyName("Description") @set:PropertyName("Description")
    var description: String = "",

    @get:PropertyName("Image") @set:PropertyName("Image")
    var imageUrl: String = "",

    @get:PropertyName("Latitude") @set:PropertyName("Latitude")
    var latitude: Double = 0.0,

    @get:PropertyName("Longitude") @set:PropertyName("Longitude")
    var longitude: Double = 0.0
)