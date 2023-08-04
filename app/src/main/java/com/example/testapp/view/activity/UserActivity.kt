package com.example.testapp.view.activity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.R
import com.example.testapp.databinding.ActivityMainBinding
import com.example.testapp.data.model.UsersItem
import com.example.testapp.data.model.UsersListResponse
import com.example.testapp.util.CheckValidation
import com.example.testapp.util.PaginationScrollListener
import com.example.testapp.view.adapter.AdapterUsersPagination
import com.example.testapp.viewmodel.UserViewModel
import java.util.concurrent.TimeoutException

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var mAdapterUsersPagination: AdapterUsersPagination
    private val pageStart: Int = 0
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = pageStart
    companion object {
        const val USER_ID = "0"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
        window.statusBarColor = ContextCompat.getColor(this, R.color.profilePrimaryDark)
        userViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(UserViewModel::class.java)

        initMyOrderRecyclerView()
        observerDataRequest()

        mAdapterUsersPagination.setUserOnClickListener(object  : AdapterUsersPagination.SetUserOnClickListener{
            override fun setOnClickListener(usersItem: UsersItem) {
                val intent = Intent(applicationContext, UserDetailsActivity::class.java)
                Log.d(TAG, "setOnClickListener: "+usersItem.id)
                intent.putExtra(USER_ID,""+usersItem.id)
                startActivity(intent)
            }
        })
    }

    private fun initMyOrderRecyclerView() {
        //attach adapter to  recyclerview
        mAdapterUsersPagination = AdapterUsersPagination(this@UserActivity)
        binding.adapterUsers = mAdapterUsersPagination
        binding.recyclerUsers.setHasFixedSize(true)
        binding.recyclerUsers.itemAnimator = DefaultItemAnimator()

        loadFirstPage()

        binding.recyclerUsers.addOnScrollListener(object : PaginationScrollListener(binding.recyclerUsers.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1

                Handler(Looper.myLooper()!!).postDelayed({
                    loadNextPage()
                }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return totalPages
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })


    }

    private fun loadFirstPage() {
        hideErrorView()
        if (CheckValidation.isConnected(this)) {
            userViewModel.requestFirstPageTopUsers(currentPage)
        }else{
            showErrorView(null)
        }
    }

    fun loadNextPage() {
        if (CheckValidation.isConnected(this)) {
            userViewModel.requestFirstNextPageUsers(currentPage)
        }else{
            mAdapterUsersPagination.showRetry(true, fetchErrorMessage(null))
        }
    }

    private fun observerDataRequest(){
        userViewModel.usersFirstPageResponse.observe(this) {
            if (it is UsersListResponse) {
                hideErrorView()
                val results: MutableList<UsersItem> = fetchResults(it) as MutableList<UsersItem>
                binding.mainProgress.visibility = View.GONE
                mAdapterUsersPagination.addAll(results)
                totalPages = it.total!!

                if (currentPage <= totalPages) mAdapterUsersPagination.addLoadingFooter()
                else isLastPage = true
            }else if (it is Throwable){
                showErrorView(it)
            }else{
                Log.d("TAG_TEST" , "Error First Page")

            }
        }

        userViewModel.usersNextPageResponse.observe(this) {
            if (it is UsersListResponse) {

                val results = fetchResults(it) as MutableList<UsersItem>
                mAdapterUsersPagination.removeLoadingFooter()
                isLoading = false
                mAdapterUsersPagination.addAll(results)

                if (currentPage != totalPages) mAdapterUsersPagination.addLoadingFooter()
                else isLastPage = true

            }else if (it is Throwable){
                mAdapterUsersPagination.showRetry(true, fetchErrorMessage(it))
            }else{
                Log.d("TAG_TEST" , "Error First Page")
            }

        }
    }

    private fun fetchResults(usersListResponse: UsersListResponse): List<UsersItem?>? {
        return usersListResponse.users
    }

    private fun showErrorView(throwable: Throwable?) {
        if (binding.lyError.errorLayout.visibility == View.GONE) {
            binding.lyError.errorLayout.visibility = View.VISIBLE
            binding.mainProgress.visibility = View.GONE

            if (!CheckValidation.isConnected(this)) {
                binding.lyError.errorTxtCause.setText(R.string.error_msg_no_internet)
            } else {
                if (throwable is TimeoutException) {
                    binding.lyError.errorTxtCause.setText(R.string.error_msg_timeout)
                } else {
                    binding.lyError.errorTxtCause.setText(R.string.error_msg_unknown)
                }
            }
        }
    }

    private fun hideErrorView() {
        if (binding.lyError.errorLayout.visibility == View.VISIBLE) {
            binding.lyError.errorLayout.visibility = View.GONE
            binding.mainProgress.visibility = View.VISIBLE
        }
    }

    private fun fetchErrorMessage(throwable: Throwable?): String {
        var errorMsg: String = resources.getString(R.string.error_msg_unknown)

        if (!CheckValidation.isConnected(this)) {
            errorMsg = resources.getString(R.string.error_msg_no_internet)
        } else if (throwable is TimeoutException) {
            errorMsg = resources.getString(R.string.error_msg_timeout)
        }

        return errorMsg
    }


}