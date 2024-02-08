package il.co.skystar.api

import il.co.skystar.data.model.Flights
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlightsApi {

    @GET("action/datastore_search")
    suspend fun getFlights(
        @Query("resource_id") resourceId:String,
        @Query("limit") limit:Int
    ): Response<Flights>

    @GET("action/datastore_search")
    suspend fun getFiltersFlights(
        @Query("resource_id") resourceId:String,
        @Query("filters") filter:String,
        @Query("limit") limit:Int
    ): Response<Flights>

}