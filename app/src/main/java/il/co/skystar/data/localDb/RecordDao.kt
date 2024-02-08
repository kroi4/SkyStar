package il.co.skystar.data.localDb

import androidx.lifecycle.LiveData
import androidx.room.*
import il.co.skystar.data.model.Record

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord (record : List<Record>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite (favorite: Record)

    @Delete
    suspend fun removeFavorite(vararg favorite: Record)

    @Query("DELETE FROM records")
    fun clearRecords()

    @Query("SELECT * FROM records")
    fun getRecords(): LiveData<List<Record>>

    @Query("SELECT * FROM records")
    suspend fun getFavorites(): List<Record>

    @Query("SELECT * FROM records WHERE _id = :id")
    fun fetchFavorite(id: Int): LiveData<Record>

    @Query("SELECT COUNT(*) FROM records")
    suspend fun countFavorites(): Int

    @Query("SELECT * FROM records WHERE (CHOPER = :airlineCode AND CHFLTN = :flightNumber AND CHSTOL = :scheduledTime)")
    suspend fun getSpecificRecord(airlineCode: String, flightNumber: String, scheduledTime: String) : Record

    @Query("SELECT COUNT(*) FROM records WHERE (CHOPER = :airlineCode AND CHFLTN = :flightNumber AND CHSTOL = :scheduledTime)")
    suspend fun checkForFavorite(airlineCode: String, flightNumber: String, scheduledTime: String): Int

    @Query("UPDATE records SET CHPTOL = :actualTime, CHRMINE = :statusEn, CHRMINH = :statusHe WHERE CHOPER = :airlineCode AND CHFLTN = :flightNumber AND CHSTOL = :scheduledTime")
    suspend fun updateRecord(airlineCode: String, flightNumber: String, scheduledTime: String, actualTime: String, statusEn: String, statusHe: String)

    @Query("SELECT * FROM records WHERE ((CHOPER || CHFLTN) LIKE '%' || :flightNumber || '%') AND ((CHLOC1TH || CHLOC1T) LIKE '%' || :flightCity || '%') AND(CHSTOL BETWEEN :startDateTime AND :endDateTime) AND (CHAORD = :flightType) AND (CHOPERD LIKE '%' || :airlineName|| '%')")
    suspend fun searchFlightWithDate(flightNumber: String, startDateTime: String, endDateTime: String, flightType: String, airlineName: String, flightCity: String) : List<Record>

    @Query("SELECT * FROM records WHERE ((CHOPER || CHFLTN) LIKE '%' || :flightNumber || '%') AND ((CHLOC1TH || CHLOC1T) LIKE '%' || :flightCity || '%') AND (CHAORD = :flightType) AND (CHOPERD LIKE '%' || :airlineName || '%')")
    suspend fun searchFlightWithoutDate(flightNumber: String, flightType: String, airlineName: String, flightCity: String) : List<Record>

}