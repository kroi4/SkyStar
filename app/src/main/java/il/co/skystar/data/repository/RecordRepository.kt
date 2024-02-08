package il.co.skystar.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import il.co.skystar.data.localDb.FavoritesDatabase
import il.co.skystar.data.localDb.RecordDao
import il.co.skystar.data.localDb.RecordDatabase
import il.co.skystar.data.model.Flights
import il.co.skystar.data.model.Record
import il.co.skystar.data.remoteDb.RecordRemoteDataSource
import il.co.skystar.utils.Constants.RESOURCE_ID
import il.co.skystar.utils.performFetchingAndSaving
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordRepository @Inject constructor(
    private val recordDatabase: RecordDatabase,
    private val favoriteDatabase: FavoritesDatabase,
    private val remoteDatasource: RecordRemoteDataSource,
    private val localDatasource: RecordDao
) {

    private val recordsLiveData = MutableLiveData<Flights>()

    val records: LiveData<Flights>
        get() = recordsLiveData

    fun getAllFlights() = performFetchingAndSaving(
        {localDatasource.getRecords()},
        {remoteDatasource.getRecords(RESOURCE_ID, 5000)},
        {localDatasource.clearRecords()},
        {localDatasource.insertRecord(it.result.records)}
    )

    fun getDepartures() = performFetchingAndSaving(
        {localDatasource.getRecords()},
        {remoteDatasource.getFilteredRecords(RESOURCE_ID, "{\"CHAORD\":[\"D\"]}", 3000)},
        {localDatasource.clearRecords()},
        {localDatasource.insertRecord(it.result.records)}
    )

    fun getArrivals() = performFetchingAndSaving(
        {localDatasource.getRecords()},
        {remoteDatasource.getFilteredRecords(RESOURCE_ID, "{\"CHAORD\":[\"A\"]}", 3000)},
        {localDatasource.clearRecords()},
        {localDatasource.insertRecord(it.result.records)}
    )
//
//    fun clearRecords() {
//        recordDatabase.recordDao().clearRecords()
//    }

    fun getFavorites() : List<Record> = runBlocking {
        favoriteDatabase.recordDao().getFavorites()
    }

    suspend fun addFavorite(favorite: Record) {
        favoriteDatabase.recordDao().insertFavorite(favorite)
    }

    suspend fun removeFavorite(favorite: Record) {
        favoriteDatabase.recordDao().removeFavorite(favorite)
    }

    fun countFavorites() : Int  = runBlocking {
        favoriteDatabase.recordDao().countFavorites()
    }

    fun checkForFavorite(airlineCode: String, flightNumber: String, scheduledTime: String) : Int  = runBlocking {
        favoriteDatabase.recordDao().checkForFavorite(airlineCode, flightNumber, scheduledTime)
    }

    fun checkIfInFlights(airlineCode: String, flightNumber: String, scheduledTime: String) : Int  = runBlocking {
        recordDatabase.recordDao().checkForFavorite(airlineCode, flightNumber, scheduledTime)
    }

    fun getSpecificRecord(airlineCode: String, flightNumber: String, scheduledTime: String) : Record  = runBlocking {
        recordDatabase.recordDao().getSpecificRecord(airlineCode, flightNumber, scheduledTime)
    }

    suspend fun updateRecord(airlineCode: String, flightNumber: String, scheduledTime: String, actualTime: String, statusEn: String, statusHe: String) {
        favoriteDatabase.recordDao().updateRecord(airlineCode, flightNumber, scheduledTime, actualTime, statusEn, statusHe)
    }

    fun searchFlightWithDate(flightNumber: String, startDateTime: String, endDateTime: String, flightType: String, airlineName: String, flightCity: String) : List<Record> = runBlocking {
        recordDatabase.recordDao().searchFlightWithDate(flightNumber, startDateTime, endDateTime, flightType, airlineName, flightCity)
    }

    fun searchFlightWithoutDate(flightNumber: String, flightType: String, airlineName: String, flightCity: String) : List<Record> = runBlocking {
        recordDatabase.recordDao().searchFlightWithoutDate(flightNumber, flightType, airlineName, flightCity)
    }

}