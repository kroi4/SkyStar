package il.co.skystar.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers

fun <T,A> performFetchingAndSaving(localDbFetch: () -> LiveData<T>,
                                   remoteDbFetch: suspend () ->Resource<A>,
                                   localDbClear: (suspend () -> Unit)?,
                                   localDbSave: suspend (A) -> Unit
) : LiveData<Resource<T>> =

    liveData(Dispatchers.IO) {

        emit(Resource.loading())

        val source = localDbFetch().map { Resource.success(it) }
        emitSource(source)

        val fetchResource = remoteDbFetch()

        if (fetchResource.status is Error) {
            if (localDbClear != null) {
                localDbClear()
            }
            emit(Resource.error(fetchResource.status.message))
            emitSource(source)
        } else if (fetchResource.status is Success) {
            if (localDbClear != null) {
                localDbClear()
            }
            localDbSave(fetchResource.status.data!!)
        }
    }