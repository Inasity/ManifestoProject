package com.example.android.manifestproject.signinscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.manifestproject.data.ManifestoDatabaseDao
import java.lang.IllegalArgumentException
import javax.sql.CommonDataSource

class SignInViewModelFactory (
    private val dataSource: ManifestoDatabaseDao,
    private val application: Application,
    private val guestId: Long) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
                return SignInViewModel(dataSource, application, guestId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}