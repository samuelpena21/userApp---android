package com.sapp.userapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sapp.userapp.R
import com.sapp.userapp.api.models.User

class SavedUserAdapter(
    private val mContext: Context,
    private val mSavedUserList : MutableList<User>,
    private val listener: (User) -> Unit
) : RecyclerView.Adapter<SavedUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fav_user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mSavedUserList[position]
        val ivUserProfile = holder.ivUserProfile
        ivUserProfile.setOnClickListener { listener(user) }
        Glide.with(mContext).load(user.picture.large).placeholder(R.drawable.ic_outline_account_circle_24)
            .into(ivUserProfile)
    }

    override fun getItemCount(): Int {
        return mSavedUserList.size
    }

    fun updateUserList(updatedUserList: List<User>) {
        if (mSavedUserList == updatedUserList) return

        mSavedUserList.clear()
        mSavedUserList.addAll(updatedUserList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivUserProfile: ImageView = itemView.findViewById(R.id.ivSavedUser)

    }
}