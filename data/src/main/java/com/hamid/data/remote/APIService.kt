package com.hamid.data.remote

import com.hamid.data.model.RemoteApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("movie/popular")
    fun fetchMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Deferred<Response<RemoteApiResponse>>

}
