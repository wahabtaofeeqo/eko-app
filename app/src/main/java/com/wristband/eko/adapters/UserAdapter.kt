package com.wristband.eko.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wristband.eko.databinding.UserViewBinding
import com.wristband.eko.entities.User

class UserAdapter(context: Context, private val list: List<User>): RecyclerView.Adapter<UserAdapter.UserVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding: UserViewBinding = UserViewBinding.inflate(inflater)
        return UserVH(binding)
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class UserVH(val binding: UserViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.name.text = user.name
            binding.code.text = user.code
            binding.category.text = user.category
        }
    }
}