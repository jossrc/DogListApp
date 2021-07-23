package com.example.doglist

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Crea el método por el cual accedemos a nuestra API
 * para consumirlo
 */
interface APIService {
    @GET
    fun getDogsByBreeds(@Url url: String): Response<DogsResponse>
}