package com.wristband.eko.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wristband.eko.databinding.UserViewBinding

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding: UserViewBinding = UserViewBinding.inflate(inflater)
        return UserVH(binding)
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        //
    }

    override fun getItemCount(): Int {
        return 10
    }

    class UserVH(binding: UserViewBinding): RecyclerView.ViewHolder(binding.root) {

    }
}