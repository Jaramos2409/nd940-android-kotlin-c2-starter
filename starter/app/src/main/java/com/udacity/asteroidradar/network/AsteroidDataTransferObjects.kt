package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.model.Asteroid

@JsonClass(generateAdapter = true)
data class NetworkNearEarthObjectsContainer(
    @Json(name = "near_earth_objects") val nearEarthObjects: Map<String, List<NetworkNearEarthObject>>
)

@JsonClass(generateAdapter = true)
data class NetworkNearEarthObject(
    val id: Long,
    val name: String,
    @Json(name = "absolute_magnitude_h") val absoluteMagnitudeH: Double,
    @Json(name = "estimated_diameter") val estimatedDiameter: NetworkEstimatedDiameterContainer,
    @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardousAsteroid: Boolean,
    @Json(name = "close_approach_data") val closeApproachData: List<NetworkCloseApproachDataContainer>,
    @Json(name = "is_sentry_object") val isSentryObject: Boolean
)

@JsonClass(generateAdapter = true)
data class NetworkEstimatedDiameterContainer(
    val kilometers: NetworkEstimatedDiameterPropertyContainer,
    val meters: NetworkEstimatedDiameterPropertyContainer,
    val miles: NetworkEstimatedDiameterPropertyContainer,
    val feet: NetworkEstimatedDiameterPropertyContainer
)

@JsonClass(generateAdapter = true)
data class NetworkEstimatedDiameterPropertyContainer(
    @Json(name = "estimated_diameter_min") val estimatedDiameterMin: Double,
    @Json(name = "estimated_diameter_max") val estimatedDiameterMax: Double
)

@JsonClass(generateAdapter = true)
data class NetworkCloseApproachDataContainer(
    @Json(name = "close_approach_date") val closeApproachDate: String,
    @Json(name = "close_approach_date_full") val closeApproachDateFull: String,
    @Json(name = "epoch_date_close_approach") val epochDateCloseApproach: Long,
    @Json(name = "relative_velocity") val relativeVelocity: NetworkRelativeVelocityContainer,
    @Json(name = "miss_distance") val missDistance: NetworkMissDistanceContainer,
    @Json(name = "orbiting_body") val orbitingBody: String
)

@JsonClass(generateAdapter = true)
data class NetworkRelativeVelocityContainer(
    @Json(name = "kilometers_per_second") val kilometersPerSecond: Double,
    @Json(name = "kilometers_per_hour") val kilometersPerHour: Double,
    @Json(name = "miles_per_hour") val milesPerHour: Double
)

@JsonClass(generateAdapter = true)
data class NetworkMissDistanceContainer(
    val astronomical: Double,
    val lunar: Double,
    val kilometers: Double,
    val miles: Double
)

fun NetworkNearEarthObjectsContainer.asDomainModel(): List<Asteroid> {
    return nearEarthObjects.values.flatMap {
        it.map { networkNearEarthObject ->
            Asteroid(
                id = networkNearEarthObject.id,
                codename = networkNearEarthObject.name,
                closeApproachDate = networkNearEarthObject.closeApproachData[0].closeApproachDate,
                absoluteMagnitude = networkNearEarthObject.absoluteMagnitudeH,
                estimatedDiameter = networkNearEarthObject.estimatedDiameter.kilometers.estimatedDiameterMax,
                relativeVelocity = networkNearEarthObject.closeApproachData[0].relativeVelocity.kilometersPerSecond,
                distanceFromEarth = networkNearEarthObject.closeApproachData[0].missDistance.astronomical,
                isPotentiallyHazardous = networkNearEarthObject.isPotentiallyHazardousAsteroid
            )
        }
    }
}

fun NetworkNearEarthObjectsContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return nearEarthObjects.values.flatMap {
        it.map { networkNearEarthObject ->
            DatabaseAsteroid(
                id = networkNearEarthObject.id,
                name = networkNearEarthObject.name,
                closeApproachDate = networkNearEarthObject.closeApproachData[0].closeApproachDate,
                absoluteMagnitude = networkNearEarthObject.absoluteMagnitudeH,
                estimatedDiameterMax = networkNearEarthObject.estimatedDiameter.kilometers.estimatedDiameterMax,
                isPotentiallyHazardous = networkNearEarthObject.isPotentiallyHazardousAsteroid,
                kilometersPerSecond = networkNearEarthObject.closeApproachData[0].relativeVelocity.kilometersPerSecond,
                astronomical = networkNearEarthObject.closeApproachData[0].missDistance.astronomical,
            )
        }
    }.toTypedArray()
}