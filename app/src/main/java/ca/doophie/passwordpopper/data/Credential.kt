package ca.doophie.passwordpopper.data

import androidx.room.*

@Entity
data class Credential(
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "url")
    var url: String?,

    @ColumnInfo(name = "fields")
    var fields: List<Pair<String, String>>,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)