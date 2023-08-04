package com.example.testapp.data.api

import com.example.testapp.data.model.UsersListResponse
import com.example.testapp.data.USERS_LIST_API
import com.example.testapp.data.model.UserDetailsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Vijila P
 */
interface API {

    @GET(USERS_LIST_API)
    fun getUsersList(@Query("limit") limit: Int = 10,@Query("skip") pageIndex: Int): Call<UsersListResponse>

    @GET("$USERS_LIST_API/{id}")
    fun getUserDetails(@Path("id")  id :Int):Call<UserDetailsResponse>
}