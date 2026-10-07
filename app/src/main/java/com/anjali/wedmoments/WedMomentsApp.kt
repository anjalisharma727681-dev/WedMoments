package com.anjali.wedmoments

import android.app.Application
import com.anjali.wedmoments.data.AppDatabase
import com.anjali.wedmoments.data.AppRepository
import com.anjali.wedmoments.data.SessionManager

class WedMomentsApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val repository: AppRepository by lazy { AppRepository(database) }
    val session: SessionManager by lazy { SessionManager(this) }
}
