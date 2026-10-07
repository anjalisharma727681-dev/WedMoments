package com.anjali.wedmoments.data

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("wedmoments_session", Context.MODE_PRIVATE)

    fun getUserId(): Long = prefs.getLong(KEY_USER_ID, -1L)

    fun isLoggedIn(): Boolean = getUserId() > 0

    fun login(userId: Long) {
        prefs.edit().putLong(KEY_USER_ID, userId).apply()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_USER_ID = "user_id"
    }
}
