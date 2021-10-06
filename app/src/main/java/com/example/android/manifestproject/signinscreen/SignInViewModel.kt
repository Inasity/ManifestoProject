package com.example.android.manifestproject.signinscreen

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Room
import com.example.android.manifestproject.data.GuestEntity
import com.example.android.manifestproject.data.ManifestoDatabaseDao
import com.example.android.manifestproject.data.ManifestoDatabase
import kotlinx.coroutines.*

class SignInViewModel(val database: ManifestoDatabaseDao,
    application: Application)
    : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var guest = MutableLiveData<GuestEntity?>()

    private val guests = database.getAllGuests()

    val regexPhone = Regex("[0-9]+")
    val regexName = Regex("[A-Za-z0-9 ]{2,12}")

    private val _login_mediator: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val login_mediator: LiveData<Boolean>
        get() = _login_mediator

    private val _name: MutableLiveData<String> = MutableLiveData<String>("")
    val name: LiveData<String>
    get() = _name

    private val _name_check: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val name_check: LiveData<Boolean>
        get() = _name_check

    private val _phone_number: MutableLiveData<String> = MutableLiveData("")
    val phone_number: LiveData<String>
    get() = _phone_number

    private val _phone_number_check: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private val _email: MutableLiveData<String> = MutableLiveData("")
    val email: LiveData<String>
    get() = _email

    private val _email_check: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private val _emergency_number: MutableLiveData<String> = MutableLiveData("")
    val emergency_number: LiveData<String>
    get() = _emergency_number

    private val _emergency_number_check: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private val _emergency_name: MutableLiveData<String> = MutableLiveData("")
    val emergency_name: LiveData<String>
    get() = _emergency_name

    private val _emergency_name_check: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private val _navigateToMainScreen = MutableLiveData<Boolean>()
    val navigateToMainScreen: LiveData<Boolean>
        get() = _navigateToMainScreen

    fun doneNavigating() {
        _navigateToMainScreen.value = null
    }

    init {
        initializeGuest()
    }

    private fun initializeGuest() {
        uiScope.launch {
            guest.value = null
        }
    }

    private suspend fun getGuestFromDatabase(): GuestEntity? {
        return withContext(Dispatchers.IO) {
            var guest = database.getGuest()
            guest
        }
    }

    fun newGuest() {
        uiScope.launch {
            val newGuest = GuestEntity()

            newGuest.fullName = _name.value.toString()
            newGuest.phoneNumber = _phone_number.value?.filter { !it.isWhitespace()}.toString()
            newGuest.email = _email.value.toString()
            newGuest.emergencyName = _emergency_name.value.toString()
            newGuest.emergencyPhoneNumber = _emergency_number.value?.filter { !it.isWhitespace()}.toString()

            insert(newGuest)

            guest.value = getGuestFromDatabase()

            Log.d("Zelda", "Guest ${guest.value?.fullName} has been created.")

            _navigateToMainScreen.value = true
        }
    }

    private suspend fun insert(guest: GuestEntity) {
        withContext(Dispatchers.IO) {
            database.insert(guest)
        }
    }

    fun checkValidity()
    {
        _login_mediator.value = _name_check.value == true &&
                _phone_number_check.value == true &&
                _email_check.value == true &&
                _emergency_number_check.value == true &&
                _emergency_name_check.value == true
    }

    fun checkName(){
        _name_check.value = _name.value?.matches(regexName) == true
        checkValidity()
        Log.d("Zelda", "Name boolean: ${_name_check.value}")
    }

    fun updateName(text: CharSequence)
    {
        _name.value = text.toString()
        //Log.d("Zelda", "Name: ${_name.value}")
        checkName()
    }

    fun checkPhoneNumber()
    {
        val numb = _phone_number.value?.filter { !it.isWhitespace()}

        _phone_number_check.value = numb?.matches(regexPhone) == true && numb?.length == 10
        Log.d("Zelda", "Phone number boolean: ${_phone_number_check.value}")
        checkValidity()
    }

    fun updatePhoneNumber(text: CharSequence)
    {
        _phone_number.value = text.toString()
        //Log.d("Zelda", "Phone number: ${_phone_number.value}")
        checkPhoneNumber()
    }

    fun checkEmail(email: CharSequence){
        _email_check.value = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        Log.d("Zelda", "Email boolean: ${_email_check.value}")
        checkValidity()
    }

    fun updateEmail(text: CharSequence)
    {
        _email.value = text.toString()
        //Log.d("Zelda", "Email: ${_email.value}")
        checkEmail(text.toString())
    }

    fun checkEmergencyNumber()
    {
        val numb = _emergency_number.value?.filter { !it.isWhitespace()}

        _emergency_number_check.value = numb?.matches(regexPhone) == true && numb?.length == 10
        Log.d("Zelda", "Emergency contact boolean: ${_emergency_number_check.value}")
        checkValidity()
    }

    fun updateEmergencyNumber(text: CharSequence)
    {
        _emergency_number.value = text.toString()
        //Log.d("Zelda", "Emergency contact number: ${_emergency_number.value}")
        checkEmergencyNumber()
    }

    fun checkEmergencyName()
    {
        _emergency_name_check.value = _emergency_name.value?.matches(regexName) == true
        checkValidity()
        Log.d("Zelda", "Emergency name boolean: ${_emergency_name_check.value}")
    }

    fun updateEmergencyName(text: CharSequence)
    {
        _emergency_name.value = text.toString()
        //Log.d("Zelda", "Emergency contact namer: ${_emergency_name.value}")
        checkEmergencyName()
    }
}