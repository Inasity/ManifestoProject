package com.example.android.manifestproject.mainscreen

import android.app.Application
import androidx.lifecycle.*
import com.example.android.manifestproject.data.ManifestoDatabaseDao
import com.example.android.manifestproject.formatGuests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainScreenViewModel(
    private val database: ManifestoDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var guests = database.getAllGuests()

    val guestString = Transformations.map(guests) {guests ->
        formatGuests(guests, application.resources)
    }

    private val _navigateToLogInScreen = MutableLiveData<Boolean?>()

    val navigateToLogInScreen: LiveData<Boolean?>
        get() = _navigateToLogInScreen

    fun doneNavigating() {
        _navigateToLogInScreen.value = false
    }



}