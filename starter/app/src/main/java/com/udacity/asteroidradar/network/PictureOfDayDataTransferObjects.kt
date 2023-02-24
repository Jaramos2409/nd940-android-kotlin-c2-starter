package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabasePictureOfDay
import com.udacity.asteroidradar.model.PictureOfDay

@JsonClass(generateAdapter = true)
data class NetworkPictureOfDayContainer(
    val copyright: String,
    val date: String,
    val explanation: String,
    @Json(name = "hdurl") val hdUrl: String,
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "service_version") val serviceVersion: String,
    val title: String,
    val url: String
)

fun NetworkPictureOfDayContainer.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = mediaType,
        title = title,
        url = url,
        date = date
    )
}

fun NetworkPictureOfDayContainer.asDatabaseModel(): DatabasePictureOfDay {
    return DatabasePictureOfDay(
        url = url,
        mediaType = mediaType,
        title = title,
        date = date
    )
}