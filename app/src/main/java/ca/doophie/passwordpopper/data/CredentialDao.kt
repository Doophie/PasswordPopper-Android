package ca.doophie.passwordpopper.data

import androidx.room.*

@Dao
interface CredentialDao {
    @Query("Select * FROM credential")
    fun getAll(): List<Credential>

    @Insert
    fun insertAll(vararg credentials: Credential)

    @Delete
    fun delete(credential: Credential)
}