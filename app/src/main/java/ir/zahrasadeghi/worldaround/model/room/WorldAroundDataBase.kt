package ir.zahrasadeghi.worldaround.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Venue::class], version = 1)
abstract class WorldAroundDataBase : RoomDatabase() {

    abstract fun venueDao(): VenueDao

    companion object {
        fun getDatabase(context: Context): WorldAroundDataBase {
            synchronized(this) {
                return Room.databaseBuilder(
                    context.applicationContext,
                    WorldAroundDataBase::class.java,
                    "worldaround_database"
                ).build()
            }
        }
    }
}