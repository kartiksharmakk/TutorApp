package com.example.myapplication.Data
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthenticationViewModel: ViewModel() {
    private val mutableEmail = MutableLiveData<String>()
    private val mutablePhone = MutableLiveData<String>()
    private val mutableCountryCode = MutableLiveData<String>()
    private val mutableName = MutableLiveData<String>()
    private val mutableGender = MutableLiveData<String>()
    private val mutableUserType = MutableLiveData<String>()


    val username: LiveData<String> get() = mutableEmail
    val phone: LiveData<String> get() = mutablePhone
    val countryCode: LiveData<String> get() = mutableCountryCode
    val name: LiveData<String> get() = mutableName
    val gender: LiveData<String> get() = mutableGender
    val userType: LiveData<String> get() = mutableUserType

    fun updateCredentials(email: String, code: String, phone: String){
        viewModelScope.launch {
            mutableEmail.value = email
            mutableCountryCode.value = code
            mutablePhone.value = phone
        }
    }
}