package ca.doophie.passwordpopper.data

import androidx.room.*

@Entity
data class ConnectionParam(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "connection_ip")
    var connectionIP: String,

    @ColumnInfo(name = "port")
    var port: Int,

    @ColumnInfo(name = "key")
    var key: String
)