package com.example.madcamp_week1.util

import android.content.Context

class PreferenceHelper(context: Context) {
    private val preferences = context.getSharedPreferences("FavoritePrefs", Context.MODE_PRIVATE)

    fun setFavorite(id: String, isFavorite: Boolean) {
        preferences.edit().putBoolean(id, isFavorite).apply()
    }

    fun isFavorite(id: String): Boolean {
        return preferences.getBoolean(id, false)
    }
}