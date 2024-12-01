package com.wearabouts.models

import com.google.firebase.firestore.PropertyName

data class User(

    @get:PropertyName("Id") @set:PropertyName("Id")
    var id: String = "",

    @get:PropertyName("email") @set:PropertyName("email")
    var email: String = "",

    @get:PropertyName("name") @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("icon") @set:PropertyName("icon")
    var icon: String = "https://images.unsplash.com/photo-1696785465297-62c79906d1a2?q=80&w=2043&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",

    @get:PropertyName("city") @set:PropertyName("city")    
    var city: String = "Bogota D.C",

    @get:PropertyName("country") @set:PropertyName("country")
    var country: String = "Colombia",

    @get:PropertyName("rating") @set:PropertyName("rating")
    var rating: Double = 0.0,

)

