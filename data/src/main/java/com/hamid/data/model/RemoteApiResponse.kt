package com.hamid.data.model

import com.google.gson.annotations.SerializedName

data class RemoteApiResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<MovieResponse>,
    @SerializedName("error") val error: String? = null

)