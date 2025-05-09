package com.nepreconsultintg.edigital.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nepreconsultintg.edigital.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository): ViewModel() {
    var cpf by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by  mutableStateOf(false)
    var loginSuccess by mutableStateOf<Boolean?>(null)

    fun onLogin() {
        viewModelScope.launch {
            isLoading = true
            loginSuccess = repository.login(cpf, password)
            isLoading = false
        }
    }
}