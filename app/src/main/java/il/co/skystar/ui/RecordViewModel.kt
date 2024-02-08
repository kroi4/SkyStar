package il.co.skystar.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.co.skystar.data.model.Flights
import il.co.skystar.data.model.Record
import il.co.skystar.data.repository.RecordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(private val recordRepository: RecordRepository) :
    ViewModel() {

    var departures = recordRepository.getDepartures()
    var arrivals = recordRepository.getArrivals()
    var allFlights = recordRepository.getAllFlights()

    fun getAllFlights() {
        viewModelScope.launch(Dispatchers.IO) {
            allFlights = recordRepository.getAllFlights()
        }
    }

    fun getDepartures() {
        viewModelScope.launch(Dispatchers.IO) {
            departures = recordRepository.getDepartures()
        }
    }

    fun getArrivals() {
        viewModelScope.launch(Dispatchers.IO) {
            arrivals = recordRepository.getArrivals()
        }
    }

    val records: LiveData<Flights>
        get() = recordRepository.records

    fun getFavorites(): List<Record> {
        return recordRepository.getFavorites()
    }

    fun addFavorite(favorite: Record) {
        viewModelScope.launch(Dispatchers.IO) {
            recordRepository.addFavorite(favorite)
        }
    }

    fun removeFavorite(favorite: Record) {
        viewModelScope.launch(Dispatchers.IO) {
            recordRepository.removeFavorite(favorite)
        }
    }

    fun getCurrentNumberOfFavorites(): Int {
        return recordRepository.countFavorites()
    }

    fun checkIfAlreadyFavorite(
        airlineCode: String,
        flightNumber: String,
        scheduledTime: String
    ): Boolean {
        return recordRepository.checkForFavorite(airlineCode, flightNumber, scheduledTime) == 1
    }

    fun checkIfFlightStillExists(
        airlineCode: String,
        flightNumber: String,
        scheduledTime: String
    ): Boolean {
        return recordRepository.checkIfInFlights(airlineCode, flightNumber, scheduledTime) > 0
    }

    fun getSpecificRecord(
        airlineCode: String,
        flightNumber: String,
        scheduledTime: String
    ): Record {
        return recordRepository.getSpecificRecord(airlineCode, flightNumber, scheduledTime)
    }

    fun updateRecord(
        airlineCode: String,
        flightNumber: String,
        scheduledTime: String,
        actualTime: String,
        statusEn: String,
        statusHe: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            recordRepository.updateRecord(
                airlineCode,
                flightNumber,
                scheduledTime,
                actualTime,
                statusEn,
                statusHe
            )
        }
    }

    fun searchFlightWithDate(
        flightNumber: String,
        startDateTime: String,
        endDateTime: String,
        flightType: String,
        airlineName: String,
        flightCity: String
    ): List<Record> {
        return recordRepository.searchFlightWithDate(
            flightNumber,
            startDateTime,
            endDateTime,
            flightType,
            airlineName,
            flightCity
        )
    }

    fun searchFlightWithoutDate(
        flightNumber: String,
        flightType: String,
        airlineName: String,
        flightCity: String
    ): List<Record> {
        return recordRepository.searchFlightWithoutDate(
            flightNumber,
            flightType,
            airlineName,
            flightCity)
    }
}