package com.example.android.manifestproject.dialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.manifestproject.data.ManifestoDatabaseDao
import com.example.android.manifestproject.mainscreen.MainScreenViewModel

class GuestDialogViewModelFactory (
    private val dataSource: ManifestoDatabaseDao,
    private val application: Application,
    private val guestId: Long
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GuestDialogViewModel::class.java)) {
            return GuestDialogViewModel(dataSource, guestId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}