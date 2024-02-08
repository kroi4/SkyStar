package il.co.skystar.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import il.co.skystar.api.FlightsApi
import il.co.skystar.data.localDb.FavoritesDatabase
import il.co.skystar.data.localDb.RecordDatabase
import il.co.skystar.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): FlightsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlightsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecordDatabase(@ApplicationContext appContext : Context) : RecordDatabase =
        RecordDatabase.getDatabase(appContext)

    @Provides
    @Singleton
    fun provideFavoriteDatabase(@ApplicationContext appContext : Context) : FavoritesDatabase =
        FavoritesDatabase.getDatabase(appContext)

    @Provides
    @Singleton
    fun provideRecordDao(dataBase: RecordDatabase) =
        dataBase.recordDao()
}