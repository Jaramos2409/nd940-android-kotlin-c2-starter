package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.model.Asteroid

@Entity
data class DatabaseAsteroid constructor(
    @PrimaryKey val id: Long,
    val name: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameterMax: Double,
    val isPotentiallyHazardous: Boolean,
    val kilometersPerSecond: Double,
    val astronomical: Double
)

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.name,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameterMax,
            relativeVelocity = it.kilometersPerSecond,
            distanceFromEarth = it.astronomical,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}
