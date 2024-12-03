package com.wearabouts.models

import com.google.firebase.firestore.PropertyName

data class Clothe(

    @get:PropertyName("Id") @set:PropertyName("Id")
    var id: String = "",

    @get:PropertyName("title") @set:PropertyName("title")
    var name: String = "",

    @get:PropertyName("price") @set:PropertyName("price")
    var price: Double = 0.0,

    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "A beautiful piece of clothing",

    @get:PropertyName("size") @set:PropertyName("size")
    var size: String = "M",

    @get:PropertyName("images") @set:PropertyName("images")
    var images: List<String> = emptyList(),

    @get:PropertyName("labels") @set:PropertyName("labels")
    var labels: List<String> = emptyList(),

    @get:PropertyName("seller") @set:PropertyName("seller")
    var seller: String = "d@d.com"

)

