package il.co.skystar.data.remoteDb

import il.co.skystar.api.FlightsApi
import javax.inject.Inject

class RecordRemoteDataSource @Inject constructor(
    private val apiService: FlightsApi
) : BaseDataSource() {

    suspend fun getRecords(resourceId: String, limit: Int) = getResult { apiService.getFlights(resourceId, limit) }
    suspend fun getFilteredRecords(resourceId: String, filter: String, limit: Int) = getResult { apiService.getFiltersFlights(resourceId, filter, limit) }
}