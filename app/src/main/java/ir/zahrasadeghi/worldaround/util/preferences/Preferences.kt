package ir.zahrasadeghi.worldaround.util.preferences

import android.content.SharedPreferences
import java.util.*


object Preferences {

    private lateinit var preferences: SharedPreferences
    private var nonPersistentData = HashMap<String, Any>()

    fun init(sharedPreferences: SharedPreferences) {
        preferences = sharedPreferences
    }

    @JvmOverloads
    fun put(key: String, value: Any, persistent: Boolean = true) {
        if (persistent) {
            val editor = preferences.edit()
            when (value) {
                is Int -> editor.putInt(key, value)
                is String -> editor.putString(key, value)
                is Float -> editor.putFloat(key, value)
                is Boolean -> editor.putBoolean(key, value)
                is Long -> editor.putLong(key, value)
                else -> throw IllegalArgumentException("Argument is not serializable")
            }
            editor.apply()
        } else {
            nonPersistentData[key] = value
        }
    }

    @JvmOverloads
    fun getOrPut(key: String, defaultValue: Any, persistent: Boolean = true): Any? {
        if (!contains(key)) {
            put(key, defaultValue, persistent)
        }
        return get(key, defaultValue)
    }

    fun removeNonPersistentData(key: String) {
        if (nonPersistentData.containsKey(key)) {
            nonPersistentData.remove(key)
        }
    }

    fun remove(key: String) {
        removeNonPersistentData(key)
        if (preferences.contains(key)) {
            preferences.edit().remove(key).apply()
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return get(key, defaultValue) as Boolean
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return (get(key, defaultValue) as Int?)!!
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return (get(key, defaultValue) as Long?)!!
    }

    fun getString(key: String, defaultValue: String): String {
        return (get(key, defaultValue) as String?)!!
    }

    operator fun get(key: String, defaultValue: Any?): Any? {
        if (nonPersistentData.containsKey(key)) {
            return nonPersistentData[key]
        } else {
            if (defaultValue == null || defaultValue is String)
                return preferences.getString(key, defaultValue as String?)
            else if (defaultValue is Int)
                return preferences.getInt(key, (defaultValue as Int?)!!)
            else if (defaultValue is Boolean)
                return preferences.getBoolean(key, (defaultValue as Boolean?)!!)
            else if (defaultValue is Float)
                return preferences.getFloat(key, (defaultValue as Float?)!!)
            else if (defaultValue is Long)
                return preferences.getLong(key, (defaultValue as Long?)!!)
        }
        return defaultValue
    }

    operator fun contains(key: String): Boolean {
        return preferences.contains(key) || nonPersistentData.containsKey(key)
    }

    init {
        nonPersistentData = HashMap()
    }
}
