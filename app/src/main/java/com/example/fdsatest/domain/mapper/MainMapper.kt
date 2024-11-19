package com.example.fdsatest.domain.mapper

import android.icu.util.Calendar
import com.example.fdsatest.data.local.models.DestinationData
import com.example.fdsatest.data.remote.models.Destination
import com.example.fdsatest.data.remote.models.DestinationType
import com.example.fdsatest.data.remote.models.Timestamp
import com.example.fdsatest.data.remote.models.TimestampDTO
import com.example.fdsatest.domain.models.DestinationDomain

object MainMapper {
    fun destinationToDestinationData(destination: Destination): DestinationData {
        return DestinationData(
            Id = destination.Id.toString(),
            Name = destination.Name,
            Description = destination.Description,
            CountryCode = destination.CountryCode,
            Type = destination.Type.toString(),
            LastModify = TimestampDTO.parse(
                destination.LastModify.toString(),
                "dd/mm/yyyy HH:mm:ss"
            )
        )
    }

    fun destinationDataToDestination(destinationData: DestinationData): Destination {
        return Destination(
            Id = destinationData.Id.toInt(),
            Name = destinationData.Name,
            Description = destinationData.Description,
            CountryCode = destinationData.CountryCode,
            Type = DestinationType.valueOf(destinationData.Type),
            LastModify = destinationData.LastModify
        )
    }

    fun destinationToDestinationDomain(destination: Destination): DestinationDomain {
        return DestinationDomain(
            id = destination.Id.toString(),
            name = destination.Name,
            description = destination.Description,
            countryMode = destination.CountryCode,
            type = destination.Type,
        )
    }

    fun destinationDomainToDestinationData(destinationDomain: DestinationDomain): DestinationData {
        return DestinationData(
            Id = destinationDomain.id ?: "",
            Name = destinationDomain.name ?: "",
            Description = destinationDomain.description ?: "",
            CountryCode = destinationDomain.countryMode ?: "",
            Type = (destinationDomain.type ?: DestinationType.Country.name).toString(),
            LastModify = destinationDomain.lastModify?.toDTO() ?: TimestampDTO()
        )
    }

    fun destinationDomainToDestination(destinationDomain: DestinationDomain): Destination {
        return Destination(
            Id = destinationDomain.id?.toInt() ?: 0,
            Name = destinationDomain.name ?: "",
            Description = destinationDomain.description ?: "",
            CountryCode = destinationDomain.countryMode ?: "",
            Type = DestinationType.valueOf(destinationDomain.type?.name ?: ""),
            LastModify = TimestampDTO()
        )
    }

    fun destinationDataToDestinationDomain(destinationData: DestinationData): DestinationDomain {
        return DestinationDomain(
            id = destinationData.Id,
            name = destinationData.Name,
            description = destinationData.Description,
            countryMode = destinationData.CountryCode,
            type = DestinationType.valueOf(destinationData.Type),
            lastModify = destinationData.LastModify.toTimestamp()
        )
    }


    private fun TimestampDTO.toTimestamp(): Timestamp {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, hour, minute, second)
        calendar.set(Calendar.MILLISECOND, millisecond)
        return Timestamp(calendar.timeInMillis)
    }

    private fun Timestamp.toDTO(): TimestampDTO {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = millis
        }
        return TimestampDTO(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) + 1,
            day = calendar.get(Calendar.DAY_OF_MONTH),
            hour = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE),
            second = calendar.get(Calendar.SECOND),
            millisecond = calendar.get(Calendar.MILLISECOND)
        )
    }
}