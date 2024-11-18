package com.example.fdsatest.domain.models

import com.example.fdsatest.data.remote.models.DestinationType
import com.example.fdsatest.data.remote.models.Timestamp

data class DestinationDomain(
    val id: String? = null,
    var name:String? = null,
    val description:String? = null,
    val countryMode:String? = null,
    val type: DestinationType? = null,
    val picture:String? = null,
    val lastModify: Timestamp? = null
)