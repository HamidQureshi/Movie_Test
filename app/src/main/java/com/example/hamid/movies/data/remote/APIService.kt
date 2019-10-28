package com.example.hamid.movies.data.remote

import com.example.hamid.movies.data.model.RemoteApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("movie/popular")
    fun fetchMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Single<RemoteApiResponse>

}
