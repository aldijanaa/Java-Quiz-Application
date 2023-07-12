package com.example.quizapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.User
import com.example.quizapp.repository.AppRepository
import com.example.quizapp.ui.Screen
import kotlinx.coroutines.launch

class AppViewModel(private val repository: AppRepository) : ViewModel() {

    //function responsible for registering a new user
    fun registerUser(username: String, password: String) {
        val user = User(username, password)
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }


    //function responsible to log the user in
    fun loginUser(username: String, password: String) = viewModelScope.launch {
        val user = repository.loginUser(username, password)
    }
}
