package com.foodvisor.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.foodvisor.data.local.Converters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "restaurants")
@TypeConverters(Converters::class)
data class Restaurant(
    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @SerializedName("nom")
    val nom: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("cuisine")
    val cuisine: String,

    @SerializedName("prixMoyen")
    val prixMoyen: Double,

    @SerializedName("noteGoogle")
    val noteGoogle: Double,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("adresse")
    val adresse: String,

    @SerializedName("telephone")
    val telephone: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("isFavorite")
    var isFavorite: Boolean = false,

    @SerializedName("etoilesMichelin")
    val etoilesMichelin: Int = 0,

    @SerializedName("horaires")
    val horaires: Map<String, String>? = null,

    @SerializedName("thumbImageUrl")
    val thumbImageUrl: List<String>? = null,

    @SerializedName("featuredImageUrl")
    val featuredImageUrl: String? = null
)