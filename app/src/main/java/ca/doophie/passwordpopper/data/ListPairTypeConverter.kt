package ca.doophie.passwordpopper.data

import androidx.room.TypeConverter
import java.util.*

class ListPairTypeConverter {
    @TypeConverter
    fun storedStringToPairList(value: String): List<Pair<String, String>> {
        return value.split("~!!!!!~").map {
            val vals = it.split("!~~~~~!")
            Pair(vals[0], vals[1])
        }
    }

    @TypeConverter
    fun pairListToStoredString(pl: List<Pair<String, String>>): String {
        return pl.joinToString(separator = "~!!!!!~") { it.first + "!~~~~~!" + it.second }
    }
}