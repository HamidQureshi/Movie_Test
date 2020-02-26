package com.hamid.domain.model.model

data class Response(
    var status: Status,
    var data: List<MovieFormatted>
)