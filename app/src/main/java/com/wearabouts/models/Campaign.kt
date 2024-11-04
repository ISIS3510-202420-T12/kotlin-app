package com.wearabouts.models

import com.google.firebase.firestore.PropertyName

data class Campaing(

    @get:PropertyName("Goal") @set:PropertyName("Goal")
    var goal: Double = 0.0,

    @get:PropertyName("Image") @set:PropertyName("Image")
    var imageUrl: String = "",

    @get:PropertyName("Name") @set:PropertyName("Name")
    var name: String = "",

    // @get:PropertyName("ONGs") @set:PropertyName("ONGs")
    // var ong: String = "",

    @get:PropertyName("Reached") @set:PropertyName("Reached")
    var progress: Double = 0.0
)