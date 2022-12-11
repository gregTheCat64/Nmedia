package com.example.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.auth.AuthState
import com.example.nmedia.dto.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appAuth: AppAuth,
): ViewModel() {
    val data: LiveData<AuthState> = appAuth.authStateFlow.asLiveData(Dispatchers.Default)

    val authorized: Boolean
    get() = appAuth.authStateFlow.value.id != 0L
}