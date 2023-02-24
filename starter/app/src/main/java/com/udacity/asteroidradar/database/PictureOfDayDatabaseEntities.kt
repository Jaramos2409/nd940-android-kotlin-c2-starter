package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.model.PictureOfDay

@Entity
data class DatabasePictureOfDay constructor(
    @PrimaryKey val url: String,
    val mediaType: String,
    val title: String,
    val date: String
)

fun DatabasePictureOfDay.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = mediaType,
        title = title,
        url = url,
        date = date
    )
}