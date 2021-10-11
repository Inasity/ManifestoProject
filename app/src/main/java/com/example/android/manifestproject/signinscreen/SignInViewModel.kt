package com.example.android.manifestproject.signinscreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.manifestproject.data.GuestEntity
import com.example.android.manifestproject.data.ManifestoDatabaseDao
import kotlinx.coroutines.*

class SignInViewModel(private val database: ManifestoDatabaseDao,
    application: Application,
    private val guestId: Long)
    : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var guest = MutableLiveData<GuestEntity?>()

    private val regexPhone = Regex("[0-9]+")
    private val regexName = Regex("[A-Za-z0-9 ]{2,12}")

    private val _loginMediator: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val loginMediator: LiveData<Boolean>
        get() = _loginMediator

    private val _name: MutableLiveData<String> = MutableLiveData<String>("")
    val name: LiveData<String>
    get() = _name

    private val _nameCheck: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val nameCheck: LiveData<Boolean>
        get() = _nameCheck

    private val _phoneNumber: MutableLiveData<String> = MutableLiveData("")
    val phoneNumber: LiveData<String>
    get() = _phoneNumber

    private val _phoneNumberCheck: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val phoneNumberCheck: LiveData<Boolean>
        get() = _phoneNumberCheck

    private val _email: MutableLiveData<String> = MutableLiveData("")
    val email: LiveData<String>
    get() = _email

    private var _emailSequence: CharSequence = ""

    private val _emailCheck: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val emailCheck: LiveData<Boolean>
        get() = _emailCheck

    private val _emergencyNumber: MutableLiveData<String> = MutableLiveData("")
    val emergencyNumber: LiveData<String>
    get() = _emergencyNumber

    private val _emergencyNumberCheck: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val emergencyNumberCheck: LiveData<Boolean>
        get() = _emergencyNumberCheck

    private val _emergencyName: MutableLiveData<String> = MutableLiveData("")
    val emergencyName: LiveData<String>
    get() = _emergencyName

    private val _emergencyNameCheck: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val emergencyNameCheck:LiveData<Boolean>
        get() = _emergencyNameCheck

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
        viewModelScope.launch {
            if (guestId != -1L)
            {
                Log.d("Zelda", "Guest ID: $guestId")
                val returningGuest = database.get(guestId)
                guest.value = returningGuest
                Log.d("Zelda", "Guest ${guest.value?.fullName} has arrived.")
                _name.value = guest.value?.fullName
                Log.d("Zelda", "Name: ${_name.value}")
                _phoneNumber.value = guest.value?.phoneNumber
                Log.d("Zelda", "Phone: ${_phoneNumber.value}")
                _email.value = guest.value?.email
                Log.d("Zelda", "Email: ${_email.value}")
                _emergencyName.value = guest.value?.emergencyName
                Log.d("Zelda", "Emergency contact: ${_emergencyName.value}")
                _emergencyNumber.value = guest.value?.emergencyPhoneNumber
                Log.d("Zelda", "Emergency phone: ${_emergencyNumber.value}")
                checkAll()

            }
            else
            {
                guest.postValue(null)
                Log.d("Zelda", "New guest has arrived.")
            }
        }
    }

    private suspend fun getGuestFromDatabase(): GuestEntity? {
        return withContext(Dispatchers.IO) {
            val guest = database.getGuest()
            guest
        }
    }

    fun newGuest() {
        uiScope.launch {
            if(guestId != -1L)
            {
                guest.value?.fullName = _name.value.toString()
                guest.value?.phoneNumber = _phoneNumber.value?.filter { !it.isWhitespace()}.toString()
                guest.value?.email = _email.value.toString()
                guest.value?.emergencyName = _emergencyName.value.toString()
                guest.value?.emergencyPhoneNumber = _emergencyNumber.value?.filter { !it.isWhitespace()}.toString()

                guest.value?.let { update(it) }

                Log.d("Zelda", "Guest ${guest.value?.fullName} has been updated.")
            }
            else
            {
                val newGuest = GuestEntity()

                newGuest.fullName = _name.value.toString()
                newGuest.phoneNumber = _phoneNumber.value?.filter { !it.isWhitespace()}.toString()
                newGuest.email = _email.value.toString()
                newGuest.emergencyName = _emergencyName.value.toString()
                newGuest.emergencyPhoneNumber = _emergencyNumber.value?.filter { !it.isWhitespace()}.toString()

                insert(newGuest)

                guest.value = getGuestFromDatabase()

                Log.d("Zelda", "Guest ${guest.value?.fullName} has been created.")
            }


            _navigateToMainScreen.value = true
        }
    }

    fun navigateBack()
    {
        _navigateToMainScreen.value = true
    }

    private suspend fun insert(guest: GuestEntity) {
        withContext(Dispatchers.IO) {
            database.insert(guest)
        }
    }

    private suspend fun update(guest: GuestEntity) {
        withContext(Dispatchers.IO) {
            database.update((guest))
        }
    }

    private fun checkValidity()
    {
        _loginMediator.value = _nameCheck.value == true &&
                _phoneNumberCheck.value == true &&
                _emailCheck.value == true &&
                _emergencyNumberCheck.value == true &&
                _emergencyNameCheck.value == true
    }

    private fun checkName(){
        _nameCheck.value = _name.value?.matches(regexName) == true
        checkValidity()
        Log.d("Zelda", "Name boolean: ${_nameCheck.value}")
    }

    fun updateName(text: CharSequence)
    {
        _name.value = text.toString()
        //Log.d("Zelda", "Name: ${_name.value}")
        checkName()
    }

    private fun checkPhoneNumber()
    {
        val numb = _phoneNumber.value?.filter { !it.isWhitespace()}

        _phoneNumberCheck.value = numb?.matches(regexPhone) == true && numb.length == 10
        Log.d("Zelda", "Phone number boolean: ${_phoneNumberCheck.value}")
        checkValidity()
    }

    fun updatePhoneNumber(text: CharSequence)
    {
        _phoneNumber.value = text.toString()
        //Log.d("Zelda", "Phone number: ${_phone_number.value}")
        checkPhoneNumber()
    }

    private fun checkEmail(){
        _emailCheck.value = android.util.Patterns.EMAIL_ADDRESS.matcher(_emailSequence).matches()
        Log.d("Zelda", "Email boolean: ${_emailCheck.value}")
        checkValidity()
    }

    fun updateEmail(text: CharSequence)
    {
        _emailSequence = text
        _email.value = text.toString()
        //Log.d("Zelda", "Email: ${_email.value}")
        checkEmail()
    }

    private fun checkEmergencyNumber()
    {
        val numb = _emergencyNumber.value?.filter { !it.isWhitespace()}

        _emergencyNumberCheck.value = numb?.matches(regexPhone) == true && numb.length == 10
        Log.d("Zelda", "Emergency contact boolean: ${_emergencyNumberCheck.value}")
        checkValidity()
    }

    fun updateEmergencyNumber(text: CharSequence)
    {
        _emergencyNumber.value = text.toString()
        //Log.d("Zelda", "Emergency contact number: ${_emergency_number.value}")
        checkEmergencyNumber()
    }

    private fun checkEmergencyName()
    {
        _emergencyNameCheck.value = _emergencyName.value?.matches(regexName) == true
        checkValidity()
        Log.d("Zelda", "Emergency name boolean: ${_emergencyNameCheck.value}")
    }

    fun updateEmergencyName(text: CharSequence)
    {
        _emergencyName.value = text.toString()
        //Log.d("Zelda", "Emergency contact namer: ${_emergency_name.value}")
        checkEmergencyName()
    }

    private fun checkAll()
    {
        checkName()
        checkPhoneNumber()
        checkEmail()
        checkEmergencyName()
        checkEmergencyNumber()
    }

    fun showNameError(): Boolean {
        return _name.value?.length ?: 0 > 0 && _nameCheck.value == false
    }

    fun showPhoneError(): Boolean {
        return _phoneNumber.value?.length ?: 0 > 0 && _phoneNumberCheck.value == false
    }

    fun showEmailError(): Boolean {
        return _email.value?.length ?: 0 > 0 && _emailCheck.value == false
    }

    fun showEmergencyNameError(): Boolean {
        return _emergencyName.value?.length ?: 0 > 0 && _emergencyNameCheck.value == false
    }

    fun showEmergencyPhoneError(): Boolean {
        return _emergencyNumber.value?.length ?: 0 > 0 && _emergencyNumberCheck.value == false
    }
}