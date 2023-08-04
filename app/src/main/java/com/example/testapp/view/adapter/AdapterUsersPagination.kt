package com.example.testapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.BR
import com.example.testapp.R
import com.example.testapp.databinding.ItemLoadingBinding
import com.example.testapp.databinding.ItemUsersBinding
import com.example.testapp.data.model.UsersItem
import com.example.testapp.view.activity.UserActivity
import com.example.testapp.view.interfaces.PaginationAdapterCallback

/**
 * @author Vijila P
 */

class AdapterUsersPagination(private var mActivity: UserActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() ,
    PaginationAdapterCallback {

    private val item: Int = 0
    private val loading: Int = 1

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    private var errorMsg: String? = ""

    private lateinit var setUserOnClickListener: SetUserOnClickListener

    private var usersModels: MutableList<UsersItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType == item){
            val binding: ItemUsersBinding =DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_users,parent,false)

            UsersList(binding)
        }else{
            val binding: ItemLoadingBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_loading, parent, false)
            LoadingVH(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = usersModels[position]
        if(getItemViewType(position) == item){
            val myOrderVH: UsersList = holder as UsersList
            myOrderVH.itemRowBinding.userImageProgress.visibility = View.VISIBLE
            myOrderVH.bind(model)
            holder.itemView.setOnClickListener() {
                if (setUserOnClickListener != null) {
                    setUserOnClickListener!!.setOnClickListener( model )
                }
            }
        }else{
            val loadingVH: LoadingVH = holder as LoadingVH
            if (retryPageLoad) {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.VISIBLE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.GONE

                if(errorMsg != null) loadingVH.itemRowBinding.loadmoreErrortxt.text = errorMsg
                else loadingVH.itemRowBinding.loadmoreErrortxt.text = mActivity.getString(R.string.error_msg_unknown)

            } else {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.GONE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.VISIBLE
            }

            loadingVH.itemRowBinding.loadmoreRetry.setOnClickListener{
                showRetry(false, "")
                retryPageLoad()
            }
            loadingVH.itemRowBinding.loadmoreErrorlayout.setOnClickListener{
                showRetry(false, "")
                retryPageLoad()
            }
        }
    }

    override fun getItemCount(): Int {
        return if (usersModels.size > 0) usersModels.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            item
        }else {
            if (position == usersModels.size - 1 && isLoadingAdded) {
                loading
            } else {
                item
            }
        }
    }

    override fun retryPageLoad() {
        mActivity.loadNextPage()
    }


    class UsersList(binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemUsersBinding = binding
        fun bind(obj: Any?) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
        }
    }

    class LoadingVH(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemLoadingBinding = binding
    }

    fun showRetry(show: Boolean, errorMsg: String) {
        retryPageLoad = show
        notifyItemChanged(usersModels.size - 1)
        this.errorMsg = errorMsg
    }

    fun addAll(users: MutableList<UsersItem>) {
        for(user in users){
            add(user)
        }
    }
    fun setUserOnClickListener(setUserOnClickListener: SetUserOnClickListener){
        this.setUserOnClickListener = setUserOnClickListener
    }
    interface SetUserOnClickListener {
        fun setOnClickListener(user:UsersItem)
    }
    fun add(user: UsersItem) {
        usersModels.add(user)
        notifyItemInserted(usersModels.size - 1)
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(UsersItem())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position: Int =usersModels.size -1
        val user: UsersItem = usersModels[position]

        if(user != null){
            usersModels.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}