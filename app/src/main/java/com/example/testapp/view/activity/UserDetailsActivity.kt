package com.example.testapp.view.activity

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testapp.R
import com.example.testapp.data.model.UserDetailsResponse
import com.example.testapp.data.model.UsersItem
import com.example.testapp.data.model.UsersListResponse
import com.example.testapp.databinding.ActivityMainBinding
import com.example.testapp.databinding.ActivityUserDetailsBinding
import com.example.testapp.util.CheckValidation
import com.example.testapp.view.adapter.AdapterUsersPagination
import com.example.testapp.viewmodel.UserDetailsViewModel
import com.example.testapp.viewmodel.UserViewModel

class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var userDetailsViewModel: UserDetailsViewModel
     var userId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_details)
        binding.activity = this
        window.statusBarColor = ContextCompat.getColor(this, R.color.profilePrimaryDark)
        userDetailsViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(UserDetailsViewModel::class.java)
        binding.viewmodel = userDetailsViewModel
        var uid : String = intent.getStringExtra(UserActivity.USER_ID)!!
        userId = uid.toInt()
        if (CheckValidation.isConnected(this)) {
            userDetailsViewModel.requestUserDetails(userId)
        }else{
            Log.d(TAG, "onCreate: Error")
        }
        observerDataRequest()
    }
    private fun observerDataRequest(){
        userDetailsViewModel.userDetailsResponse.observe(this,object : Observer<Any> {
            override fun onChanged(value: Any) {
                if (value is UserDetailsResponse) {
                    binding.userImageProgress.visibility = View.VISIBLE
                    binding.model = value
                }
            }
        })
    }

}