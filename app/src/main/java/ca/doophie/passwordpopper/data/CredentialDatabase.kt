package ca.doophie.passwordpopper.data

import android.content.Context
import androidx.room.*

@Database(
    entities = [Credential::class, ConnectionParam::class],
    version = 1
)
@TypeConverters(ListPairTypeConverter::class)
abstract class CredentialDatabase : RoomDatabase() {
    abstract fun credentialDao(): CredentialDao

    abstract fun connectionParamDao(): ConnectionParamDao
}

class DatabaseHandler {
    companion object {
        fun init(context: Context) {
            instance = Room.databaseBuilder(context, CredentialDatabase::class.java, "creds")
                .fallbackToDestructiveMigration()
                .build()
        }

        var instance: CredentialDatabase? = null
    }
}