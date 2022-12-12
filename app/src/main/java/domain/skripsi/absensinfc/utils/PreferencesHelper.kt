package domain.skripsi.absensinfc.utils

import android.content.Context
import android.content.SharedPreferences


class PreferencesHelper(context: Context) {
    companion object {
        const val PREF_IS_LOGIN = "PREF_IS_LOGIN"
        const val PREF_USER_TOKEN = "PREF_USER_TOKEN"

        //user info
        const val PREF_USER_NAME = "PREF_USER_NAME"
        const val PREF_USER_NIP = "PREF_USER_NIP"
        const val PREF_USER_PHONE = "PREF_USER_PHONE"
        const val PREF_USER_EMAIL = "PREF_USER_EMAIL"
        const val PREF_USER_PHOTO = "PREF_USER_PHOTO"
    }

    private val prefName = "PREFS_NAME"
    private var sharedPref: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun put(key: String, value: String) = editor.putString(key, value).apply()
    fun getString(key: String): String? = sharedPref.getString(key, null)
    fun put(key: String, value: Boolean) = editor.putBoolean(key, value).apply()
    fun getBoolean(key: String): Boolean = sharedPref.getBoolean(key, false)
    fun logout() = editor.clear().apply()

}