package com.example.doglist

import com.google.gson.annotations.SerializedName

/**
 * El @SerializedName permite indicar el nombre de la propiedad que está
 * viniendo en la petición, de esa manera evitamos que el nombre de
 * nuestros atributos tengan el mismo nombre que lo que viene en
 * el API REST
 */
data class DogsResponse(
    @SerializedName("status") var status: String,
    @SerializedName("message") var images: List<String>
)
