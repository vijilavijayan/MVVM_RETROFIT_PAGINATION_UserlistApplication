package com.example.testapp.data.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.testapp.data.api.API
import com.example.testapp.data.api.ApiClient
import com.example.testapp.data.model.UserDetailsResponse
import com.example.testapp.data.model.UsersListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author Vijila P
 */
class RetrofitRepository private constructor (app: Application) {

    private var instanceApi: API

    init {
        ApiClient.init(app)
        instanceApi= ApiClient.instance
    }

    companion object{
        private var retrofitRepository: RetrofitRepository?=null
        @Synchronized
        fun getInstance(app: Application): RetrofitRepository? {
            if (retrofitRepository == null) {
                retrofitRepository = RetrofitRepository(app)
            }
            return retrofitRepository
        }
    }

    fun loadPage(usersResponse: MutableLiveData<Any>, page : Int) {
        instanceApi.getUsersList( pageIndex = page).enqueue(object :
            Callback<UsersListResponse> {
            override fun onFailure(call: Call<UsersListResponse>, t: Throwable) {
                usersResponse.value = t
            }

            override fun onResponse(
                call: Call<UsersListResponse>,
                response: Response<UsersListResponse>
            ) {

                if (response.isSuccessful){
                    usersResponse.value = response.body()
                }else{
                    usersResponse.value = "error"
                }
            }
        })
    }
    fun fetchUserDetails(userDetailsResponse: MutableLiveData<Any>, id : Int) {
        instanceApi.getUserDetails(id).enqueue(object :
            Callback<UserDetailsResponse> {
            override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                userDetailsResponse.value = t
            }
            override fun onResponse(
                call: Call<UserDetailsResponse>,
                response: Response<UserDetailsResponse>
            ) {

                if (response.isSuccessful){
                    userDetailsResponse.value = response.body()
                }else{
                    userDetailsResponse.value = "error"
                }
            }
        })
    }

}