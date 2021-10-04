package com.example.android.manifestproject.signinscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.manifestproject.data.ManifestoDBRepo
import com.example.android.manifestproject.data.ManifestoDBRepoImp

class SignInViewModel(): ViewModel() {

    private val manifestoDBRepo: ManifestoDBRepo = ManifestoDBRepoImp()

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

    val buttonVisible = Transformations.map(_name_check)
    {
        it
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