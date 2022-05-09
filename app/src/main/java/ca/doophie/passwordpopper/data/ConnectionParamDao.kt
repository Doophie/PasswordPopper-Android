package ca.doophie.passwordpopper.data

import androidx.room.*

@Dao
interface ConnectionParamDao {
    @Query("Select * FROM connectionparam")
    fun getAll(): List<ConnectionParam>

    @Insert
    fun insertAll(vararg connectionParam: ConnectionParam)

    @Delete
    fun delete(connectionParam: ConnectionParam)
}
