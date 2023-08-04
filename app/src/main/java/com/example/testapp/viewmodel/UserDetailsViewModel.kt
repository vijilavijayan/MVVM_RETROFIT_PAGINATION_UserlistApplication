package com.example.testapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testapp.data.repository.RetrofitRepository

/**
 * @author Vijila P
 */
class UserDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private var retrofitRepository: RetrofitRepository? = RetrofitRepository.getInstance(application)

    private var _userDetailsResponse = MutableLiveData<Any>()

    fun requestUserDetails(uid : Int) {
        retrofitRepository!!.fetchUserDetails(_userDetailsResponse,uid)
    }

    val userDetailsResponse : LiveData<Any>
        get() = _userDetailsResponse

}