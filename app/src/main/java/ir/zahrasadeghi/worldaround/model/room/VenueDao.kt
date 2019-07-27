package ir.zahrasadeghi.worldaround.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VenueDao {

    @Query("SELECT * FROM venue LIMIT :limit OFFSET :offset")
    suspend fun getAllVenues(limit: Int, offset: Int): List<Venue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(venue: Venue)

    @Query("DELETE FROM venue")
    fun clearTable()
}