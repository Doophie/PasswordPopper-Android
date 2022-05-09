package ca.doophie.passwordpopper.data

import android.util.Base64.NO_WRAP
import android.util.Base64.encodeToString
import androidx.room.TypeConverter
import ca.doophie.passwordpopper.crypto.AESEncrypt

class ListPairTypeConverter {

    private val tempKey = ByteArray(32) { it.toByte() }

    @TypeConverter
    fun storedStringToPairList(value: String): List<Pair<String, String>> {
        return value.split("~!!!!!~").map {
            val values = it.split("!~~~~~!")
            Pair(
                AESEncrypt().decrypt(tempKey, android.util.Base64.decode(values[0], NO_WRAP)),
                AESEncrypt().decrypt(tempKey, android.util.Base64.decode(values[1], NO_WRAP))
            )
        }
    }

    @TypeConverter
    fun pairListToStoredString(pl: List<Pair<String, String>>): String {
        return pl.joinToString(separator = "~!!!!!~") {
            encodeToString(AESEncrypt().encrypt(tempKey, it.first), NO_WRAP) +
                    "!~~~~~!" +
            encodeToString(AESEncrypt().encrypt(tempKey, it.second), NO_WRAP)
        }
    }
}