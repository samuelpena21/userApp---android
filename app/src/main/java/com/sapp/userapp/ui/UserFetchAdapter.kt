package com.sapp.userapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sapp.userapp.R
import com.sapp.userapp.api.models.User

class UserFetchAdapter(
    private val mContext: Context,
    private val mUserList: MutableList<User>,
    private val listener: (User) -> Unit
) :
    RecyclerView.Adapter<UserFetchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUserList[position]
        val ivUserProfile = holder.ivUserProfile
        val tvUserName = holder.tvUserName
        tvUserName.text = user.name.toString()
        Glide.with(mContext).load(user.picture.large).placeholder(R.drawable.ic_outline_account_circle_24)
            .into(ivUserProfile)
        ivUserProfile.setOnClickListener { listener (user) }
    }

    override fun getItemCount(): Int {
        return mUserList.size
    }

    fun updateUserList(updatedUserList: List<User>) {
        if (mUserList == updatedUserList) return
        mUserList.addAll(updatedUserList)
        notifyItemRangeInserted(mUserList.size - 1, mUserList.size)
    }

    fun updateAndClearUserList(updatedUserList: List<User>) {
        if (mUserList == updatedUserList) return
        mUserList.clear()
        mUserList.addAll(updatedUserList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivUserProfile: ImageView = itemView.findViewById(R.id.userImage)
        var tvUserName: TextView = itemView.findViewById(R.id.userName)

    }
}