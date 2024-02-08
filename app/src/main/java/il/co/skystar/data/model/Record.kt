package il.co.skystar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "records")
data class Record(
    val CHAORD: String,
    val CHCINT: String?,
    val CHCKZN: String?,
    val CHFLTN: String,
    val CHLOC1: String,
    val CHLOC1CH: String,
    val CHLOC1D: String,
    val CHLOC1T: String,
    val CHLOC1TH: String,
    val CHLOCCT: String,
    val CHOPER: String,
    val CHOPERD: String,
    val CHPTOL: String,
    val CHRMINE: String,
    val CHRMINH: String,
    val CHSTOL: String,
    val CHTERM: String,
    @PrimaryKey
    var _id: Int
)