package ca.doophie.passwordpopper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Credential::class], version = 2)
@TypeConverters(ListPairTypeConverter::class)
abstract class CredentialDatabase : RoomDatabase() {
    abstract fun credentialDao(): CredentialDao
}