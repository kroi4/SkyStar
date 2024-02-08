package il.co.skystar.data.localDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import il.co.skystar.data.model.Record

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object{
        @Volatile
        private var INSTANCE: FavoritesDatabase? = null

        fun getDatabase(context: Context) : FavoritesDatabase {
            if (INSTANCE == null){
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoritesDatabase::class.java,
                        "favorites_database"
                    ).fallbackToDestructiveMigration().build()
                }
            }

            return INSTANCE!!
        }
    }
}