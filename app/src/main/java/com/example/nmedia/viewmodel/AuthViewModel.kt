package com.example.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.dto.Token
import kotlinx.coroutines.Dispatchers

class AuthViewModel: ViewModel() {

    val data: LiveData<Token?> = AppAuth.getInstance().data.asLiveData(Dispatchers.Default)

    val authorized: Boolean
    get() = AppAuth.getInstance().data.value?.token != null
}