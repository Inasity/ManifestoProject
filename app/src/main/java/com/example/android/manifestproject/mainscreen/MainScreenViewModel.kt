package com.example.android.manifestproject.mainscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.manifestproject.data.GuestEntity
import com.example.android.manifestproject.data.ManifestoDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

class MainScreenViewModel(
    private val database: ManifestoDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    var guests: LiveData<List<GuestEntity>> = database.getAllGuests()


    private val _navigateToLogInScreen = MutableLiveData<Boolean?>()

    fun doneNavigating() {
        _navigateToLogInScreen.value = false
    }

    suspend fun delete(guest: GuestEntity) {
        withContext(Dispatchers.IO) {
            database.delete(guest)
        }
    }

}