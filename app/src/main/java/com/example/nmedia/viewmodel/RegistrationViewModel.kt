package com.example.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nmedia.db.AppDb
import com.example.nmedia.repository.PostRepository
import com.example.nmedia.repository.PostRepositoryImpl
import com.example.nmedia.util.SingleLiveEvent
import kotlinx.coroutines.launch

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    private val _tokenReceived = SingleLiveEvent<Int>()
    val tokenReceived: LiveData<Int>
        get() = _tokenReceived

    fun registerUser(login: String, pass: String, name: String){
        viewModelScope.launch {
            try {
                repository.registerUser(login,pass,name)
                _tokenReceived.value = 0
            } catch (e: Exception){
                _tokenReceived.value = -1
            }

        }
    }
}