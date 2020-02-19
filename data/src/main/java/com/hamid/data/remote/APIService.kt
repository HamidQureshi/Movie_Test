package com.hamid.data.remote

import com.hamid.data.model.RemoteApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("movie/popular")
    suspend fun fetchMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<RemoteApiResponse>

}
