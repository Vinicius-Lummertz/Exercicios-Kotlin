package br.edu.satc.todolistcompose.data

import android.content.Context
import android.content.SharedPreferences

class TaskPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getShowOnlyPending(): Boolean {
        return sharedPreferences.getBoolean(KEY_SHOW_ONLY_PENDING, false)
    }

    fun setShowOnlyPending(showOnlyPending: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_SHOW_ONLY_PENDING, showOnlyPending)
            .apply()
    }

    companion object {
        private const val PREF_NAME = "todo_preferences"
        const val KEY_SHOW_ONLY_PENDING = "show_only_pending"
    }
}
