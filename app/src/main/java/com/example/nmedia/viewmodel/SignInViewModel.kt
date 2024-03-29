package com.example.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nmedia.api.ApiService
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.dao.PostDao
import com.example.nmedia.dao.PostRemoteKeyDao
import com.example.nmedia.db.AppDb
import com.example.nmedia.repository.PostRepository
import com.example.nmedia.repository.PostRepositoryImpl
import com.example.nmedia.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    postDao: PostDao,
    apiService: ApiService,
    appAuth: AppAuth,
    postRemoteKeyDao: PostRemoteKeyDao,
    appDb: AppDb
) : ViewModel() {
    private val repository: PostRepository =
        PostRepositoryImpl(postDao,apiService,appAuth,postRemoteKeyDao, appDb)

    private val _tokenReceived = SingleLiveEvent<Int>()
    val tokenReceived: LiveData<Int>
    get() = _tokenReceived

    fun updateUser(login: String, pass: String){
        viewModelScope.launch {
            try {
                repository.updateUser(login,pass)
                _tokenReceived.value = 0
            } catch (e: Exception){
                _tokenReceived.value = -1
            }

        }
    }
}