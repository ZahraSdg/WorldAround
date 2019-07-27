package ir.zahrasadeghi.worldaround.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venue")
data class Venue(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "address")
    var address: String,
    @ColumnInfo(name = "category_icon_prefix")
    var catIconPrefix: String,
    @ColumnInfo(name = "category_icon_suffix")
    var catIconSuffix: String,
    @ColumnInfo(name = "distance")
    var distance: Int
)