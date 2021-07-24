package com.example.doglist

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Crea el método por el cual accedemos a nuestra API
 * para consumirlo
 */
interface APIService {
    // Todas las funcionas asíncronas deben tener el suspend
    // para que funcione en la carrutina
    @GET
    suspend fun getDogsByBreeds(@Url url: String): Response<DogsResponse>
}