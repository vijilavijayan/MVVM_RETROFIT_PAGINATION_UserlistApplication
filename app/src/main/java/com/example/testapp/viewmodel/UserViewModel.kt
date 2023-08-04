package com.example.testapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.data.repository.RetrofitRepository

/**
 * @author Vijila P
 */
class UserViewModel(application: Application) : AndroidViewModel(application) {

    private var retrofitRepository: RetrofitRepository? = RetrofitRepository.getInstance(application)

    private var _usersFirstPageResponse = MutableLiveData<Any>()
    private var _usersNextPageResponse = MutableLiveData<Any>()

    fun requestFirstPageTopUsers(page : Int) {
        retrofitRepository!!.loadPage(_usersFirstPageResponse , page)
    }

    fun requestFirstNextPageUsers(page : Int) {
        retrofitRepository!!.loadPage(_usersNextPageResponse , page)
    }

    val usersFirstPageResponse : LiveData<Any>
        get() = _usersFirstPageResponse
    val usersNextPageResponse : LiveData<Any>
        get() = _usersNextPageResponse

}